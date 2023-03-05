package com.standalone.mystocks.handlers.generic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class DatabaseManager {
    @SuppressLint("StaticFieldLeak")
    private static OpenDB sqliteOpenInstance;

    public static SQLiteDatabase getDatabase(Context context) {
        if (sqliteOpenInstance == null) {
            try {
                sqliteOpenInstance = new OpenDB(context, getProperty(context, "database_name"), Integer.parseInt(getProperty(context, "database_version")));
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return sqliteOpenInstance.getDatabase();
    }

    public static void importDatabase(Context context, String filename) {
        try {
            //database path
            final String databasePath = context.getDatabasePath(getProperty(context, "database_name")).toString();
            File dbFile = new File(databasePath);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(filename);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(context, "Backup Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void exportDatabase(Context context, String filename) {
        try {
            //database path
            final String databasePath = context.getDatabasePath(getProperty(context, "database_name")).toString();
            File dbFile = new File(filename);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(databasePath);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(context, "Import Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Unable to import database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private static String getProperty(Context context, String key) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = context.getAssets().open("config.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }
}
