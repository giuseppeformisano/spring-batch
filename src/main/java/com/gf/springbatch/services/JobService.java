package com.gf.springbatch.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobOperator jobOperator;

    @Autowired
    JobExplorer jobExplorer;

    @Autowired
    JobRegistry jobRegistry;

    public void runJob(String jobName, Boolean errorStep2) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addJobParameter("errorStep2", errorStep2, Boolean.class)
                .toJobParameters();
        try {
            jobLauncher.run(jobRegistry.getJob(jobName), jobParameters);
        } catch (JobInstanceAlreadyCompleteException e) {
            jobOperator.startNextInstance("job");
        }
    }

}
