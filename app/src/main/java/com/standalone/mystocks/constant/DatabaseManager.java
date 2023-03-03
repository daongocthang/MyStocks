package com.standalone.mystocks.constant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.standalone.mystocks.handlers.generic.OpenDB;

public class DatabaseManager {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "db_mystocks";
    private static OpenDB instance;

    public static SQLiteDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = new OpenDB(context, DATABASE_NAME, VERSION);
        }
        return instance.getDatabase();
    }
}
