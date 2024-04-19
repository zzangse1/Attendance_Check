package com.zzangse.attendance_check.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding signupBinding;
    private String m_emailId, m_password, m_nickName, m_name, m_birth, m_sex = "", m_phoneNumber;
    private boolean isVisibility = true;
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
        initFirebase();
        onClickBtn();
        onClickEditTextPassWordShow();
        onClickSignUp();
    }

    private void initView() {
        signupBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(signupBinding.getRoot());
    }

    private void initFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }


    // 비밀번호 확인 기능
    private void onClickEditTextPassWordShow() {
        signupBinding.ibShowPassword.setOnClickListener(v -> {
            if (!isVisibility) {
                signupBinding.ibShowPassword.setImageResource(R.drawable.ic_visibility_off);
                signupBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                isVisibility = true;
            } else {
                signupBinding.ibShowPassword.setImageResource(R.drawable.ic_visibility);
                signupBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                isVisibility = false;
            }
        });
    }


    private void onClickSignUp() {
        signupBinding.btnSend.setOnClickListener(v -> {
            // id 체크
            m_emailId = signupBinding.etEmailId.getText().toString();
            m_password = signupBinding.etPassword.getText().toString();
            m_birth = signupBinding.etBirth.getText().toString();
            m_nickName = signupBinding.etNickName.getText().toString();
            m_name = signupBinding.etName.getText().toString();
            m_phoneNumber = signupBinding.etNumber.getText().toString();


            IS_VALID_ID = checkEmailId(m_emailId);
            IS_VALID_PASSWORD = checkPassword(m_password);
            IS_VALID_BIRTH = checkBirth(m_birth);
            IS_VALID_NICK_NAME = checkNickName(m_nickName);
            IS_VALID_NAME = checkName(m_name);
            IS_VALID_SEX = checkSex(m_sex);
            IS_VALID_PHONE_NUMBER = checkPhoneNumber(m_phoneNumber);

            setCheckValid(IS_VALID_ID, IS_VALID_PASSWORD, IS_VALID_BIRTH,
                    IS_VALID_NICK_NAME, IS_VALID_NAME, IS_VALID_SEX, IS_VALID_PHONE_NUMBER);

            setClearFocus();
        });

    }

    // 회원가입 처리
    private void setCheckValid(boolean isValidId, boolean isValidPassword, boolean isValidBirthday, boolean isValidEmail, boolean isValidName, boolean isValidSex, boolean isValidPhoneNumber) {
        // Firebase Auth 진행
        if (isValidId && isValidPassword && isValidBirthday && isValidEmail && isValidName && isValidSex && isValidPhoneNumber) {
            mFirebaseAuth.createUserWithEmailAndPassword(m_emailId, m_password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // firebase 삽입
                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                        UserAccount account = new UserAccount();
                        account.setIdToken(firebaseUser.getUid());
                        account.setId(firebaseUser.getEmail());
                        account.setBirth(m_birth);
                        account.setPassword(m_password);
                        account.setNickName(m_nickName);
                        account.setName(m_name);
                        account.setSex(m_sex);
                        account.setNumber(m_phoneNumber);

                        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                        Toast.makeText(SignupActivity.this, "회원가입성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignupActivity.this, "회원가입실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.et_unclick_password);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.et_error_sex);
        if (!sex.isEmpty()) {
            signupBinding.etSexLayout.setBackground(drawable_ok);
            signupBinding.tvErrorSex.setVisibility(View.GONE);
            return true;
        } else {
            signupBinding.etSexLayout.setBackground(drawable_error);
            signupBinding.tvErrorSex.setText(WARNING_MSG_NO_SEX);
            signupBinding.tvErrorSex.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private boolean checkName(String name) {
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_person);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error_person);
        boolean isMatch = Pattern.matches(REGEX_NAME, name);
        if (isMatch) {
            signupBinding.etName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            signupBinding.tvErrorName.setVisibility(View.GONE);
            return true;
        } else {
            signupBinding.etName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            signupBinding.tvErrorName.setText(WARNING_MSG_NO_NAME);
            signupBinding.tvErrorName.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private boolean checkNickName(String nickName) {
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_nickname);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error_nickname);
        boolean isMatchNickName = nickName.length() >= 2 && nickName.length() <= 8;
        if (nickName.isEmpty()) {
            signupBinding.etNickName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            signupBinding.tvErrorNickName.setText(WARNING_MSG_NO_NICKNAME);
            signupBinding.tvErrorNickName.setVisibility(View.VISIBLE);
            return false;
        } else if (!isMatchNickName) {
            signupBinding.etNickName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            signupBinding.tvErrorNickName.setText(WARNING_MSG_RULE_NICKNAME);
            signupBinding.tvErrorNickName.setVisibility(View.VISIBLE);
            return false;
        } else {
            signupBinding.etNickName.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            signupBinding.tvErrorNickName.setVisibility(View.GONE);
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
            signupBinding.etEmailId.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            signupBinding.tvErrorId.setText(WARNING_MSG_NO_EMAIL_ID);
            signupBinding.tvErrorId.setVisibility(View.VISIBLE);
            return false;
        } else if (!isMatch) {
            // 정규식에 맞지 않을 때
            signupBinding.etEmailId.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            signupBinding.tvErrorId.setText(WARNING_MSG_RULE_EMAIL_ID);
            signupBinding.tvErrorId.setVisibility(View.VISIBLE);
            return false;
        } else {
            // 정상적으로 입력 됐을 때
            signupBinding.etEmailId.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            signupBinding.etEmailId.setBackgroundResource(R.drawable.selector_et_id);
            signupBinding.tvErrorId.setVisibility(View.GONE);
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
            signupBinding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            signupBinding.tvErrorPassword.setText(WARNING_MSG_NO_PASSWORD);
            signupBinding.tvErrorPassword.setVisibility(View.VISIBLE);
            return false;
        } else if (!isMatch || isMatchPassword) {
            // 정규식에 맞지 않을 때
            signupBinding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            signupBinding.tvErrorPassword.setText(WARNING_MSG_RULE_PASSWORD);
            signupBinding.tvErrorPassword.setVisibility(View.VISIBLE);
            return false;
        } else {
            // 정상적으로 입력 됐을 때
            signupBinding.etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            signupBinding.tvErrorPassword.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean checkBirth(String birthday) {
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_calendar);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error_calendar);
        boolean isMatch = Pattern.matches(YYYYMMDD, birthday);
        if (isMatch) {
            signupBinding.etBirth.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            signupBinding.tvErrorBirth.setVisibility(View.GONE);
            return true;
        } else {
            signupBinding.etBirth.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            signupBinding.tvErrorBirth.setText(WARNING_MSG_NO_BIRTHDAY);
            signupBinding.tvErrorBirth.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        Drawable drawable_ok = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_phone);
        Drawable drawable_error = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error_phone);
        boolean isMatch = Pattern.matches(REGEX_PHONE_NUMBER, phoneNumber);
        if (isMatch) {
            signupBinding.etNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_ok,null,null,null);
            signupBinding.tvErrorPhoneNumber.setVisibility(View.GONE);
            return true;
        } else {
            signupBinding.etNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable_error,null,null,null);
            signupBinding.tvErrorPhoneNumber.setText(WARNING_MSG_NO_PHONE_NUMBER);
            signupBinding.tvErrorPhoneNumber.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private void onClickBtn() {
        // 버튼 클릭 리스너 등록
        signupBinding.btnMale.setOnClickListener(v -> updateButtonState(signupBinding.btnMale));
        signupBinding.btnFemale.setOnClickListener(v -> updateButtonState(signupBinding.btnFemale));
        signupBinding.btnNone.setOnClickListener(v -> updateButtonState(signupBinding.btnNone));
    }

    private void updateButtonState(@NonNull Button selectedButton) {
        // 모든 버튼의 초기 상태 설정
        int unselectedBackgroundStart = R.drawable.btn_unclick_sex_start;
        int unselectedBackgroundCenter = R.drawable.btn_unclick_sex_center;
        int unselectedBackgroundEnd = R.drawable.btn_unclick_sex_end;
        int unselectedTextColor = getColor(R.color.gray_100);
        m_sex = selectedButton.getText().toString();
        signupBinding.btnMale.setBackgroundResource(unselectedBackgroundStart);
        signupBinding.btnMale.setTextColor(unselectedTextColor);
        signupBinding.btnFemale.setBackgroundResource(unselectedBackgroundCenter);
        signupBinding.btnFemale.setTextColor(unselectedTextColor);
        signupBinding.btnNone.setBackgroundResource(unselectedBackgroundEnd);
        signupBinding.btnNone.setTextColor(unselectedTextColor);

        // 선택된 버튼의 상태 변경
        int selectedBackgroundColor;
        int selectedTextColor = getColor(R.color.navy_200);
        if (selectedButton == signupBinding.btnMale) {
            selectedBackgroundColor = R.drawable.btn_click_sex_start;
        } else if (selectedButton == signupBinding.btnFemale) {
            selectedBackgroundColor = R.drawable.btn_click_sex_center;
        } else {
            selectedBackgroundColor = R.drawable.btn_click_sex_end;
        }
        selectedButton.setBackgroundResource(selectedBackgroundColor);
        selectedButton.setTextColor(selectedTextColor);
    }
}

