package com.zzangse.attendance_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zzangse.attendance_check.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding loginBinding;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFirebase();
        onClickLogin();
        onClickSignUp();
    }

    private void initFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }

    private void initView() {
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
    }


    private void onClickLogin() {
        loginBinding.btnLogin.setOnClickListener(v->{
            String id = loginBinding.etEmailId.getText().toString();
            String password = loginBinding.etPassword.getText().toString();
            mFirebaseAuth.signInWithEmailAndPassword(id, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // 로그인 성공
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "false", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    private void onClickSignUp() {
        loginBinding.tvSingup.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
            startActivity(intent);
        });
    }
}