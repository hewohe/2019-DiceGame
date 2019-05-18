package com.example.project;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import com.friendlyarm.FriendlyThings.HardwareControler;

public class SecondActivity extends AppCompatActivity {
    public int val = 0;
    private MediaPlayer mediaPlayer;
    private Button begin;
    private Button Rule;

    // serial port settings
    public String devName = "/dev/ttyAMA3";  // 设置使用的串口
    private int speed = 9600;        // 波特率
    private int dataBits = 8;
    private int stopBits = 1;
    private int devfd = -1;

    private int language = 0;
    private final String start_en = "Start!";
    private final String rule_en = "Game Rules";

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        if (devfd != -1) {
            HardwareControler.close(devfd);
            devfd = -1;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        begin = findViewById(R.id.begin);
        Rule = findViewById(R.id.rule);

        //语言切换
        Intent intent = getIntent();
        language = intent.getIntExtra("language", 0);
        if (language == 1) {
            begin.setText(start_en);
            Rule.setText(rule_en);
        }

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.release();
                // 向32发送开始信号
                SendData("S");
                Intent intent = new Intent(SecondActivity.this,
                        MultiplayerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.release();
                Intent intent = new Intent(SecondActivity.this, Rule_CN.class);
                startActivity(intent);
                finish();
            }
        });

        // Serial port
        devfd = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);
        if (devfd >= 0) {
        } else {
            devfd = -1;
        }

        mediaPlayer = MediaPlayer.create(SecondActivity.this, R.raw.rec3);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    private void SendData(String string) {
        HardwareControler.write(devfd, string.getBytes());
    }
}
