package org.study.spring_batch_study.chater4.config;

import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = FlatFileItemJobConfig.class)
@EnableAutoConfiguration
public class FlatFileItemJobTest {
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void runJob() throws Exception {
		jobLauncherTestUtils.launchJob();
	}
}