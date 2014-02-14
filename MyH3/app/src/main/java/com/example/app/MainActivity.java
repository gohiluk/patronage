package com.example.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import java.util.ArrayList;

import android.util.Log;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

/**
 * Created by gohilukk on 12.02.14.
 */

public class MainActivity extends ActionBarActivity {

    static final int ADD_HOARD_REQUEST = 1;
    ArrayList<Item> gridArray;
    CustomGridViewAdapter customGridAdapter;
    GridView gridView;
    public static final String MODE = "mode";
    public static final String ADD = "add";
    public static final String EDIT = "edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridView);
        registerForContextMenu(gridView);
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
                intent.putExtra(MODE, ADD);
                startActivityForResult(intent, ADD_HOARD_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.main_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_item:
                String[] nameToDelete = {gridArray.get(info.position).getTitle()};
                Log.d("TAG", nameToDelete[0]);
                Integer i = getContentResolver().delete(DatabaseProvider.CONTENT_URI_HOARD, DatabaseHandler.NAME+"=?", nameToDelete);
                Log.d("TAG", DatabaseHandler.NAME+"="+nameToDelete);
                Log.d("TAG", i.toString());
                gridArray.remove(info.position);
                this.customGridAdapter.notifyDataSetChanged();
                break;
            case R.id.edit_item:
                String nameToEdit = gridArray.get(info.position).getTitle();
                Intent intent = new Intent(getApplicationContext(), AddHoard.class);
                intent.putExtra(MODE, EDIT);
                intent.putExtra(DatabaseHandler.NAME,nameToEdit);
                startActivityForResult(intent, ADD_HOARD_REQUEST);
                break;
        }
        return super.onContextItemSelected(item);
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