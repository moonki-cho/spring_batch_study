package org.study.spring_batch_study.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.study.spring_batch_study.model.CompositeIndex;
import org.study.spring_batch_study.model.CompositeIndexHeader;
import org.study.spring_batch_study.model.CompositeIndexLineAggregator;
import org.study.spring_batch_study.processor.CompositeIndexProcessor;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class CompositeIndexJobConfig {
	private final ItemProcessor<CompositeIndex, CompositeIndex> itemProcessor = new CompositeIndexProcessor();

	@Bean
	@StepScope
	public FlatFileItemReader<CompositeIndex> compositeIndexFlatFileItemReader(@Value("#{jobParameters[month]}") Long month) {
		DelimitedLineTokenizer lineTokenizer = getCompositeIndexDelimitedLineTokenizer(month);
		DefaultLineMapper<CompositeIndex> defaultLineMapper = getCompositeIndexDefaultLineMapper(lineTokenizer);

		return new FlatFileItemReaderBuilder<CompositeIndex>()
				.name("CompositeIndexFlatFileItemReader")
				.resource(new ClassPathResource("./compositeIndex.csv"))
				.encoding("EUC-KR")
				.linesToSkip(1)
				.lineMapper(defaultLineMapper)
				.targetType(CompositeIndex.class)
				.build();
	}

	private DelimitedLineTokenizer getCompositeIndexDelimitedLineTokenizer(long month) {
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		List<String> names = new ArrayList<>();
		names.add("indexName");

		for (int index = 0; index < month; index++) {
			names.add("val" + index);
		}

		lineTokenizer.setNames(names.toArray(new String[0]));

		return lineTokenizer;
	}

	private DefaultLineMapper<CompositeIndex> getCompositeIndexDefaultLineMapper(DelimitedLineTokenizer lineTokenizer) {
		DefaultLineMapper<CompositeIndex> defaultLineMapper = new DefaultLineMapper<>();
		defaultLineMapper.setLineTokenizer(lineTokenizer);

		defaultLineMapper.setFieldSetMapper(fieldSet -> {
			List<String> values = new ArrayList<>();
			for (int index = 0; index < fieldSet.getFieldCount() - 1; index++) {
				values.add(fieldSet.readString("val" + index));
			}

			return CompositeIndex.builder()
					.indexName(fieldSet.readString("indexName"))
					.values(values)
					.build();
		});

		return defaultLineMapper;
	}

	@Bean
	@StepScope
	public FlatFileItemWriter<CompositeIndex> compositeIndexFlatFileItemWriter(@Value("#{jobParameters['year']}") Long year, @Value("#{jobParameters['month']}") Long month) {
		return new FlatFileItemWriterBuilder<CompositeIndex>()
				.name("compositeIndexFlatFileItemWriter")
				.resource(new FileSystemResource("./output/compositeIndex_new.csv"))
				.encoding("UTF-8")
				.append(false)
				.headerCallback(new CompositeIndexHeader(year, month))
				.lineAggregator(new CompositeIndexLineAggregator())
				.build();
	}

	@Bean
	public Step compositeIndexFlatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		log.info("------------------ Init compositeIndexFlatFileStep -----------------");

		return new StepBuilder("compositeIndexFlatFileStep", jobRepository)
				.<CompositeIndex, CompositeIndex>chunk(10, transactionManager)
				.reader(compositeIndexFlatFileItemReader(null))
				.processor(itemProcessor)
				.writer(compositeIndexFlatFileItemWriter(null, null))
				.build();
	}

	@Bean
	public Job compositeIndexFlatFileJob(Step compositeIndexFlatFileStep, JobRepository jobRepository) {
		log.info("------------------ Init compositeIndexFlatFileJob ----------------");
		return new JobBuilder("compositeIndexFlatFileJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(compositeIndexFlatFileStep)
				.build();
	}
}
