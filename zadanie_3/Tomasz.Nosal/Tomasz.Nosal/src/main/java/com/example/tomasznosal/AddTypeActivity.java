package com.example.tomasznosal;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTypeActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtype);

        Button buttonAdd = (Button) findViewById(R.id.buttonAdd);
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText type = (EditText) findViewById(R.id.editTextTypeOfExpenditure);
                ContentValues values = new ContentValues();
                values.put(DatabaseHandler.TYPE, type.getText().toString());
                new InsertTypeOfExpenditure(getApplicationContext(), values).execute();
                finish();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
