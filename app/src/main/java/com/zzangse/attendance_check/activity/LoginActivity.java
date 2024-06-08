package com.zzangse.attendance_check.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivityLoginBinding;
import com.zzangse.attendance_check.request.SignInRequest;

import org.json.JSONException;
import org.json.JSONObject;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private boolean isVisibility = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initKakao();
        onClickLogin();
        onClickEditTextPassWordShow();
        onClickSignUp();
        onClickKakaoLogin();
    }

    private void initView() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initKakao() {
        Log.d("initKakao", "appkey");
        KakaoSdk.init(this,"f4e9cb70147f65159b379f578067d383");
        String kakao = KakaoSdk.INSTANCE.getKeyHash();
        Log.d("hash", kakao);
    }

    private void onClickKakaoLogin() {
        Function2<OAuthToken,Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    Log.d("oAuthToken", "oAuthToken NULL");
                }
                if (throwable != null) {
                    Log.d("throwable", "throwable NULL");
                }
                updateKakaoLogin();
                return null;
            }
        };

        binding.ibKakaoLogin.setOnClickListener(v->{
            if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
            } else {
                UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
            }
        });
    }


    private void updateKakaoLogin() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                Log.d("user", user + "");
                if (user != null) {
//                    Log.d("KAKAO", "invoke id" + user.getId());
//                    Log.d("KAKAO", "invoke email" + user.getKakaoAccount().getEmail());
                    Log.d("KAKAO", "invoke nickName" + user.getKakaoAccount().getEmail());
//                    Log.d("KAKAO", "invoke gender" + user.getKakaoAccount().getGender());
//                    Log.d("KAKAO", "invoke age" + user.getKakaoAccount().getBirthday());
//                    Log.d("KAKAO", "invoke age" + user.getKakaoAccount().getPhoneNumber());
                    binding.tvTest.setText(user.getId()+"");

                } else {
                    Log.d("KAKAO", "로그인 X");
                }
                return null;
            }
        });
    }

    private void onClickEditTextPassWordShow() {
        binding.ibShowPassword.setOnClickListener(v -> {
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
            Log.d("TEST", "BTN");
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
                            Log.d("TEST", userID);
                            Log.d("TEST", userPassword);
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
            SignInRequest request = new SignInRequest(userID, userPassword, listener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(request);
        });

    }

    private void onClickSignUp() {
        binding.tvSingup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }


}