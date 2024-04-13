package com.zzangse.attendance_check;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
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
                replace(checkFragment);
            } else if (item.getItemId() == R.id.menu_chart) {
                replace(chartFragment);
            }else if (item.getItemId() == R.id.menu_edit) {
                replace(editFragment);
            } else if (item.getItemId() == R.id.menu_more) {
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