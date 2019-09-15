 package com.example.serialportdemo;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.util.Log;
import android.text.Html;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;
import com.friendlyarm.FriendlyThings.HardwareControler;
import com.friendlyarm.FriendlyThings.BoardType;
import com.example.serialportdemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener {
    private static final String TAG = "SerialPort";
    private TextView fromTextView = null;   // 用来接收收到的数据，真正使用的时候可以加上动画效果，不用TextView去显示；
    private EditText toEditor = null;     // 要发送的输入数据
    private final int MAXLINES = 200;       // 最大显示行数
    private StringBuilder remoteData = new StringBuilder(256 * MAXLINES);   // 字符串的最大容量

    // NanoPC-T4 UART3 *
    private String devName = "/dev/ttyAMA3";  // 设置使用的串口，3号串口
    private int speed = 115200;		// 设置波特率
    private int dataBits = 8;       // 8位数据位
    private int stopBits = 1;       // 1位截止位
    private int devfd = -1;         // 句柄值，用来判断状态；和Matlab比较相似

    @Override
    public void onDestroy() {       // *
        timer.cancel();
        if (devfd != -1) {
            HardwareControler.close(devfd);     // 关闭接口；-1是关闭信号
            devfd = -1;
        }
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.serialport_dataprocessview_landscape);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.serialport_dataprocessview);
        }		//横竖屏检测并切换；真正程序可以不用

        String winTitle = devName + "," + speed + "," + dataBits + "," + stopBits;
        setTitle(winTitle);

        ((Button)findViewById(R.id.sendButton)).setOnClickListener(this);

        fromTextView = (TextView)findViewById(R.id.fromTextView);
        toEditor = (EditText)findViewById(R.id.toEditor);

        /* no focus when begin */
        toEditor.clearFocus();
        toEditor.setFocusable(false);
        toEditor.setFocusableInTouchMode(true);

        // *
        devfd = HardwareControler.openSerialPort( devName, speed, dataBits, stopBits );     //打开串口，如果成功的话的返回值>=0
        if (devfd >= 0) {
            timer.schedule(task, 0, 500);   //从现在起过0ms以后，每隔500ms执行一次task；
        } else {
            devfd = -1;
            fromTextView.append("Fail to open " + devName + "!");
        }
    }

    //以下是屏显部分的代码 *
    private final int BUFSIZE = 512;
    private byte[] buf = new byte[BUFSIZE];
    private Timer timer = new Timer();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {    //新建handler事件；
            switch (msg.what) {
                case 1:
                    if (HardwareControler.select(devfd, 0, 0) == 1) {
                        int retSize = HardwareControler.read(devfd, buf, BUFSIZE);  //从devfd中读取BUFSIZE大小的数据到buf中，成功返回读取的字节数，反之为-1
                        if (retSize > 0) {
                            String str = new String(buf, 0, retSize);   //把buf中的数据转给str;
                            remoteData.append(str);

                            //Log.d(TAG, "#### LineCount: " + fromTextView.getLineCount() + ", remoteData.length()=" + remoteData.length());
                            if (fromTextView.getLineCount() > MAXLINES) {   //接受到的数据过多
                                int nLineCount = fromTextView.getLineCount();   //得到TextView的行数
                                int i = 0;
                                for (i = 0; i < remoteData.length(); i++) {
                                    if (remoteData.charAt(i) == '\n') {
                                        nLineCount--;   //每检测到一行，总行数减一，一直减到行数在MAXLINES之内截止；

                                        if (nLineCount <= MAXLINES) {
                                            break;
                                        }
                                    }
                                }
                                remoteData.delete(0, i);    //删掉从0->i的值；即，原则是保留新的值，舍掉旧值；
                                //Log.d(TAG, "#### remoteData.delete(0, " + i + ")");
                                fromTextView.setText(remoteData.toString());
                            } else {
                                fromTextView.append(str);   //这种情况下直接显示即可；
                            }

                            ((ScrollView)findViewById(R.id.scroolView)).fullScroll(View.FOCUS_DOWN);
                        }
                    }
                    break;
            }
            super.handleMessage(msg);   //不断调用handle事件，循环检查；
        }
    };
    private TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    //多按键检测
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.sendButton:
                String str = toEditor.getText().toString();
                if (str.length() > 0) {
                    if (str.charAt(str.length()-1) != '\n') {   //强制换行
                        str = str + "\n";
                    }
                    int ret = HardwareControler.write(devfd, str.getBytes());   //向devfd写入getBytes()个数据；成功返回写入的字节数，失败返回-1；
                    if (ret > 0) {
                        toEditor.setText("");   //写入成功，输入栏置空即可；

                        str = ">>> " + str;     //设置要屏显的内容；
                        if (remoteData.length() > 0) {
                            if (remoteData.charAt(remoteData.length()-1) != '\n') {     //接收的内容强制换行；
                                remoteData.append('\n'); 
                                fromTextView.append("\n");
                            }
                        }
                        remoteData.append(str);
                        fromTextView.append(str);

                        ((ScrollView)findViewById(R.id.scroolView)).fullScroll(View.FOCUS_DOWN);
                    } else {
                        Toast.makeText(this,"Fail to send!",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}