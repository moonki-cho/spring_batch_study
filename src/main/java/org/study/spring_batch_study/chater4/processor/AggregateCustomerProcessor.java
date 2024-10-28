package org.study.spring_batch_study.chater4.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.study.spring_batch_study.chater4.model.Customer;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class AggregateCustomerProcessor implements ItemProcessor<Customer, Customer> {
    private static final String CUSTOMERS_KEY = "TOTAL_CUSTOMERS";
    private static final String AGES_KEY = "TOTAL_AGES";

    private final ConcurrentHashMap<String, Integer> aggregateCustomers;

    public AggregateCustomerProcessor(ConcurrentHashMap<String, Integer> aggregateCustomers) {
        this.aggregateCustomers = aggregateCustomers;
    }

    @Override
    public Customer process(Customer item) throws Exception {
        aggregateCustomers.putIfAbsent(CUSTOMERS_KEY, 0);
        aggregateCustomers.putIfAbsent(AGES_KEY, 0);

        aggregateCustomers.put(CUSTOMERS_KEY, aggregateCustomers.get(CUSTOMERS_KEY) + 1);
        aggregateCustomers.put(AGES_KEY, aggregateCustomers.get(AGES_KEY) + item.getAge());

        return item;
    }
}
