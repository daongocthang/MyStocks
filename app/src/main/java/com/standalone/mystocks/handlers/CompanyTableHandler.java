package com.standalone.mystocks.handlers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import com.standalone.mystocks.handlers.generic.OpenDB;
import com.standalone.mystocks.handlers.generic.SqliteTableHandler;
import com.standalone.mystocks.models.DataStock;

public class CompanyTableHandler extends SqliteTableHandler<DataStock> {
    static final String TBL_NAME = "tbl_company";
    static final String COL_ID = "id";
    static final String COL_SYMBOL = "symbol";
    static final String COL_NAME = "short_name";

    public CompanyTableHandler(OpenDB openDB) {
        super(openDB, new MetaTable(TBL_NAME, new String[]{
                COL_ID + " TEXT PRIMARY KEY",
                COL_SYMBOL + " TEXT",
                COL_NAME + " TEXT",
        }));
    }

    @SuppressLint("Range")
    @Override
    public DataStock cursorToData(Cursor curs) {
        DataStock d = new DataStock();
        d.setStockNo(curs.getString(curs.getColumnIndex(COL_ID)));
        d.setSymbol(curs.getString(curs.getColumnIndex(COL_SYMBOL)));
        d.setShortName(curs.getString(curs.getColumnIndex(COL_NAME)));

        return d;
    }

    @Override
    public ContentValues convertToContentValues(DataStock d) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, d.getStockNo());
        cv.put(COL_SYMBOL, d.getSymbol());
        cv.put(COL_NAME, d.getShortName());

        return cv;
    }

    public void insert(DataStock d) {
        db.insert(TBL_NAME, null, convertToContentValues(d));
    }
}
