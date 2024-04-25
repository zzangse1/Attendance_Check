package com.zzangse.attendance_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.zzangse.attendance_check.fragment.ChartFragment;
import com.zzangse.attendance_check.fragment.CheckFragment;
import com.zzangse.attendance_check.fragment.EditFragment;
import com.zzangse.attendance_check.fragment.MoreFragment;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private FragmentManager fragmentManager;
    private CheckFragment checkFragment;
    private ChartFragment chartFragment;
    private EditFragment editFragment;
    private MoreFragment moreFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFragment();
        setupBottomNav();
    }

    private void setupBottomNav() {
        mainBinding.bottomNav.setOnItemSelectedListener(new BottomNavSelect());
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        checkFragment = new CheckFragment();
        chartFragment = new ChartFragment();
        editFragment = new EditFragment();
        moreFragment = new MoreFragment();

        replace(checkFragment);
    }

    private void initView() {
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
    }

    private String initAccount() {
        Intent intent = getIntent();
        return intent != null ? intent.getStringExtra("userID") : "false";
    }


    class BottomNavSelect implements NavigationBarView.OnItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            if (item.getItemId() == R.id.menu_check) {
                fragment = checkFragment;
            } else if (item.getItemId() == R.id.menu_chart) {
                Log.d("BottomNavSelect", "프래그먼트 chart 이동");
                fragment = chartFragment;
            } else if (item.getItemId() == R.id.menu_edit) {
                Log.d("BottomNavSelect", "프래그먼트 edit 이동");
                fragment = editFragment;
            } else if (item.getItemId() == R.id.menu_more) {
                Log.d("BottomNavSelect", "프래그먼트 more 이동");
                fragment = moreFragment;
            }
            if (fragment != null) {
                replace(fragment);
                Bundle bundle = new Bundle();
                bundle.putString("userID", initAccount());
                fragment.setArguments(bundle);
            }
            return true;
        }
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }
}