package org.study.spring_batch_study.chater4.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.study.spring_batch_study.chater4.model.CompositeIndex;

import java.math.BigDecimal;

@Slf4j
public class CompositeIndexProcessor implements ItemProcessor<CompositeIndex, CompositeIndex> {
	@Override
	public CompositeIndex process(CompositeIndex item) {
		for (int index = item.getValues().size() - 1; index >= 0; index--) {
			String value = item.getValues().get(index);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(value);
			stringBuilder.append("(");

			if (index == 0) {
				stringBuilder.append("-");
			} else {
				BigDecimal currentValue = new BigDecimal(value);
				BigDecimal beforeValue = new BigDecimal(item.getValues().get(index - 1));

				if (beforeValue.compareTo(BigDecimal.valueOf(0)) == 0) {
					stringBuilder.append("-");
				} else {
					BigDecimal comparedValue = currentValue.subtract(beforeValue);
					BigDecimal ratio = comparedValue.divide(beforeValue.abs(), 3, BigDecimal.ROUND_CEILING);
					stringBuilder.append(ratio.multiply(BigDecimal.valueOf(100)));

				}
			}

			stringBuilder.append(")");
			item.getValues().set(index, stringBuilder.toString());
		}

		return item;
	}
}
