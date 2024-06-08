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
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationBarView;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.data.GroupViewModel;
import com.zzangse.attendance_check.databinding.ActivityMainBinding;
import com.zzangse.attendance_check.fragmentmain.ChartFragment;
import com.zzangse.attendance_check.fragmentmain.CheckFragment;
import com.zzangse.attendance_check.fragmentmain.EditFragment;
import com.zzangse.attendance_check.fragmentmain.MoreFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private CheckFragment checkFragment;
    private ChartFragment chartFragment;
    private EditFragment editFragment;
    private MoreFragment moreFragment;
    private GroupViewModel groupViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFragment();
        setupBottomNav();
        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);

    }


    public GroupViewModel getGroupViewModel() {
        return groupViewModel;
    }
    private void setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener(new BottomNavSelect());
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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
            }
            return true;
        }
    }

    private void setBundle(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("userID", initAccount());
        fragment.setArguments(bundle);
    }

    private void replace(Fragment fragment) {
        setBundle(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }
}