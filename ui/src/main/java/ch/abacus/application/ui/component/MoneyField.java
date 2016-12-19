package ch.abacus.application.ui.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.springframework.util.StringUtils;

import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.server.UserError;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import ch.abacus.application.common.data.Money;

public class MoneyField extends CustomField<Money> {
	private static final long serialVersionUID = -1092049429141453981L;

	private TextField textField;
	private Label currencyCode;

	private HorizontalLayout content;
	private Registration valueChangeListener;

	private static final String STYLE_NAME = "money-field";

	public MoneyField(String caption) {
		addStyleName(STYLE_NAME);
		setWidth(100, Unit.PERCENTAGE);

		setCaption(caption);
		content = new HorizontalLayout();
		content.setSpacing(true);
		content.setWidth(100, Unit.PERCENTAGE);

		textField = new TextField();
		textField.setWidth(100, Unit.PERCENTAGE);

		currencyCode = new Label();
		currencyCode.setWidth(50, Unit.PIXELS);

		content.addComponents(textField, currencyCode);
		content.setComponentAlignment(currencyCode, Alignment.MIDDLE_RIGHT);
		content.setExpandRatio(textField, 1);
	}

	private void onValueChange() {
		int cursorPosition = textField.getCursorPosition();
		textField.setComponentError(null);
		if (StringUtils.isEmpty(textField.getValue())) {
			return;
		}

		try {
			BigDecimal parsedValue = parseValue();
			String formattedValue = formatValue(parsedValue);
			valueChangeListener.remove();
			textField.setValue(formattedValue);
			textField.setCursorPosition(cursorPosition);
			textField.addStyleName("currency");
			valueChangeListener = textField.addValueChangeListener(e -> onValueChange());
			fireEvent(new ValueChangeEvent<>(this, true));
		} catch (ParseException e) {
			textField.setComponentError(new UserError("Invalid format", ContentMode.TEXT, ErrorLevel.WARNING));
		}
	}

	private String formatValue(BigDecimal parsedValue) {
		NumberFormat currencyInstance = NumberFormat.getNumberInstance(UI.getCurrent().getLocale());
		currencyInstance.setMaximumFractionDigits(2);
		currencyInstance.setMinimumFractionDigits(2);
		currencyInstance.setRoundingMode(RoundingMode.HALF_EVEN);
		currencyInstance.setGroupingUsed(true);

		return currencyInstance.format(parsedValue);
	}

	private BigDecimal parseValue() throws ParseException {
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(UI.getCurrent().getLocale());
		format.setParseBigDecimal(true);
		return (BigDecimal) format.parse(textField.getValue());
	}

	@Override
	public Money getValue() {
		if (StringUtils.isEmpty(textField.getValue())) {
			return null;
		}

		try {
			return Money.of(currencyCode.getValue(), parseValue());
		} catch (ParseException e) {
			textField.setComponentError(new UserError("Invalid format", ContentMode.TEXT, ErrorLevel.WARNING));
			return null;
		}
	}

	@Override
	protected Component initContent() {
		return content;
	}

	@Override
	protected void doSetValue(Money value) {
		if (valueChangeListener != null) {
			valueChangeListener.remove();
		}

		if (value == null) {
			textField.clear();
			currencyCode.setValue(null);
		} else {
			textField.setValue(formatValue(value.getAmount()));
			currencyCode.setValue(value.getCurrencyCode());
		}

		valueChangeListener = textField.addValueChangeListener(e -> onValueChange());
	}
}
