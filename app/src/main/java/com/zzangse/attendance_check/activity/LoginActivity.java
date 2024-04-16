package com.zzangse.attendance_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zzangse.attendance_check.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding loginBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        onClickBtn();
        onClickSignUp();
    }

    private void initView() {
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
    }

    private void onClickBtn() {
        loginBinding.btnLogin.setOnClickListener(v->{
            Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
        });
    }

    private void onClickSignUp() {
        loginBinding.tvSingup.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
            startActivity(intent);
        });
    }
}