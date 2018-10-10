package com.exceptionType;

public class BalanceNotEnoughException extends ATMException {
    public BalanceNotEnoughException() {
        super();
    }

    public BalanceNotEnoughException(String message) {
        super(message);
    }

    public BalanceNotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }
}
