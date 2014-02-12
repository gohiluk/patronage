package com.example.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import java.util.ArrayList;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;


/**
 * Created by gohilukk on 12.02.14.
 */

public class MainActivity extends ActionBarActivity {

    static final int ADD_HOARD_REQUEST = 1;
    ArrayList<Item> gridArray;
    CustomGridViewAdapter customGridAdapter;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setGridView();
    }

    private void setGridView() {
        gridArray = new ArrayList<>();
        Cursor c = getContentResolver().query(DatabaseProvider.CONTENT_URI_HOARD, null, null, null, null);
        assert c != null;
        if (c.moveToFirst()) {
            do {
                String name = c.getString(c.getColumnIndex(DatabaseHandler.NAME));
                String imgpath = c.getString(c.getColumnIndex(DatabaseHandler.IMAGE_PATH));
                Bitmap bitmap = BitmapFactory.decodeFile(imgpath);
                gridArray.add(new Item(name, bitmap));
            } while (c.moveToNext());
        }
        gridView = (GridView) findViewById(R.id.gridView);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_add_hoard, gridArray);
        gridView.setAdapter(customGridAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_addHoard:
                Intent intent = new Intent(getApplicationContext(), AddHoard.class);
                startActivityForResult(intent, ADD_HOARD_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_HOARD_REQUEST) {
            if (resultCode == RESULT_OK) {
                setGridView();
            }
        }
    }
}