package com.adfg.rest;

/**
 * @author S750976
 */
public class BalanceIsBelowException extends IllegalArgumentException {
    public BalanceIsBelowException(String message) {
        super(message);
    }
}
