package com.standalone.mystocks.handlers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.standalone.mystocks.handlers.generic.TableHelper;
import com.standalone.mystocks.models.Stock;

public class AssetHandler extends TableHelper<Stock> {
    static final int VERSION = 1;
    static final String DB_NAME = "db_mystocks";
    static final String TBL_NAME = "tbl_asset";
    static final String COL_ID = "id";
    static final String COL_SYMBOL = "symbol";
    static final String COL_PRICE = "price";
    static final String COL_SHARES = "shares";


    public AssetHandler(Context context) {
        super(context, DB_NAME, new MetaTable(TBL_NAME,
                new String[]{
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT",
                        COL_SYMBOL + " TEXT",
                        COL_PRICE + " INTEGER",
                        COL_SHARES + " INTEGER"
                }), VERSION);
    }

    @SuppressLint("Range")
    @Override
    public Stock cursorToData(Cursor curs) {
        Stock s = new Stock();
        s.setId(curs.getInt(curs.getColumnIndex(COL_ID)));
        s.setSymbol(curs.getString(curs.getColumnIndex(COL_SYMBOL)));
        s.setPrice(curs.getDouble(curs.getColumnIndex(COL_PRICE)));
        s.setShares(curs.getInt(curs.getColumnIndex(COL_SHARES)));
        return s;
    }

    @Override
    public ContentValues convertToContentValues(Stock s) {
        ContentValues cv = new ContentValues();
        cv.put(COL_SYMBOL, s.getSymbol());
        cv.put(COL_PRICE, s.getPrice());
        cv.put(COL_SHARES, s.getShares());
        return cv;
    }

    public void insert(Stock s) {
        db.insert(TBL_NAME, null, convertToContentValues(s));
    }

    public void update(Stock s) {
        db.update(TBL_NAME, convertToContentValues(s), COL_ID + " = ?", new String[]{String.valueOf(s.getId())});
    }

    public void remove(int id) {
        db.delete(TBL_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
