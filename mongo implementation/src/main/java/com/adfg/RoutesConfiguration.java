package com.adfg;

import com.adfg.domain.Card;
import com.adfg.domain.Transaction;
import com.adfg.repository.CardRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RoutesConfiguration {

    @Bean
    RouterFunction<?> card(CardRepository cardRepository) {
        return nest(path("/card"), nest(accept(MediaType.APPLICATION_JSON),
                route(
                        POST("/"), request -> {
                            cardRepository.insert(request.bodyToMono(Card.class));
                            return ok().build();
                        })));
    }

    @Bean
    RouterFunction<?> transaction(TransactionService transactionService) {
        return nest(path("/transaction"), nest(accept(MediaType.APPLICATION_JSON),
                route(
                        POST("/"), request -> ok().body(fromObject(
                                transactionService.proceed(request.bodyToMono(Transaction.class))
                        ))
                ).andRoute(GET("/"), request -> ok().body(fromObject(
                        transactionService.getCardReport(
                                request.queryParam("hours").orElse("0"),
                                request.queryParam("cardId").orElse("0")))
                )))
        );
    }
}