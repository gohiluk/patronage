package com.example.tomasznosal;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gohilukk on 24.12.13.
 */
public class GetListOfExpenditure extends AsyncTask<Void, Void, Void> {

    Context context;
    ListView listView;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private static final String ORDER_BY = DatabaseHandler.TABLE_EXPENDITURES + "." + DatabaseHandler.DATE;

    /*
    * Konstruktor
    * @param context - interfejs pozwalajcy uzyć metody getContentResolver
    * @param listView - listView, w który zostanie wyświetlona odpowiedź zapytania
     */
    GetListOfExpenditure(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Cursor c = context.getContentResolver().query(DatabaseProvider.CONTENT_URI_EXPENDITURES, null, null, null, ORDER_BY);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(DatabaseHandler.ID, c.getString(c.getColumnIndex(DatabaseHandler.ID)));
                temp.put(DatabaseHandler.DATE, c.getString(c.getColumnIndex(DatabaseHandler.DATE)));
                temp.put(DatabaseHandler.TYPE, c.getString(c.getColumnIndex(DatabaseHandler.TYPE)));
                temp.put(DatabaseHandler.NAME, c.getString(c.getColumnIndex(DatabaseHandler.NAME)));
                temp.put(DatabaseHandler.VALUE, c.getString(c.getColumnIndex(DatabaseHandler.VALUE)));
                list.add(temp);
            } while (c.moveToNext());
        }

        SimpleAdapter adapter = new SimpleAdapter(
                context,
                list,
                R.layout.custom_row_view,
                new String[]{
                        DatabaseHandler.DATE,
                        DatabaseHandler.TYPE,
                        DatabaseHandler.NAME,
                        DatabaseHandler.VALUE,
                        DatabaseHandler.ID},
                new int[]{
                        R.id.textViewLeftTop,
                        R.id.textViewRightTop,
                        R.id.textViewLefBottom,
                        R.id.textViewRightBottom,
                        R.id.textViewId}

        );
        listView.setAdapter(adapter);
        return null;
    }
}
