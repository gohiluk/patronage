package com.example.tomasznosal;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by gohilukk on 24.12.13.
 */
public class InsertTypeOfExpenditure extends AsyncTask<Void, Void, Void> {

    Context context;
    ContentValues values;

    public InsertTypeOfExpenditure(Context context, ContentValues values){
        this.context = context;
        this.values = values;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        this.context.getContentResolver().insert(
                DatabaseProvider.CONTENT_URI_TYPES, values);
        return null;
    }
}
