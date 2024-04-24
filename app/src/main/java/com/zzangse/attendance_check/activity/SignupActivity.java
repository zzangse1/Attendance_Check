package com.zzangse.attendance_check.activity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.UserAccount;
import com.zzangse.attendance_check.databinding.ActivitySignupBinding;
import com.zzangse.attendance_check.request.SignupRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private boolean isVisibility = true;
    private String m_sex="";
    private FirebaseAuth mFirebaseAuth; // 파이어 베이스 연결
    private DatabaseReference mDatabaseRef; // 실시간 데이터 베이스
    // *** 정규식
    private static final String YYYYMMDD = "(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])";
    private static final String REGEX_ID = "^[a-z0-9_-]+$";
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9!@#$]+$";
    private static final String REGEX_EMAIL_ID = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    private static final String REGEX_NAME = "^[가-힣]{2,6}$";
    private static final String REGEX_PHONE_NUMBER = "^\\d{3}-?\\d{3,4}-?\\d{4}$";
    private static final String WARNING_MSG_NO_EMAIL_ID = "•이메일: 필수 정보 입니다.";
    private static final String WARNING_MSG_ERROR_ID = "•이메일: 사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.";
    private static final String WARNING_MSG_RULE_EMAIL_ID = "•이메일: [test@test.com]와 같은 형식으로 작성해주세요.";
    private static final String WARNING_MSG_RULE_PASSWORD = "•비밀번호: 8~16자의 영문 대/소문자, 숫자, [!,@,#,$]를 사용해 주세요.";
    private static final String WARNING_MSG_NO_PASSWORD = "•비밀번호: 필수 정보입니다.";
    private static final String WARNING_MSG_NO_NICKNAME = "•닉네임: 필수 정보입니다.";
    private static final String WARNING_MSG_RULE_NICKNAME = "•닉네임: 2~8자로 설정해주세요.";
    private static final String WARNING_MSG_NO_BIRTHDAY = "•생년월일: 필수 정보입니다.";
    private static final String WARNING_MSG_NO_NAME = "•이름: 필수 정보입니다.";
    private static final String WARNING_MSG_NO_SEX = "•성별: 필수 정보입니다.";
    private static final String WARNING_MSG_NO_PHONE_NUMBER = "•휴대전화번호: 필수 정보입니다.";
    private static boolean IS_VALID_ID = false;
    private static boolean IS_VALID_PASSWORD = false;
    private static boolean IS_VALID_NICK_NAME = false;
    private static boolean IS_VALID_NAME = false;
    private static boolean IS_VALID_BIRTH = false;
    private static boolean IS_VALID_SEX = false;
    private static boolean IS_VALID_PHONE_NUMBER = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        onClickBack();
        onClickBtn();
        onClick();
        onClickEditTextPassWordShow();
    }

    private void initView() {
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }

    private void onClickBack() {
        binding.ibBack.setOnClickListener(v->{
            onBackPressed();
            finish();
        });
    }


    // 비밀번호 확인 기능
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


//    private void onClickSignUp() {
//        binding.btnSend.setOnClickListener(v -> {
//            // id 체크
//            m_emailId = binding.etEmailId.getText().toString();
//            m_password = binding.etPassword.getText().toString();
//            m_birth = binding.etBirth.getText().toString();
//            m_nickName = binding.etNickName.getText().toString();
//            m_name = binding.etName.getText().toString();
//            m_phoneNumber = binding.etNumber.getText().toString();
//
//
//            IS_VALID_ID = checkEmailId(m_emailId);
//            IS_VALID_PASSWORD = checkPassword(m_password);
//            IS_VALID_BIRTH = checkBirth(m_birth);
//            IS_VALID_NICK_NAME = checkNickName(m_nickName);
//            IS_VALID_NAME = checkName(m_name);
//            IS_VALID_SEX = checkSex(m_sex);
//            IS_VALID_PHONE_NUMBER = checkPhoneNumber(m_phoneNumber);
//
////            setCheckValid(IS_VALID_ID, IS_VALID_PASSWORD, IS_VALID_BIRTH,
////                    IS_VALID_NICK_NAME, IS_VALID_NAME, IS_VALID_SEX, IS_VALID_PHONE_NUMBER);
//
//            setClearFocus();
//        });
//
//    }

    private void onClick() {
        binding.btnSend.setOnClickListener(v -> {
            String userID = binding.etId.getText().toString();
            String userPassword = binding.etPassword.getText().toString();
            String userName = binding.etName.getText().toString();
            int userAge = Integer.parseInt(binding.etBirth.getText().toString());

            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.d("hello", "1");
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        boolean isSuccess = jsonObject.getBoolean("success");
                        Log.d("hello", "2");
                        if (isSuccess) {
                            Log.d("hello", "3");
                            Toast.makeText(getApplicationContext(), "회원등록 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d("hello", "4");
                            Toast.makeText(getApplicationContext(), "회원등록 실패", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.d("hello", "5");
                        throw new RuntimeException(e);
                    }
                }
            };
            Log.d("hello", "6");
            SignupRequest request = new SignupRequest(userID, userPassword, userName, userAge, listener);
            RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
            queue.add(request);
        });
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
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.et_unclick_password);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.et_error_sex);
        if (!sex.isEmpty()) {
            binding.etSexLayout.setBackground(drawable_ok);
            binding.tvErrorSex.setVisibility(View.GONE);
            return true;
        } else {
            binding.etSexLayout.setBackground(drawable_error);
            binding.tvErrorSex.setText(WARNING_MSG_NO_SEX);
            binding.tvErrorSex.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private boolean checkName(String name) {
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_person);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error_person);
        boolean isMatch = Pattern.matches(REGEX_NAME, name);
        if (isMatch) {
            binding.etName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            binding.tvErrorName.setVisibility(View.GONE);
            return true;
        } else {
            binding.etName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            binding.tvErrorName.setText(WARNING_MSG_NO_NAME);
            binding.tvErrorName.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private boolean checkNickName(String nickName) {
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_nickname);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error_nickname);
        boolean isMatchNickName = nickName.length() >= 2 && nickName.length() <= 8;
        if (nickName.isEmpty()) {
            binding.etNickName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            binding.tvErrorNickName.setText(WARNING_MSG_NO_NICKNAME);
            binding.tvErrorNickName.setVisibility(View.VISIBLE);
            return false;
        } else if (!isMatchNickName) {
            binding.etNickName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            binding.tvErrorNickName.setText(WARNING_MSG_RULE_NICKNAME);
            binding.tvErrorNickName.setVisibility(View.VISIBLE);
            return false;
        } else {
            binding.etNickName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            binding.tvErrorNickName.setVisibility(View.GONE);
            return true;
        }
    }


    // 아이디 => 5~20자의 영문 소문자 ,숫자 특수기호_,-만 사용가능
    private boolean checkEmailId(String emailId) {
        boolean isMatch = Pattern.matches(REGEX_EMAIL_ID, emailId);
        // 아이디 입력 에러 구분
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_person);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error_person);
        if (emailId.isEmpty()) {
            // 기입 안했을 때
            binding.etId.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            binding.tvErrorId.setText(WARNING_MSG_NO_EMAIL_ID);
            binding.tvErrorId.setVisibility(View.VISIBLE);
            return false;
        } else if (!isMatch) {
            // 정규식에 맞지 않을 때
            binding.etId.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            binding.tvErrorId.setText(WARNING_MSG_RULE_EMAIL_ID);
            binding.tvErrorId.setVisibility(View.VISIBLE);
            return false;
        } else {
            // 정상적으로 입력 됐을 때
            binding.etId.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            binding.etId.setBackgroundResource(R.drawable.selector_et_id);
            binding.tvErrorId.setVisibility(View.GONE);
            return true;
        }
    }


    // 비밀번호 => 8~16자의 영문 대/소문자, 숫자, 특문
    private boolean checkPassword(String password) {
        boolean isMatch = Pattern.matches(REGEX_PASSWORD, password);
        boolean isMatchPassword = password.length() < 8 || password.length() > 16;
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_lock);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error_lock);
        // 비밀번호 입력 에러 구분
        if (password.isEmpty()) {
            // 기입 안했을 때
            binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            binding.tvErrorPassword.setText(WARNING_MSG_NO_PASSWORD);
            binding.tvErrorPassword.setVisibility(View.VISIBLE);
            return false;
        } else if (!isMatch || isMatchPassword) {
            // 정규식에 맞지 않을 때
            binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            binding.tvErrorPassword.setText(WARNING_MSG_RULE_PASSWORD);
            binding.tvErrorPassword.setVisibility(View.VISIBLE);
            return false;
        } else {
            // 정상적으로 입력 됐을 때
            binding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            binding.tvErrorPassword.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean checkBirth(String birthday) {
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_calendar);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error_calendar);
        boolean isMatch = Pattern.matches(YYYYMMDD, birthday);
        if (isMatch) {
            binding.etBirth.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            binding.tvErrorBirth.setVisibility(View.GONE);
            return true;
        } else {
            binding.etBirth.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            binding.tvErrorBirth.setText(WARNING_MSG_NO_BIRTHDAY);
            binding.tvErrorBirth.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_phone);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error_phone);
        boolean isMatch = Pattern.matches(REGEX_PHONE_NUMBER, phoneNumber);
        if (isMatch) {
            binding.etNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            binding.tvErrorPhoneNumber.setVisibility(View.GONE);
            return true;
        } else {
            binding.etNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            binding.tvErrorPhoneNumber.setText(WARNING_MSG_NO_PHONE_NUMBER);
            binding.tvErrorPhoneNumber.setVisibility(View.VISIBLE);
            return false;
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
        int unselectedTextColor = getColor(R.color.gray_100);
        m_sex = selectedButton.getText().toString();
        binding.btnMale.setBackgroundResource(unselectedBackgroundStart);
        binding.btnMale.setTextColor(unselectedTextColor);
        binding.btnFemale.setBackgroundResource(unselectedBackgroundCenter);
        binding.btnFemale.setTextColor(unselectedTextColor);
        binding.btnNone.setBackgroundResource(unselectedBackgroundEnd);
        binding.btnNone.setTextColor(unselectedTextColor);

        // 선택된 버튼의 상태 변경
        int selectedBackgroundColor;
        int selectedTextColor = getColor(R.color.navy_200);
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

