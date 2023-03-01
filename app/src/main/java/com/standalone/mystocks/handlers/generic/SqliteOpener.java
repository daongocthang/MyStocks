package com.standalone.mystocks.handlers.generic;

import android.database.sqlite.SQLiteDatabase;

public interface SqliteOpener {
    public void open(SQLiteDatabase db);
}
