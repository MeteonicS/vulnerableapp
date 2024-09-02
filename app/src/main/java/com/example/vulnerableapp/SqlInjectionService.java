package com.example.vulnerableapp;

import android.database.sqlite.SQLiteDatabase;

public class SqlInjectionService {
    public static void queryDatabase(String userInput, SQLiteDatabase database) {
        String query = "SELECT * FROM users WHERE username = '" + userInput + "'";
        database.rawQuery(query, null);
    }
}
