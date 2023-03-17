package com.example.projectprm392_spring2023;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

@Dao
public interface HistoryDAO {
    @Insert
    void insertHistory(History history);

    @Query("SELECT * FROM history LIMIT :limit OFFSET :offset ")
    List<History> getListHistory(int limit, int offset);

    @Delete
    void deleteHistory(History history);

    @Query("SELECT * FROM history WHERE title LIKE '%' || :title || '%' OR title LIKE '%' || :title || '%'")
    List<History> searchHistory(String title);

    @Update
    void updateHistory(History history);
}