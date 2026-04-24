package org.example;

public class BinaryQuestion extends Question {
    String answer1;
    String answer2;

    public BinaryQuestion(String prompt, String answer1, String answer2) {
        this.prompt = prompt;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    public String getAnswer1() {
        return this.answer1;
    }

    public String getAnswer2() {
        return this.answer2;
    }
}
