package com.gf.springbatch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobsConfig {

    @Bean
    public Job job(JobRepository jobRepository,
                   @Autowired Flow flowStep1And2,
                   @Autowired Flow flowStep3And4,
                   @Autowired Step step5,
                   @Autowired Step step6
    ) {
        return new JobBuilder("job", jobRepository)
                .start(flowStep1And2)
                .next(flowStep3And4)
                .next(step5)
                .next(step6)
                .build()
                .build();
    }

}
