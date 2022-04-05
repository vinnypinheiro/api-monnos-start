package com.api.monnos.repository;

import com.api.monnos.model.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
public interface CounterRepository extends MongoRepository<Counter, String> {

}
