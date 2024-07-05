package com.zzangse.attendance_check.activity;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivityMoreBinding;
import com.zzangse.attendance_check.fragmentmore.ChangePasswordFragment;
import com.zzangse.attendance_check.fragmentmore.WithdrawalAppFragment;

public class MoreActivity extends AppCompatActivity {
    private ActivityMoreBinding binding;
    private FragmentManager fragmentManager;
    private ChangePasswordFragment changePasswordFragment;
    private WithdrawalAppFragment withdrawalAppFragment;
    private String userID;
    private String userNickName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFragment();

    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        int fragmentIndex = getIntent().getIntExtra("fragment_index", 0);
        userID = getIntent().getStringExtra("userID");
        userNickName = getIntent().getStringExtra("userNickName");
        Log.d("moreActivity", userID + ", " + userNickName);
        switchFragment(fragmentIndex);

    }

    private void initView() {
        binding = ActivityMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void switchFragment(int index) {
        Fragment selectedFragment = null;
        String tag = null;
        Bundle withDrawal_bundle = new Bundle();
        withDrawal_bundle.putString("userNickName", userNickName);
        withDrawal_bundle.putString("userID", userID);
        Bundle changePW_bundle = new Bundle();
        changePW_bundle.putString("userID", userID);

        if (index == 0) {
            selectedFragment = ChangePasswordFragment.newInstance();
            selectedFragment.setArguments(changePW_bundle);
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            tag = "change_password_fragment";
        } else if (index == 1) {
            selectedFragment = WithdrawalAppFragment.newInstance();
            selectedFragment.setArguments(withDrawal_bundle);
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            tag = "withdrawal_app_fragment";
        }
        if (selectedFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction()
                    .replace(R.id.fragment_more, selectedFragment, tag)
                    .setReorderingAllowed(true);
            transaction.commit();
        }

    }
}
