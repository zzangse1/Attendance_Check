package com.zzangse.attendance_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
       // onClickLogin();
        onClick();
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
    private void onClick() {
        binding.btnLogin.setOnClickListener(v -> {
            String userID = binding.etId.getText().toString();
            String userPassword = binding.etPassword.getText().toString();

            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            String userID = jsonObject.getString("userID");
                            String userPassword = jsonObject.getString("userPassword");
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userID", userID);
                            intent.putExtra("userPassword", userPassword);
                            startActivity(intent);
                        } else {
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
    private void onClickLogin(){
        binding.btnLogin.setOnClickListener(v->{
            String userID = binding.etId.getText().toString();
            String userPW = binding.etPassword.getText().toString();

            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("hello2","1");
                        JSONObject jsonObject = new JSONObject(response);
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            Log.d("hello2","2");
                            String userID = jsonObject.getString("userID");
                            String userPW = jsonObject.getString("userPassword");
                            Toast.makeText(getApplicationContext(), "로그인에 성공", Toast.LENGTH_SHORT).show();
                            // 로그인 성공 시 메인액티비티로
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userID",userID);
                            intent.putExtra("userPass",userPW);
                            startActivity(intent);
                        } else {
                            Log.d("hello2","3");
                            Toast.makeText(getApplicationContext(), "로그인에 실패", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        Log.d("hello2","4");
                        throw new RuntimeException(e);
                    }
                }
            };
            Log.d("hello2","5");
            LoginRequest loginRequest = new LoginRequest(userID,userPW,listener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        });
    }

    private void onClickSignUp() {
        binding.tvSingup.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
            startActivity(intent);
        });
    }
}