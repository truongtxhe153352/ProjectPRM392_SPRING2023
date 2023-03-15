package com.example.projectprm392_spring2023;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "history")

public class History implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int resourceId;
    private String date;
    private String content;

    public History(int id, int resourceId, String date, String content) {
        this.id = id;
        this.resourceId = resourceId;
        this.date = date;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}