package com.tomasznosal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class SplashActivity extends ActionBarActivity {

    private Thread threadMainActivity;
    private boolean stop = false;
    private static final int SPLASHTIME = 5000;
    private static final int PARTOFSPLASHTIME = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        threadMainActivity = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    int time = 0;
                    while (!stop && time <= SPLASHTIME) {
                        sleep(PARTOFSPLASHTIME);
                        time += PARTOFSPLASHTIME;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (!stop) {
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        finish();
                    } else {
                        finish();
                    }
                }
            }
        };
        threadMainActivity.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (threadMainActivity.isAlive()) {
                this.stop = true;
            }
            return true;
        } else{
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_splash, container, false);
        }
    }

}
