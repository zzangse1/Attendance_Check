package com.zzangse.attendance_check.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputLayout;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivitySignupBinding;
import com.zzangse.attendance_check.request.SignUpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private String m_sex = "";
    // *** 정규식
    private static final String YYYYMMDD = "(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String REGEX_ID = "^[a-z0-9_-]{5,20}$";
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9!@#$]+$";
    private static final String REGEX_NAME = "^[가-힣]{2,6}$";
    private static final String REGEX_PHONE_NUMBER = "^\\d{3}-?\\d{3,4}-?\\d{4}$";
    private static final String WARNING_MSG_NO_ID = "•아이디: 필수 정보 입니다.";
    private static final String WARNING_MSG_NO_EMAIL = "•이메일: 필수 정보 입니다.";
    private static final String WARNING_MSG_NO_PASSWORD = "•비밀번호: 필수 정보입니다.";
    private static final String WARNING_MSG_NO_NICKNAME = "•닉네임: 필수 정보입니다.";
    private static final String WARNING_MSG_SAME_ID = "•아이디: 이미 사용중인 아이디 입니다.";
    private static final String WARNING_MSG_SAME_EMAIL = "•이메일: 이미 사용중인 이메일 입니다.";
    private static final String WARNING_MSG_SAME_PHONE_NUMBER = "•휴대전화번호: 이미 사용 중입니다.";
    private static final String WARNING_MSG_RULE_ID = "•아이디: 5~20자의 영문 소문자, 숫자를 사용해 주세요.";
    private static final String WARNING_MSG_RULE_EMAIL = "•이메일: 5~30자의 이메일 형식을 맞춰주세요 [sample@domain.com]";
    private static final String WARNING_MSG_RULE_PASSWORD = "•비밀번호: 8~20자의 영문 대/소문자, 숫자, [!,@,#,$]를 사용해 주세요.";
    private static final String WARNING_MSG_RULE_BIRTHDAY = "•생년월일: [ 20240101 ] 형식으로 작성해 주세요.";
    private static final String WARNING_MSG_RULE_NICKNAME = "•닉네임: 2~8자로 설정해주세요.";
    private static final String WARNING_MSG_RULE_NUMBER = "•전화번호: [ 01012341234 ] 형식으로 작성해 주세요.";
    private static final String WARNING_MSG_RULE_NAME = "•이름: 2~6자로 설정해주세요.";
    private static final String WARNING_MSG_NO_BIRTHDAY = "•생년월일: 필수 정보입니다.";
    private static final String WARNING_MSG_NO_NAME = "•이름: 필수 정보입니다.";
    private static final String WARNING_MSG_NO_SEX = "•성별: 필수 정보입니다.";
    private static final String WARNING_MSG_NO_PHONE_NUMBER = "•전화번호: 필수 정보입니다.";

    private static boolean IS_VALID_ID = false;
    private static boolean IS_VALID_EMAIL = false;
    private static boolean IS_VALID_PASSWORD = false;
    private static boolean IS_VALID_NICK_NAME = false;
    private static boolean IS_VALID_NAME = false;
    private static boolean IS_VALID_BIRTH = false;
    private static boolean IS_VALID_SEX = false;
    private static boolean IS_VALID_PHONE_NUMBER = false;
    private boolean isPrivacy = false;
    private boolean isService = false;
    private String userToken = "ZZANGSE";

    // 할일
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initIntent();
        onClickBtn();
        onClickSignUp();
        onClickBack();
        onClickTvPrivacy();
        enableAll(isPrivacy, isService);
        onClickCheckBox();
        showAD();
    }

    private void showAD() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        AdRequest request = new AdRequest.Builder().build();
        binding.adView.loadAd(request);
    }


    private void initIntent() {
        Intent intent = getIntent();
        userToken = intent.getStringExtra("userToken");
        if (userToken.equals("KAKAO")) {
            String userEmail = intent.getStringExtra("userID");
            String userName = intent.getStringExtra("userName");
            String userNickName = intent.getStringExtra("userNickName");
            String userBirth = intent.getStringExtra("userBirth");
            String userSex = intent.getStringExtra("userSex");
            String userPhoneNumber = intent.getStringExtra("userPhoneNumber");
            userPhoneNumber = formatPhoneNumber(userPhoneNumber);
            Log.d("userSex", userSex + "");
            Log.d("userToken", userToken + "");
            binding.etEmail.setText(userEmail);
            binding.etName.setText(userName);
            binding.etNickname.setText(userNickName);
            binding.etBirth.setText(userBirth);
            binding.etNumber.setText(userPhoneNumber);
        } else {
            userToken = "ZZANGSE";
        }
    }

    // 카카오 전화번호 +82 형식 포멧
    private String formatPhoneNumber(String userPhoneNumber) {
        if (!userPhoneNumber.isEmpty()) {
            String numberWithoutCountryCode = userPhoneNumber.replaceFirst("^\\+82\\s*", "");
            String formattedNumber = numberWithoutCountryCode.replaceAll("[^0-9]", "");

            if (!formattedNumber.startsWith("0")) {
                formattedNumber = "0" + formattedNumber;
            }

            return formattedNumber;
        } else {
            return "";
        }

    }

    private void initView() {
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void onClickBack() {
        binding.toolbarSignup.setNavigationOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private void onClickTvPrivacy() {
        binding.tvPrivacy.setOnClickListener(v -> {
            String url = "https://sites.google.com/view/zzangse-privacy/%ED%99%88";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            binding.cbPrivacy.setChecked(true);
            binding.cbPrivacy.setEnabled(true);
            isPrivacy = true;
        });
        binding.tvService.setOnClickListener(v -> {
            String url = "https://sites.google.com/view/zzangse-service/%ED%99%88";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            binding.cbService.setChecked(true);
            binding.cbService.setEnabled(true);
            isService = true;
        });
    }

    private void enableAll(boolean isPrivacy, boolean isService) {
        boolean isShow = isPrivacy && isService;
        binding.etIdLayout.setEnabled(isShow);
        binding.etPasswordLayout.setEnabled(isShow);
        binding.etEmailLayout.setEnabled(isShow);
        binding.etNicknameLayout.setEnabled(isShow);
        binding.etNameLayout.setEnabled(isShow);
        binding.etBirthLayout.setEnabled(isShow);
        binding.btnMale.setEnabled(isShow);
        binding.btnFemale.setEnabled(isShow);
        binding.btnNone.setEnabled(isShow);
        binding.etNumberLayout.setEnabled(isShow);
    }

    private void onClickCheckBox() {
        binding.cbPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPrivacy = isChecked;
                enableAll(isPrivacy, isService);
            }
        });
        binding.cbService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isService = isChecked;
                enableAll(isPrivacy, isService);
            }
        });
    }


    private void onClickSignUp() {
        binding.btnSend.setOnClickListener(v -> {
            // id 체크
            String userID = binding.etId.getText().toString();
            String userEmail = binding.etEmail.getText().toString();
            String userPassword = binding.etPassword.getText().toString();
            String m_birth = binding.etBirth.getText().toString();
            String userNickName = binding.etNickname.getText().toString();
            String userName = binding.etName.getText().toString();
            String userPhoneNumber = binding.etNumber.getText().toString();


            IS_VALID_ID = checkId(userID);
            IS_VALID_EMAIL = checkEmail(userEmail);
            IS_VALID_PASSWORD = checkPassword(userPassword);
            IS_VALID_BIRTH = checkBirth(m_birth);
            IS_VALID_NICK_NAME = checkNickName(userNickName);
            IS_VALID_NAME = checkName(userName);
            IS_VALID_SEX = checkSex(m_sex);
            IS_VALID_PHONE_NUMBER = checkPhoneNumber(userPhoneNumber);

            setCheckValid(IS_VALID_ID, IS_VALID_EMAIL, IS_VALID_PASSWORD, IS_VALID_BIRTH,
                    IS_VALID_NICK_NAME, IS_VALID_NAME, IS_VALID_SEX, IS_VALID_PHONE_NUMBER);

            setClearFocus();
        });

    }

    private void setCheckValid(boolean isValidId, boolean isValidEmail, boolean isValidPassword, boolean isValidBirth,
                               boolean isValidNickName, boolean isValidName, boolean isValidSex, boolean isValidPhoneNumber) {
        if (isValidId && isValidEmail && isValidPassword && isValidBirth && isValidNickName && isValidName && isValidSex && isValidPhoneNumber) {
            onClick();
        } else {
            Toast.makeText(getApplicationContext(), "회원등록 실패", Toast.LENGTH_SHORT).show();
        }
    }


    private void onClick() {
        String userID = binding.etId.getText().toString();
        String userEmail = binding.etEmail.getText().toString();
        String userPassword = binding.etPassword.getText().toString();
        String userNickName = binding.etNickname.getText().toString();
        String userName = binding.etName.getText().toString();
        String userBirth = binding.etBirth.getText().toString();
        String userSex = m_sex;
        String userPhoneNumber = binding.etNumber.getText().toString();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    String errorCode = jsonObject.getString("message");
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), "회원등록 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d("issue", errorCode + "");
                        isDuplication(errorCode);
                    }
                } catch (JSONException e) {
                    Log.d("hello", "5");
                    throw new RuntimeException(e);
                }
            }
        };
        Log.d("hello", "6");
        SignUpRequest request = new SignUpRequest(userID, userEmail, userPassword, userNickName,
                userName, userBirth, userSex, userPhoneNumber, userToken, listener);
        RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
        queue.add(request);
    }

    private void isDuplication(String errorCode) {
        if (errorCode.equals("ZOE_SIGN_UP_007")) {
            setErrorLayout(binding.etIdLayout, binding.tvErrorId, WARNING_MSG_SAME_ID);
            setErrorLayout(binding.etEmailLayout, binding.tvErrorEmail, WARNING_MSG_SAME_EMAIL);
            setErrorLayout(binding.etNumberLayout, binding.tvErrorPhoneNumber, WARNING_MSG_SAME_PHONE_NUMBER);
        } else if (errorCode.equals("ZOE_SIGN_UP_006")) {
            setErrorLayout(binding.etEmailLayout, binding.tvErrorEmail, WARNING_MSG_SAME_EMAIL);
            setErrorLayout(binding.etNumberLayout, binding.tvErrorPhoneNumber, WARNING_MSG_SAME_PHONE_NUMBER);
        } else if (errorCode.equals("ZOE_SIGN_UP_005")) {
            setErrorLayout(binding.etIdLayout, binding.tvErrorId, WARNING_MSG_SAME_ID);
            setErrorLayout(binding.etNumberLayout, binding.tvErrorPhoneNumber, WARNING_MSG_SAME_PHONE_NUMBER);
        } else if (errorCode.equals("ZOE_SIGN_UP_004")) {
            setErrorLayout(binding.etIdLayout, binding.tvErrorId, WARNING_MSG_SAME_ID);
            setErrorLayout(binding.etEmailLayout, binding.tvErrorEmail, WARNING_MSG_SAME_EMAIL);
        } else if (errorCode.equals("ZOE_SIGN_UP_001")) {
            setErrorLayout(binding.etIdLayout, binding.tvErrorId, WARNING_MSG_SAME_ID);
        } else if (errorCode.equals("ZOE_SIGN_UP_002")) {
            setErrorLayout(binding.etEmailLayout, binding.tvErrorEmail, WARNING_MSG_SAME_EMAIL);
        } else if (errorCode.equals("ZOE_SIGN_UP_003")) {
            setErrorLayout(binding.etNumberLayout, binding.tvErrorPhoneNumber, WARNING_MSG_SAME_PHONE_NUMBER);
        } else {
            clearErrorLayout(binding.etIdLayout, binding.tvErrorId);
            clearErrorLayout(binding.etEmailLayout, binding.tvErrorEmail);
            clearErrorLayout(binding.etNumberLayout, binding.tvErrorPhoneNumber);
        }

    }

    private void setClearFocus() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            focusedView.clearFocus();
        }
    }

    private boolean checkSex(String sex) {
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(), R.drawable.plain_outline_background_no_color);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(), R.drawable.plain_outline_background_red);
        if (sex.isEmpty()) {
            // binding.etSexLayout.setBackground(drawable_error);
            binding.sexLayout.setBackground(drawable_error);
            binding.tvErrorSex.setText(WARNING_MSG_NO_SEX);
            binding.tvErrorSex.setVisibility(View.VISIBLE);
            return false;
        } else {
            // binding.etSexLayout.setBackground(drawable_ok);
            binding.sexLayout.setBackground(drawable_ok);
            binding.tvErrorSex.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean checkName(String name) {
        boolean isMatch = Pattern.matches(REGEX_NAME, name);
        if (name.isEmpty()) {
            setErrorLayout(binding.etNameLayout, binding.tvErrorName, WARNING_MSG_NO_NAME);
            return false;
        } else if (!isMatch) {
            setErrorLayout(binding.etNameLayout, binding.tvErrorName, WARNING_MSG_RULE_NAME);
            return false;
        } else {
            clearErrorLayout(binding.etNameLayout, binding.tvErrorName);
            return true;
        }
    }

    private boolean checkNickName(String nickName) {
        boolean isMatchNickName = nickName.length() >= 2 && nickName.length() <= 8;
        if (nickName.isEmpty()) {
            setErrorLayout(binding.etNicknameLayout, binding.tvErrorNickName, WARNING_MSG_NO_NICKNAME);
            return false;
        } else if (!isMatchNickName) {
            setErrorLayout(binding.etNicknameLayout, binding.tvErrorNickName, WARNING_MSG_RULE_NICKNAME);
            return false;
        } else {
            clearErrorLayout(binding.etNicknameLayout, binding.tvErrorNickName);
            return true;
        }
    }


    // 아이디 => 5~20자의 영문 소문자, 숫자
    private boolean checkId(String userID) {
        boolean isMatch = Pattern.matches(REGEX_ID, userID);
        // 아이디 입력 에러 구분
        if (userID.isEmpty()) {
            // 기입 안했을 때
            setErrorLayout(binding.etIdLayout, binding.tvErrorId, WARNING_MSG_NO_ID);
            return false;
        } else if (!isMatch) {
            // 정규식에 맞지 않을 때
            setErrorLayout(binding.etIdLayout, binding.tvErrorId, WARNING_MSG_RULE_ID);
            return false;
        } else {
            // 정상적으로 입력 됐을 때
            clearErrorLayout(binding.etIdLayout, binding.tvErrorId);
            return true;
        }
    }

    private void clearErrorLayout(TextInputLayout layout, TextView textView) {
        textView.setVisibility(View.GONE);
        layout.setError(null);
    }

    private void setErrorLayout(TextInputLayout layout, TextView textView, String errorMsg) {
        layout.setError(errorMsg);
        layout.setErrorEnabled(true);
        textView.setVisibility(View.VISIBLE);
        textView.setText(layout.getError());
    }

    // 이메일 => 5~30자의 이메일 형식
    private boolean checkEmail(String emailId) {
        boolean isMatch = Pattern.matches(REGEX_EMAIL, emailId);
        // 아이디 입력 에러 구분
        if (emailId.isEmpty()) {
            // 기입 안했을 때
            setErrorLayout(binding.etEmailLayout, binding.tvErrorEmail, WARNING_MSG_NO_EMAIL);
            return false;
        } else if (!isMatch) {
            // 정규식에 맞지 않을 때
            setErrorLayout(binding.etEmailLayout, binding.tvErrorEmail, WARNING_MSG_RULE_EMAIL);
            return false;
        } else {
            // 정상적으로 입력 됐을 때
            clearErrorLayout(binding.etEmailLayout, binding.tvErrorEmail);
            return true;
        }
    }


    // 비밀번호 => 8~16자의 영문 대/소문자, 숫자, 특문
    private boolean checkPassword(String password) {
        boolean isMatch = Pattern.matches(REGEX_PASSWORD, password);
        boolean isMatchPassword = password.length() < 8 || password.length() > 20;
        // 비밀번호 입력 에러 구분
        if (password.isEmpty()) {
            // 기입 안했을 때
            setErrorLayout(binding.etPasswordLayout, binding.tvErrorPassword, WARNING_MSG_NO_PASSWORD);
            return false;
        } else if (!isMatch || isMatchPassword) {
            // 정규식에 맞지 않을 때
            setErrorLayout(binding.etPasswordLayout, binding.tvErrorPassword, WARNING_MSG_RULE_PASSWORD);
            return false;
        } else {
            // 정상적으로 입력 됐을 때
            clearErrorLayout(binding.etPasswordLayout, binding.tvErrorPassword);
            return true;
        }
    }

    private boolean checkBirth(String birthday) {
        boolean isMatch = Pattern.matches(YYYYMMDD, birthday);
        if (birthday.isEmpty()) {
            setErrorLayout(binding.etBirthLayout, binding.tvErrorBirth, WARNING_MSG_NO_BIRTHDAY);
            return false;
        } else if (!isMatch) {
            setErrorLayout(binding.etBirthLayout, binding.tvErrorBirth, WARNING_MSG_RULE_BIRTHDAY);
            return false;
        } else {
            clearErrorLayout(binding.etBirthLayout, binding.tvErrorBirth);
            return true;
        }
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        boolean isMatch = Pattern.matches(REGEX_PHONE_NUMBER, phoneNumber);
        if (phoneNumber.isEmpty()) {
            setErrorLayout(binding.etNumberLayout, binding.tvErrorPhoneNumber, WARNING_MSG_NO_PHONE_NUMBER);
            return false;
        } else if (!isMatch) {
            setErrorLayout(binding.etNumberLayout, binding.tvErrorPhoneNumber, WARNING_MSG_RULE_NUMBER);
            return false;
        } else {
            clearErrorLayout(binding.etNumberLayout, binding.tvErrorPhoneNumber);
            return true;
        }
    }

    private void onClickBtn() {
        // 버튼 클릭 리스너 등록
        binding.btnMale.setOnClickListener(v -> updateButtonState(binding.btnMale));
        binding.btnFemale.setOnClickListener(v -> updateButtonState(binding.btnFemale));
        binding.btnNone.setOnClickListener(v -> updateButtonState(binding.btnNone));
    }


    private void updateButtonState(@NonNull Button selectedButton) {
        // 모든 버튼의 초기 상태 설정
        int unselectedBackgroundStart = R.drawable.btn_unclick_sex_start;
        int unselectedBackgroundCenter = R.drawable.btn_unclick_sex_center;
        int unselectedBackgroundEnd = R.drawable.btn_unclick_sex_end;
        int unselectedTextColor = getColor(R.color.grey_100);
        m_sex = selectedButton.getText().toString();
        binding.btnMale.setBackgroundResource(unselectedBackgroundStart);
        binding.btnMale.setTextColor(unselectedTextColor);
        binding.btnFemale.setBackgroundResource(unselectedBackgroundCenter);
        binding.btnFemale.setTextColor(unselectedTextColor);
        binding.btnNone.setBackgroundResource(unselectedBackgroundEnd);
        binding.btnNone.setTextColor(unselectedTextColor);

        // 선택된 버튼의 상태 변경
        int selectedBackgroundColor;
        int selectedTextColor = getColor(R.color.navy_300);
        if (selectedButton == binding.btnMale) {
            selectedBackgroundColor = R.drawable.btn_click_sex_start;
        } else if (selectedButton == binding.btnFemale) {
            selectedBackgroundColor = R.drawable.btn_click_sex_center;
        } else {
            selectedBackgroundColor = R.drawable.btn_click_sex_end;
        }
        selectedButton.setBackgroundResource(selectedBackgroundColor);
        selectedButton.setTextColor(selectedTextColor);
    }
}

