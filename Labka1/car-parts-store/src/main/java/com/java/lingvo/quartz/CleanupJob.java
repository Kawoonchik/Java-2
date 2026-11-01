package com.java.lingvo.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class CleanupJob implements Job {

    private final QuartzBusinessService businessService;

    public CleanupJob(QuartzBusinessService businessService) {
        this.businessService = businessService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        businessService.cleanupOldData();
    }
}