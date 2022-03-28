package com.aiglasses.service;

public class ConnectThread implements Runnable{
    @Override
    public void run() {
        SocketClient socketClient = new SocketClient();
        socketClient.MainSocketClient();
    }
}
