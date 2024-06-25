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
import com.zzangse.attendance_check.MySharedPreferences;
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
        initKakao();
        initView();
        initAutoLogin();
        onClickLogin();
        onClickEditTextPassWordShow();
        onClickSignUp();
        onClickKakaoLogin();
        onClickFindAccount();
    }

    @Override
    protected void onResume() {
        // 로그아웃시 작동
        super.onResume();
        boolean isLogout = getIntent().getBooleanExtra("LOGOUT", false);
        if (isLogout) {
            clearLoginFields();
        }
    }

    private void initView() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void isCheckedBox(String userID, String userPassword) {
        Log.d("isCHeck", binding.cbAutoLogin.isChecked() + "asdf");
        if (binding.cbAutoLogin.isChecked()) {
            binding.etId.setText(userID);
            binding.etPassword.setText(userPassword);
            MySharedPreferences.setCheckBox(getApplicationContext(), "true");
            MySharedPreferences.setUserId(getApplicationContext(), userID);
            MySharedPreferences.setUserPass(getApplicationContext(), userPassword);
        } else {
            MySharedPreferences.setCheckBox(getApplicationContext(), "false");
        }
    }

    private void initAutoLogin() {
        if (MySharedPreferences.getCheckBox(getApplicationContext()).equals("false")) {
            binding.etId.setText("");
            binding.etPassword.setText("");
            Log.d("자동로그인", "비어있음");
        } else {
            binding.cbAutoLogin.setChecked(true);
            autoLogin(MySharedPreferences.getUserId(getApplicationContext()), MySharedPreferences.getUserPass(getApplicationContext()));
        }
    }

    private void clearLoginFields() {
        binding.etId.setText("");
        binding.etPassword.setText("");
    }

    private void autoLogin(String userID, String userPassword) {
        Log.d("자동 저장", userID + ", " + userPassword);
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
                        String userToken = jsonObject.getString("userToken");
                        Log.d("TEST", userID);
                        Log.d("TEST", userPassword);
                        Log.d("TEST", userToken);
                        Toast.makeText(getApplicationContext(), "자동 로그인 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        // 자동로그인
                        isCheckedBox(userID, userPassword);
                        intent.putExtra("userID", userID);
                        intent.putExtra("userToken", userToken);
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
    }

    private void initKakao() {
        Log.d("initKakao", "appkey");
        // KakaoSdk.init(this, getString(R.string.kakao_app_key));          // 카카오 init
        KakaoSdk.init(this, getString(R.string.kakao_app_test_key)); // 카카오 init테스트
        String kakao = KakaoSdk.INSTANCE.getKeyHash();
        Log.d("hash", kakao);
    }

    private void onClickKakaoLogin() {
        binding.ibKakaoLogin.setOnClickListener(v -> {
            Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
                @Override
                public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                    Log.d("oAuth", oAuthToken + "");
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


    private String transferSex(String kakaoSex) {
        if (kakaoSex.equals("MALE")) {
            return kakaoSex = "남자";
        } else if (kakaoSex.equals("FEMALE")) {
            return kakaoSex = "여자";
        } else {
            return kakaoSex = "선택안함";
        }
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
                    String userBirthYear = user.getKakaoAccount().getBirthyear();
                    String userBirth = user.getKakaoAccount().getBirthday();
                    String userSex = String.valueOf(user.getKakaoAccount().getGender());
                    String userPhoneNumber = user.getKakaoAccount().getPhoneNumber();
                    String userToken = "KAKAO";
                    userSex = transferSex(userSex);  // 카카오 성별 한글화

                    moveToSignUp(userID, userNickName, userName, userBirthYear + userBirth, userSex, userPhoneNumber, userToken);
                } else {
                    Log.d("KAKAO", "로그인 X");
                }
                return null;
            }
        });
    }

    private void moveToSignUp(String userID, String userNickName, String userName, String userBirth,
                              String userSex, String userPhoneNumber, String userToken) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("userNickName", userNickName);
        intent.putExtra("userName", userName);
        intent.putExtra("userBirth", userBirth);
        intent.putExtra("userSex", userSex);
        intent.putExtra("userPhoneNumber", userPhoneNumber);
        intent.putExtra("userToken", userToken);
        startActivity(intent);
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
                            String userToken = jsonObject.getString("userToken");
                            Log.d("TEST", userID);
                            Log.d("TEST", userPassword);
                            Log.d("TEST", userToken);
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            // 자동로그인
                            isCheckedBox(userID, userPassword);
                            intent.putExtra("userID", userID);
                            intent.putExtra("userToken", userToken);
                            startActivity(intent);
                            // finish();
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
            intent.putExtra("userToken", "ZZANGSE");
            intent.putExtra("userSex", "선택안함");
            startActivity(intent);
        });
    }

    private void onClickFindAccount() {
        binding.tvFind.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, FindAccountActivity.class);
            startActivity(intent);
        });
    }


}