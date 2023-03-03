package com.standalone.mystocks.constant;

import android.content.Context;

import com.standalone.mystocks.handlers.generic.OpenDB;

public class Artisan {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "db_mystocks";

    public static OpenDB createOpenDB(Context context) {
        return new OpenDB(context, DATABASE_NAME, VERSION);
    }


    public static final double STOP_LOSS_RATE = 0.07;
}
