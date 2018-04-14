package com.adfg.repository;

import com.adfg.domain.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Date;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findByCheckInTimeGreaterThanAndCardId(Date date, String id);
}