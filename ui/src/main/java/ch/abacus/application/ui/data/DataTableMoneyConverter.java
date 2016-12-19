package ch.abacus.application.ui.data;

import java.util.Currency;
import java.util.Locale;

import org.springframework.format.number.CurrencyStyleFormatter;

import com.vaadin.spring.annotation.SpringComponent;

import ch.abacus.application.common.data.Money;

@SpringComponent
public class DataTableMoneyConverter implements DataTableConverter<Money> {

	@Override
	public String convertToString(Locale locale, Money value) {
		CurrencyStyleFormatter formatter = new CurrencyStyleFormatter();
		formatter.setCurrency(Currency.getInstance(value.getCurrencyCode()));
		return formatter.print(value.getAmount(), locale);
	}
}
