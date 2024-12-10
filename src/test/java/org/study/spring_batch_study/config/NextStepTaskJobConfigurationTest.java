package org.study.spring_batch_study.config;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@EnableAutoConfiguration
@SpringBatchTest
@SpringBootTest(classes = NextStepTaskJobConfiguration.class)
class NextStepTaskJobConfigurationTest {
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