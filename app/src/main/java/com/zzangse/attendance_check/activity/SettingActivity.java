package com.zzangse.attendance_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivitySettingBinding;
import com.zzangse.attendance_check.fragmentsetting.MemberAddFragment;
import com.zzangse.attendance_check.fragmentsetting.MemberInfoFragment;
import com.zzangse.attendance_check.fragmentsetting.MemberModifyFragment;
import com.zzangse.attendance_check.fragmentsetting.MemberViewFragment;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private FragmentManager fragmentManager; // 참조할 수 있는 변수 선언
    private MemberViewFragment viewFragment;
    private MemberAddFragment addFragment;
    private MemberInfoFragment infoFragment;
    private MemberModifyFragment modifyFragment;
    private String groupName = "";
    private String userID;
    private int priNum;

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

    private void getIntentItem() {
        Intent intent = getIntent();
        groupName = intent.getStringExtra("groupName");
        userID = intent.getStringExtra("userID");

        Log.d("groupName", groupName);
        Log.d("userID", userID);
    }
    
    private void initFragment() {
        getIntentItem();
        fragmentManager = getSupportFragmentManager();

        Bundle viewBundle = new Bundle();
        viewBundle.putString("groupName", groupName);
        viewBundle.putString("userID", userID);
        viewFragment = MemberViewFragment.newInstance(viewBundle);

        Bundle addBundle = new Bundle();
        addBundle.putString("groupName", groupName);
        addBundle.putString("userID", userID);
        addFragment = MemberAddFragment.newInstance(addBundle);

        Bundle infoBundle = new Bundle();
        infoBundle.putInt("priNum", priNum);
        infoBundle.putString("userID", userID);
        infoFragment = MemberInfoFragment.newInstance(infoBundle);

        Bundle modifyBundle = new Bundle();
        modifyBundle.putInt("priNum", priNum);
        modifyBundle.putString("userID", userID);
        modifyFragment = MemberModifyFragment.newInstance(modifyBundle);

        fragmentManager.beginTransaction().add(R.id.fragment_setting, viewFragment).commit();
    }


    public void onFragmentChanged(int index, Bundle bundle) {
        Fragment selectedFragment = null;
        String tag = null;
        if (index == 0) {
            selectedFragment = MemberInfoFragment.newInstance(bundle);
            tag = "info_fragment";
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (index == 1) {
            selectedFragment = MemberAddFragment.newInstance(bundle);
            tag = "add_fragment";
        } else if (index == 2) {
            Log.d("setting", priNum + "");
            selectedFragment = MemberModifyFragment.newInstance(bundle);
            tag = "modify_fragment";
        } else if (index == 3) {
            selectedFragment = MemberViewFragment.newInstance(bundle);
            tag = "view_fragment";
            // 백스택 초기화
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (selectedFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction()
                    .replace(R.id.fragment_setting, selectedFragment, tag)
                    .setReorderingAllowed(true);

            // view_fragment로 돌아가는 트랜잭션은 백스택에 추가하지 않음
            if (index != 3) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
        }
    }
}