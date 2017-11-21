package com.adfg.repository;

import com.adfg.domain.StationEntity;
import org.springframework.data.repository.CrudRepository;

public interface StationRepository extends CrudRepository<StationEntity, Long>{

    StationEntity findByNameAndZone(String name, Integer zone);

}
