package com.example.tomasznosal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gohilukk on 21.12.13.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "expenditureManager";

    // Contacts table name
    public static final String TABLE_EXPENDITURES = "expenditures";
    public static final String TABLE_TYPE_OF_EXPENDITURE = "typeOfExpenditure";

    // Expenditures Table Columns names
    public static final String DATE = "date";
    public static final String TYPE_ID = "type_id";
    public static final String NAME = "name";
    public static final String VALUE = "value";

    // Type of expenditure Table Columns names
    public static final String ID = "id";
    public static final String TYPE = "type";

    // Query to create table type of expenditure
    private static final String CREATE_TABLE_TYPE_OF_EXPENDITURE = "CREATE TABLE " + TABLE_TYPE_OF_EXPENDITURE + " ("
            + ID + " INTEGER PRIMARY KEY, " + TYPE + " TEXT)";

    // Query to create table expenditure
    private static final String CREATE_TABLE_EXPENDITURES = "CREATE TABLE " + TABLE_EXPENDITURES + " (" +
            ID + " INTEGER PRIMARY KEY," + DATE + " DATE," + TYPE_ID + " INTEGER," +
            NAME + " TEXT," + VALUE + " FLOAT," + "FOREIGN KEY("+ TYPE_ID +") REFERENCES " +
            TABLE_TYPE_OF_EXPENDITURE + "(" + ID + "))";

    // Query to drop tables
    private static final String DROP_TABLE_OF_EXPENDITURE = "DROP TABLE IF EXISTS " + TABLE_TYPE_OF_EXPENDITURE;
    private static final String DROP_TABLE_EXPENDITURE = "DROP TABLE IF EXISTS " + TABLE_EXPENDITURES;

    // Query to enable foreign key
    private static final String ENABLE_FOREIGN_KEY = "PRAGMA foreign_keys=ON;";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TYPE_OF_EXPENDITURE);
        db.execSQL(CREATE_TABLE_EXPENDITURES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_OF_EXPENDITURE);
        db.execSQL(DROP_TABLE_EXPENDITURE);

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
