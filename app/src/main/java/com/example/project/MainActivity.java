package com.example.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.friendlyarm.FriendlyThings.HardwareControler;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    // 控件声明
    public Button Accountlog;
    public Button Cardlog;

    public MediaPlayer mediaPlayer;
    private final String M = "M";
    public String str;
    public int language;
    private int num = 0;
    private final String Card_en = "Login with Card";
    private final String Account_en = "Login with Account";

    // serial port settings
    public String devName = "/dev/ttyAMA3";
    public  int speed = 9600;
    public int dataBits = 8;
    public int stopBits = 1;
    private int devfd = -1;
    private final int BUFFSIZE = 512;
    private byte[] buf = new byte[BUFFSIZE];

    //定义定时器
    private Timer timer = new Timer();
    private Timer timer1 = new Timer();

    //定义 Handler 事件，循环检测；
    //已于5.16重写，解决内存泄漏问题；
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (HardwareControler.select(devfd, 0, 0) == 1) {
                    int retSize = HardwareControler.read(devfd, buf, BUFFSIZE);
                    if (retSize > 0) {
                        mediaPlayer.stop();
                        str = new String(buf, 0, retSize);
                        num += 1;
                        //保存用户信息
                        saveLoginInfo(str);

                        //增加 LOADING 页面
                        //只要读取到数，就会显示Loading页面，所以不能写在timer1内部
                        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setTitle("Loading");
                        progressDialog.setMessage(str + "欢迎回来！");
                        progressDialog.setCancelable(true);
                        progressDialog.show();

                        //插卡直接进入；也可以选择注册登录
                        //插卡直接登录
                        if (num == 1) {
                            timer1.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this,
                                            SecondActivity.class);
                                    intent.putExtra("language", language);
                                    startActivity(intent);
                                    finish();
                                }}, 1500);
                        }

                    }
                }
            }
            return false;
        }
    });

    @Override
    public void onDestroy() {
        //释放音频资源
        mediaPlayer.release();
        //释放定时器
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
        setContentView(R.layout.activity_main);

        Cardlog = findViewById(R.id.Cardlog);
        Accountlog = findViewById(R.id.Accountlog);
        //切换语言
        Intent intent = getIntent();
        language = intent.getIntExtra("language", 0);
        if (language == 1) { //切换英文模式
            Cardlog.setText(Card_en);
            Accountlog.setText(Account_en);
        }

        //打开串口；
        devfd = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);
        //执行Handler事件；
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

        //账号登录，跳转到登录页面；
        Accountlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //播放rec2
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rec2);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

    }

    private void saveLoginInfo (String str) {

        int i, j;
        //直接使用"loginInfo"保存信息；
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //存入USER0 USER1 的数据，用0，1来标志用户
        if (str.equals("USER0")) {
            i = 1;
            j = 0;
        } else {
            i = 0;
            j = 1;
        }
        editor.putInt("USER0", i);
        editor.putInt("USER1", j);

        editor.apply();
    }
}
