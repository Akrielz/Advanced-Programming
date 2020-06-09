package com.example.sgbd.Controllers;

import java.util.List;

public class Question {
    private String id;
    private String email;
    private String questionId;
    private String questionCount;
    private String showAnswers;
    private String realAnswers;
    private String givenAnswers;

    private String questionDescription;
    private List<String> answersDescription;

    public Question(double score){
        questionDescription = "You have accomplished " + score + " points!";
    }
    public Question(String explanations) {questionDescription = explanations;}

    public Question(String id, String email, String questionId, String questionCount, String showAnswers, String realAnswers) {
        this.id = id;
        this.email = email;
        this.questionId = questionId;
        this.questionCount = questionCount;
        this.showAnswers = showAnswers;
        this.realAnswers = realAnswers;
    }

    public Question(String id, String email, String questionId, String questionCount, String showAnswers, String realAnswers, String givenAnswers) {
        this.id = id;
        this.email = email;
        this.questionId = questionId;
        this.questionCount = questionCount;
        this.showAnswers = showAnswers;
        this.realAnswers = realAnswers;
        this.givenAnswers = givenAnswers;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public void setAnswersDescription(List<String> answersDescription) {
        this.answersDescription = answersDescription;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public List<String> getAnswersDescription() {
        return answersDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(String questionCount) {
        this.questionCount = questionCount;
    }

    public String getShowAnswers() {
        return showAnswers;
    }

    public void setShowAnswers(String showAnswers) {
        this.showAnswers = showAnswers;
    }

    public String getRealAnswers() {
        return realAnswers;
    }

    public void setRealAnswers(String realAnswers) {
        this.realAnswers = realAnswers;
    }

    public String getGivenAnswers() {
        return givenAnswers;
    }

    public void setGivenAnswers(String givenAnswers) {
        this.givenAnswers = givenAnswers;
    }
}
