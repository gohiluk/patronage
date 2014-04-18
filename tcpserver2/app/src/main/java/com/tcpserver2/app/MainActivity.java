package com.tcpserver2.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends BaseActivity {

    public static final String COMMAND = "command";
    public static final String LOCK = "lock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.buttonStartA);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(MainActivity.this, MyService.class);
        MainActivity.this.stopService(i);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String command = intent.getStringExtra(COMMAND);
        if (command != null) {
            if (command.equals(LOCK)) {
                Intent i = new Intent(MainActivity.this, LockAcitivity.class);
                startActivity(i);
            }
        }
    }
}
