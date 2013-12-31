package com.example.tomasznosal;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gohilukk on 24.12.13.
 */

public class GetListOfTypes extends AsyncTask<String, Void, Void> {

    Context context;
    Spinner spinner;
    TextView textView;
    Button button;
    private static final String ERROR_TYPES = "No added types of expenditure. First add new type of expenditure.";
    private static final String EMPTY = "empty";
    private static final int INDEX_ARG0 = 0;
    private static final String SORT_BY = DatabaseHandler.TYPE;

   /**
    * Konstruktor
    * @param context - interfejs pozwalajcy uzyć metody getContentResolver
    * @param spinner - spinner, w który zostanie wyświetlona odpowiedź zapytania
    * @param textView - textView, w ktorym pojawia się informacja, że trzeba dodać najpierw
    *                 typ wydatków, aby dodatawać wydatki
    * @param button - kontrolka potrzebna do ustawienia przycisku jako aktywny lub nieaktywny
    */
    public GetListOfTypes(Context context, Spinner spinner, TextView textView, Button button){
        this.context = context;
        this.spinner = spinner;
        this.textView = textView;
        this.button = button;
    }

    @Override
    protected Void doInBackground(String... myString) {

        ArrayList<String> types = new ArrayList<String>();
        Cursor c = context.getContentResolver().query(DatabaseProvider.CONTENT_URI_TYPES, null, null, null, SORT_BY);
        if (!c.moveToFirst()) {
            types.add(EMPTY);
            textView.setText(ERROR_TYPES);
            button.setEnabled(false);
        }else{
            do{
                types.add(c.getString(c.getColumnIndex(DatabaseHandler.TYPE)));
            } while (c.moveToNext());
            button.setEnabled(true);
        }
        ArrayAdapter adapter = new ArrayAdapter(this.context, R.layout.spinner_item, types);
        Integer spinnerPosition = adapter.getPosition(myString[INDEX_ARG0]);
        spinner.setAdapter(adapter);
        spinner.setSelection(spinnerPosition);
        return null;
    }
}
