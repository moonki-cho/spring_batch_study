package org.study.spring_batch_study.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.study.spring_batch_study.model.Customer;

@Slf4j
public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {
	@Override
	public Customer process(Customer item) throws Exception {
		log.info("Item Processor ------------------- {}", item);
		return item;
	}
}
