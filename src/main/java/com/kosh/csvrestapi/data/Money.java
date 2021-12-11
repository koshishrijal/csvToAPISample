package com.kosh.csvrestapi.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kosh.csvrestapi.exception.MoneyProcessorException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class Money {
    private final String currency;
    private final BigDecimal amount;

    @Override
    public String toString() {
        return currency + amount;
    }

    public Money(String income) throws MoneyProcessorException {
        if (income == null || income.isEmpty() || income.length() < 2) {
            log.debug("Error for income: {}",income);
            throw new MoneyProcessorException("Income must be in format currencyAmount, eg $200 ",400);
        }
        this.currency = income.substring(0, 1);
        if (Character.getType(this.currency.charAt(0)) != Character.CURRENCY_SYMBOL) {
            log.debug("Error for income: {}",income);
            throw new MoneyProcessorException("Invalid currency symbol '" + this.currency+"', should be in format currencyAmount, eg $200",400);
        }
        try {
            this.amount = new BigDecimal(income.substring(1));
        } catch (NumberFormatException e) {
            log.debug("Error Number format for  income: {}",income);
            throw new MoneyProcessorException("Invalid income " + income.substring(1),400);
        }
    }

    @JsonGetter("income")
    public String getStrValue() {
        return currency + amount;
    }

    @JsonIgnore
    public String getCurrency() {
        return currency;
    }

    @JsonIgnore
    public BigDecimal getAmount() {
        return amount;
    }

}
