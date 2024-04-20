package com.zzangse.attendance_check.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivityGroupAddBinding;

public class GroupAddActivity extends AppCompatActivity {
    private ActivityGroupAddBinding activityGroupAddBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        activityGroupAddBinding = ActivityGroupAddBinding.inflate(getLayoutInflater());
        setContentView(activityGroupAddBinding.getRoot());
    }
}