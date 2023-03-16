package com.example.projectprm392_spring2023;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "history")

public class History implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String chatgptResponse;

    private String promptUsed;

    private String imageUri;

    private String title;

    public History(String chatgptResponse, String promptUsed, String imageUri, String title) {
        this.chatgptResponse = chatgptResponse;
        this.promptUsed = promptUsed;
        this.imageUri = imageUri;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChatgptResponse() {
        return chatgptResponse;
    }

    public void setChatgptResponse(String chatgptResponse) {
        this.chatgptResponse = chatgptResponse;
    }

    public String getPromptUsed() {
        return promptUsed;
    }

    public void setPromptUsed(String promptUsed) {
        this.promptUsed = promptUsed;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}