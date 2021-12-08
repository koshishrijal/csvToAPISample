package com.kosh.csvrestapi.data;

import com.kosh.csvrestapi.exception.MoneyProcessorException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    public void testMoneyConstructor_NoCurrencyException() {
        Assert.assertThrows(MoneyProcessorException.class, () -> new Money("2000"));
    }

    @Test
    public void testMoneyConstructor_NumberFormatException() {
        Assert.assertThrows(MoneyProcessorException.class, () -> new Money("$fghj"));
    }

    @Test
    public void testMoneyConstructor_Amount() {
        try {
            Money money = getMoney();
            Assert.assertEquals(new BigDecimal(2000), money.getAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMoneyConstructor_Currency() {
        Money money = null;
        try {
            money = getMoney();
        } catch (MoneyProcessorException moneyProcessorException) {
            moneyProcessorException.printStackTrace();
        }
        Assert.assertEquals("$", money.getCurrency());
    }

    @Test
    public void testMoneyConstructor_StringValue() {
        Money money = null;
        try {
            money = getMoney();
        } catch (MoneyProcessorException moneyProcessorException) {
            moneyProcessorException.printStackTrace();
        }
        Assert.assertEquals("$2000", money.getStrValue());
    }

    public static Money getMoney() throws MoneyProcessorException {
        return new Money("$2000");
    }

}