package com.zzangse.attendance_check.fragmentmore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zzangse.attendance_check.databinding.FragmentChangePasswordBinding;
import com.zzangse.attendance_check.request.ChangePasswordRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;
    private String userID;
    private static final String WARNING_MSG_RULE_PASSWORD = "•비밀번호: 8~20자의 영문 대/소문자, 숫자, [!,@,#,$]를 사용해 주세요.";
    private static final String WARNING_MSG_NO_EQUAL_PASSWORD = "•비밀번호: 다시 확인해주세요.";
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9!@#$]+$";

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater);
        return binding.getRoot();
    }

    private void getBundle() {
        if (getArguments() != null) {
            userID = getArguments().getString("userID");
            Log.d("changPW", userID + "");
        } else {
            Log.d("changPW", userID + " 비었음");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 구현
        getBundle();
        onClickSaveBtn();
        onClickClose();
    }

    private boolean nullPassword(String password, String passwordCheck) {
        if (password.isEmpty() || passwordCheck.isEmpty()) {
            Log.d("password", "비어있음");
            binding.tvPasswordError.setText(WARNING_MSG_NO_EQUAL_PASSWORD);
            binding.tvPasswordError.setVisibility(View.VISIBLE);
            return false;
        } else {
            binding.tvPasswordError.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean checkPassword(String password, String passwordCheck) {
        boolean isMatch = Pattern.matches(REGEX_PASSWORD, passwordCheck);
        boolean isMatchPassword = passwordCheck.length() < 8 || passwordCheck.length() > 20
                && password.length() < 8 || password.length() > 20;
        // 비밀번호 입력 에러 구분
        if (passwordCheck.isEmpty()) {
            // 기입 안했을 때
            Log.d("password", "기입 안했을 때");
            binding.tvPasswordError.setText(WARNING_MSG_NO_EQUAL_PASSWORD);
            binding.tvPasswordError.setVisibility(View.VISIBLE);
            return false;
        } else if (!password.equals(passwordCheck)) {
            // 비밀번호 일치하지 않을 때 (비밀번호, 비밀번호 확인)
            Log.d("password", "비밀번호 일치하지 않을 때 ");
            binding.tvPasswordError.setText(WARNING_MSG_NO_EQUAL_PASSWORD);
            binding.tvPasswordError.setVisibility(View.VISIBLE);
            return false;
        } else if (!isMatch || isMatchPassword) {
            // 정규식에 맞지 않을 때
            Log.d("password", "정규식에 맞지 않을 때 ");
            binding.tvPasswordError.setText(WARNING_MSG_RULE_PASSWORD);
            binding.tvPasswordError.setVisibility(View.VISIBLE);
            return false;
        } else {
            // 정상적으로 입력 됐을 때
            Log.d("password", "정상적으로 입력 됐을 때 ");
            binding.tvPasswordError.setVisibility(View.GONE);
            return true;
        }
    }

    private void updatePassword() {
        String newPassword = binding.etNewPassword.getText().toString();
        String newPasswordCheck = binding.etNewPasswordCheck.getText().toString();
        String userPassword = binding.etUserPassword.getText().toString();
        if (nullPassword(newPassword, newPasswordCheck)) {
            if (checkPassword(newPassword, newPasswordCheck)) {
                ChangePasswordRequest request = new ChangePasswordRequest(userID, userPassword, newPassword, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String msg = jsonObject.getString("message");
                            Log.d("s", msg);
                            if (msg.equals("비밀번호가 변경되었습니다.")) {
                                Toast.makeText(getContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                requireActivity().finish();
                            } else if (msg.equals("현재 비밀번호가 일치하지 않습니다.")) {
                                binding.tvUserPasswordError.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (getContext() != null) {
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(request);
                }
            }

        }

    }


    private void onClickClose() {
        binding.toolbarChangePassword.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                requireActivity().finish();
                return true;
            }
        });
    }

    private void onClickSaveBtn() {
        binding.btnSave.setOnClickListener(v -> {
            updatePassword();
        });
    }
}