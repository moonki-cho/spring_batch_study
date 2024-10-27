package org.study.spring_batch_study.chater4.model;

public class CustomerLineAggregator implements org.springframework.batch.item.file.transform.LineAggregator<Customer> {
    @Override
    public String aggregate(Customer item) {
        return item.getName() + "," + item.getAge();
    }
}
