package com.aiglasses.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;
import com.aiglasses.R;
/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
* TODO: Customize class - update intent actions and extra parameters.
 */
public class MusicService extends IntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "com.aiglasses.service.action.FOO";
    public static final String ACTION_BAZ = "com.aiglasses.service.action.BAZ";
    // action声明
    public static final String ACTION_SOUND_RIGHT = "com.aiglasses.service.action.right";
    public static final String ACTION_SOUND_LEFT = "com.aiglasses.service.action.left";
    public static final String ACTION_SOUND_FRONT = "com.aiglasses.service.action.front";
    public static final String ACTION_SOUND_RIGHTFRONT = "com.aiglasses.service.action.rightfront";
    public static final String ACTION_SOUND_LEFTFRONT = "com.aiglasses.service.action.leftfront";
    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.aiglasses.service.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.aiglasses.service.extra.PARAM2";


    // 声明MediaPlayer对象
    private MediaPlayer mediaPlayer;

    public MusicService() {
        super("MusicService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        //Intent intent = getIntent();
        final String action = intent.getAction();
        if (ACTION_FOO.equals(action)) {
            final String param1 = intent.getStringExtra(EXTRA_PARAM1);
            final String param2 = intent.getStringExtra(EXTRA_PARAM2);
            handleActionFoo(param1, param2);
        } else if (ACTION_BAZ.equals(action)) {
            final String param1 = intent.getStringExtra(EXTRA_PARAM1);
            final String param2 = intent.getStringExtra(EXTRA_PARAM2);
            handleActionBaz(param1, param2);
        }
        // 根据intent设置的action来执行对应服务的操作
        if (ACTION_SOUND_RIGHT.equals(action)) {
            Toast.makeText(MusicService.this, "播放音乐。", Toast.LENGTH_SHORT).show();
            handleActionSound_Right();
        } if (ACTION_SOUND_LEFT.equals(action)) {
            handleActionSound_Left();
        } if (ACTION_SOUND_FRONT.equals(action)) {
            handleActionSound_Front();
        } if (ACTION_SOUND_RIGHTFRONT.equals(action)) {
            handleActionSound_RightFront();
        } if (ACTION_SOUND_LEFTFRONT.equals(action)) {
            handleActionSound_LeftFront();
        }
    }

    /**
     * 该服务执行的操作用来播放背景音乐
     */
    private void handleActionSound_Right() {
        if (mediaPlayer == null){
            // 根据音乐资源文件创建MediaPlayer对象 设置循环播放属性 开始播放
                mediaPlayer = MediaPlayer.create(this, R.raw.sound_right);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
        }
    }
    private void handleActionSound_Left() {
        if (mediaPlayer == null){
            // 根据音乐资源文件创建MediaPlayer对象 设置循环播放属性 开始播放
                mediaPlayer = MediaPlayer.create(this, R.raw.sound_left);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
        }
    }
    private void handleActionSound_Front() {
        if (mediaPlayer == null){
            // 根据音乐资源文件创建MediaPlayer对象 设置循环播放属性 开始播放
                mediaPlayer = MediaPlayer.create(this, R.raw.sound_front);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
        }
    }
    private void handleActionSound_RightFront() {
        if (mediaPlayer == null){
            // 根据音乐资源文件创建MediaPlayer对象 设置循环播放属性 开始播放
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_rightfront);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }
    private void handleActionSound_LeftFront() {
        if (mediaPlayer == null) {
            // 根据音乐资源文件创建MediaPlayer对象 设置循环播放属性 开始播放
            mediaPlayer = MediaPlayer.create(this, R.raw.sound_leftfront);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }
    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("未实现。");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("未实现。");
    }
}