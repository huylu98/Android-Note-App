package com.example.maintenance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "notesDb";
    public static final String TABLE_NOTES = "maintance";

    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_TASK = "task";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DISTANCE = "distance";

    public NotesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NOTES + "(" +
                KEY_ID + " integer primary key ," +
                KEY_DATE + " text," +
                KEY_TASK + " text," +
                KEY_AMOUNT + " text," +
                KEY_DISTANCE + " text" + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NOTES);
        onCreate(db);
    }
}
