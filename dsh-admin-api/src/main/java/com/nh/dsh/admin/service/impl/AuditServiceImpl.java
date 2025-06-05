package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.common.constant.Constant;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.mapper.AuditMapper;
import com.nh.dsh.admin.model.dto.AuditDto;
import com.nh.dsh.admin.model.entity.*;
import com.nh.dsh.admin.model.query.AuditQuery;
import com.nh.dsh.admin.model.vo.AuditVo;
import com.nh.dsh.admin.security.user.SecurityUser;
import com.nh.dsh.admin.task.quartz.DynamicScheduleService;
import com.nh.dsh.admin.security.cache.QuartzTaskStoreCache;
import com.nh.dsh.admin.service.AuditService;
import com.nh.dsh.admin.service.BookResourceService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl extends BaseServiceImpl<AuditMapper, AuditEntity> implements AuditService {

    @Resource
    AuditMapper auditMapper;
    @Resource
    BookResourceService bookResourceService;
    @Resource
    DynamicScheduleService scheduleService;
    @Resource
    QuartzTaskStoreCache taskStoreCache;

    @Override
    public Result addAudit(AuditDto auditDto) {
        try {
            AuditEntity auditEntity = new AuditEntity();
            BeanUtils.copyProperties(auditDto, auditEntity);
            boolean save = save(auditEntity);
            if (save) {
                return Result.ok(auditDto.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return Result.error("新增异常");
    }

    @Override
    public PageResult<AuditEntity> queryListByPage(AuditQuery query) {
        Page<AuditEntity> entityPage = getPage(query);
        LambdaQueryWrapper<AuditEntity> wrapper = new LambdaQueryWrapper<AuditEntity>()
                .eq(AuditEntity::getDeleteFlag,0)
                .eq(AuditEntity::getServiceType, 0)
                .like(AuditEntity::getName, query.getName())
                .orderByDesc(AuditEntity::getCreateTime);
        Page<AuditEntity> pageResult = baseMapper.selectPage(entityPage, wrapper);
        return new PageResult<AuditEntity>(pageResult.getRecords(), pageResult.getTotal());
    }

    @Override
    public Result modifyAudit(AuditDto auditDto) {
        try {
            AuditEntity auditEntity = new AuditEntity();
            BeanUtils.copyProperties(auditDto, auditEntity);
            if (auditDto.getServiceType() == 1 && auditDto.getExecutionTime().isEmpty()) {
                // 若为定时任务并且时间不为空，取消定时任务
                if (scheduleService.isJobRunning(Constant.AUTO_AUDIT_JOB)) {
                    // 判断定时任务是否启动,若启动则删除定时任务
                    scheduleService.deleteJobTask(Constant.AUTO_AUDIT_JOB, Constant.AUTO_AUDIT_TRIGGER);
                }
            }
            boolean save = saveOrUpdate(auditEntity);
            if (save) {
                return Result.ok(auditEntity.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return Result.error("新增异常");
    }

    @Override
    public Result delete(List<Integer> ids) {
        try {
            if (ids.isEmpty()) {
                return Result.error("参数不能为空");
            }
            int i = baseMapper.deleteBatchIds(ids);
            if (i == ids.size()) {
                return Result.ok(i);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return Result.error("删除异常");
    }

    @Override
    public AuditEntity getAuditTask() {
        List<AuditEntity> auditEntities = baseMapper.selectList(new QueryWrapper<AuditEntity>().eq("service_type", 1).eq("delete_flag", 0));
        if (auditEntities.isEmpty()) {
            return null;
        }
        return auditEntities.get(0);
    }

    @Override
    public Result startAuditTask(AuditVo auditVo) {
        if (auditVo.getInterval() == 0) {
            taskStoreCache.saveTaskFlag(Constant.AUDIT_TASK_IS_STOP_KEY, false);
            // 立刻开始
            Boolean result = bookResourceService.auditResource();
            if (result) {
                return Result.ok("审核完成");
            }
            return Result.error("审核异常");
        } else {
            //开启定时任务
            String[] weeks = new String[]{};
            String[] times = new String[]{};
            String weekStr = "";
            taskStoreCache.saveTaskFlag(Constant.AUDIT_TASK_IS_STOP_KEY, false);
            if (auditVo.getCron().contains(",")) {
                weeks = auditVo.getCron().split(",");
                for (int i = 0; i < weeks.length; i++) {
                    if (weeks[i].equals("1")) {
                        // 当前为定时任务开启的时间
                        weekStr += Constant.WEEK_ARRAY.get (i) + ",";
                    }
                }
            }
            weekStr = weekStr.substring(0, weekStr.lastIndexOf(","));
            if (auditVo.getTime().contains(":")) {
                times = auditVo.getTime().split(":");
            }
            String cron = "0 " + times[1] + " " + times[0] + " ? * " + weekStr + " *";
            // 修改定时任务状态为未开始
            AuditEntity auditTask = getAuditTask();
            auditTask.setStatus(1);
            if(scheduleService.isJobRunning(Constant.AUTO_AUDIT_JOB)) {
                // 若job已存在，更新job
                scheduleService.updateJobService(cron, Constant.AUTO_AUDIT_TRIGGER);
                return Result.ok("更新定时任务");
            } else {
                scheduleService.addJobService(cron, Constant.AUTO_AUDIT_JOB,Constant.AUTO_AUDIT_TRIGGER,0,0, SecurityUser.getManagerId());
                return Result.ok("新增定时任务");
            }
        }
    }

    @Override
    public Result stopAuditTask() {
        AuditEntity auditTask = getAuditTask();
        if (auditTask.getExecutionTime().isEmpty()) {
            // 执行周期为立刻执行，向redis中写入停止标志
            taskStoreCache.saveTaskFlag(Constant.AUDIT_TASK_IS_STOP_KEY, true);
        } else {
            // 执行周期为每周执行，向redis中写入停止标志并删除定时任务
            taskStoreCache.saveTaskFlag(Constant.AUDIT_TASK_IS_STOP_KEY, true);
            //删除定时任务
            scheduleService.deleteJobTask(Constant.AUTO_AUDIT_JOB, Constant.AUTO_AUDIT_TRIGGER);
        }
        auditTask.setStatus(0);
        boolean b = saveOrUpdate(auditTask);
        return Result.ok(b);
    }
}
