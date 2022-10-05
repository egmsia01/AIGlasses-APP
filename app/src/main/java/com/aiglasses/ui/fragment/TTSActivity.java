package com.aiglasses.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.aiglasses.R;
import com.aiglasses.app.AppActivity;
import com.aiglasses.tts.OnlineAPIActivity;
import java.util.ArrayList;

public class TTSActivity extends AppActivity {
    TextToSpeech t1;
    EditText ed1;
    Button b1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_online_api;
    }

    @Override
    protected void initView() {
        /*
        ed1 = (EditText)findViewById(R.id.editText);
        b1 = (Button)findViewById(R.id.button);
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.CHINA);
                }
            }
        });

        b1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = ed1.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    public void onPause(){
        if(t1 != null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }


    @Override
    protected void initData() {

    }
         */
    }

    @Override
    protected void initData() {
        initPermission();
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE // demo使用
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    /**
     * 在线API合成
     * @param view
     */
    public void onlineAPI(View view) {
        startActivity(new Intent(this, OnlineAPIActivity.class));
    }
}