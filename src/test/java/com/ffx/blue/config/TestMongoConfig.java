package com.ffx.blue.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ComponentScan({"com.ffx.blue.article.service"})
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = "com.ffx.blue.article.repository")
public class TestMongoConfig {
//    @Value("${spring.data.mongodb.host}")
//    private String mongoHost;
//
//    @Value("${spring.data.mongodb.port}")
//    private String mongoPort;
//
//    @Value("${spring.data.mongodb.database}")
//    private String mongoDatabase;
//
//    @Bean(name="mongoClient")
//    public MongoClient mongoClient() throws IOException {
//        return new MongoClient(mongoHost, Integer.parseInt(mongoPort));
//    }
//
//    @Autowired
//    @Bean(name="mongoDbFactory")
//    public MongoDbFactory mongoDbFactory(MongoClient mongoClient) {
//        return new SimpleMongoDbFactory(mongoClient, mongoDatabase);
//    }
//
//    @Autowired
//    @Bean(name="mongoTemplate")
//    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
//        return new MongoTemplate(mongoClient, mongoDatabase);
//    }

}