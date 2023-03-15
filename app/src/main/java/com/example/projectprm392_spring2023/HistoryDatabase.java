package com.example.projectprm392_spring2023;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.room.Room;

    @Database(entities = {History.class}, version = 1)
    public abstract class HistoryDatabase extends RoomDatabase {
        private static final String DATABASE_NAME = "history.db";
        private static HistoryDatabase instance;

        public static synchronized HistoryDatabase getInstance(Context context){
            if(instance == null){
                instance = Room.databaseBuilder(context.getApplicationContext(), HistoryDatabase.class, DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
            return instance;
        }
        public abstract HistoryDAO historyDAO();
    }