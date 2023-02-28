package com.standalone.mystocks.handlers.generic;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OpenHelper<T extends TableHelper> extends SQLiteOpenHelper {

    private List<T> tableHelpers;

    public OpenHelper(Context context, String dbName, int version) {
        super(context, dbName, null, version);
        tableHelpers = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (T t : tableHelpers) {
            t.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (T t : tableHelpers) {
            t.onUpgrade(db, oldVersion, newVersion);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (T t : tableHelpers) {
            t.onDowngrade(db, oldVersion, newVersion);
        }
    }

    public void addTableHelper(T t) {
        tableHelpers.add(t);
    }
}
