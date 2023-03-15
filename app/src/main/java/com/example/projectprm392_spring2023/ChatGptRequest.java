package com.example.projectprm392_spring2023;

public class ChatGptRequest {
    private String message;
    private String secretKey;

    public ChatGptRequest(String message, String secretKey) {
        this.message = message;
        this.secretKey = secretKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
