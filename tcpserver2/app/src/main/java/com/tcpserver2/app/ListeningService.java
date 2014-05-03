package com.tcpserver2.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
public class ListeningService extends Service {

    public static final String LOCK = "lock";
    public static final String UNLOCK = "unlock";
    public static final String INTENT_FILTER = "notification";
    public static final String MESSAGE_TO_LOCK_OR_UNLOCK = "messageToLockOrUnLock";
    public static final String ERROR_MESSAGE = "errorMessage";
    private static final String DEFAULT_INPUT = "defaultInput";
    private static final String ERROR_WHILE_CONNECTING_SERVER = "Error while connecting server";
    private static final String COULD_NOT_CLOSE_SOCKET = "Could not close socket";

    private boolean ifLocked = false;
    private boolean applicationIsRunning = true;

    private ServerSocket serverSocket;

    private static final int PORT = 6789;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ListeningThread listeningThread = new ListeningThread();
        listeningThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private class ListeningThread extends Thread {

        @Override
        public void run() {
            connectServer();
            if (serverSocket != null)
            while (applicationIsRunning) {
                String clientSentence = waitForSomeInput();
                sendSuitableBroadcast(clientSentence);
            }
        }

        private void connectServer(){
            try {
                serverSocket = new ServerSocket(PORT);
            } catch (IOException e) {
                sendError(ERROR_WHILE_CONNECTING_SERVER);
            }
        }

        private String waitForSomeInput() {
            try {
                Socket connectionSocket = serverSocket.accept();
                InputStreamReader inputStreamReader = new InputStreamReader(connectionSocket.getInputStream());
                BufferedReader inputFromClient = new BufferedReader(inputStreamReader);
                return inputFromClient.readLine();
            } catch (IOException e) {
                applicationIsRunning = false;
                return DEFAULT_INPUT;
            }
        }

        private void sendSuitableBroadcast(String clientSentence) {
            if (!ifLocked && clientSentence.equals(LOCK)) {
                inverseValueIfLocked();
                sendBroadcastWithMessage(LOCK);
            }
            if (ifLocked && clientSentence.equals(UNLOCK)) {
                inverseValueIfLocked();
                sendBroadcastWithMessage(UNLOCK);
            }
        }

        private void inverseValueIfLocked(){
            ifLocked = !ifLocked;
        }

        private void sendBroadcastWithMessage(String message) {
            Intent intent = new Intent(INTENT_FILTER);
            intent.putExtra(MESSAGE_TO_LOCK_OR_UNLOCK, message);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
                sendError(COULD_NOT_CLOSE_SOCKET);
        }
    }

    private void sendError(String message) {
        Intent intent = new Intent(INTENT_FILTER);
        intent.putExtra(ERROR_MESSAGE, message);
        sendBroadcast(intent);
    }
}
