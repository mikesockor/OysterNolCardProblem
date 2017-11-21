package com.adfg.repository;

import com.adfg.domain.AgglomerationEntity;
import org.springframework.data.repository.CrudRepository;

public interface AgglomerationRepository extends CrudRepository<AgglomerationEntity, Long>{

    AgglomerationEntity findByName(String name);

}
