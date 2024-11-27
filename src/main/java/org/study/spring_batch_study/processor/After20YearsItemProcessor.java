package org.study.spring_batch_study.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.study.spring_batch_study.model.CustomerForMybatis;

@Slf4j
public class After20YearsItemProcessor implements ItemProcessor<CustomerForMybatis, CustomerForMybatis> {
	@Override
	public CustomerForMybatis process(CustomerForMybatis item) {
		log.info("After20YearsItemProcessor - Customer : {}", item);
		item.setAge(item.getAge() + 20);

		return item;
	}
}
