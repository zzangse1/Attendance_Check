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
import com.zzangse.attendance_check.request.SignUpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private boolean isVisibility = true;
    private boolean isKakao = false;

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
        KakaoSdk.init(this, getString(R.string.kakao_app_key));
        String kakao = KakaoSdk.INSTANCE.getKeyHash();
        Log.d("hash", kakao);
    }

    private void onClickKakaoLogin() {
        binding.ibKakaoLogin.setOnClickListener(v -> {
            Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
                @Override
                public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                    if (oAuthToken != null) {
                        Log.d("oAuthToken", oAuthToken + "oAuthToken not NULL");
                        updateKakaoLogin();
                    }
                    if (throwable != null) {
                        // 사용자가 로그인을 취소한 경우
                        Log.d("KAKAO", "로그인 취소");
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "로그인이 취소되었습니다.", Toast.LENGTH_SHORT).show());
                    }
                    return null;
                }
            };

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
                    Log.d("KAKAO", "invoke nickName" + user.getKakaoAccount().getProfile().getNickname());
                    Log.d("KAKAO", "invoke email" + user.getKakaoAccount().getEmail());
                    String userID = user.getKakaoAccount().getEmail();
                    String userNickName = user.getKakaoAccount().getProfile().getNickname();
                    String userName = user.getKakaoAccount().getName();
                    String userToken = "KAKAO";
                    signUpKakao(userID, "12341234", userNickName, "카카오",
                            "19981014", "선택안함", "01043214321", userToken);

                } else {
                    Log.d("KAKAO", "로그인 X");
                    isKakao = false;
                }
                return null;
            }
        });
    }

    private void signUpKakao(String kakaoID, String kakaoPassword, String kakaoNickName,
                             String kakaoName, String kakaoBirth, String kakaoSex, String kakaoPhoneNumber, String userToken) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    String issue = jsonObject.getString("message");
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                        Log.d("KAKAO", issue);
                        loginKakao(isKakao, kakaoID);
                    } else if (issue.equals("카카오 회원가입 등록이 되어있습니다.")) {
                        // 카카오 로그인 진행
                        isKakao = true;
                        loginKakao(isKakao, kakaoID);
                    } else {
                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                        Log.d("KAKAO", issue);
                        isKakao = false;
                    }
                } catch (JSONException e) {
                    Log.d("JSON", e.toString());
                }
            }
        };
        SignUpRequest request = new SignUpRequest(kakaoID, kakaoPassword, kakaoNickName, kakaoName,
                kakaoBirth, kakaoSex, kakaoPhoneNumber, userToken, listener);
        Log.d("kakaoSIgn", kakaoID + ", " + kakaoPassword + ", " + kakaoNickName + ", " + kakaoName
                + ", " + kakaoBirth + ", " + kakaoSex + ", " + kakaoPhoneNumber);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(request);
    }

    private void loginKakao(boolean isKakao, String kakaoID) {
        if (isKakao) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userID", kakaoID);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "카카오 취소", Toast.LENGTH_SHORT).show();
        }
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
                            //finish();
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