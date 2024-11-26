package org.study.spring_batch_study.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.study.spring_batch_study.model.CustomerForMybatis;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class MyBatisWriterJobConfig {
	public static final int CHUNK_SIZE = 2;
	public static final String MYBATIS_CHUNK_JOB = "MYBATIS_CHUNK_JOB2";

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Bean
	public FlatFileItemReader<CustomerForMybatis> flatFileItemReader() {

		return new FlatFileItemReaderBuilder<CustomerForMybatis>()
				.name("FlatFileItemReader")
				.resource(new ClassPathResource("./customer.csv"))
				.encoding("UTF-8")
				.delimited().delimiter(",")
				.names("name", "age", "gender")
				.targetType(CustomerForMybatis.class)
				.build();
	}


	@Bean
	public MyBatisBatchItemWriter<CustomerForMybatis> mybatisItemWriter() {
		return new MyBatisBatchItemWriterBuilder<CustomerForMybatis>()
				.sqlSessionFactory(sqlSessionFactory)
				.statementId("chapter7.jobs.insertCustomers")
				.build();
	}

	@Bean
	public Step customerJdbcCursorStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
		log.info("------------------ Init customerJdbcCursorStep -----------------");

		return new StepBuilder("customerJdbcCursorStep", jobRepository)
				.<CustomerForMybatis, CustomerForMybatis>chunk(CHUNK_SIZE, transactionManager)
				.reader(flatFileItemReader())
				.writer(mybatisItemWriter())
				.build();
	}

	@Bean
	public Job customerJdbcCursorPagingJob(Step customerJdbcCursorStep, JobRepository jobRepository) {
		log.info("------------------ Init customerJdbcCursorPagingJob -----------------");
		return new JobBuilder(MYBATIS_CHUNK_JOB, jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(customerJdbcCursorStep)
				.build();
	}
}