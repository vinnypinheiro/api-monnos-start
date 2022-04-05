package com.api.monnos.service;

import com.api.monnos.model.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Vinicio Pinheiro on 05/04/2022.
 */
@Service
public class CounterService {

    @Autowired
    private MongoOperations mongoOperations;

    public long getNextSequence(String collectionName) {

        Counter counter = mongoOperations.findAndModify(
                query(where("_id").is(collectionName)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                Counter.class);

        return counter.getSeq();
    }

    public boolean collectionExists(String collectionName) {
        return mongoOperations.collectionExists(collectionName);
    }

    public void createCollection() {
       Counter counter = Counter.builder()
                .seq(0)
                 .id("Counter")
                 .build();

        mongoOperations.insert(counter);
    }
}
