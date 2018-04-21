package com.adfg;

import com.adfg.domain.Transaction;
import com.adfg.domain.TransactionResponse;
import com.adfg.repository.CardRepository;
import com.adfg.repository.TransactionRepository;
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

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class TransactionService {

    @Value("${custom.card.max-fare}")
    private Double cardMaxFare;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    Mono<ServerResponse> proceed(Mono<Transaction> monoTransaction) {

        return monoTransaction
                .flatMap(monoTrx ->
                        cardRepository.findById(monoTrx.getCardId())
                                .flatMap(crd -> Mono.just(new TransactionResponse())
                                        .flatMap(tr -> {
                                            if (monoTrx.getType().equals("IN")) {

                                                if ((crd.getBalance() - cardMaxFare) < 0 || crd.getStationType() != null) {
                                                    tr.setMessage((crd.getBalance() - cardMaxFare) < 0 ? ("balance is below " + cardMaxFare) : "already checked in: " + crd.getStationType());
                                                } else {
                                                    tr.setCost(crd.getBalance());
                                                    crd.setBalance(crd.getBalance() - cardMaxFare);
                                                    crd.setCheckInTime(new Date());
                                                    crd.setStationType(monoTrx.getStationType());
                                                    crd.setStationZone(monoTrx.getStationZone());
                                                }

                                            } else {
                                                crd.computeRefund(crd, monoTrx, cardMaxFare);
                                                crd.setCheckInTime(null);
                                                crd.setStationType(null);
                                                crd.setStationZone(null);
                                                tr.setCost(crd.getBalance());
                                            }
                                            if (tr.getMessage() == null)
                                                return cardRepository.save(crd).flatMap(cr -> {
                                                    monoTrx.setCheckInTime(new Date());
                                                    return transactionRepository.save(monoTrx)
                                                            .flatMap(fex -> ok().body(BodyInserters.fromObject(fex)));
                                                });
                                            else
                                                return Mono.just(new TransactionResponse(String.format("Card %s error %s", monoTrx.getCardId(), tr.getMessage()), 0))
                                                        .flatMap(fex -> ok().body(BodyInserters.fromObject(fex)));
                                        }))
                                .switchIfEmpty(Mono.just(new TransactionResponse(String.format("Card does`nt exist in system %s", monoTrx.getCardId()), 0))
                                        .flatMap(fex -> ok().body(BodyInserters.fromObject(fex)))));

    }

    Flux<Transaction> getCardReport(String hours, String cardId) {
        return transactionRepository.findByCheckInTimeGreaterThanAndCardId(Date.from(
                LocalDateTime.now()
                        .minusHours(Integer.valueOf(hours))
                        .atZone(ZoneId.systemDefault())
                        .toInstant()), cardId);
    }
}