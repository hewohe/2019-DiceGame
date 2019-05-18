package com.example.project;

/**
 * 实现视频播放，根据串口发数跳转
 */

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.friendlyarm.FriendlyThings.HardwareControler;

import java.util.Timer;
import java.util.TimerTask;

public class BlankPage extends AppCompatActivity {

    public MediaPlayer mediaPlayer;

    // serial port settings
    public String devName = "/dev/ttyAMA3";  // 设置使用的串口
    public final String PEOPLE = "people";
    public int speed = 9600;        // 波特率
    public int dataBits = 8;
    public int stopBits = 1;
    public int devfd = -1;
    private final int BUFFSIZE = 512;
    private byte[] buf = new byte[BUFFSIZE];

    private Timer timer = new Timer();

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (HardwareControler.select(devfd, 0, 0) == 1) {
                    int retSize = HardwareControler.read(devfd, buf, BUFFSIZE);
                    if (retSize > 0) {
                        String str = new String(buf, 0, retSize);
                        if (str.equals(PEOPLE)) {
                            Intent intent = new Intent(
                                    BlankPage.this, Mode_Choice.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
            return false;
        }
    });

    @Override
    public void onDestroy() {
        timer.cancel();
        if (devfd != -1) {
            HardwareControler.close(devfd);
            devfd = -1;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_page);

        //串口设置
        devfd = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);
        if (devfd >= 0) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }, 0, 1);
        } else {
            devfd = -1;
        }

        //播放音频，会一直在背景循环，不会停止；
        mediaPlayer = MediaPlayer.create(BlankPage.this, R.raw.myth);
        mediaPlayer.setLooping(true);

        //先检测是否在播放
        if (!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }
}
