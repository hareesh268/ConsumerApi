package com.optum.ds.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.optum.ds.repository")
@EnableMongoAuditing
public class MongoConfigConnection {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Bean(name="mongoDBConnections")
    public MongoClient mongoClient() {
        return MongoClients.create(connectionString);
    }
}

