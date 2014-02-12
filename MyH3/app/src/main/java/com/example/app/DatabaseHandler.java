package com.example.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gohilukk on 12.02.14.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "hoardManager";

    // Contacts table name
    public static final String TABLE_HOARD = "hoard";

    // Type of hoard Table Columns names
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String IMAGE_PATH = "imgpath";
    public static final String DESCRIPTION = "description";


    // Query to create table hoard
    private static final String CREATE_TABLE_HOARD = "CREATE TABLE " + TABLE_HOARD + " ("
            + ID + " INTEGER PRIMARY KEY, " + NAME + " TEXT, " + IMAGE_PATH + " TEXT, " + DESCRIPTION + " TEXT)";


    // Query to drop tables
    private static final String DROP_TABLE_HOARD = "DROP TABLE IF EXISTS " + TABLE_HOARD;

    // Query to enable foreign key
    private static final String ENABLE_FOREIGN_KEY = "PRAGMA foreign_keys=ON;";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HOARD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_HOARD);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL(ENABLE_FOREIGN_KEY);
        }
    }

}
