package com.adfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoutesConfiguration {

    @Bean
    RouterFunction<?> movieRouterFunction(final RouterHandler handler) {
        return route(POST("/card"), handler::saveCard)
                .andRoute(POST("/transaction"), handler::proceedTransaction)
                .andRoute(GET("/transaction"), handler::getTransactions);
    }
}