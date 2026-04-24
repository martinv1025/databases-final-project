package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

public class JobApplication {
    String title;
    String description;
    Email email;
    ArrayList<Question> questions;
    int difficulty;
    boolean accepted;

    public JobApplication() {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase finalProjDb = mongoClient.getDatabase("ApplicationSim3000");
            MongoCollection<Document> jobCollection  = finalProjDb.getCollection("job_title_desc");
            MongoCollection<Document> textQuestionCollection  = finalProjDb.getCollection("text_question");
            MongoCollection<Document> binaryQuestionCollection  = finalProjDb.getCollection("binary_question");
            MongoCollection<Document> rejectionEmailCollection  = finalProjDb.getCollection("rej_subj");

            // Assign difficulty and accepted value
            difficulty =  (int)(Math.random() * 5) + 1;
            int questionQty =  (int)(((Math.random()) + 1) * difficulty);
            accepted = (difficulty * 4.0f)/100 > Math.random();

            // Assign title and description
            AggregateIterable<Document> randomJobDocs = jobCollection.aggregate(Arrays.asList(Aggregates.sample(1)));
            Document randomTitleDoc = randomJobDocs.first();
            title = randomTitleDoc.getString("title");
            description = randomTitleDoc.getString("description");

            // Assign rejection email header
            AggregateIterable<Document> randomEmailDocs = jobCollection.aggregate(Arrays.asList(Aggregates.sample(1)));
            Document randomEmailDoc = randomEmailDocs.first();
            email = new Email(randomEmailDoc.getString("rej_subj_text"));

            // populate question array
            for (int i = 0; i < questionQty; i++){
                Question question;
                if (Math.random() < 0.5) {
                    AggregateIterable<Document> randomDocs = textQuestionCollection.aggregate(Arrays.asList(Aggregates.sample(1)));
                    Document randomQDoc = randomDocs.first();
                    String promptValue = randomQDoc.getString("prompt");
                    question = new TextQuestion(promptValue);
                } else {
                    AggregateIterable<Document> randomDocs = binaryQuestionCollection.aggregate(Arrays.asList(Aggregates.sample(1)));
                    Document randomQDoc = randomDocs.first();
                    String promptValue = randomQDoc.getString("prompt");
                    String answer1Value =  randomQDoc.getString("answer1");
                    String answer2Value =  randomQDoc.getString("answer2");
                    question = new BinaryQuestion(promptValue, answer1Value, answer2Value);
                }
                questions.add(question);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public String getEmailSubject() {
        return email.getSubject();
    }
}
