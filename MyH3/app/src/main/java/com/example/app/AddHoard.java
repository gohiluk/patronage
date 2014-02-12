package com.example.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by gohilukk on 12.02.14.
 */
public class AddHoard extends Activity {

    static final int PICK_FILE_REQUEST = 1;  // The request code
    ContentValues values = new ContentValues();
    String imagePath = null;
    EditText editTextName;
    EditText editTextDescription;
    final Context context = this;
    static final String NOT_UNIQUE_NAME = "Name should be unique. This name already exists.";
    static final String EMPTY_FIELD_NAME = "Name cannot be empty. Please type name.";
    static final String IMG_PATH = "imgPath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hoard);

        ImageView iv = (ImageView) findViewById(R.id.imageView);
        Resources res = getResources();
        int id = R.drawable.gallery_icon;
        Bitmap myBitmap = BitmapFactory.decodeResource(res, id);
        iv.setImageBitmap(myBitmap);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickContactIntent = new Intent(getApplicationContext(), FileDialog.class);
                startActivityForResult(pickContactIntent, PICK_FILE_REQUEST);
            }
        });

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        Button buttonDone = (Button) findViewById(R.id.button);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> listOfNames= new ArrayList<>();
                Cursor c = getContentResolver().query(DatabaseProvider.CONTENT_URI_HOARD, null, null, null, null);
                assert c != null;
                if (c.moveToFirst()) {
                    do {
                        listOfNames.add(c.getString(c.getColumnIndex(DatabaseHandler.NAME)));
                    } while (c.moveToNext());
                }

                if(editTextName.getText().toString().equals("")) {
                    showAlert(EMPTY_FIELD_NAME);
                }
                else {
                    if (listOfNames.contains(editTextName.getText().toString())) {
                        showAlert(NOT_UNIQUE_NAME);
                    } else {
                        values.put(DatabaseHandler.NAME, editTextName.getText().toString());
                        values.put(DatabaseHandler.IMAGE_PATH, imagePath);
                        values.put(DatabaseHandler.DESCRIPTION, editTextDescription.getText().toString());
                        getContentResolver().insert(DatabaseProvider.CONTENT_URI_HOARD, values);
                        setResult(Activity.RESULT_OK, null);
                        finish();
                    }
                }
            }
        });
    }

    private void showAlert(String title) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == PICK_FILE_REQUEST) {
            if (resultCode == RESULT_OK) {
                String imgPath = data.getStringExtra(IMG_PATH);
                File imgFile = new  File(imgPath);
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ImageView myImage = (ImageView) findViewById(R.id.imageView);
                    myImage.setImageBitmap(myBitmap);
                    imagePath = imgPath;
                }
            }
        }
    }
}