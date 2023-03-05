package com.standalone.mystocks.handlers.generic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseManager {

    @SuppressLint("StaticFieldLeak")
    private static OpenDB sqliteOpenInstance;

    private static OpenDB getSqliteOpenInstance(Context context) {
        if (sqliteOpenInstance == null) {
            try {
                sqliteOpenInstance = new OpenDB(context, getProperty(context, "database_name"), Integer.parseInt(getProperty(context, "database_version")));
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return sqliteOpenInstance;
    }

    public static SQLiteDatabase getDatabase(Context context) {
        return getSqliteOpenInstance(context).getDatabase();
    }

    public static void importDatabase(Context context, String filename) {
        getSqliteOpenInstance(context).restore(filename);
    }

    public static void exportDatabase(Context context, String filename) {
        getSqliteOpenInstance(context).backup(filename);
    }

    private static String getProperty(Context context, String key) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = context.getAssets().open("config.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }
}
