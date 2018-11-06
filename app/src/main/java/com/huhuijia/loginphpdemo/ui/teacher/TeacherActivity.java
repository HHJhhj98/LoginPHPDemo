package com.huhuijia.loginphpdemo.ui.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class TeacherActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mTeacherLV;
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
                        if (temp.getString("sno") != null||temp.getString("sno") != "") {
                            data.setGuidance(temp.getString("guidance"));
                            data.setSno(temp.getString("sno"));
                            data.setSubject(temp.getString("subject"));
                            data.setUsername(temp.getString("username"));
                            //这个地方可以获取到值但是适配器那位0
                            datas.add(data);
                        }


                    }

                    mTeacherLV.setAdapter(new MyAdapter());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        initView();
    }

    private void initView() {
        mTeacherLV = (ListView) findViewById(R.id.teacherLV);
        mTeacherLV.setOnItemClickListener(this);
        select();
    }

    private void select() {
        //子线程更新UI
        new Thread() {
            public void run() {
                try {
                    StringBuilder builder = new StringBuilder();
                    String path = "http://119.29.94.69/android/teacher_all.php";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //区别2、请求方式post
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0(compatible;MSIE 9.0;Windows NT 6.1;Trident/5.0)");
                    //区别3、必须指定两个请求的参数
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//请求的类型  表单数据
                    //System.out.println(sno);
                    String data = "";
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
                        Log.e(TeacherActivity.class.toString(), "Failed");
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

            View view = View.inflate(TeacherActivity.this, R.layout.item_teacher, null);
            TextView mSnoTV = (TextView) view.findViewById(R.id.snoTV);
            TextView mUsernameTV = (TextView) view.findViewById(R.id.usernameTV);
            TextView mSubjectTV = (TextView) view.findViewById(R.id.subjectTV);
            TextView mGuidanceTV = (TextView) view.findViewById(R.id.guidanceTV);
            TextView mCheckBTN = (Button) view.findViewById(R.id.checkBTN);
            final Data data = datas.get(position);
            mGuidanceTV.setText("指导老师：" + data.getGuidance());
            mSnoTV.setText("学号：" + data.getSno());
            mSubjectTV.setText("题目：" + data.getSubject());
            mUsernameTV.setText("姓名：" + data.getUsername());
            mCheckBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TeacherActivity.this, DetailedActivity.class);
                    intent.putExtra("sno", data.getSno());
                    startActivity(intent);
                }
            });
            return view;
        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
