package com.tcpserver2.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpGui();
    }

    private void setUpGui() {
        Button buttonGoToNextActivity = (Button) findViewById(R.id.buttonStartA);
        buttonGoToNextActivity.setOnClickListener(new View.OnClickListener() {
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
        Intent intent = new Intent(MainActivity.this, ListeningService.class);
        stopService(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String commandToLock = intent.getStringExtra(BaseActivity.COMMAND_TO_LOCK);
        if (commandToLock != null) {
            startLockActivityIfStringEqualsLock(commandToLock);
        }
    }

    private void startLockActivityIfStringEqualsLock(String commandToLock) {
        if (commandToLock.equals(ListeningService.LOCK)) {
            Intent i = new Intent(MainActivity.this, LockAcitivity.class);
            startActivity(i);
        }
    }
}
