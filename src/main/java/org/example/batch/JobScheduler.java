package org.example.batch;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job importProjectJob;

    // Spring automatically injects the JobLauncher and your Job here
    public JobScheduler(JobLauncher jobLauncher, Job importProjectJob) {
        this.jobLauncher = jobLauncher;
        this.importProjectJob = importProjectJob;
    }

    // Run this method every 10,000 milliseconds (10 seconds)
    @Scheduled(fixedRate = 10000)
    public void runJob() throws Exception {

        // Create a unique parameter using the current time
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        System.out.println("Starting the Batch Job at: " + System.currentTimeMillis());

        // Launch the job!
        jobLauncher.run(importProjectJob, params);
    }
}