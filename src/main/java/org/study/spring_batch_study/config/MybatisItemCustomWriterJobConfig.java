package org.study.spring_batch_study.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
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
import org.study.spring_batch_study.writer.CustomItemWriter;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class MybatisItemCustomWriterJobConfig {
	public static final int CHUNK_SIZE = 100;
	public static final String ENCODING = "UTF-8";
	public static final String MY_BATIS_ITEM_WRITER = "MY_BATIS_ITEM_WRITER";

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Autowired
	private CustomItemWriter customItemWriter;

	@Bean
	public FlatFileItemReader<CustomerForMybatis> flatFileItemReader() {

		return new FlatFileItemReaderBuilder<CustomerForMybatis>()
				.name("FlatFileItemReader")
				.resource(new ClassPathResource("./customer.csv"))
				.encoding(ENCODING)
				.delimited().delimiter(",")
				.names("name", "age", "gender")
				.targetType(CustomerForMybatis.class)
				.build();
	}

	@Bean
	public Step flatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.info("------------------ Init flatFileStep -----------------");

		return new StepBuilder("flatFileStep", jobRepository)
				.<CustomerForMybatis, CustomerForMybatis>chunk(CHUNK_SIZE, transactionManager)
				.reader(flatFileItemReader())
				.writer(customItemWriter)
				.build();
	}

	@Bean
	public Job flatFileJob(Step flatFileStep, JobRepository jobRepository) {
		log.info("------------------ Init flatFileJob -----------------");
		return new JobBuilder(MY_BATIS_ITEM_WRITER, jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(flatFileStep)
				.build();
	}
}
