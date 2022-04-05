package com.api.monnos.config;

import com.api.monnos.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@Configuration
public class CounterConfiguration {

    @Autowired
    private CounterService counterService;

    @PostConstruct
    private void configureCouter() {

        if(!counterService.collectionExists("Counter")) {
            counterService.createCollection();
        }
    }
}
