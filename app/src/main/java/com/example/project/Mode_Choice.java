package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import com.friendlyarm.FriendlyThings.HardwareControler;

import java.util.Locale;

public class Mode_Choice extends AppCompatActivity {

    private MediaPlayer media;
    private Button btn_game;
    private Button maintain;
    private Button language_choice;
    private int devfd = -1;
    public String devName = "/dev/ttyAMA3";  // 设置使用的串口
    //常用字符串
    private final String M = "M";
    private final String P = "P";
    private final String Maintain_CN = "维护模式";
    private final String Maintain_EN = "Maintain";
    private final String Game_CN = "游戏模式";
    private final String Game_EN = "Game Mode";
    private int sign = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode__choice);

        //打开串口
        int speed = 9600;        // 波特率
        int dataBits = 8;
        int stopBits = 1;
        devfd = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);

        btn_game = findViewById(R.id.btn_game);
        maintain = findViewById(R.id.Mantain);
        language_choice = findViewById(R.id.language_choice);

        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media.stop();
                media.release();
                HardwareControler.write(devfd, P.getBytes());
                Intent intent = new Intent(Mode_Choice.this, MainActivity.class);
                intent.putExtra("language",sign);
                startActivity(intent);
                finish();
            }
        });

        maintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media.stop();
                media.release();
                HardwareControler.write(devfd,M.getBytes());
                Intent intent = new Intent(Mode_Choice.this, Admin.class);
                startActivity(intent);
                finish();
            }
        });

        media = MediaPlayer.create(Mode_Choice.this, R.raw.rec1);
        media.start();
        media.setLooping(false);

        //set language change
        language_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = maintain.getText().toString();
                if (temp.equals(Maintain_CN)) {
                    sign = 1;
                    maintain.setText(Maintain_EN);
                    btn_game.setText(Game_EN);
                }
                else {
                    maintain.setText(Maintain_CN);
                    btn_game.setText(Game_CN);
                }
            }
        });
    }

}
