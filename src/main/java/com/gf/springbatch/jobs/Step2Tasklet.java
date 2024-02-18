package com.gf.springbatch.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class Step2Tasklet implements Tasklet {

    final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Inizio step 2");

        ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
        ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();
        JobParameters jobParameters = contribution.getStepExecution().getJobParameters();

        if (jobParameters.getParameters().containsKey("errorStep2") &&
                (Boolean) jobParameters.getParameter("errorStep2").getValue())
            throw new RuntimeException("Forced error in step 2");
        else {
            logger.info("Fine step 2");
            return RepeatStatus.FINISHED;
        }
    }

}
