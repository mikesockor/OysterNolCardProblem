package com.adfg;

import com.adfg.domain.Card;
import com.adfg.domain.Transaction;
import com.adfg.repository.CardRepository;
import com.adfg.service.TransactionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.util.function.Function;

@SpringBootApplication
@EnableScheduling
@FunctionScan
@EnableReactiveMongoRepositories
@EnableMongoAuditing
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    Function<Flux<Card>, Flux<Card>> card(CardRepository cardRepository) {
        return ids -> ids
                .flatMap(rbm -> cardRepository.findById(rbm.getId())
                        .flatMap(ex -> {
                            ex.setBalance(ex.getBalance() + rbm.getBalance());
                            return cardRepository.save(ex);
                        })
                        .switchIfEmpty(cardRepository.save(rbm))
                );
    }

    @Bean
    Function<Flux<Transaction>, Flux<Transaction>> transaction(TransactionService transactionService) {
        return transactionService::proceed;
    }

    @Bean
    Function<Flux<Tuple2<String, Double>>, Flux<Transaction>> transactions(TransactionService transactionService) {
        return (ids) -> ids
                .flatMap(ds ->
                        transactionService.getCardReport(ds.getT1(), ds.getT2())
                );
    }
}