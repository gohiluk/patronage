package com.example.tomasznosal;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddExpenditureActivity extends ActionBarActivity {

    private static final String ERROR_EMPTY_VALUES = "Value/date can not be empty.";
    private static final String MODE = "mode";
    private static final String ADD = "add";
    private static final String EDIT = "edit";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DOT = ".";
    private static final String OK = "Ok";
    private static final int NUMBER_OF_DIGITS = 3;

    private Intent previouslyIntent;
    private String mode;

    Button buttonAdd;
    Button buttonCancel;

    EditText valueText;
    EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        previouslyIntent = getIntent();
        new GetListOfTypes(getApplicationContext(), (Spinner) findViewById(R.id.types_spinner),
                (TextView) findViewById(R.id.textViewError),
                (Button) findViewById(R.id.buttonAddEpenditure)).execute(previouslyIntent.getStringExtra(DatabaseHandler.TYPE));

        mode = previouslyIntent.getStringExtra(MODE);
        if (mode.equals(ADD)) {
            modeAdd();
        }
        if (mode.equals(EDIT)) {
            modeEdit(previouslyIntent);
        }

        valueText = (EditText) findViewById(R.id.editTextValue);
        valueText.addTextChangedListener(mGlobal_TextWatcher);

        editTextDate = (EditText) findViewById(R.id.editTextDate);
        buttonCancel = (Button) findViewById(R.id.buttonCancelExpenditure);
        buttonCancel.setOnClickListener(mGlobal_OnClickListener);
        buttonAdd = (Button) findViewById(R.id.buttonAddEpenditure);
        buttonAdd.setOnClickListener(mGlobal_OnClickListener);
    }

    final TextWatcher mGlobal_TextWatcher = new TextWatcher() {
        String text;
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            text = charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if ( charSequence.toString().contains(DOT) ){
                int dotPosition = charSequence.toString().indexOf(DOT);
                if ( charSequence.toString().length() > (dotPosition + NUMBER_OF_DIGITS) ) {
                    valueText.setText(text);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    };

    final View.OnClickListener mGlobal_OnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.buttonAddEpenditure:
                    if (!valueText.getText().toString().isEmpty() && !editTextDate.getText().toString().isEmpty()){
                        ContentValues values = new ContentValues();
                        values.put(DatabaseHandler.DATE, ((EditText) findViewById(R.id.editTextDate)).getText().toString());
                        values.put(DatabaseHandler.NAME, ((EditText)findViewById(R.id.editTextName)).getText().toString());
                        values.put(DatabaseHandler.VALUE, Float.parseFloat(((EditText)findViewById(R.id.editTextValue)).getText().toString()));
                        Spinner spinner = (Spinner) findViewById(R.id.types_spinner);

                        if (mode.equals(ADD))
                            new InsertExpenditure(getApplicationContext(), values, spinner.getSelectedItem().toString()).execute();

                        if (mode.equals(EDIT)) {
                            String id = previouslyIntent.getStringExtra(DatabaseHandler.ID);
                            new UpdateExpenditure(getApplicationContext(), values, spinner.getSelectedItem().toString(), id).execute();
                        }
                        Intent i = new Intent( AddExpenditureActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        TextView textViewError = (TextView) findViewById(R.id.textViewError);
                        textViewError.setText(ERROR_EMPTY_VALUES);
                    }
                    break;
                case R.id.buttonCancelExpenditure:
                    Intent i = new Intent( AddExpenditureActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    break;
            }
        }
    };

   /**
    * Funkcja ustawiająca GUI pod funkcjonalność dodawania wydatków
    */
    private void modeAdd() {
        String date = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        final EditText editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextDate.setText(date);
    }

    /**
     * Funkcja ustawiająca GUI pod funkcjonalność edytowania wydatków
     */
    private void modeEdit(Intent myIntent) {
        EditText editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextDate.setText(myIntent.getStringExtra(DatabaseHandler.DATE));
        EditText editTextValue = (EditText) findViewById(R.id.editTextValue);
        editTextValue.setText(myIntent.getStringExtra(DatabaseHandler.VALUE));
        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        editTextName.setText(myIntent.getStringExtra(DatabaseHandler.NAME));
        Button buttonAdd = (Button) findViewById(R.id.buttonAddEpenditure);
        buttonAdd.setText(OK);
    }
}