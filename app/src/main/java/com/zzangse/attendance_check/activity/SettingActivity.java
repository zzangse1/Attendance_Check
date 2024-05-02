package com.zzangse.attendance_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivitySettingBinding;
import com.zzangse.attendance_check.fragment.MemberAddFragment;
import com.zzangse.attendance_check.fragment.MemberInfoFragment;
import com.zzangse.attendance_check.fragment.MemberModifyFragment;
import com.zzangse.attendance_check.fragment.MemberViewFragment;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private FragmentManager fragmentManager; // 참조할 수 있는 변수 선언
    private MemberViewFragment viewFragment;
    private MemberAddFragment addFragment;
    private MemberInfoFragment infoFragment;
    private MemberModifyFragment modifyFragment;
    private String groupName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFragment();
    }
    private void initView() {
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initFragment() {
        getGroupName();
        fragmentManager = getSupportFragmentManager();
        viewFragment = MemberViewFragment.newInstance(groupName);
        addFragment = MemberAddFragment.newInstance();
        infoFragment = MemberInfoFragment.newInstance();
        modifyFragment = MemberModifyFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.fragment_setting, viewFragment).commit();
    }

    private void getGroupName() {
        Intent intent = getIntent();
        groupName = intent.getStringExtra("groupName");
        Log.d("groupName", groupName);
    }


    public void onFragmentChanged(int index) {
        Fragment selectedFragment = null;
        String tag = null;
        if (index == 0) {
            selectedFragment= MemberInfoFragment.newInstance();
            tag = "info_fragment";
        } else if (index == 1) {
            selectedFragment = MemberAddFragment.newInstance();
            tag = "add_fragment";
        } else if (index == 2) {
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