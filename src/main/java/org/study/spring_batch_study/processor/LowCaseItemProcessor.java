package org.study.spring_batch_study.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.study.spring_batch_study.model.CustomerForMybatis;

@Slf4j
public class LowCaseItemProcessor implements ItemProcessor<CustomerForMybatis, CustomerForMybatis> {
	@Override
	public CustomerForMybatis process(CustomerForMybatis item) {
		log.info("LowCaseItemProcessor - Customer : {}", item);
		item.setName(item.getName().toLowerCase());
		item.setGender(item.getGender().toLowerCase());

		return item;
	}
}
