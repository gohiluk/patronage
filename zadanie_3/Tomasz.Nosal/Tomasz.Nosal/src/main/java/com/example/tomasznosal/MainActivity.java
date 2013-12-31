package com.example.tomasznosal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    ListView listView;
    private static final int ERROR_VALUE = -1;
    private static final String MODE = "mode";
    private static final String ADD = "add";
    private static final String EDIT = "edit";
    private static final String ELEMENT_DELETED = "Element deleted.";
    private static final String ID_EQUAL = DatabaseHandler.ID + "=";
    private static final String VALUE_EQUAL = DatabaseHandler.VALUE + "=";
    private static final String NAME_EQUAL = DatabaseHandler.NAME + "=";
    private static final String TYPE_EQUAL = DatabaseHandler.TYPE + "=";
    private static final String DATE_EQUAL = DatabaseHandler.DATE + "=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        new GetListOfExpenditure(getApplicationContext(), (ListView) findViewById(R.id.listView)).execute();
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addExpenditure:
                Intent intent = new Intent(MainActivity.this, AddExpenditureActivity.class);
                intent.putExtra(MODE, ADD);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_addType:
                startActivity(new Intent(MainActivity.this, AddTypeActivity.class));
                return true;
            case R.id.action_deleteEditType:
                startActivity(new Intent(MainActivity.this, DeleteEditTypeActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String[] obj = listView.getItemAtPosition(info.position).toString().replace("{", "").replace("}", "").replace(" ", "").split(",");

        Integer indexId = ERROR_VALUE, indexDate = ERROR_VALUE, indexValue = ERROR_VALUE, indexName = ERROR_VALUE, indexType = ERROR_VALUE;
        for (int i = 0; i < obj.length; i++) {
            if (obj[i].contains(DatabaseHandler.ID)) indexId = i;
            if (obj[i].contains(DatabaseHandler.DATE)) indexDate = i;
            if (obj[i].contains(DatabaseHandler.VALUE)) indexValue = i;
            if (obj[i].contains(DatabaseHandler.TYPE)) indexType = i;
            if (obj[i].contains(DatabaseHandler.NAME)) indexName = i;
        }

        switch (item.getItemId()) {
            case R.id.delete:
                Integer i = getContentResolver().delete(DatabaseProvider.CONTENT_URI_EXPENDITURES,
                        ID_EQUAL + obj[indexId].replace(ID_EQUAL, ""), null);
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                Toast.makeText(getApplicationContext(), ELEMENT_DELETED, Toast.LENGTH_LONG).show();
                finish();
                return true;
            case R.id.edit:
                Intent intent = new Intent(MainActivity.this, AddExpenditureActivity.class);
                intent.putExtra(MODE, EDIT);
                intent.putExtra(DatabaseHandler.ID, obj[indexId].replace(ID_EQUAL, ""));
                intent.putExtra(DatabaseHandler.VALUE, obj[indexValue].replace(VALUE_EQUAL, ""));
                intent.putExtra(DatabaseHandler.NAME, obj[indexName].replace(NAME_EQUAL, ""));
                intent.putExtra(DatabaseHandler.TYPE, obj[indexType].replace(TYPE_EQUAL, ""));
                intent.putExtra(DatabaseHandler.DATE, obj[indexDate].replace(DATE_EQUAL, ""));
                startActivity(intent);
                finish();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
