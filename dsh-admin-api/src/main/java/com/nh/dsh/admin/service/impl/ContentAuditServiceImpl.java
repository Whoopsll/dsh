package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.google.gson.Gson;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.mysql.cj.util.StringUtils;
import com.nh.dsh.admin.common.constant.Constant;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.mapper.AuditLogMapper;
import com.nh.dsh.admin.mapper.ContentAuditMapper;
import com.nh.dsh.admin.model.dto.ContentAuditDto;
import com.nh.dsh.admin.model.entity.AuditLogEntity;
import com.nh.dsh.admin.model.entity.ContentAuditEntity;
import com.nh.dsh.admin.model.query.ContentAuditQuery;
import com.nh.dsh.admin.model.vo.ContentAuditVo;
import com.nh.dsh.admin.security.user.SecurityUser;
import com.nh.dsh.admin.task.async.AsyncService;
import com.nh.dsh.admin.task.quartz.DynamicScheduleService;
import com.nh.dsh.admin.security.cache.QuartzTaskStoreCache;
import com.nh.dsh.admin.service.ContentAuditService;
import com.nh.dsh.admin.utils.DateUtils;
import jakarta.annotation.Resource;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentAuditServiceImpl extends BaseServiceImpl<ContentAuditMapper, ContentAuditEntity> implements ContentAuditService {

    @Resource
    ContentAuditMapper contentAuditMapper;
    @Resource
    DynamicScheduleService scheduleService;
    @Resource
    QuartzTaskStoreCache taskStoreCache;
    @Resource
    AsyncService asyncService;
    @Resource
    AuditLogMapper auditLogMapper;

    @Override
    public Result addInfo(ContentAuditDto auditDto) {
        try {
            ContentAuditEntity contentAuditEntity = new ContentAuditEntity();
            BeanUtils.copyProperties(auditDto, contentAuditEntity);
            if (auditDto.getId() == null) {
                contentAuditEntity.setDeleteFlag(0);
                contentAuditEntity.setCreateTime(new Date());
                contentAuditEntity.setExecuteResult(-1);
            }
            //判断审核的执行类型，如果是立刻执行，则更新
            if(contentAuditEntity.getExecuteType() == 0) {
                //立刻执行,改变执行状态为执行中...
                contentAuditEntity.setExecuteStatus(1);
                //添加执行时间
                contentAuditEntity.setAuditStartTime(DateUtils.format(LocalDateTime.now(), DateUtils.DATE_TIME_PATTERN));
            } else {
                //定时执行，改变执行状态为未执行
                contentAuditEntity.setExecuteStatus(0);
            }
            //保存
            boolean save = saveOrUpdate(contentAuditEntity);
            if (save) {
                //如果时定时任务，开启定时任务
                if(auditDto.getExecuteType() == 1) {
                    //开启定时任务
                    taskStoreCache.saveTaskFlag(Constant.AUDIT_TASK_IS_STOP_KEY, false);
                    LocalDateTime startTime = DateUtils.parse(auditDto.getAuditStartTime(), DateUtils.DATE_TIME_PATTERN);
                    String cron = startTime.getSecond() + " " + startTime.getMinute() + " " + startTime.getHour() + " " + startTime.getDayOfMonth() + " " + startTime.getMonthValue() + " ? " + startTime.getYear();
                    System.out.println("corn====" + cron);
                    // 修改定时任务状态为未开始
                    String key = Constant.AUTO_AUDIT_JOB + "_" + auditDto.getType() + "_" + contentAuditEntity.getId();
                    String trigger = Constant.AUTO_AUDIT_TRIGGER + "_" + auditDto.getType() + "_" + contentAuditEntity.getId();
                    if(scheduleService.isJobRunning(key)) {
                        // 若job已存在，更新job
                        scheduleService.updateJobService(cron, trigger);
                        System.out.println("更新定时任务");
                    } else {
                        scheduleService.addJobService(cron, key,trigger, 1, contentAuditEntity.getId(),SecurityUser.getManagerId());
                        System.out.println("新增定时任务");
                    }
                } else {
                    //立刻执行
                    System.out.println("开始异步任务");
                    asyncService.asyncAuditTask(contentAuditEntity.getId(), SecurityUser.getManagerId());
                }
                return Result.ok(auditDto.getId());
            }
            return Result.error("审核信息添加异常");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public PageResult<ContentAuditEntity> queryListByPage(ContentAuditQuery query) {
        // try {
            Page<ContentAuditEntity> entityPage = getPage(query);
            //构建查询参数
            LambdaQueryWrapper<ContentAuditEntity> wrapper = new LambdaQueryWrapper<ContentAuditEntity>()
                    .eq(ContentAuditEntity::getDeleteFlag, 0)
                    .eq(ContentAuditEntity::getType, query.getType())
                    .like(ContentAuditEntity::getName, query.getName())
                    .orderByDesc(ContentAuditEntity::getCreateTime);
            //分页查询
            Page<ContentAuditEntity> pageResult = baseMapper.selectPage(entityPage, wrapper);
            //构建返回的VO
            List<ContentAuditVo> result = pageResult.getRecords().stream().map(entity -> {
                ContentAuditVo auditVo = new ContentAuditVo();
                BeanUtils.copyProperties(entity, auditVo);
                return auditVo;
            }).collect(Collectors.toList());
            return new PageResult(result, pageResult.getTotal());
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     throw new ServerException(e.getMessage());
        // }
    }

    @Override
    public Result deleteInfo(List<Integer> ids) {
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
    public Result getAuditDetail(int auditId) {
        try {
            ContentAuditEntity contentAuditEntity = baseMapper.selectById(auditId);
            if (contentAuditEntity == null) {
                return Result.error("获取审核详情信息异常");
            }
            ContentAuditVo contentAuditVo = new ContentAuditVo();
            BeanUtils.copyProperties(contentAuditEntity, contentAuditVo);
            AuditLogEntity auditLogEntity = auditLogMapper.selectOne(new QueryWrapper<AuditLogEntity>().eq("delete_flag",0).eq("audit_id", contentAuditEntity.getId()));
            contentAuditVo.setExecuteLog(auditLogEntity.getContent());
            // if (auditLogEntity != null) {
            //     Gson gson = new Gson();
            //     List<String> logList = gson.fromJson(auditLogEntity.getContent(), List.class);
            //     boolean isHasResult = false;
            //     String currentLog = "";
            //     for (int i = 0; i < logList.size(); i++) {
            //         String log = logList.get(i);
            //         if (i == 0) {
            //             currentLog += log + "\n";
            //         }
            //         if(log.contains("鉴定结论") && !isHasResult) {
            //             isHasResult = true;
            //             currentLog = log;
            //             continue;
            //         }
            //         // 添加统计信息
            //         if(isHasResult) {
            //             //如果有，判断当前行是否包含鉴定结论字样，若没有进行拼接
            //             if(i == logList.size() - 1) {
            //                 if(log.contains("鉴定结论")) {
            //                     //最后一行，直接写入log
            //                     currentLog += "\n";
            //                 }
            //                 currentLog += log + "  ";
            //                 continue;
            //             } else  {
            //                 if(log.contains("鉴定结论")) {
            //                     currentLog += log + "\n";
            //                     continue;
            //                 } else {
            //                     currentLog += log + "  ";
            //                     continue;
            //                 }
            //             }
            //         } else {
            //             currentLog += log = "\n";
            //         }
            //     }
            //     contentAuditVo.setExecuteLog(currentLog);
            // } else {
            //     contentAuditVo.setExecuteLog("");
            // }
            return Result.ok(contentAuditVo);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

}
