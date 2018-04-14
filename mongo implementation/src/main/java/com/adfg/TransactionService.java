package com.adfg;

import com.adfg.domain.Transaction;
import com.adfg.domain.TransactionResponse;
import com.adfg.repository.CardRepository;
import com.adfg.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class TransactionService {

    @Value("${card.maxFare}")
    private Double cardMaxFare;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    TransactionResponse proceed(Mono<Transaction> monoTransaction) {

        Transaction transaction = monoTransaction.block();
        return cardRepository.findById(transaction.getCardId())
                .map(crd -> {
                            TransactionResponse trxResponse = new TransactionResponse();
                            if (transaction.getType().equals("IN")) {

                                if ((crd.getBalance() - cardMaxFare) < 0 || crd.getStationType() != null) {
                                    trxResponse.setMessage((crd.getBalance() - cardMaxFare) < 0 ? ("balance is below " + cardMaxFare) : "already checked in: " + crd.getStationType());
                                } else {
                                    trxResponse.setCost(crd.getBalance());
                                    crd.setBalance(crd.getBalance() - cardMaxFare);
                                    crd.setCheckInTime(new Date());
                                    crd.setStationType(transaction.getStationType());
                                    crd.setStationZone(transaction.getStationZone());
                                }

                            } else {
                                crd.computeRefund(crd, transaction, cardMaxFare);
                                crd.setCheckInTime(null);
                                crd.setStationType(null);
                                crd.setStationZone(null);
                                trxResponse.setCost(crd.getBalance());
                            }
                            if (trxResponse.getMessage() == null)
                                cardRepository.save(crd)
                                        .subscribe(cr -> {
                                                    transaction.setCheckInTime(new Date());
                                                    transactionRepository.save(transaction).subscribe();
                                                }
                                        );
                            return trxResponse;
                        }
                ).block();
    }

    List<Transaction> getCardReport(String hours, String cardId) {
        return transactionRepository.findByCheckInTimeGreaterThanAndCardId(Date.from(
                LocalDateTime.now()
                        .minusHours(Integer.valueOf(hours))
                        .atZone(ZoneId.systemDefault())
                        .toInstant()), cardId)
                .collectList()
                .block();
    }
}