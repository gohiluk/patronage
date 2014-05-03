package com.tcpserver2.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {

    public static final String COMMAND_TO_LOCK = "command";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isMyServiceRunning()) {
            Intent intent = new Intent(getBaseContext(), ListeningService.class);
            getBaseContext().startService(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ListeningService.INTENT_FILTER));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            checkMessageIfLockOrUnclock(intent);
            checkErrors(intent);
        }

        private void checkMessageIfLockOrUnclock(Intent intent) {
            String stringExtra = intent.getStringExtra(ListeningService.MESSAGE_TO_LOCK_OR_UNLOCK);
            if (stringExtra != null) {
                if (stringExtra.equals(ListeningService.LOCK)) {
                    rewindToMainActivityWithLockCommand();
                }
                if (stringExtra.equals(ListeningService.UNLOCK)) {
                    finish();
                }
            }
        }

        private void checkErrors(Intent intent) {
            String errorMessage = intent.getStringExtra(ListeningService.ERROR_MESSAGE);
            if (errorMessage != null) {
                if (getApplicationContext() != null) {
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        }

        private void rewindToMainActivityWithLockCommand() {
            Intent intent = new Intent(BaseActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(COMMAND_TO_LOCK,ListeningService.LOCK);
            startActivity(intent);
        }
    };

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ListeningService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}