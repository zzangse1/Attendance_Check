package com.zzangse.attendance_check.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zzangse.attendance_check.databinding.ActivityFindAccountBinding;

public class FindAccountActivity extends AppCompatActivity {
    private ActivityFindAccountBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        onClickBack();
        onClickRadioBtn();
    }

    private void initView() {
        binding = ActivityFindAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    private void onClickBack() {
        binding.toolbarFindAccount.setNavigationOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private void onClickRadioBtn() {
        binding.rb1.setOnClickListener(v->{
            binding.rb2.setChecked(false);
        });
        binding.rb2.setOnClickListener(v->{
            binding.rb1.setChecked(true);
        });

    }
}