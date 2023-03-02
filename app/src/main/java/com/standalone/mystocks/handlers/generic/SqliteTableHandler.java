package com.standalone.mystocks.handlers.generic;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class SqliteTableHandler<T> implements SqliteOpener {
    protected String dbName;
    protected MetaTable table;
    protected SQLiteDatabase db;

    public SqliteTableHandler(OpenDB openDB, String dbName, MetaTable metaTable) {
        this.table = metaTable;
        this.dbName = dbName;

        openDB.assign(this);
    }

    @Override
    public void open(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(table.getCreateTableStmt());
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
            return "CREATE TABLE IF NOT EXISTS " + tableName + "(" + String.join(", ", colDefinitions) + ");";
        }

        public String getDropTableStmt() {
            return "DROP TABLE IF EXISTS " + tableName;
        }


        public String getName() {
            return tableName;
        }
    }
}
