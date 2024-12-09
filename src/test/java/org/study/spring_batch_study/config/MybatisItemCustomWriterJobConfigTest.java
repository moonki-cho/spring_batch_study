package org.study.spring_batch_study.config;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.study.spring_batch_study.service.CustomService;
import org.study.spring_batch_study.writer.CustomItemWriter;

import java.time.LocalDateTime;


@EnableAutoConfiguration
@SpringBatchTest
@SpringBootTest(classes = {MybatisItemCustomWriterJobConfig.class, CustomService.class, CustomItemWriter.class})
class MybatisItemCustomWriterJobConfigTest {
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void runJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLocalDateTime("currentLocalDataTime", LocalDateTime.now())
				.toJobParameters();

		jobLauncherTestUtils.launchJob(jobParameters);
	}
}