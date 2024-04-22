package com.zzangse.attendance_check.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zzangse.attendance_check.databinding.ActivityGroupAddBinding;

public class GroupAddActivity extends AppCompatActivity {
    private ActivityGroupAddBinding activityGroupAddBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        onClickBtn();
    }

    private void initView() {
        activityGroupAddBinding = ActivityGroupAddBinding.inflate(getLayoutInflater());
        setContentView(activityGroupAddBinding.getRoot());
    }

    private void onClickBtn() {
        activityGroupAddBinding.btnOk.setOnClickListener(v->{
            Toast.makeText(this,"생성되었습니다.",Toast.LENGTH_SHORT).show();
            onBackPressed();
        });
    }
}