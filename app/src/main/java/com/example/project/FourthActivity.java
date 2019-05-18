package com.example.project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.frakbot.jumpingbeans.JumpingBeans;
import java.util.Timer;
import java.util.TimerTask;

public class FourthActivity extends AppCompatActivity {

    private Timer timer = new Timer();
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        final TextView textview = findViewById(R.id.textview);

        // Append jumping dots
        JumpingBeans jumpingBeans1 = JumpingBeans.with(textview)
                .appendJumpingDots()
                .build();

        // first word jumping
        JumpingBeans jumpingBeans2 = JumpingBeans.with(textview)
                .makeTextJump(0, textview.getText().toString().indexOf(" "))
                .setIsWave(true)
                .setLoopDuration(3000)
                .build();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mediaPlayer.stop();
                mediaPlayer.release();
                Intent intent = new Intent(FourthActivity.this, BlankPage.class);
                startActivity(intent);
            }
        }, 10000); //10s跳转页面

        mediaPlayer = MediaPlayer.create(FourthActivity.this, R.raw.rec5);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }
}
