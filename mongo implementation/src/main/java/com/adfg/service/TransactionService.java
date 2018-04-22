package com.adfg.service;

import com.adfg.domain.Card;
import com.adfg.domain.Transaction;
import com.adfg.repository.CardRepository;
import com.adfg.repository.TransactionRepository;
import com.adfg.rest.AlreadyCheckedInException;
import com.adfg.rest.BalanceIsBelowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.BiFunction;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class TransactionService {

    @Value("${custom.card.max-fare}")
    private       Double                cardMaxFare;
    private final CardRepository        cardRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    public Mono<ServerResponse> proceed(Mono<Transaction> monoTransaction) throws AlreadyCheckedInException, BalanceIsBelowException {

        BiFunction<Card, Transaction, Mono<Card>> cf1 = (crd, trx) -> {
            if (trx.getType().equals("IN")) {

                if (crd.getBalance() < cardMaxFare)
                    throw new BalanceIsBelowException(String.valueOf(cardMaxFare));
                if (crd.getStationType() != null)
                    throw new AlreadyCheckedInException();

                crd.setBalance(crd.getBalance() - cardMaxFare);
                crd.setCheckInTime(new Date());
                crd.setStationType(trx.getStationType());
                crd.setStationZone(trx.getStationZone());
            } else {
                crd.computeRefund(crd, trx, cardMaxFare);
                crd.setCheckInTime(null);
                crd.setStationType(null);
                crd.setStationZone(null);
            }
            return Mono.just(crd);
        };

        return monoTransaction
            .flatMap(monoTrx -> cardRepository.findById(monoTrx.getCardId())
                .flatMap(crd -> cf1.apply(crd, monoTrx)
                    .flatMap(cc -> cardRepository.save(cc)
                        .flatMap(cr -> {
                            monoTrx.setCheckInTime(new Date());
                            monoTrx.setCost(cr.getBalance());
                            return transactionRepository.save(monoTrx).flatMap(fex -> ok().body(BodyInserters.fromObject(fex)));
                        })))
                .switchIfEmpty(ServerResponse.notFound().build())
            );
    }

    public Flux<Transaction> getCardReport(String hours, String cardId) {
        return transactionRepository.findByCheckInTimeGreaterThanAndCardId(Date.from(
            LocalDateTime.now()
                .minusHours(Integer.valueOf(hours))
                .atZone(ZoneId.systemDefault())
                .toInstant()), cardId);
    }
}