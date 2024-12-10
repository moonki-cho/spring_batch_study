package org.study.spring_batch_study.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.study.spring_batch_study.model.CustomerForMybatis;

import java.util.Map;

@Slf4j
@Service
public class CustomService {
	public Map<String, String> processToOtherService(CustomerForMybatis item) {

		log.info("Call API to OtherService....");

		return Map.of("code", "200", "message", "OK");
	}
}
