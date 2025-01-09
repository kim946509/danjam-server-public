package com.example.danjamserver.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.danjamserver.chat.repository")
public class MongoRepositoriesConfiguration {
}
