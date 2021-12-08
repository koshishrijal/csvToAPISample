package com.kosh.csvrestapi.exception;

public class MoneyProcessorException extends Exception {

    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public MoneyProcessorException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
