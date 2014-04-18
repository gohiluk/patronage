package com.tcpserver2.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isMyServiceRunning()) {
            Intent i = new Intent(getBaseContext(), MyService.class);
            getBaseContext().startService(i);
            Log.d("TAG","wystartowano serwis");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG","register receiver");
        registerReceiver(receiver, new IntentFilter("notification"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG","UNregister receiver");
        unregisterReceiver(receiver);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("mytext");
            if (stringExtra != null) {
                if (stringExtra.equals("lock")){
                    Log.d("TAG","onReceive: lock");
                    Intent intent2 = new Intent(BaseActivity.this, MainActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent2.putExtra("command","lock");
                    startActivity(intent2);
                }
                if (stringExtra.equals("unlock")) {
                    Log.d("TAG", "onReceive: unlock");
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