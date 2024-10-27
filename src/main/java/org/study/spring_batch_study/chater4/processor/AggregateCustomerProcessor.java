package org.study.spring_batch_study.chater4.processor;

import org.springframework.batch.item.ItemProcessor;
import org.study.spring_batch_study.chater4.model.Customer;

import java.util.concurrent.ConcurrentHashMap;

public class AggregateCustomerProcessor implements ItemProcessor<Customer, Customer> {
    ConcurrentHashMap<String, Integer> aggregateCustomers;

    public AggregateCustomerProcessor(ConcurrentHashMap<String, Integer> aggregateCustomers) {
        this.aggregateCustomers = aggregateCustomers;
    }

    @Override
    public Customer process(Customer item) throws Exception {
        aggregateCustomers.putIfAbsent("TOTAL_CUSTOMERS", 0);
        aggregateCustomers.putIfAbsent("TOTAL_AGES", 0);

        aggregateCustomers.put("TOTAL_CUSTOMERS", aggregateCustomers.get("TOTAL_CUSTOMERS") + 1);
        aggregateCustomers.put("TOTAL_AGES", aggregateCustomers.get("TOTAL_AGES") + item.getAge());
        return item;
    }
}
