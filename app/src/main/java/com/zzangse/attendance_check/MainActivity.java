package com.zzangse.attendance_check;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.zzangse.attendance_check.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setJetPackNavigation();
    }

    private void initView() {
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
    }

    private void setJetPackNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    }

    @Override
    public void onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed();

        }

    }
}