package com.zzangse.attendance_check.activity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.ActivitySignupBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding signupBinding;
    private String id, password, email, name, date, phone;
    private boolean isVisibility = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        onClickBtn();
        onClickEditTextPassWordShow();
    }

    private void initView() {
        signupBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(signupBinding.getRoot());
    }


    // 비밀번호 확인 기능
    private void onClickEditTextPassWordShow() {
        signupBinding.ibShowPassword.setOnClickListener(v -> {
            if (!isVisibility) {
                signupBinding.ibShowPassword.setImageResource(R.drawable.ic_visibility_off);
                signupBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                Log.d("onClickEditTextPassWordShow","안 보인다");
                isVisibility = true;
            } else {
                Log.d("onClickEditTextPassWordShow","보인다");
                signupBinding.ibShowPassword.setImageResource(R.drawable.ic_visibility);
                signupBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                isVisibility = false;
            }
        });
    }

    private void onClickBtn() {
        // 버튼 클릭 리스너 등록
        signupBinding.btnMale.setOnClickListener(v -> updateButtonState(signupBinding.btnMale));
        signupBinding.btnFemale.setOnClickListener(v -> updateButtonState(signupBinding.btnFemale));
        signupBinding.btnNone.setOnClickListener(v -> updateButtonState(signupBinding.btnNone));
    }

    private void updateButtonState(Button selectedButton) {
        // 모든 버튼의 초기 상태 설정
        int unselectedBackgroundStart = R.drawable.btn_unclick_gender_start;
        int unselectedBackgroundCenter = R.drawable.btn_unclick_gender_center;
        int unselectedBackgroundEnd = R.drawable.btn_unclick_gender_end;
        int unselectedTextColor = getColor(R.color.gray_100);
        String buttonText = selectedButton.getText().toString();
        Log.d("test", buttonText);
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
            selectedBackgroundColor = R.drawable.btn_click_gender_start;
        } else if (selectedButton == signupBinding.btnFemale) {
            selectedBackgroundColor = R.drawable.btn_click_gender_center;
        } else {
            selectedBackgroundColor = R.drawable.btn_click_gender_end;
        }
        selectedButton.setBackgroundResource(selectedBackgroundColor);
        selectedButton.setTextColor(selectedTextColor);
    }
}

