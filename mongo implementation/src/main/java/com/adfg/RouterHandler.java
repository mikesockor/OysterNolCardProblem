package com.adfg;

import com.adfg.domain.Card;
import com.adfg.domain.Transaction;
import com.adfg.domain.TransactionResponse;
import com.adfg.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class RouterHandler {

    private CardRepository cardRepository;
    private TransactionService transactionService;

    @Autowired
    public RouterHandler(CardRepository cardRepository, TransactionService transactionService) {
        this.cardRepository = cardRepository;
        this.transactionService = transactionService;
    }

    Mono<ServerResponse> saveCard(final ServerRequest request) {

        return request.bodyToMono(Card.class)
                .flatMap(rbm -> {
                    Mono<Double> doubleMono = cardRepository.findById(rbm.getId())
                            .map(Card::getBalance)
                            .defaultIfEmpty(0.00)
                            .doOnNext(dv -> {
                                rbm.setBalance(rbm.getBalance() + dv);
                                cardRepository.save(rbm);
                            });
                    return doubleMono;
                })
                .flatMap(bi -> ok().body(BodyInserters.fromObject(bi)));
    }

    Mono<ServerResponse> proceedTransaction(final ServerRequest request) {
        return ServerResponse.ok()
                .body(transactionService.proceed(request.bodyToMono(Transaction.class)), TransactionResponse.class);
    }

    Mono<ServerResponse> getTransactions(final ServerRequest request) {
        return ServerResponse.ok()
                .body(transactionService.getCardReport(
                        request.queryParam("hours").orElse("0"),
                        request.queryParam("cardId").orElse("0")), Transaction.class);
    }
}
