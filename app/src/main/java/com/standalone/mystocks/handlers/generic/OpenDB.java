package com.standalone.mystocks.handlers.generic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class OpenDB extends SQLiteOpenHelper {
    private final Context context;

    public OpenDB(Context context, String dbName, int version) {
        super(context, dbName, null, version);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @NonNull
    public SQLiteDatabase getDatabase() {
        return getWritableDatabase();
    }

    public void backup(String filename) {

        //database path
        final String databasePath = context.getDatabasePath(getDatabaseName()).toString();

        try {

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

    public void restore(String filename) {

        final String databasePath = context.getDatabasePath(getDatabaseName()).toString();

        try {

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

    @SuppressLint("Range")
    private void dropAllTables(SQLiteDatabase db) {
        Cursor curs = null;
        db.beginTransaction();
        try {
            curs = db.rawQuery("SELECT name FROM sqlite_master WHERE type IS 'table'", null);
            if (curs != null) {
                if (curs.moveToFirst()) {
                    do {
                        db.execSQL("DROP TABLE IF EXISTS " + curs.getString(curs.getColumnIndex("name")));
                    } while (curs.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert curs != null;
            curs.close();
        }
    }
}
