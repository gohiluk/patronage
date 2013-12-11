package com.tomasznosal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NameActivity extends Activity {

    final Context context = this;
    private Button button;
    private static boolean flaga=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        button = (Button) findViewById(R.id.sign_in_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom);
                flaga=true;
                EditText name = (EditText)findViewById(R.id.name);
                if (name.getText().toString().isEmpty()) {
                    dialog.setTitle("Please enter your name");
                }
                else {
                    dialog.setTitle("Hello " + name.getText().toString());
                }

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        flaga=false;
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText name = (EditText)findViewById(R.id.name);
        outState.putString("name", name.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String name = savedInstanceState.getString("name");
        EditText nameText = (EditText)findViewById(R.id.name);
        nameText.setText(name);

        if (flaga) {
            button.performClick();
        }
    }
}
