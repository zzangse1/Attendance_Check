package com.zzangse.attendance_check.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivitySettingBinding;
import com.zzangse.attendance_check.fragment.MemberInfoFragment;
import com.zzangse.attendance_check.fragment.MemberModifyFragment;
import com.zzangse.attendance_check.fragment.MemberViewFragment;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding activitySettingBinding;
    private FragmentManager fragmentManager; // 참조할 수 있는 변수 선언
    private MemberViewFragment viewFragment;
    private MemberInfoFragment infoFragment;
    private MemberModifyFragment modifyFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFragment();

    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        viewFragment = new MemberViewFragment();
        infoFragment = new MemberInfoFragment();
        modifyFragment = new MemberModifyFragment();
        fragmentManager.beginTransaction().add(R.id.fragment_setting,viewFragment).commit();
    }

    private void initView() {
        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(activitySettingBinding.getRoot());
    }


    public void onFragmentChanged(int index) {
        if (index == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_setting,infoFragment)
                    .setReorderingAllowed(true)
                    .addToBackStack("0")
                    .commit();
        } else if (index == 1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_setting,modifyFragment)
                    .setReorderingAllowed(true)
                    .addToBackStack("1")
                    .commit();
        }
    }
}