package com.example.project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.friendlyarm.FriendlyThings.HardwareControler;
import java.util.Timer;
import java.util.TimerTask;

public class MultiplayerActivity extends AppCompatActivity {

    //控件定义
    private TextView Hscore;
    private TextView Rscore;
    private TextView Indication;
    private TextView Score_ColumH;
    private TextView Score_ColumR;

    //使用的字符串常量
    private static final String Hturn = "YOUR TURN";
    private static final String Rturn = "PC TURN";
    private static final String HWIN = "YOU WIN!";
    private static final String RWIN = "YOU LOSE!";
    private static final String DRAW = "DRAW";
    private static final String YOURSCORE = "Your Score: ";
    private static final String ROBOTSCORE = "Robot Score: ";
    public final String OVER  = "O";

    //使用的变量
    public String sign; //标志用户，机器
    public String score; //标志比赛状态
    public String Rmarks; //用来记录Robot的分数，最终结果显示；
    public String val;
    public Context content = MultiplayerActivity.this;

    // serial port settings
    private final int BUFFSIZE = 512;
    public String devName = "/dev/ttyAMA3";  // 设置使用的串口
    public int speed = 9600;        // 波特率
    public int dataBits = 8;
    public int stopBits = 1;
    private int devfd = -1;
    private int num = 0; //用来标志收到的次数；
    private byte[] buf = new byte[BUFFSIZE];


    private Timer timer = new Timer();
    private Timer timer2 = new Timer();

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (HardwareControler.select(devfd, 0, 0) == 1) {
                    int retSize = HardwareControler.read(devfd, buf, BUFFSIZE);
                    if (retSize > 0) {
                        String str = new String(buf, 0, retSize);
                        sign = str.substring(0, 1); //判断是A/B;
                        score = str.substring(1, 2); //读取分数值，A B都有
                        // 根据字符串长度进行接下来的判断
                        int i = str.length();
                        if (i == 2) {// AE AC A1 BC BN BY
                            if (sign.equals("A")) {
                                switch (score) {
                                    case "E":
                                        val = "E";
                                        Indication.setText(DRAW);
                                        GameOver();
                                        break;
                                    case "C":
                                        Rscore.setText("----");
                                        Indication.setText(Rturn);
                                        break;
                                    default:
                                        Rscore.setText("----");
                                        Hscore.setText(score);
                                        break;
                                }

                            } else { //BY BC BN
                                if (score.equals("C")) { //BC
                                    Indication.setText(Hturn);
                                } else { //BN BY
                                    Rscore.setText(score);
                                }
                            }
                        } else {//AW9 BW9 A12 AW12 BW12
                            String string = str.substring(1, 2); //取出W
                            String string2 = str.substring(1); //A12 取出12
                            Rmarks = str.substring(2);
                                if (sign.equals("A")) { //AW9 AW12 A12
                                    if (string.equals("W")) { //AW12 AW9
                                        val = "W";
                                        num += 1;
                                        Indication.setText(HWIN);
                                        String temp1 = YOURSCORE + Get_String(Hscore);
                                        String temp2 = ROBOTSCORE +Rmarks;
                                        Score_ColumH.setText(temp1);
                                        Score_ColumR.setText(temp2);
                                        GameOver();
                                    } else { //A12
                                        Hscore.setText(string2);
                                    }
                                } else { //BW9 BW12
                                    val = "L";
                                    num += 1;
                                    Indication.setText(RWIN);
                                    String temp1 = YOURSCORE + Get_String(Hscore);
                                    String temp2 = ROBOTSCORE +Rmarks;
                                    Score_ColumH.setText(temp1);
                                    Score_ColumR.setText(temp2);
                                    GameOver();
                                }
                            }
                        }
                    }
                }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        //初始化
        Hscore = findViewById(R.id.Human_Score);
        Rscore = findViewById(R.id.Rebot_Score);
        Indication = findViewById(R.id.Indication);
        Score_ColumH = findViewById(R.id.Score_ColumnH);
        Score_ColumR = findViewById(R.id.Score_ColumnR);

        // serial port setting
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

        //返回和退出界面
        Button btn_re_2 = findViewById(R.id.btn_re_2);
        Button btn_to_4 = findViewById(R.id.btn_to_4);

        btn_re_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(content)
                        .setNegativeButton(getResources().getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(
                                        MultiplayerActivity.this, SecondActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        btn_to_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(content)
                        .setNegativeButton(getResources().getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(
                                        MultiplayerActivity.this, SecondActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        if (devfd != -1) {
            HardwareControler.write(devfd, OVER.getBytes());
            HardwareControler.close(devfd);
            devfd = -1;
        }
        super.onDestroy();
    }

    //获取TextView的字符串；
    public String Get_String(TextView textView) {
        return textView.getText().toString();
    }

    public void GameOver() {
        Rscore.setText("----");

        /*
        * 加入 num 进行判断，num = 1代表现在已接收到了结果，可以跳转；
        * 带值跳转必须只能传递一次，不然显示的结果就是错误的
        * */
        if (num == 1) {
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    //向32发送结束信号
                    HardwareControler.write(devfd,OVER.getBytes());

                    Intent intent = new Intent(MultiplayerActivity.this, Rank.class);
                    intent.putExtra("score", val);
                    startActivity(intent);
                }
            }, 2500);
        }

    }
}
