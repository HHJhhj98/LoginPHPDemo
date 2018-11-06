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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.huhuijia.loginphpdemo.entity.Constants.ERROR;
import static com.huhuijia.loginphpdemo.entity.Constants.SUCCESS;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTv;
    private EditText mUsernameET;
    private Button mForgetBtn;
    private TextView mRegisterTV;
    private TextView mProblemTV;
    private TextView mAboutTV;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            switch (msg.what) {
                case SUCCESS:
                    String username = mUsernameET.getText().toString().trim();
                    Toast.makeText(ForgetActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                    intent.putExtra("sno", username);
                    if (s.equals(username)) {
                    } else {
                        intent.putExtra("password", (String) msg.obj);
                    }
                    startActivity(intent);
                    finish();
                    break;

                case ERROR:
                    Toast.makeText(ForgetActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    private void initView() {
        mTv = (TextView) findViewById(R.id.tv);
        mUsernameET = (EditText) findViewById(R.id.usernameET);
        mForgetBtn = (Button) findViewById(R.id.forgetBtn);
        mForgetBtn.setOnClickListener(this);

        mRegisterTV = (TextView) findViewById(R.id.registerTV);
        mRegisterTV.setOnClickListener(this);
        mProblemTV = (TextView) findViewById(R.id.problemTV);
        mProblemTV.setOnClickListener(this);
        mAboutTV = (TextView) findViewById(R.id.aboutTV);
        mAboutTV.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgetBtn:
                btn_ok();
                break;
            case R.id.aboutTV:
                startActivity(new Intent(ForgetActivity.this, RegisterActivity.class));
                break;
            case R.id.problemTV:
                startActivity(new Intent(ForgetActivity.this, RegisterActivity.class));
                break;
        }
    }

    public void btn_ok() {
        final String username = mUsernameET.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(ForgetActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread() {
            public void run() {
                try {
                    String path = "http://119.29.94.69/android/forget.php";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//请求的类型  表单数据
                    String data = "sno=" + username + "&button=";
                    conn.setRequestProperty("Content-Length", data.length() + "");//数据的长度
                    conn.setDoOutput(true);//设置向服务器写数据
                    byte[] bytes = data.getBytes();
                    conn.getOutputStream().write(bytes);//把数据以流的方式写给服务
                    int code = conn.getResponseCode();
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


                } catch (Exception e) {
                    Message mas = Message.obtain();
                    mas.what = ERROR;
                    handler.sendMessage(mas);
                }
            }

            ;
        }.start();
    }
}
