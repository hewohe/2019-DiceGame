package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Rank extends AppCompatActivity {

    public Button Newstart;
    public Button quit;
    private TextView win_num; //赢数
    private TextView draw_num; //平局数
    private TextView lose_num; //输局
    public int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        Newstart = findViewById(R.id.NewStart);
        quit = findViewById(R.id.Quit);
        win_num = findViewById(R.id.win_num);
        draw_num = findViewById(R.id.draw_num);
        lose_num = findViewById(R.id.lose_num);

        //获取上个页面的成绩值；并转换成相应的比分；
        Intent intent = getIntent();
        String temp = intent.getStringExtra("score");
        switch (temp) {
            case "W":
                score = 100;
                break;
            case "E":
                score = 10;
                break;
            case "L":
                score = 1;
                break;
            default:
                score = 0;
                break;
        }

        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //获取,更新，显示用户成绩
        int user0_score = sp.getInt("USER0SCORE", 0);
        int user1_score = sp.getInt("USER1SCORE", 0);
        if (sp.getInt("USER0", 0) == 1){ //此时是 USER0
            //增加成绩；
            user0_score += score;
            Get_Num(user0_score);
            editor.putInt("USER0SCORE",user0_score);
        } else {
            user1_score += score;
            editor.putInt("USER1SCORE",user1_score);
            Get_Num(user1_score);
        }
        editor.apply();

        //界面跳转
        Newstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rank.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rank.this, FourthActivity.class);
                startActivity(intent);
            }
        });
    }

    //提取分数值，并且显示在屏幕上；
    public void Get_Num(int mark) {
        if (mark >= 100) {
            int i = mark/100;
            String tem1 = String.valueOf(i); //取出赢的次数
            win_num.setText(tem1);

            int j = mark/10 - mark/100*10;
            String tem2 = String.valueOf(j);
            draw_num.setText(tem2);

            int k = mark%10;
            String tem3 = String.valueOf(k);
            lose_num.setText(tem3);
        } else if (mark >= 10) {
            win_num.setText("0");

            int i = mark/10;
            String tem1 = String.valueOf(i);
            draw_num.setText(tem1);

            int j = mark%10;
            String tem2 = String.valueOf(j);
            lose_num.setText(tem2);
        } else {
            win_num.setText("0");

            draw_num.setText("0");

            String tem = String.valueOf(mark);
            lose_num.setText(tem);
        }
    }
}
