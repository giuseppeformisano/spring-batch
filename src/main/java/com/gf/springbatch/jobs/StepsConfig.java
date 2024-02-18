package com.gf.springbatch.jobs;

import com.gf.springbatch.entities.Sale;
import com.gf.springbatch.entities.TypeSale;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepsConfig {

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      @Autowired Step1Tasklet tasklet1) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(tasklet1, transactionManager)
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      @Autowired Step2Tasklet tasklet2) {
        return new StepBuilder("step2", jobRepository)
                .tasklet(tasklet2, transactionManager)
                .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      @Autowired Step3Tasklet tasklet3) {
        return new StepBuilder("step3", jobRepository)
                .tasklet(tasklet3, transactionManager)
                .build();
    }

    @Bean
    public Step step4(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      @Autowired Step4Tasklet tasklet4) {
        return new StepBuilder("step4", jobRepository)
                .tasklet(tasklet4, transactionManager)
                .build();
    }

    @Bean
    public Step step5(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      @Autowired ItemReader<Sale> step5JpaItemReader,
                      @Autowired ItemWriter<Sale> step5FileItemWriter) {
        return new StepBuilder("step5", jobRepository)
                .<Sale, Sale>chunk(1000, transactionManager)
                .reader(step5JpaItemReader)
                .writer(step5FileItemWriter)
                .build();
    }

    @Bean
    public Step step6(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      @Autowired ItemReader<Sale> step6FileItemReader,
                      @Autowired ItemProcessor<Sale, TypeSale> step6ItemProcessor,
                      @Autowired ItemWriter<TypeSale> step6ItemWriter) {
        return new StepBuilder("step6", jobRepository)
                .<Sale, TypeSale>chunk(1000, transactionManager)
                .reader(step6FileItemReader)
                .processor(step6ItemProcessor)
                .writer(step6ItemWriter)
                .build();
    }

}