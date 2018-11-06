package com.huhuijia.loginphpdemo.ui.teacher;

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
import android.widget.Toast;

import com.huhuijia.loginphpdemo.R;
import com.huhuijia.loginphpdemo.entity.Data;
import com.huhuijia.loginphpdemo.entity.StreamTools;
import com.huhuijia.loginphpdemo.ui_login.LoginActivity;
import com.huhuijia.loginphpdemo.ui_login.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.huhuijia.loginphpdemo.entity.Constants.ERROR;
import static com.huhuijia.loginphpdemo.entity.Constants.SUCCESS;

public class DetailedActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mSnoET;
    private EditText mUsernameET;
    private EditText mSubjectET;
    private EditText mTypeET;
    private EditText mSourceET;
    private EditText mGuidanceET;
    private EditText mTechnologyET;
    private Button mSBTN;
    private String sno;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONArray arr = new JSONArray((String) msg.obj);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject temp = (JSONObject) arr.get(i);
                            // Log.d("json", temp.getInt("id")+temp.getString("exp_name")+temp.getString("exp_tech"));

                            mGuidanceET.setText(temp.getString("guidance"));
                            mSnoET.setText(temp.getString("sno"));
                            mSubjectET.setText(temp.getString("subject"));
                            mUsernameET.setText(temp.getString("username"));
                            mTypeET.setText(temp.getString("type"));
                            mTechnologyET.setText(temp.getString("technology"));
                            mSourceET.setText(temp.getString("source"));
                            //这个地方可以获取到值但是适配器那位0

                        }


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;

                case ERROR:
                    Toast.makeText(DetailedActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
    }

    private void initView() {
        mSnoET = (EditText) findViewById(R.id.snoET);
        mUsernameET = (EditText) findViewById(R.id.usernameET);
        mSubjectET = (EditText) findViewById(R.id.subjectET);
        mTypeET = (EditText) findViewById(R.id.typeET);
        mSourceET = (EditText) findViewById(R.id.sourceET);
        mGuidanceET = (EditText) findViewById(R.id.guidanceET);
        mTechnologyET = (EditText) findViewById(R.id.technologyET);
        mSBTN = (Button) findViewById(R.id.sBTN);

        mSBTN.setOnClickListener(this);

        Intent intent=getIntent();
        sno=intent.getStringExtra("sno");
        submit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sBTN:
                Intent intent = new Intent(DetailedActivity.this, TeacherActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void submit() {
        // validate
//        String snoETString = mSnoET.getText().toString().trim();
//        if (TextUtils.isEmpty(snoETString)) {
//            Toast.makeText(this, "snoETString不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String usernameETString = mUsernameET.getText().toString().trim();
//        if (TextUtils.isEmpty(usernameETString)) {
//            Toast.makeText(this, "usernameETString不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String subjectETString = mSubjectET.getText().toString().trim();
//        if (TextUtils.isEmpty(subjectETString)) {
//            Toast.makeText(this, "subjectETString不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String typeETString = mTypeET.getText().toString().trim();
//        if (TextUtils.isEmpty(typeETString)) {
//            Toast.makeText(this, "typeETString不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String sourceETString = mSourceET.getText().toString().trim();
//        if (TextUtils.isEmpty(sourceETString)) {
//            Toast.makeText(this, "sourceETString不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String guidanceETString = mGuidanceET.getText().toString().trim();
//        if (TextUtils.isEmpty(guidanceETString)) {
//            Toast.makeText(this, "guidanceETString不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String technologyETString = mTechnologyET.getText().toString().trim();
//        if (TextUtils.isEmpty(technologyETString)) {
//            Toast.makeText(this, "technologyETString不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }

        new Thread() {
            public void run() {
                try {
                    String path = "http://119.29.94.69/android/teacher.php";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//请求的类型  表单数据
                    String data = "sno=" + sno;
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
