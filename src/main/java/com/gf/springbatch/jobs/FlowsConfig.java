package com.gf.springbatch.jobs;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class FlowsConfig {

    @Bean
    public Flow flowStep1And2(
            @Autowired Step step1,
            @Autowired Step step2
    ) {
        return new FlowBuilder<SimpleFlow>("flowStep1And2")
                .start(new FlowBuilder<SimpleFlow>("flowStep1").start(step1).build())
                .next(new FlowBuilder<SimpleFlow>("flowStep2").start(step2).build())
                .build();
    }

    @Bean
    public Flow flowStep3And4(
            @Autowired Step step3,
            @Autowired Step step4
    ) {
        return new FlowBuilder<SimpleFlow>("flowStep3And4")
                .split(new SimpleAsyncTaskExecutor())
                .add(new FlowBuilder<SimpleFlow>("flowStep3").start(step3).build(),
                        new FlowBuilder<SimpleFlow>("flowStep4").start(step4).build())
                .build();
    }

}
