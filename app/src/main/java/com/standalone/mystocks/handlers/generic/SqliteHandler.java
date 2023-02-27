package com.standalone.mystocks.handlers.generic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class SqliteHandler<T> extends SQLiteOpenHelper {
    protected String dbName;
    protected MetaTable table;
    protected SQLiteDatabase db;


    public SqliteHandler(Context context, String dbName, MetaTable metaTable, int Version) {
        super(context, dbName, null, Version);
        this.table = metaTable;
        this.dbName = dbName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table.getCreateTableStmt());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(table.getDropTableStmt());
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void openDb() {
        db = this.getWritableDatabase();
    }

    public abstract T cursorToData(Cursor cursor);

    public abstract ContentValues convertToContentValues(T t);

    public List<T> fetchAll() {
        List<T> res = new ArrayList<>();
        Cursor curs = null;
        db.beginTransaction();
        try {
            curs = db.query(table.getName(), null, null, null, null, null, null);
            if (curs != null) {
                if (curs.moveToFirst()) {
                    do {
                        res.add(cursorToData(curs));
                    } while (curs.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert curs != null;
            curs.close();
        }
        return res;
    }

    public static class MetaTable {
        private final String tableName;
        private final String[] colDefinitions;

        public MetaTable(String tableName, String[] colDefinitions) {
            this.tableName = tableName;
            this.colDefinitions = colDefinitions;
        }

        public String getCreateTableStmt() {
            return "CREATE TABLE " + tableName + "(" + String.join(", ", colDefinitions) + ");";
        }

        public String getDropTableStmt() {
            return "DROP TABLE IF EXISTS " + tableName;
        }


        public String getName() {
            return tableName;
        }
    }
}
