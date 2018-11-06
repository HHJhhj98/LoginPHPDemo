package com.huhuijia.loginphpdemo.ui_login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huhuijia.loginphpdemo.R;
import com.huhuijia.loginphpdemo.entity.StreamTools;
import com.huhuijia.loginphpdemo.ui.teacher.TeacherActivity;
import com.huhuijia.loginphpdemo.ui_student.AddActivity;
import com.huhuijia.loginphpdemo.ui_student.SelectActivity;
import com.huhuijia.loginphpdemo.ui_student.StudentActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.huhuijia.loginphpdemo.entity.Constants.ERROR;
import static com.huhuijia.loginphpdemo.entity.Constants.SUCCESS;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtQq;
    private EditText mEtPwd;
    private Button mLoginBtn;
    private TextView mForgetTV;
    private TextView mRegisterTV;
    private TextView mProblemTV;
    private TextView mAboutTV;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    String s=(String) msg.obj;
                    String sno=mEtQq.getText().toString().trim();
                    Toast.makeText(LoginActivity.this, (String) msg.obj+sno, Toast.LENGTH_SHORT).show();
                    if ( s.equals("登录失败")) {

                    }else if (s.equals("false")){
                        Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                        intent.putExtra("sno", sno);
                        startActivity(intent);
                        finish();
                    }else if (s.equals("true")){
                        Intent intent = new Intent(LoginActivity.this, TeacherActivity.class);
                        intent.putExtra("sno", (String) msg.obj);
                        startActivity(intent);
                        finish();
                    }
                    break;

                case ERROR:
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        ;
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void getData() {
        Intent intent = getIntent();
        String sno = intent.getStringExtra("sno");
        String password = intent.getStringExtra("password");
        mEtQq.setText(sno);
        mEtPwd.setText(password);
    }

    private void initView() {
        mEtQq = (EditText) findViewById(R.id.et_qq);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);

        mLoginBtn.setOnClickListener(this);

        mForgetTV = (TextView) findViewById(R.id.forgetTV);
        mForgetTV.setOnClickListener(this);
        mRegisterTV = (TextView) findViewById(R.id.registerTV);
        mRegisterTV.setOnClickListener(this);
        mProblemTV = (TextView) findViewById(R.id.problemTV);
        mProblemTV.setOnClickListener(this);
        mAboutTV = (TextView) findViewById(R.id.aboutTV);
        mAboutTV.setOnClickListener(this);

        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                login();
                break;
            case R.id.forgetTV:
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                break;
            case R.id.registerTV:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.aboutTV:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.problemTV:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    public void login() {
        final String qq = mEtQq.getText().toString();
        final String psd = mEtPwd.getText().toString();

        if (TextUtils.isEmpty(qq) || TextUtils.isEmpty(psd)) {
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread() {
            public void run() {
                try {
                    //http://localhost/xampp/android/login.php
                    //区别1、url的路径不同
                    String path = "http://119.29.94.69/android/login.php";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //区别2、请求方式post
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)");
                    //区别3、必须指定两个请求的参数
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//请求的类型  表单数据
                    String data = "sno=" + qq + "&password=" + psd + "&button=";
                    conn.setRequestProperty("Content-Length", data.length() + "");//数据的长度
                    //区别4、记得设置把数据写给服务器
                    conn.setDoOutput(true);//设置向服务器写数据
                    byte[] bytes = data.getBytes();
                    conn.getOutputStream().write(bytes);//把数据以流的方式写给服务器
                    int code = conn.getResponseCode();
                    System.out.println(code);
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamTools.readStream(is);
                        Message mas = Message.obtain();
                        mas.what = SUCCESS;
                        mas.obj = result;
                        handler.sendMessage(mas);

                    } else {
                        Message mas = Message.obtain();
                        mas.what = ERROR;
                        handler.sendMessage(mas);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Message mas = Message.obtain();
                    mas.what = ERROR;
                    handler.sendMessage(mas);
                }
            }
        }.start();

    }
}
