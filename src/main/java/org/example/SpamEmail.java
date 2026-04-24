package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;

import java.util.Arrays;

public class SpamEmail extends Email {
    public SpamEmail() {
        super("");
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase finalProjDb = mongoClient.getDatabase("ApplicationSim3000");
            MongoCollection<Document> emailCollection = finalProjDb.getCollection("spam_subj");

            AggregateIterable<Document> randomEmailDocs = emailCollection.aggregate(Arrays.asList(Aggregates.sample(1)));
            Document randomEmailDoc = randomEmailDocs.first();
            this.subject = randomEmailDoc.getString("spam_subj_text");
        }
    }
}
