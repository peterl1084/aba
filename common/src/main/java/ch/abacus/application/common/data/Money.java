package ch.abacus.application.common.data;

import java.math.BigDecimal;

public class Money {
    private String currencyCode;
    private BigDecimal amount;

    private Money(String currencyCode, BigDecimal amount) {
        this.currencyCode = currencyCode;
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static Money of(String currencyCode, double amount) {
        return new Money(currencyCode, BigDecimal.valueOf(amount));
    }

    public static Money of(String currencyCode, BigDecimal amount) {
        return new Money(currencyCode, amount);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof Money) {
            return this.getAmount().compareTo(((Money) other).getAmount()) == 0
                    && this.getCurrencyCode().equalsIgnoreCase(((Money) other).getCurrencyCode());
        }

        return false;
    }
}
