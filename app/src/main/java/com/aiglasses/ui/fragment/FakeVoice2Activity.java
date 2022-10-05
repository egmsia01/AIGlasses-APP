package com.aiglasses.ui.fragment;

import android.media.MediaPlayer;
import android.view.View;
import com.aiglasses.R;
import com.aiglasses.aop.SingleClick;
import com.aiglasses.app.AppActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class FakeVoice2Activity extends AppActivity {

    private static MediaPlayer mMediaPlayer;
    public String sendString;
    boolean send = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fake_voice2;
    }

    @Override
    protected void initView() {
        new Thread(this::MainSentClient2).start();
        setOnClickListener(R.id.btn_home_front_3_5,
                R.id.btn_home_front_3,R.id.btn_home_front_2_5,R.id.btn_home_front_2,
                R.id.btn_home_front_1_5,R.id.btn_home_front_1,
                R.id.btn_home_leftfront_2,R.id.btn_home_leftfront_1,R.id.btn_home_leftguai,R.id.btn_home_rightguai,R.id.btn_home_stop,
                R.id.btn_home_gps,R.id.btn_home_zhnagaiwu_1);
    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btn_home_front_3_5) {
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
        }else if (viewId == R.id.btn_home_leftguai) {
            sendString = "btn_home_leftguai";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.left_safe);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_rightguai) {
            sendString = "btn_home_rightguai";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.right_safe);
            mMediaPlayer.start();
        } else if (viewId == R.id.btn_home_stop) {
            sendString = "btn_home_stop";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.pl_stop);
            mMediaPlayer.start();
        }
        /*
        else if (viewId == R.id.btn_home_gps) {
            sendString = "btn_home_gps";
            send = true;
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.gps_door);
            mMediaPlayer.start();
        }

         */
        else if (viewId == R.id.btn_home_zhnagaiwu_1) {
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.have_zhangaiwu_1);
            mMediaPlayer.start();
        }
    }

    public void MainSentClient2() {
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