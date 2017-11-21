package com.adfg.repository;

import com.adfg.domain.Transaction;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long>{

    List<Transaction> findByCheckTimeGreaterThanAndCardEntityId(@Temporal(TemporalType.DATE) Date localDateTime, Long id);

}
