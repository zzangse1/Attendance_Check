package com.zzangse.attendance_check.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivitySettingBinding;
import com.zzangse.attendance_check.fragment.MemberInfoFragment;
import com.zzangse.attendance_check.fragment.MemberModifyFragment;
import com.zzangse.attendance_check.fragment.MemberViewFragment;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding activitySettingBinding;
    private MemberViewFragment viewFragment;
    private MemberInfoFragment infoFragment;
    private MemberModifyFragment modifyFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewFragment = new MemberViewFragment();
        infoFragment = new MemberInfoFragment();
        modifyFragment = new MemberModifyFragment();

        replace(viewFragment);
        initView();
    }

    private void initView() {
        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(activitySettingBinding.getRoot());
    }

    public void replace(Fragment fragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_setting,fragment);
        transaction.commit();
    }
}