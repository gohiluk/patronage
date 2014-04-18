package com.tcpserver2.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Description
 *
 * @author gohilukk
 *         Date: 18.04.14
 */
public class MyService extends Service {

    Thread thread;
    public static final String LOCK = "lock";
    public static final String UNLOCK = "unlock";
    public static final String FILTER = "notification";
    public static final String MY_MESSAGE = "myMessage";
    public boolean iflocked = false;
    private ServerSocket mySocket;
    private boolean flaga = true;
    private static final int PORT = 6789;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        thread = new Thread() {
            public void run() {
                String clientSentence;
                mySocket = null;

                try {
                    mySocket = new ServerSocket(PORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (flaga) {
                    Socket connectionSocket = null;
                    try {
                        connectionSocket = mySocket.accept();

                        BufferedReader inFromClient =
                                new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                        clientSentence = inFromClient.readLine();

                        if (!iflocked && clientSentence.equals(LOCK)) {
                            Intent intentt = new Intent(FILTER);
                            intentt.putExtra(MY_MESSAGE, LOCK);
                            iflocked = true;
                            sendBroadcast(intentt);
                        }
                        if (iflocked && clientSentence.equals(UNLOCK)) {
                            Intent intentt = new Intent(FILTER);
                            intentt.putExtra(MY_MESSAGE, UNLOCK);
                            iflocked = false;
                            sendBroadcast(intentt);
                        }

                    } catch (IOException e) {
                        flaga = false;
                        e.printStackTrace();
                    }
                }// end while
            }// end run
        };//end thread
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mySocket != null)
                mySocket.close();
        } catch (IOException e) {

        }
    }
}
