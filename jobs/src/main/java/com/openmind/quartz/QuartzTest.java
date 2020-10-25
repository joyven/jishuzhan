package com.openmind.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTest {
    public static void main(String[] args) throws SchedulerException {
        // 1、创建调度器Scheduler
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        // 2、创建JobDetail实例，并与RamJob绑定
        JobDetail jobDetail = JobBuilder.newJob(RamJob.class)
                .withDescription("This is a job")
                .withIdentity("job1", "group1")
                .usingJobData("level", "白衣着身，心有锦缎")
                .build();

        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("job2", "春暖花开，平安归来");

        // 3、构建Trigger实例，每隔1s执行一次
        Trigger trigger = TriggerBuilder.newTrigger()
                .withDescription("This is a trigger!")
//                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(3,10))
                .withSchedule(CronScheduleBuilder.cronSchedule("0 22 15 * * ?"))
                .withIdentity("trigger1", "group1")
                .build();

        // 4、将触发器以及调度任务详情绑定到调度器上，调度执行
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();

    }
}
