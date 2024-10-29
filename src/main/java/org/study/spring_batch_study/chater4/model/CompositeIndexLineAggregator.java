package org.study.spring_batch_study.chater4.model;

import org.springframework.batch.item.file.transform.LineAggregator;

public class CompositeIndexLineAggregator implements LineAggregator<CompositeIndex> {
	@Override
	public String aggregate(CompositeIndex item) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(item.getIndexName());

		for (String value : item.getValues()) {
			stringBuilder.append(",").append(value);
		}

		return stringBuilder.toString();
	}
}
