package com.tcpserver2.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        setUpGui();
    }

    private void setUpGui() {
        Button buttonGoToNextActivity = (Button) findViewById(R.id.buttonStartB);
        buttonGoToNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AActivity.this, BActivity.class);
                startActivity(intent);
            }
        });
    }
}
