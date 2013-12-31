package com.example.tomasznosal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

/**
 * Created by gohilukk on 24.12.13.
 */
public class UpdateExpenditure extends AsyncTask<Void, Void, Void> {

    Context context;
    ContentValues values;
    String type;
    String selection;
    String selectionUpdate;
    String id;

    UpdateExpenditure(Context context, ContentValues values, String type, String id) {
        this.context = context;
        this.values = values;
        this.type = type;
        this.selection = DatabaseHandler.TYPE + "=='" + type + "'";
        this.selectionUpdate = DatabaseHandler.ID + "==" + id;
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        int typId=0;
        Cursor c = context.getContentResolver().query(DatabaseProvider.CONTENT_URI_TYPES, null, selection, null, null);
        if (c.moveToFirst()) {
            do{
                typId = Integer.parseInt(c.getString(c.getColumnIndex(DatabaseHandler.ID)));
            } while (c.moveToNext());
        }

        values.put(DatabaseHandler.TYPE_ID, typId);
        context.getContentResolver().update(DatabaseProvider.CONTENT_URI_EXPENDITURES,values,selectionUpdate,null);
        return null;
    }
}
