package com.zzangse.attendance_check.fragmentfind;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.FragmentFindIdBinding;
import com.zzangse.attendance_check.request.FindAccountIdRequest;
import com.zzangse.attendance_check.request.FindAccountRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class FindIdFragment extends Fragment {
    private FragmentFindIdBinding binding;
    private String ERROR_MSG_NO_NAME = "이름을 입력해주세요.";
    private String ERROR_MSG_NO_EMAIL = "이메일을 입력해주세요.";
    private String ERROR_MSG_NOT_MATCH_EMAIL = "가입시 입력한 이름과 이메일이 맞지 않습니다.";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFindIdBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickRadioBtn();
        settingBtn();
        onClickSendNumber();
    }

    private void onClickSendNumber() {
        binding.btnTopCertificationNumber.setOnClickListener(v -> {
            // db에 이름과 휴대전화번호가 동일하면 인증번호 발송
            String userName = binding.etFindTopAccountName.getText().toString();
            String userPhoneNumber = binding.etFindAccountNumber.getText().toString();
            findAccountDB(userName, userPhoneNumber, "null");
            Log.d("ID찾기 휴대전화.ver", "true");
        });
        binding.btnBottomCertificationNumber.setOnClickListener(v -> {
            boolean isSuccess = visibilityNameAndEmailError();
            if (isSuccess) {
                String userName = binding.etFindBottomAccountName.getText().toString();
                String userID = binding.etFindAccountEmail.getText().toString();
                findAccountDB(userName, "null", userID);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding.rb1.isChecked()) {
            binding.linearTopLayout.setVisibility(View.VISIBLE);
            binding.linearBottomLayout.setVisibility(View.GONE);
            binding.rb2.setChecked(false);
        } else {
            binding.linearTopLayout.setVisibility(View.GONE);
            binding.linearBottomLayout.setVisibility(View.VISIBLE);
            binding.rb1.setChecked(false);
        }
    }

    private void onClickRadioBtn() {
        binding.rb1.setChecked(true);
        binding.linearBottomLayout.setVisibility(View.GONE);
        binding.rb1.setOnClickListener(v -> {
            binding.linearTopLayout.setVisibility(View.VISIBLE);
            binding.linearBottomLayout.setVisibility(View.GONE);
            binding.rb2.setChecked(false);
        });
        binding.rb2.setOnClickListener(v -> {
            binding.linearTopLayout.setVisibility(View.GONE);
            binding.linearBottomLayout.setVisibility(View.VISIBLE);
            binding.rb1.setChecked(false);
        });
    }

    private void setActivateBtn(EditText editText, Button button, int inputLength) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int tvLength = editText.length();
                if (tvLength == inputLength) {
                    button.setEnabled(true);
                    button.setBackgroundResource(R.drawable.btn_selector);
                } else {
                    button.setEnabled(false);
                    button.setBackgroundResource(R.drawable.btn_clicked);
                }
            }
        });
    }

    private void findAccount(String randCode,String userEmail) {
        Response.Listener<String>listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    boolean isSuccess = jsonObject.getBoolean("success");
//                    if (isSuccess) {
//                        //성공
//                    } else {
//                        //실패
//                    }
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
            }
        };
        FindAccountRequest request = new FindAccountRequest(randCode, userEmail,listener);
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }
    private String sendRandomCode() {
        Random random = new Random();
        StringBuilder randCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            randCode.append(digit);
        }
        return randCode.toString();
    }

    // 공통 오류 확인 메서드
    private boolean visibilityError(TextView errorView, EditText inputField, String errorMessage) {
        if (inputField.getText().toString().isEmpty()) {
            errorView.setText(errorMessage);
            errorView.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorView.setVisibility(View.GONE);
            return true;
        }
    }

    // 이름 오류 확인 메서드
    private boolean visibilityNameError() {
        return visibilityError(binding.tvNameError, binding.etFindBottomAccountName, ERROR_MSG_NO_NAME);
    }

    // 이메일 오류 확인 메서드
    private boolean visibilityEmailError() {
        return visibilityError(binding.tvEmailError, binding.etFindAccountEmail, ERROR_MSG_NO_EMAIL);
    }

    // 이름과 이메일 모두 확인 메서드
    private boolean visibilityNameAndEmailError() {
        boolean isNameValid = visibilityNameError();
        boolean isEmailValid = visibilityEmailError();
        return isNameValid && isEmailValid;
    }
    private void settingBtn() {
        setActivateBtn(binding.etFindAccountNumber, binding.btnTopCertificationNumber, 11);
        setActivateBtn(binding.etFindAccountTopCertificationNumber, binding.btnTopConfirm, 4);
        setActivateBtn(binding.etFindAccountBottomCertificationNumber, binding.btnBottomConfirm, 4);
    }


    private void dbError(boolean isEmailSuccess) {
        if (isEmailSuccess) {
            binding.tvEmailError.setVisibility(View.GONE);
        } else {
            binding.tvEmailError.setText(ERROR_MSG_NOT_MATCH_EMAIL);
            binding.tvEmailError.setVisibility(View.VISIBLE);
        }
    }

    private void findAccountDB(String userName, String userPhoneNumber, String userEmail) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isEmailSuccess = jsonObject.getBoolean("email_success");
                    boolean isNumberSuccess = jsonObject.getBoolean("phone_success");
                    dbError(isEmailSuccess);
                    if (isEmailSuccess) {
                        Log.d("isEmailSuccess", isEmailSuccess + "");
                        findAccount(sendRandomCode(),userEmail);
                    } else {
                        Log.d("isEmailSuccess", isEmailSuccess + "");
                    }
                    if (isNumberSuccess) {
                        Log.d("isNumberSuccess", isNumberSuccess + "");

                    } else {
                        Log.d("isNumberSuccess", isNumberSuccess + "");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        FindAccountIdRequest request = new FindAccountIdRequest(userName, userPhoneNumber, userEmail, listener);
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }
}
