package com.aiglasses.service;

import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SocketClient {

    public String MsgBriage(String s){
        return s;
    }

    public void MainSocketClient() {

        Socket socket = null;
        Scanner scanner = null;
        PrintWriter pw = null;
        BufferedReader br = null;

        try {
            socket = new Socket("114.115.213.98",8888);
            //创建读取服务端发送消息的流对象
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true) {
                String SocketInput = br.readLine();
                System.out.println("服务端说："+SocketInput);
                MsgBriage(SocketInput);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(socket!= null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(pw != null){
                pw.close();
            }
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(scanner != null){
                scanner.close();
            }
        }
    }
}

