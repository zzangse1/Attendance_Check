package com.zzangse.attendance_check.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivityFindAccountBinding;
import com.zzangse.attendance_check.fragmentfind.FindIdFragment;
import com.zzangse.attendance_check.fragmentfind.FindPwFragment;

public class FindAccountActivity extends AppCompatActivity {
    private ActivityFindAccountBinding binding;
    private FragmentManager fragmentManager;
    private FindIdFragment findIdFragment;
    private FindPwFragment findPwFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        onClickBack();
        initFragment();
        onClickTab();
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

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        findIdFragment = new FindIdFragment();
        findPwFragment = new FindPwFragment();
        replace(findIdFragment);
    }



    private void replace(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.find_fragment, fragment);
        transaction.commit();
    }

    private void onClickTab() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replace(findIdFragment);
                } else {
                    replace(findPwFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}