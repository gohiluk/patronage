package com.tcpserver2.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class BaseActivity extends Activity {

    public static final String LOCK = "lock";
    public static final String UNLOCK = "unlock";
    public static final String FILTER = "notification";
    public static final String MY_MESSAGE = "myMessage";
    public static final String COMMAND = "command";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isMyServiceRunning()) {
            Intent i = new Intent(getBaseContext(), MyService.class);
            getBaseContext().startService(i);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(FILTER));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra(MY_MESSAGE);
            if (stringExtra != null) {
                if (stringExtra.equals(LOCK)){
                    Intent intent2 = new Intent(BaseActivity.this, MainActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent2.putExtra(COMMAND,LOCK);
                    startActivity(intent2);
                }
                if (stringExtra.equals(UNLOCK)) {
                    finish();
                }
            }
        }
    };

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MyService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}