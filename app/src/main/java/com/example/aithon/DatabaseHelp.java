package com.example.aithon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelp extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "aithon.db";
    static final String TABLE_NAME = "info";
    static final String COLS_2 = "name";
    static final String COLS_3 = "email";
    static final String COLS_4 = "number";
    DatabaseHelp(Context context) {
        super(context, DATABASE_NAME , null , 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT , email TEXT , number TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
