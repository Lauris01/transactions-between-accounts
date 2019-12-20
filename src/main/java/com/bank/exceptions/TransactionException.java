package com.bank.exceptions;

public class TransactionException extends RuntimeException {
    public TransactionException(String message) {
        super(message);
    }
    public TransactionException() {
        super("Transaction failed. Try again");
    }
}
