package org.study.spring_batch_study.jobs.task01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class GreetingTasklet implements Tasklet, InitializingBean {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("---- GreetingTasklet execute ----");
        log.info("GreetingTasklet : {}, {}", contribution, chunkContext);
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("---- GreetingTasklet afterPropertiesSet ----");
    }
}
