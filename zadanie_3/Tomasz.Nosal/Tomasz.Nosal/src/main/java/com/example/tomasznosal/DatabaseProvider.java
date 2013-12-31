package com.example.tomasznosal;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by gohilukk on 22.12.13.
 */
public class DatabaseProvider extends ContentProvider {

    private DatabaseHandler db;
    private SQLiteDatabase sqldb;

    public static final String AUTHORITY = "com.example.provider.expenditureManager";

    public static final int TYPES = 100;
    public static final int TYPE_ID = 110;
    public static final String TYPES_BASE_PATH = "types";
    public static final Uri CONTENT_URI_TYPES = Uri.parse("content://" + AUTHORITY
            + "/" + TYPES_BASE_PATH);

    public static final int EXPENDITURES = 120;
    public static final int EXPENDITURE_ID = 130;
    public static final String EXPENDITURES_BASE_PATH = "expenditures";
    public static final Uri CONTENT_URI_EXPENDITURES = Uri.parse("content://" + AUTHORITY
            + "/" + EXPENDITURES_BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, TYPES_BASE_PATH, TYPES);
        sURIMatcher.addURI(AUTHORITY, TYPES_BASE_PATH + "/#", TYPE_ID);
        sURIMatcher.addURI(AUTHORITY, EXPENDITURES_BASE_PATH, EXPENDITURES);
        sURIMatcher.addURI(AUTHORITY, EXPENDITURES_BASE_PATH + "/#", EXPENDITURE_ID);
    }

    private static final String UNKNOWN_URI = "Unknown URI";
    private static final String JOINED_TABLES = DatabaseHandler.TABLE_EXPENDITURES +
            " LEFT OUTER JOIN " + DatabaseHandler.TABLE_TYPE_OF_EXPENDITURE +
            " ON (expenditures.TYPE_ID = typeOfExpenditure.id)";

    private static final String EXPEND_ID = DatabaseHandler.TABLE_EXPENDITURES + "." +
            DatabaseHandler.ID;
    private static final String EXPEND_NAME = DatabaseHandler.TABLE_EXPENDITURES + "." +
            DatabaseHandler.NAME;
    private static final String EXPEND_VALUE = DatabaseHandler.TABLE_EXPENDITURES + "." +
            DatabaseHandler.VALUE;
    private static final String EXPEND_DATE = DatabaseHandler.TABLE_EXPENDITURES + "." +
            DatabaseHandler.DATE;
    private static final String EXPEND_TYPE = DatabaseHandler.TABLE_TYPE_OF_EXPENDITURE + "." +
            DatabaseHandler.TYPE;


    @Override
    public boolean onCreate() {
        db = new DatabaseHandler(getContext());
        sqldb = db.getWritableDatabase();
        return (db != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

            switch (sURIMatcher.match(uri)) {
                case TYPE_ID:
                    queryBuilder.setTables(db.TABLE_TYPE_OF_EXPENDITURE);
                    queryBuilder.appendWhere(db.ID + "="
                            + uri.getLastPathSegment());
                    break;
                case TYPES:
                    queryBuilder.setTables(db.TABLE_TYPE_OF_EXPENDITURE);
                    // no filter
                    break;
                /*case EXPENDITURE_ID:
                    queryBuilder.setTables(db.TABLE_EXPENDITURES);
                    queryBuilder.appendWhere(db.ID + "="
                            + uri.getLastPathSegment());
                    break;*/
                case EXPENDITURES:
                    queryBuilder.setTables(JOINED_TABLES);
                    HashMap<String,String> mColumnMap = new HashMap<String,String>();
                    mColumnMap.put(EXPEND_ID, EXPEND_ID);
                    mColumnMap.put(EXPEND_NAME,EXPEND_NAME);
                    mColumnMap.put(EXPEND_VALUE, EXPEND_VALUE);
                    mColumnMap.put(EXPEND_TYPE, EXPEND_TYPE);
                    mColumnMap.put(EXPEND_DATE, EXPEND_DATE);
                    queryBuilder.setProjectionMap(mColumnMap);
                    break;
                default:
                    throw new IllegalArgumentException(UNKNOWN_URI);
            }
            Cursor cursor = queryBuilder.query(db.getReadableDatabase(),
                    projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        long rowID;

        switch (sURIMatcher.match(uri)){
            case TYPES:
                rowID = sqldb.insert(db.TABLE_TYPE_OF_EXPENDITURE, "", values);
                if (rowID > 0)
                {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_TYPES, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;
            case EXPENDITURES:
                rowID = sqldb.insert(db.TABLE_EXPENDITURES, "", values);
                if (rowID > 0)
                {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_EXPENDITURES, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;
            default:
                throw new SQLException(UNKNOWN_URI + " " + uri);
        }
        return _uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int count = 0;
        switch (sURIMatcher.match(uri)) {
            case EXPENDITURES:
                count = sqldb.delete(db.TABLE_EXPENDITURES, s, strings);
                break;
            case EXPENDITURE_ID:
                String id = uri.getPathSegments().get(1);
                count = sqldb.delete(db.TABLE_TYPE_OF_EXPENDITURE, db.ID + " = " + id
                        + (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""),
                        strings);
                break;
            case TYPES:
                try{
                count = sqldb.delete(db.TABLE_TYPE_OF_EXPENDITURE, s, strings);
                }
                catch(Exception e){
                    return -1;
                }
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + " " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] strings) {
        int count = 0;
        switch (sURIMatcher.match(uri)) {
            case EXPENDITURES:
                count = sqldb.update(db.TABLE_EXPENDITURES, contentValues, selection,
                        strings);
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + " " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
