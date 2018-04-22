package com.adfg.rest;

import com.adfg.service.TransactionService;
import com.adfg.domain.Card;
import com.adfg.domain.Transaction;
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

    private CardRepository     cardRepository;
    private TransactionService transactionService;

    @Autowired
    public RouterHandler(CardRepository cardRepository, TransactionService transactionService) {
        this.cardRepository = cardRepository;
        this.transactionService = transactionService;
    }

    Mono<ServerResponse> saveCard(final ServerRequest request) {

        return request.bodyToMono(Card.class)
                .flatMap(rbm ->
                        cardRepository.findById(rbm.getId())
                                .flatMap(ex -> {
                                    ex.setBalance(ex.getBalance() + rbm.getBalance());
                                    return cardRepository.save(ex)
                                            .flatMap(fex -> ok().body(BodyInserters.fromObject(fex)));
                                })
                                .switchIfEmpty(cardRepository.save(rbm)
                                        .flatMap(fex -> ok().body(BodyInserters.fromObject(fex)))));
    }

    Mono<ServerResponse> proceedTransaction(final ServerRequest request) throws BalanceIsBelowException, AlreadyCheckedInException {
        return transactionService.proceed(request.bodyToMono(Transaction.class));
    }

    Mono<ServerResponse> getTransactions(final ServerRequest request) {
        return ServerResponse.ok()
                .body(transactionService.getCardReport(
                        request.queryParam("hours").orElse("0"),
                        request.queryParam("cardId").orElse("0")), Transaction.class);
    }
}
