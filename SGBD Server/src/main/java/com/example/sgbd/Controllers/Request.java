package com.example.sgbd.Controllers;

public class Request {
    private String email;
    private String answer;
    private String hashCode;

    public Request() {
    }

    public Request(String email) {
        this.email = email;
    }

    public Request(String email, String answer) {
        this.email = email;
        this.answer = answer;
    }

    public Request(String email, String answer, String hashCode) {
        this.email = email;
        this.answer = answer;
        this.hashCode = hashCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getEmail() {
        return email;
    }

    public String getAnswer() {
        return answer;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}
