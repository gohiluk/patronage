package com.example.app;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


/**
 * Created by gohilukk on 12.02.14.
 */
public class DatabaseProvider extends ContentProvider {

    private DatabaseHandler db;
    private SQLiteDatabase sqldb;

    public static final String AUTHORITY = "com.example.provider.hoardManager";

    public static final int HOARD = 100;
    public static final int HOARD_ID = 110;
    public static final String HOARD_BASE_PATH = "hoard";
    public static final Uri CONTENT_URI_HOARD = Uri.parse("content://" + AUTHORITY
            + "/" + HOARD_BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, HOARD_BASE_PATH, HOARD);
        sURIMatcher.addURI(AUTHORITY, HOARD_BASE_PATH + "/#", HOARD_ID);
    }

    private static final String UNKNOWN_URI = "Unknown URI";



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
                case HOARD:
                    queryBuilder.setTables(DatabaseHandler.TABLE_HOARD);
                    // no filter
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
        Log.d("TAG", "Jestem w insercie");
        Log.d("TAG", uri.toString());
        switch (sURIMatcher.match(uri)){
            case HOARD:
                rowID = sqldb.insert(DatabaseHandler.TABLE_HOARD, "", values);
                if (rowID > 0)
                {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_HOARD, rowID);
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
        int count;
        switch (sURIMatcher.match(uri)) {
            case HOARD:
                try{
                    count = sqldb.delete(DatabaseHandler.TABLE_HOARD, s, strings);
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
        int count;
        switch (sURIMatcher.match(uri)) {
            case HOARD:
                count = sqldb.update(DatabaseHandler.TABLE_HOARD, contentValues, selection,
                        strings);
                break;
            default:
                throw new IllegalArgumentException(UNKNOWN_URI + " " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
