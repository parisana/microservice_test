package com.swift.consumer.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Parisana
 */
public class MongoDatabaseProvider {

    private static final Logger log= LoggerFactory.getLogger(MongoDatabaseProvider.class);

    private final static String CONFIG_NAME = "mongo";

    private static MongoDatabase mongoDatabase;

    private static void initDataSource() {
        log.info("Initializing mongo database");
        MongoClient mongoClient = new MongoClient(
                new MongoClientURI( "mongodb://user:password@localhost/test_db" ));
        mongoDatabase = mongoClient.getDatabase("test_db");


        log.info("Initializing mongo-db completed successfully");
    }

    public static MongoDatabase getMongoDatabase(){
        if (mongoDatabase==null){
            synchronized (MongoDatabaseProvider.class){
                if (mongoDatabase==null){
                    initDataSource();
                }
            }
        }
        return mongoDatabase;
    }
}