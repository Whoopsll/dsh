package com.nh.dsh.admin.task.quartz.job;

import com.google.gson.Gson;
import com.nh.dsh.admin.model.dto.AuditDto;
import com.nh.dsh.admin.model.entity.AuditEntity;
import com.nh.dsh.admin.model.entity.ContentAuditEntity;
import com.nh.dsh.admin.security.user.SecurityUser;
import com.nh.dsh.admin.service.AuditService;
import com.nh.dsh.admin.service.BookResourceService;
import com.nh.dsh.admin.service.ContentAuditService;
import com.nh.dsh.admin.task.async.AsyncService;
import com.nh.dsh.admin.websocket.WebSocketMessageVo;
import com.nh.dsh.admin.websocket.WebSocketService;
import jakarta.annotation.Resource;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeanUtils;

public class AutoAuditJob implements Job {

    @Resource
    BookResourceService bookResourceService;
    @Resource
    AuditService auditService;
    @Resource
    ContentAuditService contentAuditService;
    @Resource
    AsyncService asyncService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 开启审核二维码任务，审核完自动停止
        try {
            JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
            Integer userId = 1;
            if (jobDataMap.get("userId") != null) {
                userId = Integer.parseInt(jobDataMap.get("userId").toString());
            }
            System.out.println("参数是====" + jobDataMap.toString());
            if(jobDataMap.get("type").toString().equals("0")) {
                //自动审核
                // 修改定时任务状态为进行中
                AuditEntity auditTask = auditService.getAuditTask();
                auditTask.setStatus(2);
                AuditDto auditDto = new AuditDto();
                BeanUtils.copyProperties(auditTask, auditDto);
                auditService.modifyAudit(auditDto);
                boolean results = bookResourceService.auditResource();
                if (results) {
                    auditDto.setStatus(0);
                    auditService.modifyAudit(auditDto);
                }
            }
            if(jobDataMap.get("type").toString().equals("1")) {
                int auditId = Integer.parseInt(jobDataMap.get("auditId").toString());
                // 修改定时任务状态为进行中
                WebSocketMessageVo messageVo = new WebSocketMessageVo();
                messageVo.setResult(-1);
                messageVo.setTaskId(auditId);
                messageVo.setUserId(SecurityUser.getManagerId());
                messageVo.setStatus(1);
                Gson gson = new Gson();
                WebSocketService.broadcast(gson.toJson(messageVo));
                asyncService.asyncAuditTask(auditId, userId);

                //获取任务
                // ContentAuditEntity contentAuditEntity = contentAuditService.getBaseMapper().selectById(auditId);
                // contentAuditEntity.setExecuteStatus(1);
                // contentAuditService.saveOrUpdate(contentAuditEntity);
                // //执行审核
                // Thread.sleep(3000);
                // contentAuditEntity.setExecuteResult(1);
                // contentAuditEntity.setExecuteStatus(2);
                // contentAuditService.saveOrUpdate(contentAuditEntity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
