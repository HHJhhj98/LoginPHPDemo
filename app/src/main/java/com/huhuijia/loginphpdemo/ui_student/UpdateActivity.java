package com.huhuijia.loginphpdemo.ui_student;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huhuijia.loginphpdemo.R;
import com.huhuijia.loginphpdemo.entity.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mSubjectET;
    private EditText mTypeET;
    private EditText mSourceET;
    private EditText mGuidanceET;
    private EditText mTechnologyET;
    private Button mSBTN;
    private String sno;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == Constants.SUCCESS) {
                Toast.makeText(UpdateActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private EditText mSnoET;
    private EditText mUsernameET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
    }

    private void initView() {
        mSubjectET = (EditText) findViewById(R.id.subjectET);
        mTypeET = (EditText) findViewById(R.id.typeET);
        mSourceET = (EditText) findViewById(R.id.sourceET);
        mGuidanceET = (EditText) findViewById(R.id.guidanceET);
        mTechnologyET = (EditText) findViewById(R.id.technologyET);
        mSBTN = (Button) findViewById(R.id.sBTN);

        mSBTN.setOnClickListener(this);
        Intent intent = getIntent();
        sno = intent.getStringExtra("sno");
        mSnoET = (EditText) findViewById(R.id.snoET);
        mUsernameET = (EditText) findViewById(R.id.usernameET);
        mSnoET.setText(sno);
        mSnoET.setFocusable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sBTN:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        final String subjectETString = mSourceET.getText().toString().trim();
        if (TextUtils.isEmpty(subjectETString)) {
            Toast.makeText(this, "subjectETString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String typeETString = mTypeET.getText().toString().trim();
        if (TextUtils.isEmpty(typeETString)) {
            Toast.makeText(this, "typeETString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String sourceETString = mSourceET.getText().toString().trim();
        if (TextUtils.isEmpty(sourceETString)) {
            Toast.makeText(this, "sourceETString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String guidanceETString = mGuidanceET.getText().toString().trim();
        if (TextUtils.isEmpty(guidanceETString)) {
            Toast.makeText(this, "guidanceETString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final String technologyETString = mTechnologyET.getText().toString().trim();
        if (TextUtils.isEmpty(technologyETString)) {
            Toast.makeText(this, "technologyETString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String usernameETString = mUsernameET.getText().toString().trim();
        if (TextUtils.isEmpty(usernameETString)) {
            Toast.makeText(this, "usernameETString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //子线程更新UI
        new Thread() {
            public void run() {
                try {
                    StringBuilder builder = new StringBuilder();
                    String path = "http://119.29.94.69/android/update.php";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //区别2、请求方式post
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)");
                    //区别3、必须指定两个请求的参数
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//请求的类型  表单数据
                    conn.setDoOutput(true);//设置向服务器写数据
                    conn.setDoInput(true);
                    conn.connect();
                    //System.out.println(sno);
                    //String data = "sno=" + sno + "&subject=" + subjectETString + "&type=" + typeETString
                    // + "&source=" + sourceETString + "&guidance=" + guidanceETString + "&technology=" + technologyETString;
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("sno", sno);
                    data.put("subject", subjectETString);
                    data.put("type", typeETString);
                    data.put("source", sourceETString);
                    data.put("guidance", guidanceETString);
                    data.put("technology", technologyETString);
                    data.put("username", usernameETString);
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
                        BufferedReader reader = new BufferedReader
                                (new InputStreamReader(is, "UTF-8"));
                        for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                            builder.append(s);
                        }
                        String content = builder.toString();
                        //通知主线程更新UI
                        Message message = new Message();
                        message.what = Constants.SUCCESS;
                        message.obj = content;
                        handler.sendMessage(message);
                    } else {
                        Log.e(AddActivity.class.toString(), "Failed");
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }


            }

            ;
        }.start();


    }

}
