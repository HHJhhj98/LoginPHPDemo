package com.huhuijia.loginphpdemo.ui_student;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.huhuijia.loginphpdemo.R;
import com.huhuijia.loginphpdemo.entity.Constants;
import com.huhuijia.loginphpdemo.entity.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.huhuijia.loginphpdemo.entity.Constants.SNO;

public class SelectActivity extends AppCompatActivity {

    private ListView mLv;
    private String sno;

    private List<Data> datas = new ArrayList<Data>();
    //主线程创建消息处理器
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == Constants.SUCCESS) {
                try {
                    JSONArray arr = new JSONArray((String) msg.obj);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject temp = (JSONObject) arr.get(i);
                        // Log.d("json", temp.getInt("id")+temp.getString("exp_name")+temp.getString("exp_tech"));
                        Data data = new Data();
                        data.setGuidance(temp.getString("guidance"));
                        data.setSource(temp.getString("source"));
                        data.setSubject(temp.getString("subject"));
                        data.setTechnology(temp.getString("technology"));
                        data.setType(temp.getString("type"));
                        data.setSno(temp.getString("sno"));
                        data.setUsername(temp.getString("username"));
                        //这个地方可以获取到值但是适配器那位0
                        datas.add(data);


                    }
                    mLv.setAdapter(new MyAdapter());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initView();
    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.lv);
        Intent intent = getIntent();
        sno = intent.getStringExtra("sno");
        select();
    }

    private void select() {
        //子线程更新UI
        new Thread() {
            public void run() {
                try {
                    StringBuilder builder = new StringBuilder();
                    String path = "http://119.29.94.69/android/student.php";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //区别2、请求方式post
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)");
                    //区别3、必须指定两个请求的参数
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//请求的类型  表单数据
                    //System.out.println(sno);
                    String data = "sno=" + sno;
                    conn.setRequestProperty("Content-Length", data.length() + "");//数据的长度
                    //区别4、记得设置把数据写给服务器
                    conn.setDoOutput(true);//设置向服务器写数据
                    byte[] bytes = data.getBytes();
                    conn.getOutputStream().write(bytes);//把数据以流的方式写给服务器
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
                        Log.e(SelectActivity.class.toString(), "Failed");
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }


            }

            ;
        }.start();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            Log.d("AAA", "" + datas.size());
            return datas.size();


        }

        @Override
        public Object getItem(int position) {

            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(SelectActivity.this, R.layout.item_student, null);
            EditText mSubjectET = (EditText) view.findViewById(R.id.subjectET);
            EditText mTypeET = (EditText) view.findViewById(R.id.typeET);
            EditText mSourceET = (EditText) view.findViewById(R.id.sourceET);
            EditText mGuidanceET = (EditText) view.findViewById(R.id.guidanceET);
            EditText mTechnologyET = (EditText) view.findViewById(R.id.technologyET);
            EditText mSno=(EditText)view.findViewById(R.id.snoET);
            EditText mUsername=(EditText)view.findViewById(R.id.usernameET);
            Data data = datas.get(position);
            mGuidanceET.setText(datas.get(position).getGuidance());
            mSourceET.setText(datas.get(position).getSource());
            mSubjectET.setText(datas.get(position).getSubject());
            mTechnologyET.setText(datas.get(position).getTechnology());
            mTypeET.setText(datas.get(position).getType());
            mSno.setText(data.getSno());
            mUsername.setText(data.getUsername());
            return view;
        }


    }
}
