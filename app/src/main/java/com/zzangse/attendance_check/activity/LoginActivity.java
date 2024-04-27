package com.zzangse.attendance_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivityLoginBinding;
import com.zzangse.attendance_check.request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private boolean isVisibility = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        onClickLogin();
        onClickEditTextPassWordShow();
        onClickSignUp();
    }

    private void initView() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


    private void onClickEditTextPassWordShow() {
        binding.ibShowPassword.setOnClickListener(v->{
            if (!isVisibility) {
                binding.ibShowPassword.setImageResource(R.drawable.ic_visibility_off);
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isVisibility = true;
            } else {
                binding.ibShowPassword.setImageResource(R.drawable.ic_visibility);
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                isVisibility = false;
            }
        });
    }
    private void onClickLogin() {
        binding.btnLogin.setOnClickListener(v -> {
            String userID = binding.etId.getText().toString();
            String userPassword = binding.etPassword.getText().toString();
            binding.tvErrorLabel.setVisibility(View.GONE);
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            // json 데이터에서 userID 키에 해당하는 값을 가져옴
                            String userID = jsonObject.getString("userID");
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userID", userID);
                            startActivity(intent);
                        } else {
                            binding.tvErrorLabel.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            LoginRequest request = new LoginRequest(userID, userPassword, listener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(request);
        });

    }

    private void onClickSignUp() {
        binding.tvSingup.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
            startActivity(intent);
        });
    }


}