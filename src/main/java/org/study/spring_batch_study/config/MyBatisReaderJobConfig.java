package org.study.spring_batch_study.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.study.spring_batch_study.model.CustomerForMybatis;
import org.study.spring_batch_study.processor.After20YearsItemProcessor;
import org.study.spring_batch_study.processor.LowCaseItemProcessor;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Configuration
public class MyBatisReaderJobConfig {
	public static final int CHUNK_SIZE = 2;
	public static final String ENCODING = "UTF-8";
	public static final String MYBATIS_CHUNK_JOB = "MYBATIS_CHUNK_JOB";

	@Autowired
	private DataSource dataSource;
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Bean
	public MyBatisPagingItemReader<CustomerForMybatis> myBatisItemReader() throws Exception {
		return new MyBatisPagingItemReaderBuilder<CustomerForMybatis>()
				.sqlSessionFactory(sqlSessionFactory)
				.pageSize(CHUNK_SIZE)
				.queryId("chapter7.jobs.selectCustomers")
				.build();
	}


	@Bean
	public FlatFileItemWriter<CustomerForMybatis> customerCursorFlatFileItemWriter() {
		return new FlatFileItemWriterBuilder<CustomerForMybatis>()
				.name("customerCursorFlatFileItemWriter")
				.resource(new FileSystemResource("./output/customer_new_v4.csv"))
				.encoding(ENCODING)
				.delimited().delimiter("\t")
				.names("Name", "Age", "Gender")
				.build();
	}

	@Bean
	public CompositeItemProcessor<CustomerForMybatis, CustomerForMybatis> compositeItemProcessor() {
		return new CompositeItemProcessorBuilder<CustomerForMybatis, CustomerForMybatis>()
				.delegates(
						List.of(
								new LowCaseItemProcessor(),
								new After20YearsItemProcessor()
						)
				).build();
	}


	@Bean
	public Step customerJdbcCursorStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
		log.info("------------------ Init customerJdbcCursorStep -----------------");

		return new StepBuilder("customerJdbcCursorStep", jobRepository)
				.<CustomerForMybatis, CustomerForMybatis>chunk(CHUNK_SIZE, transactionManager)
				.reader(myBatisItemReader())
				.processor(compositeItemProcessor())
				.writer(customerCursorFlatFileItemWriter())
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