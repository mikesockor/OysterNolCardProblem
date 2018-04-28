package com.adfg.rest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * @author S750976
 */
@ControllerAdvice
public class RouterControllerAdvice {

    @ExceptionHandler(BalanceIsBelowException.class)
    public Mono balanceIsBelowException(final BalanceIsBelowException exception) {
        return Mono.error(exception);
    }

    @ExceptionHandler(AlreadyCheckedInException.class)
    public Mono balanceIsBelowException(final AlreadyCheckedInException exception) {
        return Mono.error(exception);
    }

}
