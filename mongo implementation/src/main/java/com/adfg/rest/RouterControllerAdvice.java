package com.adfg.rest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author S750976
 */
@ControllerAdvice
public class RouterControllerAdvice {

    @ExceptionHandler(BalanceIsBelowException.class)
    public Mono<ServerResponse> balanceIsBelowException(final BalanceIsBelowException exception) {
        return Mono.error(exception);
    }

    @ExceptionHandler(AlreadyCheckedInException.class)
    public Mono<ServerResponse> balanceIsBelowException(final AlreadyCheckedInException exception) {
        return Mono.error(exception);
    }

}
