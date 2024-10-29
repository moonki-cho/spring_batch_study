package org.study.spring_batch_study.chater4.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CompositeIndex {
	private String indexName;
	private List<String> values;
}
