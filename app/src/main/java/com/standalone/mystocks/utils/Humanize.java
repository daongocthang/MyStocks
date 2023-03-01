package com.standalone.mystocks.utils;

import android.annotation.SuppressLint;

import java.util.Locale;

public class Humanize {
    public static String doubleComma(double d) {
        return String.format(Locale.US, "%,.2f", d);
    }


    public static String intComma(int i) {
        return String.format(Locale.US, "%,d", i);
    }
}
