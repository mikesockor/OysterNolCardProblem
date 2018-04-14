package com.adfg.repository;

import com.adfg.domain.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Repository
public interface CardRepository extends ReactiveMongoRepository<Card, String> {
    Mono<Card> findById(String id);

    Flux<Card> findByCheckInTimeGreaterThan(Date date);
}
