package com.example.project;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.friendlyarm.FriendlyThings.HardwareControler;

import java.util.Timer;
import java.util.TimerTask;

public class Maintain extends AppCompatActivity {

    private TextView state;
    private Button quit;
    private Button btn_motor1;
    private Button btn_motor2;
    private Button color_sensor;
    private Button distance_sensor;
    private Button id_sensor;

    // serial port settings
    private final int BUFFSIZE = 512;
    public String devName = "/dev/ttyAMA3";  // 设置使用的串口
    private int speed = 9600;        // 波特率
    private int dataBits = 8;
    private int stopBits = 1;
    private int devfd = -1;
    private Timer timer = new Timer();
    private byte[] buf = new byte[BUFFSIZE];

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1) {
                    if (HardwareControler.select(devfd, 0, 0) == 1) {
                        int retSize = HardwareControler.read(devfd, buf, BUFFSIZE);
                        if (retSize > 0) {
                            String str = new String(buf, 0, retSize);
                            state.setText(str);
                        }
                    }
            }
            return false;
        }
    });

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

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
        setContentView(R.layout.activity_maintain);

        state = findViewById(R.id.State);
        quit  =  findViewById(R.id.Quit);
        btn_motor1 = findViewById(R.id.btn_motor1);
        btn_motor2 = findViewById(R.id.btn_motor2);
        color_sensor = findViewById(R.id.color_sensor);
        distance_sensor = findViewById(R.id.distance_sensor);
        id_sensor = findViewById(R.id.id_sensor);

        devfd = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);
        if (devfd >= 0) {
            timer.schedule(task, 0, 1);
        } else {
            devfd = -1;
        }

        // Motor Maintenance
        btn_motor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData("1");
            }
        });

        btn_motor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData("2");
            }
        });

        // Sensor Maintenance
        color_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData("C");
            }
        });

        distance_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData("D");
            }
        });

        id_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData("R");
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendData("Q");
                Intent intent = new Intent(Maintain.this, Mode_Choice.class);
                startActivity(intent);
            }
        });
    }


    private void SendData(String string) {
        int ret = HardwareControler.write(devfd, string.getBytes());

        if (ret > 0) {
            Toast.makeText(Maintain.this, "状态正常",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Maintain.this, "Fail to send!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
