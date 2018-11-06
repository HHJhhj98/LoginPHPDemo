package com.huhuijia.loginphpdemo.ui_student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.huhuijia.loginphpdemo.R;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSelectBTN;
    private Button mAddBTN;
    private Button mUpdaBTN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        initView();
    }

    private void initView() {
        mSelectBTN = (Button) findViewById(R.id.selectBTN);
        mAddBTN = (Button) findViewById(R.id.addBTN);
        mUpdaBTN = (Button) findViewById(R.id.updaBTN);

        mSelectBTN.setOnClickListener(this);
        mAddBTN.setOnClickListener(this);
        mUpdaBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=getIntent();
        String sno=intent.getStringExtra("sno");
        switch (v.getId()) {
            case R.id.selectBTN:
                Intent i=new Intent(StudentActivity.this,SelectActivity.class);
                i.putExtra("sno",sno);
                startActivity(i);
                break;
            case R.id.addBTN:
                Intent iadd=new Intent(StudentActivity.this,AddActivity.class);
                iadd.putExtra("sno",sno);
                startActivity(iadd);
                break;
            case R.id.updaBTN:
                Intent iu=new Intent(StudentActivity.this,UpdateActivity.class);
                iu.putExtra("sno",sno);
                startActivity(iu);
                break;
        }
    }
}
