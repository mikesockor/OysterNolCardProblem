package com.adfg;

import com.adfg.domain.Transaction;
import com.adfg.domain.TransactionResponse;
import com.adfg.repository.CardRepository;
import com.adfg.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

    Mono<TransactionResponse> proceed(Mono<Transaction> monoTransaction) {

        return monoTransaction
                .flatMap(monoTrx -> cardRepository.findById(monoTrx.getCardId())
                        .flatMap(crd -> Mono.just(new TransactionResponse())
                                .doOnNext(tr -> {
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
                                                cardRepository.save(crd).doOnNext(cr -> {
                                                            monoTrx.setCheckInTime(new Date());
                                                            transactionRepository.save(monoTrx);
                                                        }
                                                );
                                        }
                                )
                        )
                );
    }

    Flux<Transaction> getCardReport(String hours, String cardId) {
        return transactionRepository.findByCheckInTimeGreaterThanAndCardId(Date.from(
                LocalDateTime.now()
                        .minusHours(Integer.valueOf(hours))
                        .atZone(ZoneId.systemDefault())
                        .toInstant()), cardId);
    }
}