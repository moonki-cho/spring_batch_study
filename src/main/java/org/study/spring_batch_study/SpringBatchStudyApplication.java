package org.study.spring_batch_study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableBatchProcessing  // spring batch 모드로 동작하게 해주는 애노테이션 (자동 스키마 생성이 되지 않고 spring batch 5.0 이상부턴 붙이는 것이 불필요하여 주석처리)
@SpringBootApplication
public class SpringBatchStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchStudyApplication.class, args);
    }

}
