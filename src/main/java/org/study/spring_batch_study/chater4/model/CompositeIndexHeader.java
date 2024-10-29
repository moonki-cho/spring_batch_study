package org.study.spring_batch_study.chater4.model;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

import java.io.IOException;
import java.io.Writer;

public class CompositeIndexHeader implements FlatFileHeaderCallback {
	private final Long year;
	private final Long month;

	public CompositeIndexHeader(Long year, Long month) {
		this.year = year;
		this.month = month;
	}

	@Override
	public void writeHeader(Writer writer) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("지표");

		for (int index = 1; index <= month; index++) {
			stringBuilder.append(",");
			stringBuilder.append(year);
			stringBuilder.append(".");
			stringBuilder.append(index);
		}

		writer.write(stringBuilder.toString());
	}
}
