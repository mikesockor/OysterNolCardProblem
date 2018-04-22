package com.adfg;

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
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

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

        Function<Tuple2<Card, Transaction>, Tuple2<Card, Transaction>> cardTransaction = ctpl -> {
            if (ctpl.getT2().getType().equals("IN")) {

                if (ctpl.getT1().getBalance() < cardMaxFare)
                    throw new BalanceIsBelowException(String.valueOf(cardMaxFare));
                if (ctpl.getT1().getStationType() != null)
                    throw new AlreadyCheckedInException();

                ctpl.getT2().setCost(ctpl.getT1().getBalance());
                ctpl.getT1().setBalance(ctpl.getT1().getBalance() - cardMaxFare);
                ctpl.getT1().setCheckInTime(new Date());
                ctpl.getT1().setStationType(ctpl.getT2().getStationType());
                ctpl.getT1().setStationZone(ctpl.getT2().getStationZone());

            }
            else {
                ctpl.getT1().computeRefund(ctpl.getT1(), ctpl.getT2(), cardMaxFare);
                ctpl.getT1().setCheckInTime(null);
                ctpl.getT1().setStationType(null);
                ctpl.getT1().setStationZone(null);
                ctpl.getT2().setCost(ctpl.getT1().getBalance());
            }
            return Tuples.of(ctpl.getT1(), ctpl.getT2());
        };

        return monoTransaction
            .flatMap(monoTrx ->
                cardRepository.findById(monoTrx.getCardId())
                    .flatMap(crd -> {
                            Tuple2<Card, Transaction> tupleResult = cardTransaction.apply(Tuples.of(crd, monoTrx));

                            return cardRepository.save(tupleResult.getT1()).flatMap(cr -> {
                                tupleResult.getT2().setCheckInTime(new Date());
                                tupleResult.getT2().setCost(cr.getBalance());
                                return transactionRepository.save(tupleResult.getT2()).flatMap(fex -> ok().body(BodyInserters.fromObject(fex)));
                            });
                        }
                    )
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