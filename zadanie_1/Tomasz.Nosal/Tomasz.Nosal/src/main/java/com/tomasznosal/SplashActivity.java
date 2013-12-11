package com.tomasznosal;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class SplashActivity extends ActionBarActivity {

    Handler myHandler = new Handler();
    Runnable myRunnable;
    private static final int SPLASHTIME = 5000;
    boolean isRunning=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!isRunning) {
            isRunning=true;
            myHandler.postDelayed(myRunnable=new Runnable(){
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, NameActivity.class);
                    startActivity(i);
                    finish();
                }
            },SPLASHTIME);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myHandler.removeCallbacks(myRunnable);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        myHandler.removeCallbacks(myRunnable);
        finish();
    }
}
