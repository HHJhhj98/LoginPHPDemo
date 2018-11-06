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

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.huhuijia.loginphpdemo.entity.Constants.ERROR;
import static com.huhuijia.loginphpdemo.entity.Constants.SUCCESS;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mRegisterBtn;
    private TextView mForgetTV;
    private TextView mProblemTV;
    private TextView mAboutTV;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    String sno = et_sno.getText().toString().trim();
                    String password = mEtPassword.getText().toString().trim();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("sno", sno);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                    Toast.makeText(RegisterActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();

                    break;

                case ERROR:
                    Toast.makeText(RegisterActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    private EditText et_sno;
    private EditText mEtPsw;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mRegisterBtn = (Button) findViewById(R.id.registerBtn);
        mRegisterBtn.setOnClickListener(this);
        mForgetTV = (TextView) findViewById(R.id.forgetTV);
        mForgetTV.setOnClickListener(this);
        mProblemTV = (TextView) findViewById(R.id.problemTV);
        mProblemTV.setOnClickListener(this);
        mAboutTV = (TextView) findViewById(R.id.aboutTV);
        mAboutTV.setOnClickListener(this);
        et_sno = (EditText) findViewById(R.id.et_sno);
        et_sno.setOnClickListener(this);
        mEtPsw = (EditText) findViewById(R.id.et_psw);
        mEtPsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtn:
                submit();
                break;
            case R.id.forgetTV:
                startActivity(new Intent(RegisterActivity.this, ForgetActivity.class));
                break;
            case R.id.aboutTV:
                startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                break;
            case R.id.problemTV:
                startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                break;
        }
    }

    private void submit() {
        // validate
        final String sno = et_sno.getText().toString().trim();
        if (TextUtils.isEmpty(sno)) {
            Toast.makeText(this, "学号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String username = mEtUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        final String password = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        String psw = mEtPsw.getText().toString().trim();
        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(this, "psw不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!psw.equals(password)){
            Toast.makeText(this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread() {
            public void run() {
                try {
                    String path = "http://119.29.94.69/android/register.php";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//请求的类型  表单数据
                    conn.setDoOutput(true);//设置向服务器写数据
                    conn.setDoInput(true);
                    conn.connect();
                    //System.out.println(sno);
                    //String data = "sno=" + sno + "&subject=" + subjectETString + "&type=" + typeETString
                    // + "&source=" + sourceETString + "&guidance=" + guidanceETString + "&technology=" + technologyETString;
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("sno", sno);
                    data.put("password", password);
                    data.put("username", username);
                    JSONObject jsonObject = new JSONObject(data);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream out = new DataOutputStream(os);
                    out.write(jsonObject.toString().getBytes());
                    //out.writeBytes(jsonObject.toString());
                    //conn.setRequestProperty("Content-Length", data.size() + "");//数据的长度
                    //区别4、记得设置把数据写给服务器
                    out.flush();
                    os.close();
                    out.close();
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
