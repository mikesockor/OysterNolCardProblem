package com.adfg.repository;

import com.adfg.domain.CardEntity;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public interface CardRepository extends CrudRepository<CardEntity, Long>{

    CardEntity findCardById(Long id);
    List<CardEntity> findByCheckInTimeGreaterThan(@Temporal(TemporalType.DATE) Date localDateTime);
}
