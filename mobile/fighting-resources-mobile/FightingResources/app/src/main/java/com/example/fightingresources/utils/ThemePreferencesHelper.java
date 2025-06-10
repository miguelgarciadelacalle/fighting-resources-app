package com.example.fightingresources.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ThemePreferencesHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "app_config.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE = "preferences";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    public ThemePreferencesHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                KEY + " TEXT PRIMARY KEY, " +
                VALUE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void saveTheme(String theme) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY, "theme");
        values.put(VALUE, theme);
        db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public String getSavedTheme() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{VALUE}, KEY + "=?", new String[]{"theme"},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String value = cursor.getString(0);
            cursor.close();
            db.close();
            return value;
        }
        return "light";
    }
}
