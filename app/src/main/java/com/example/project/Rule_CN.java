package com.example.project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Rule_CN extends AppCompatActivity {

    public Button Return;
    public MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_cn);

        Return = findViewById(R.id.btn_to_second);

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.release();
                Intent intent = new Intent(Rule_CN.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        mediaPlayer = MediaPlayer.create(Rule_CN.this, R.raw.rec4);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }
}
