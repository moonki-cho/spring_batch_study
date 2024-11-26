package org.study.spring_batch_study.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerForMybatis {
    private Long id;
    private String name;
    private int age;
    private String gender;
}
