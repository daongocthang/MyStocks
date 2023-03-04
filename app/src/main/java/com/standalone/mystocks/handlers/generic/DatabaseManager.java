package com.standalone.mystocks.handlers.generic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.standalone.mystocks.constant.Config;

public class DatabaseManager {
    private static OpenDB instance;

    public static SQLiteDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = new OpenDB(context, Config.DATABASE_NAME, Config.DATABASE_VERSION);
        }
        return instance.getDatabase();
    }
}
