package com.aiglasses.ui.activity;

import android.media.MediaPlayer;
import android.view.View;
import com.aiglasses.R;
import com.aiglasses.aop.SingleClick;
import com.aiglasses.app.AppActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class FakeVoiceActivity extends AppActivity {

    private static MediaPlayer mMediaPlayer;
    public String sendString;
    boolean send = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fake_voice;
    }

    @Override
    protected void initView() {
        new Thread(this::MainSentClient).start();
        setOnClickListener(R.id.btn_home_front_4,R.id.btn_home_front_3_5,
                R.id.btn_home_front_3,R.id.btn_home_front_2_5,R.id.btn_home_front_2,
                R.id.btn_home_front_1_5,R.id.btn_home_front_1,R.id.btn_home_leftfront_2_5,
                R.id.btn_home_leftfront_2,R.id.btn_home_leftfront_1,R.id.btn_home_fakeleftfront,
                R.id.btn_home_fakefront,R.id.btn_home_fakerightfront,R.id.btn_home_1m,R.id.btn_home_1_5m
                ,R.id.btn_home_2m,R.id.btn_home_2_5m,R.id.btn_home_3m,R.id.btn_home_3_5m,R.id.btn_home_4m,
                R.id.btn_home_4_5m,R.id.btn_home_havepeople,R.id.btn_home_havecar,R.id.btn_home_havemalu,
                R.id.btn_home_havezhangai,R.id.btn_home_leftguai,R.id.btn_home_rightguai,R.id.btn_home_stop,R.id.btn_home_gps);
    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_home_front_4) {
            sendString = "btn_home_front_4";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_4_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_front_3_5) {
            sendString = "btn_home_front_3_5";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_3_5_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_front_3) {
            sendString = "btn_home_front_3";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_3_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_front_2_5) {
            sendString = "btn_home_front_2_5";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_2_5_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_front_2) {
            sendString = "btn_home_front_2";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_2_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_front_1_5) {
            sendString = "btn_home_front_1_5";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_1_5_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_front_1) {
            sendString = "btn_home_front_1";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.front_1_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_leftfront_2_5) {
            sendString = "btn_home_leftfront_2_5";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.leftfront_2_5_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_leftfront_2) {
            sendString = "btn_home_leftfront_2";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.leftfront_2_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_leftfront_1) {
            sendString = "btn_home_leftfront_1";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.leftfront_1_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_fakeleftfront) {
            sendString = "btn_home_fakeleftfront";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.fakeleftfront);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_fakefront) {
            sendString = "btn_home_fakefront";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.fakefront);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_fakerightfront) {
            sendString = "btn_home_fakerightfront";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.fakerightfront);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_1m) {
            sendString = "btn_home_1m";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m1);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_1_5m) {
            sendString = "btn_home_1_5m";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m1_5);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_2m) {
            sendString = "btn_home_2m";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m2);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_2_5m) {
            sendString = "btn_home_2_5m";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m2_5);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_3m) {
            sendString = "btn_home_3m";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m3);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_3_5m) {
            sendString = "btn_home_3_5m";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m3_5);
            mMediaPlayer.start();
        }  else if (viewId == R.id.btn_home_4m) {
            sendString = "btn_home_4m";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m4);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_4_5m) {
            sendString = "btn_home_4_5m";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.m4_5);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_havepeople) {
            sendString = "btn_home_havepeople";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.have_people);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_havecar) {
            sendString = "btn_home_havecar";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.have_cars);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_havemalu) {
            sendString = "btn_home_havemalu";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.have_roads);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_havezhangai) {
            sendString = "btn_home_havezhangai";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.have_barriers);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_leftguai) {
            sendString = "btn_home_leftguai";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.malu_1_5_left);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_rightguai) {
            sendString = "btn_home_rightguai";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.malu_1_5_right);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_stop) {
            sendString = "btn_home_stop";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.pl_stop);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_gps) {
            sendString = "btn_home_gps";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.gps_door);
            mMediaPlayer.start();
        }
    }

    public void MainSentClient() {
        Socket socket = null;
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            socket = new Socket("114.115.213.98",8888);
            // 创建读取服务端发送消息的流对象
            pw = new PrintWriter(socket.getOutputStream());
            while(true) {
                if (send) {
                    String msg = sendString;
                    pw.println(msg);
                    pw.flush();
                    send = false;
                    msg = "等待指令...";
                }
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
        }
    }
}