package com.example.notesApp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
@Profile("!test")
public class MongoAuditingConfig {
    //using in- *@CreatedDate, *@LastModifiedDate
    //using out- *@CreatedBy, *@LastModifiedBy
}
