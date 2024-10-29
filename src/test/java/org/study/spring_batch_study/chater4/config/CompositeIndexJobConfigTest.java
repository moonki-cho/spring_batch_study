package org.study.spring_batch_study.chater4.config;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoConfiguration
@SpringBatchTest
@SpringBootTest(classes = CompositeIndexJobConfig.class)
class CompositeIndexJobConfigTest {
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void runJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("year", 2024L)
				.addLong("month", 8L)
				.toJobParameters();

		jobLauncherTestUtils.launchJob(jobParameters);
	}
}