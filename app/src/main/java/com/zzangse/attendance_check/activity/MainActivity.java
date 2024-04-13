package com.zzangse.attendance_check.activity;

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
    private CheckFragment checkFragment;
    private ChartFragment chartFragment;
    private EditFragment editFragment;
    private MoreFragment moreFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        checkFragment = new CheckFragment();
        chartFragment = new ChartFragment();
        editFragment = new EditFragment();
        moreFragment = new MoreFragment();

        replace(new CheckFragment());
        BottomNavSelect bottomNavSelect = new BottomNavSelect();
        mainBinding.bottomNav.setOnItemSelectedListener(bottomNavSelect);

    }

    private void initView() {
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
    }

    class BottomNavSelect implements NavigationBarView.OnItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.menu_check) {
                Log.d("BottomNavSelect", "프래그먼트 check 이동");
                replace(checkFragment);
            } else if (item.getItemId() == R.id.menu_chart) {
                Log.d("BottomNavSelect", "프래그먼트 chart 이동");
                replace(chartFragment);
            }else if (item.getItemId() == R.id.menu_edit) {
                Log.d("BottomNavSelect", "프래그먼트 edit 이동");
                replace(editFragment);
            } else if (item.getItemId() == R.id.menu_more) {
                Log.d("BottomNavSelect", "프래그먼트 more 이동");
                replace(moreFragment);
            }
            return true;
        }
    }
    private void replace(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment,fragment);
        transaction.commit();
    }
}