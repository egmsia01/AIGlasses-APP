package com.aiglasses.ui.fragment;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSend extends Thread {
    private Socket socket;
    public String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ClientSend(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        this.sendMsg();
    }

    /**
     * 发送消息
     */
    private void sendMsg() {
        PrintWriter pw = null;
        try {
            //创建向对方输出消息的流对象
            pw = new PrintWriter(this.socket.getOutputStream());
            while (true) {
                pw.println(getMsg());
                pw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

