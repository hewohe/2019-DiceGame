package com.example.project;

/**
 * 登录维护模式
 * 账户：admin
 * 密码：123456
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Admin extends AppCompatActivity {

    private Button btn;//登录按钮
    private CheckBox checkBox;//是否保存密码的选择框
    private EditText edit_name, edit_psd;//用户名和密码的文本输入框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        initView();
    }


    /**
     * 初始化数据
     */
    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_psd = (EditText) findViewById(R.id.edit_psd);
        output();//刚进入就先取一次，看看当前状态

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //随便设置初始帐号和密码分别为 admin 和 abc
                if (edit_name.getText().toString().equals("admin")
                        && edit_psd.getText().toString().equals("123456")) {
                    input();//登录成功就把数据存起来
                    Intent intent = new Intent(Admin.this, Maintain.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Admin.this, "用户名或密码错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 取
     */
    private void output() {
        //第一个参数是文件名，第二个参数是模式（不明白可以去补习一下SharedPreferences的知识）
        SharedPreferences shared = getSharedPreferences("mypsd", MODE_PRIVATE);

        //第一个参数就是关键字，第二个参数为默认值，意思是说如果没找到值就用默认值代替
        String name1 = shared.getString("name", "");//同上，若没找到就让它为空""
        String psd1 = shared.getString("psd", "");
        boolean ischecked1 = shared.getBoolean("isChecked", false);
        edit_name.setText(name1);
        edit_psd.setText(psd1);
        checkBox.setChecked(ischecked1);
    }
    /**
     * 存到SD卡，判断选中获取缓存账号密码
     */
    private void input() {
        //第一个参数是文件名，第二个参数是模式（不明白可以去补习一下SharedPreferences的知识）
        SharedPreferences.Editor edit = getSharedPreferences("mypsd", MODE_PRIVATE).edit();

        //判断选择框的状态   被选中isChecked或……
        if (checkBox.isChecked()) {
            edit.putString("name", edit_name.getText().toString());
            edit.putString("psd", edit_psd.getText().toString());
            edit.putBoolean("isChecked", true);
        } else {
//            edit.clear();              //若选择全部清除就保留这行代码，注释以下三行
            edit.putString("name", edit_name.getText().toString());//只存用户名
            edit.putString("psd", "");
            edit.putBoolean("isChecked", false);
        }
        edit.commit();
    }
}
