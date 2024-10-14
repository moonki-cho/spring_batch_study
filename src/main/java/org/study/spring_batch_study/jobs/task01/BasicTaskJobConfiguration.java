package org.study.spring_batch_study.jobs.task01;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BasicTaskJobConfiguration {
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Tasklet greetingTasklet() {
        return new GreetingTasklet();
    }

    @Bean
    public Step myStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        log.info("----- init myStep -----");

        return new StepBuilder("myStep", jobRepository)
                .tasklet(greetingTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Job myJob(Step myStep, JobRepository jobRepository) {
        log.info("----- init myJob -----");

        return new JobBuilder("myJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(myStep)
                .build();
    }
}
