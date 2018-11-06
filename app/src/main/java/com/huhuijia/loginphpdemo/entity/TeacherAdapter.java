package com.huhuijia.loginphpdemo.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.huhuijia.loginphpdemo.R;

import java.util.ArrayList;
import java.util.List;

public class TeacherAdapter extends BaseAdapter implements View.OnClickListener {

    private List<Data> mdatas;
    private List<String> mList;
    private LayoutInflater mInflater;
    private Callback mCallback;


    // 自定义接口，用于回调按钮点击事件到Activity
    public interface Callback {
        public void click(View v);
    }

    public TeacherAdapter(Context context, Callback callback,List<Data> datas) {
        mInflater = LayoutInflater.from(context);
        mCallback = callback;
        mdatas=datas;
    }

    @Override
    public int getCount() {
        return mdatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mdatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_teacher, null);
            holder = new ViewHolder();
            holder.mGuidanceTV=(TextView) convertView.findViewById(R.id.guidanceTV);
            holder.mSubjectTV=(TextView) convertView.findViewById(R.id.subjectTV);
            holder.mSnoTV=(TextView) convertView.findViewById(R.id.snoTV);
            holder.mUsernameTV=(TextView) convertView.findViewById(R.id.usernameET);
            holder.mCheckBTN=(Button) convertView.findViewById(R.id.checkBTN);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Data data = mdatas.get(position);
        holder.mGuidanceTV.setText(data.getGuidance());
        holder.mSubjectTV.setText(data.getSubject());
        holder.mSnoTV.setText(data.getSno());
        holder.mUsernameTV.setText(data.getUsername());
        holder.mCheckBTN.setOnClickListener(this);
        holder.mCheckBTN.setTag(position);
        return convertView;
    }

    public class ViewHolder {
        public TextView mSubjectTV;
        public TextView mGuidanceTV;
        public  TextView mSnoTV;
        public  TextView mUsernameTV;
        public Button mCheckBTN;
    }

    //响应按钮点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

}
