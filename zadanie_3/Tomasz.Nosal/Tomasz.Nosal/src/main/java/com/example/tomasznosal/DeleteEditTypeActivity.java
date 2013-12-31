package com.example.tomasznosal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteEditTypeActivity extends Activity {

    private static final String DELETED = "Element deleted.";
    private static final String CANNOT_BE_DELETED = "Element can not be deleted. Element in use.";
    private static final String WHERE_CLAUSE = DatabaseHandler.TYPE + "=?";
    private static final String SORT_BY = DatabaseHandler.TYPE;

    private static final int ERROR = -1;
    private static final int ZERO = 0;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteedittype);

        ArrayList<String> types = new ArrayList<String>();
        Cursor c = getContentResolver().query(DatabaseProvider.CONTENT_URI_TYPES, null, null, null, SORT_BY);
        if (c.moveToFirst()) {
            do {
                types.add(c.getString(c.getColumnIndex(DatabaseHandler.TYPE)));
            } while (c.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, types);


        listView = (ListView) findViewById(R.id.listViewTypes);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.delete_type, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String type[] = new String[]{listView.getItemAtPosition(info.position).toString()};
        switch (item.getItemId()) {
            case R.id.delete:
                Integer i = getContentResolver().delete(DatabaseProvider.CONTENT_URI_TYPES, WHERE_CLAUSE, type);
                startActivity(new Intent(DeleteEditTypeActivity.this, DeleteEditTypeActivity.class));
                if (i == ERROR) {
                    Toast.makeText(getApplicationContext(), CANNOT_BE_DELETED, Toast.LENGTH_LONG).show();
                }
                if (i > ZERO) {
                    Toast.makeText(getApplicationContext(), DELETED, Toast.LENGTH_LONG).show();
                }
                finish();
                return true;
        }
        return super.onContextItemSelected(item);
    }

}
