package org.study.spring_batch_study.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.study.spring_batch_study.model.CustomerForMybatis;
import org.study.spring_batch_study.service.CustomService;

@Slf4j
@Component
public class CustomItemWriter implements ItemWriter<CustomerForMybatis> {
	private final CustomService customService;

	public CustomItemWriter(CustomService customService) {
		this.customService = customService;
	}

	@Override
	public void write(Chunk<? extends CustomerForMybatis> chunk) throws Exception {
		for (CustomerForMybatis customer: chunk) {
			log.info("Call Porcess in CustomItemWriter...");
			customService.processToOtherService(customer);
		}
	}
}
