package com.api.monnos.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@EnableMongoRepositories
public class SpringMongoConfiguration {

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(
                new MongoClient("localhost"),
                "planetas"
        );
    }
}
