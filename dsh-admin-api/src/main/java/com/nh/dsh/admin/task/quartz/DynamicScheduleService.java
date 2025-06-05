package com.nh.dsh.admin.task.quartz;

import com.nh.dsh.admin.security.user.SecurityUser;
import com.nh.dsh.admin.task.quartz.job.AutoAuditJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicScheduleService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 添加定时任务
     * @param jobName 任务名称
     * @param cron 任务时间
     */
    public void addJobService( String cron,String jobName,String triggerName, int type, int auditId, int userId) {
        try {
            // 创建任务
            if(type == 0) {
                //进行web自动审核任务
                JobDetail jobDetail = JobBuilder.newJob(AutoAuditJob.class)
                        .withIdentity(jobName, jobName + "_group")
                        .usingJobData("type", type)
                        .build();
                //创建周期
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerName + "_group")
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
                scheduler.scheduleJob(jobDetail, trigger);
            }
            if(type == 1) {
                //进行短视频、离线视频、图书文字、图书插画审核
                JobDetail jobDetail = JobBuilder.newJob(AutoAuditJob.class)
                        .withIdentity(jobName, jobName + "_group")
                        .usingJobData("type", type)
                        .usingJobData("auditId", auditId)
                        .usingJobData("userId", userId)
                        .build();
                //创建周期
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerName + "_group")
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
                scheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 动态修改
     * @param newCron 新的定时周期
     * @param triggerName 触发器分组
     */
    public void updateJobService(String newCron,String triggerName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerName + "_group");
            CronTrigger newTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(newCron))
                    .build();
            scheduler.rescheduleJob(triggerKey, newTrigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查任务是否存在
     * @param jobName 任务名称
     * @return
     */
    public boolean isJobRunning(String jobName) {
        try {
            JobKey jobKey = new JobKey(jobName, jobName + "_group");
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查任务的触发器状态
     * @param triggerName
     * @param triggerGroup
     * @return
     */
    public Trigger.TriggerState getTriggerState(String triggerName, String triggerGroup) {
        try {
            TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
            return scheduler.getTriggerState(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除定时任务
     * @param jobName
     * @param triggerName
     * @return
     */
    public boolean deleteJobTask(String jobName, String triggerName) {
        TriggerKey triggerKey = new TriggerKey(triggerName, triggerName + "_group");

        try {
            if (scheduler.checkExists(triggerKey)) {
                scheduler.unscheduleJob(triggerKey); // 删除 Trigger
                JobKey jobKey = new JobKey(jobName, jobName + "_group");
                boolean b = scheduler.deleteJob(jobKey);// 删除对应的 Job
                return b;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }
}
