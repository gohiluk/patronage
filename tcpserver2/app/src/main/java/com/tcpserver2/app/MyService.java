package com.tcpserver2.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
    public static boolean iflocked = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "onStartCommand");

        thread = new Thread()
        {
            public void run() {
                Log.d("TAG", "thread starts");

                String clientSentence;
                ServerSocket welcomeSocket = null;

                try {
                    welcomeSocket = new ServerSocket(6789);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (!this.isInterrupted()) {
                    Socket connectionSocket = null;
                    try {
                        connectionSocket = welcomeSocket.accept();

                        BufferedReader inFromClient =
                                new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                        //DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                        clientSentence = inFromClient.readLine();
                        if (!iflocked && clientSentence.equals(LOCK)){
                            Intent intentt = new Intent("notification");
                            intentt.putExtra("mytext", LOCK);
                            iflocked = true;
                            sendBroadcast(intentt);
                        }
                        if (iflocked && clientSentence.equals(UNLOCK)){
                            Intent intentt = new Intent("notification");
                            intentt.putExtra("mytext", UNLOCK);
                            iflocked = false;
                            sendBroadcast(intentt);
                        }

                        Log.d("TAG", "Received: " + clientSentence);
                        //capitalizedSentence = clientSentence.toUpperCase() + '\n';
                        //outToClient.writeBytes(capitalizedSentence);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("TAG", "thread stops");
            }
        };
        thread.start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "onCreate MyService");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.interrupt();
        Log.d("TAG", "onDestroy MyService");
    }
}
