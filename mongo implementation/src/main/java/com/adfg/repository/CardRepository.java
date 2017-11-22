package com.adfg.repository;

import com.adfg.domain.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface CardRepository extends ReactiveMongoRepository<Card, String>{
    Mono<Card> findById(String id);
    Flux<Card> findByCheckInTimeGreaterThan(Date date);
}
