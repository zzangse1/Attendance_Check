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

public class MoreActivity extends AppCompatActivity {
    private ActivityMoreBinding binding;
    private FragmentManager fragmentManager;
    private ChangePasswordFragment changePasswordFragment;
    private String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getIntentItem();
        initFragment();
    }

    private void initView() {
        binding = ActivityMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void getIntentItem() {
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
    }
    private void initFragment() {
        fragmentManager = getSupportFragmentManager();

        Bundle viewBundle = new Bundle();
        viewBundle.putString("userID", userID);
        Log.d("moreActivity ", userID + "");

        changePasswordFragment = ChangePasswordFragment.newInstance(viewBundle);

        fragmentManager.beginTransaction().add(R.id.fragment_more, changePasswordFragment).commit();

    }

    public void onFragmentChanged(int index, Bundle bundle) {
        Fragment selectedFragment = null;
        String tag = null;
        if (index == 0) {
            selectedFragment = ChangePasswordFragment.newInstance(bundle);
            tag = "change_password_fragment";
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (selectedFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction()
                    .replace(R.id.fragment_more, selectedFragment, tag)
                    .setReorderingAllowed(true);

            transaction.commit();
        }

    }
}
