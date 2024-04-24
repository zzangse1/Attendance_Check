package com.zzangse.attendance_check.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
    private void initView() {
        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(activitySettingBinding.getRoot());
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        viewFragment = MemberViewFragment.newInstance();
        infoFragment = MemberInfoFragment.newInstance();
        modifyFragment = MemberModifyFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.fragment_setting, viewFragment).commit();
    }


    public void onFragmentChanged(int index) {
        Fragment selectedFragment = null;
        String tag = null;
        if (index == 0) {
            selectedFragment= MemberInfoFragment.newInstance();
            tag = "info_fragment";
        } else if (index == 1) {
            selectedFragment = MemberModifyFragment.newInstance();
            tag = "modify_fragment";
        }
        if (selectedFragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_setting,selectedFragment,tag)
                    .setReorderingAllowed(true)
                    .addToBackStack(tag)
                    .commit();
        }
    }
}