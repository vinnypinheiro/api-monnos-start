package com.api.monnos.repository;

import com.api.monnos.model.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
public interface PlanetRepository extends MongoRepository<Planet, String> {

    Planet findByNome(String nome);

    Planet findById(long id);

}
