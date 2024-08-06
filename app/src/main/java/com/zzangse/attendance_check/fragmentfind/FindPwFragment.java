package com.zzangse.attendance_check.fragmentfind;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.LoginActivity;
import com.zzangse.attendance_check.databinding.FragmentFindPwBinding;
import com.zzangse.attendance_check.request.FindAccountDeleteRequest;
import com.zzangse.attendance_check.request.FindAccountRandomCodeRequest;
import com.zzangse.attendance_check.request.FindAccountRequest;
import com.zzangse.attendance_check.request.FindAccountSendRequest;
import com.zzangse.attendance_check.request.PasswordRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class FindPwFragment extends Fragment {
    private FragmentFindPwBinding binding;
    private static final String WARNING_MSG_NO_ID = "•아이디: 필수 정보 입니다.";
    private static final String WARNING_MSG_NO_EMAIL = "•이메일: 필수 정보 입니다.";
    private static final String WARNING_MSG_NO_NAME = "•이름: 필수 정보입니다.";
    private static final String WARNING_MSG_RULE_NAME = "•이름: 2~6자로 설정해주세요.";
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9!@#$]+$";
    private static final String WARNING_MSG_NO_MATCH = "•가입시 입력한 이름과 이메일이 맞지 않습니다.";
    private static final String WARNING_MSG_RULE_PASSWORD = "•비밀번호: 8~20자의 영문 대/소문자, 숫자, [!,@,#,$]를 사용해 주세요.";
    private static final String WARNING_MSG_NO_PASSWORD = "•비밀번호: 필수 정보입니다.";
    private static final String REGEX_NAME = "^[가-힣]{2,6}$";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String REGEX_ID = "^[a-z0-9_-]{5,20}$";
    private static final String WARNING_MSG_RULE_ID = "•아이디: 5~20자의 영문 소문자, 숫자를 사용해 주세요.";
    private static final String WARNING_MSG_RULE_EMAIL = "•이메일: 5~30자의 이메일 형식을 맞춰주세요. [sample@domain.com]";

    private int min, value;
    private CountDownTimer timer;
    private boolean isTextManuallyChanged = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentFindPwBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingBtn();
        onClickSendNumber();
        onClickRandCodeCheck();
        onClickChangeBtn();
    }

    private void onClickSendNumber() {
        binding.btnCertificationNumber.setOnClickListener(v -> {
            boolean isSuccess = visibilityNameAndEmailError();
            Log.d("정보확인", isSuccess + "");
            if (isSuccess) {
                resetManualTextFlag();
                String userName = binding.etFindAccountName.getText().toString();
                String userID = binding.etFindAccountId.getText().toString();
                String userEmail = binding.etFindAccountEmail.getText().toString();
                FindAccountDBInfo(userName, userID, userEmail);
                binding.btnCertificationNumber.setEnabled(false);
            }
        });
    }

    private void showNoFindDB() {
        binding.tvNoDb.setVisibility(View.VISIBLE);
        binding.tvNoDb.setText(WARNING_MSG_NO_MATCH);
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
                } else {
                    button.setEnabled(false);
                }
            }
        });
    }

    private void onClickRandCodeCheck() {
        binding.btnConfirm.setOnClickListener(v -> {
            String userID = binding.etFindAccountId.getText().toString();
            String userEmail = binding.etFindAccountEmail.getText().toString();
            String randCode = binding.etFindAccountCertificationNumber.getText().toString();
            Log.d("onCLickCOde", userID + ", " + randCode);
            checkCode(userID, userEmail, randCode);
        });
    }


    private void setTimer(String userID) {
        // 타이머 초기화: 3분을 180초로 설정
        final int initialMinutes = 3;
        final int initialSeconds = 0;
        min = initialMinutes;
        value = initialSeconds;
        int delay = (initialMinutes * 60 + initialSeconds) * 1000; // 3분을 밀리초로 변환

        timer = new CountDownTimer(delay, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isTextManuallyChanged) {
                    value--;
                    if (value < 0) {
                        value = 59;
                        min--;
                        if (min < 0) {
                            min = 0;
                            value = 0;
                        }
                    }
                    enableTextInput();
                    binding.btnCertificationNumber.setText(String.format("%02d:%02d", min, value));
                }
            }

            @Override
            public void onFinish() {
                binding.btnCertificationNumber.setText("종료");
                deleteDBCode(userID);
                ableTextInput();
            }
        };
        timer.start();
    }

    // 버튼 텍스트를 수동으로 변경하는 메서드
    public void setButtonTextManually(String text) {
        binding.btnCertificationNumber.setEnabled(false);
        binding.btnCertificationNumber.setText(text);
        isTextManuallyChanged = true;
    }

    // 타이머 종료 후 텍스트 변경 플래그 초기화 (필요 시)
    private void resetManualTextFlag() {
        isTextManuallyChanged = false;
    }

    private void showChangePW() {
        binding.linearTopLayout.setVisibility(View.GONE);
        binding.linearBottomLayout.setVisibility(View.VISIBLE);
    }

    private void onChangePW(String userID, String newPassword) {
        PasswordRequest request = new PasswordRequest(
                PasswordRequest.RequestType.FIND_PW,
                userID, null, newPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String msg = jsonObject.getString("message");
                    if (msg.equals("비밀번호가 변경되었습니다.")) {
                        Toast.makeText(getContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "오류발생 문의해주세요.", Toast.LENGTH_SHORT).show();
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

    private void onClickChangeBtn() {
        binding.btnChangePw.setOnClickListener(v -> {
            // 비밀번호 변경을 눌렀을 때
            String newPW = binding.etNewPassword.getText().toString();
            String checkPW = binding.etNewPasswordCheck.getText().toString();
            String userID = binding.etFindAccountId.getText().toString();
            boolean isMatch = Pattern.matches(REGEX_PASSWORD, newPW);
            if (newPW.isEmpty() || checkPW.isEmpty()) {
                binding.tvErrorNewPw.setVisibility(View.VISIBLE);
                binding.tvErrorNewPw.setText(WARNING_MSG_NO_PASSWORD);
            } else if (!newPW.equals(checkPW)) {
                binding.tvErrorNewPw.setVisibility(View.VISIBLE);
                binding.tvErrorNewPw.setText("일치하지 않음");
            } else {
                if (!isMatch) {
                    binding.tvErrorNewPw.setVisibility(View.VISIBLE);
                    binding.tvErrorNewPw.setText(WARNING_MSG_RULE_PASSWORD);
                } else {
                    onChangePW(userID, newPW);
                    binding.tvErrorNewPw.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkCode(String userID, String userEmail, String randCode) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    String userID1 = jsonObject.getString("userID");
                    String userEmail1 = jsonObject.getString("userEmail");
                    String userrandCode1 = jsonObject.getString("randCode1");
                    String userrandCode2 = jsonObject.getString("randCode2");
                    if (isSuccess) {
                        Log.d("난수 확인 완료", isSuccess + "");
                        Log.d("난수 확인 완료", userID1 + "");
                        Log.d("난수 확인 완료", userEmail1 + "");
                        Log.d("난수 확인 완료", userrandCode1 + "");
                        Log.d("난수 확인 완료", userrandCode2 + "");
                        binding.tvRandCodeErrorMsg.setVisibility(View.GONE);
                        binding.btnConfirm.setEnabled(false);
                        binding.btnConfirm.setText("인증 완료");
                        setButtonTextManually("인증 완료");
                        deleteDBCode(userID);
                        showChangePW();
                    } else {
                        Log.d("난수 확인 완료", isSuccess + "");
                        Log.d("난수 확인 완료", userID1 + "");
                        Log.d("난수 확인 완료", userEmail1 + "");
                        Log.d("난수 확인 완료", userrandCode1 + "");
                        Log.d("난수 확인 완료", userrandCode2 + "");
                        binding.tvRandCodeErrorMsg.setVisibility(View.VISIBLE);
                        binding.btnConfirm.setEnabled(true);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        FindAccountSendRequest request = new FindAccountSendRequest(
                FindAccountSendRequest.RequestType.PASSWORD_VERIFICATION,
                userID, userEmail, randCode, listener);
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    private void sendEmail(String userID, String userEmail) {
        FindAccountRandomCodeRequest request = new FindAccountRandomCodeRequest(
                FindAccountRandomCodeRequest.RequestType.PASSWORD_VERIFICATION,
                userID, userEmail, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("finAccount", "이메일 전송");
            }
        });
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    // 공통 오류 확인 메서드
    private boolean visibilityError(TextInputLayout layout, TextView textView, EditText inputField,
                                    String noMsg, String ruleMsg, String REGEX) {
        boolean isMatch = Pattern.matches(REGEX, inputField.getText().toString());
        if (inputField.getText().toString().isEmpty()) {
            setErrorLayout(layout, textView, noMsg);
            Log.d("textView", textView.getText().toString());
            return false;
        } else if (!isMatch) {
            Log.d("textView", textView.getText().toString());
            setErrorLayout(layout, textView, ruleMsg);
            return false;
        } else {
            Log.d("textView", textView.getText().toString());
            clearErrorLayout(layout, textView);
            return true;
        }
    }

    private void setErrorLayout(TextInputLayout layout, TextView textView, String errorMsg) {
        binding.tvNoDb.setVisibility(View.GONE);
        layout.setError(errorMsg);
        layout.setErrorEnabled(true);
        textView.setVisibility(View.VISIBLE);
        textView.setText(layout.getError());
    }

    private void clearErrorLayout(TextInputLayout layout, TextView textView) {
        Log.i("clear", textView + "");
        layout.setError(null);
        layout.setErrorEnabled(false);
        textView.setVisibility(View.GONE);
    }


    // 이름 오류 확인 메서드
    private boolean visibilityNameError() {
        // 조건 추가
        return visibilityError(binding.etFindAccountNameLayout, binding.tvErrorName, binding.etFindAccountName,
                WARNING_MSG_NO_NAME, WARNING_MSG_RULE_NAME, REGEX_NAME);
    }

    private boolean visibilityIDError() {
        return visibilityError(binding.etFindAccountIdLayout, binding.tvErrorId, binding.etFindAccountId,
                WARNING_MSG_NO_ID, WARNING_MSG_RULE_ID, REGEX_ID);
    }

    // 이메일 오류 확인 메서드
    private boolean visibilityEmailError() {
        return visibilityError(binding.etFindAccountEmailLayout, binding.tvErrorEmail, binding.etFindAccountEmail,
                WARNING_MSG_NO_EMAIL, WARNING_MSG_RULE_EMAIL, REGEX_EMAIL);
    }

    // 이름과 이메일 모두 확인 메서드
    private boolean visibilityNameAndEmailError() {
        boolean isNameValid = visibilityNameError();
        boolean isEmailValid = visibilityEmailError();
        boolean isIDValid = visibilityIDError();
        return isNameValid && isEmailValid && isIDValid;
    }


    private void settingBtn() {
//        setActivateBtn(binding.etFindAccountNumber, binding.btnTopCertificationNumber, 11);
//        setActivateBtn(binding.etFindAccountTopCertificationNumber, binding.btnTopConfirm, 6);
        setActivateBtn(binding.etFindAccountCertificationNumber, binding.btnConfirm, 6);
    }


    private void dbError(boolean isEmailSuccess) {
        if (isEmailSuccess) {
//            binding.tvEmailError.setVisibility(View.GONE);
            Log.d("dbError", isEmailSuccess + "");
        } else {
            Log.d("dbError", isEmailSuccess + "");
//            binding.tvEmailError.setText(ERROR_MSG_NOT_MATCH_EMAIL);
//            binding.tvEmailError.setVisibility(View.VISIBLE);
        }
    }

    private void deleteDBCode(String userID) {
        FindAccountDeleteRequest request = new FindAccountDeleteRequest(
                FindAccountDeleteRequest.RequestType.PASSWORD_VERIFICATION,
                userID, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("deleteDBCode", "삭제 완료");
            }
        });
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    private void enableTextInput() {
        binding.btnCertificationNumber.setText(R.string.find_account_certification_number_send);
        binding.btnCertificationNumber.setEnabled(false);
        binding.etFindAccountNameLayout.setEnabled(false);
        binding.etFindAccountIdLayout.setEnabled(false);
        binding.etFindAccountEmailLayout.setEnabled(false);
    }

    private void ableTextInput() {
        showNoFindDB();
        binding.btnCertificationNumber.setText("정보 확인");
        binding.btnCertificationNumber.setEnabled(true);
        binding.etFindAccountNameLayout.setEnabled(true);
        binding.etFindAccountIdLayout.setEnabled(true);
        binding.etFindAccountEmailLayout.setEnabled(true);
    }

    private void FindAccountDBInfo(String userName, String userID, String userEmail) {
        FindAccountRequest request = new FindAccountRequest(
                FindAccountRequest.RequestType.PASSWORD_VERIFICATION,
                userID, userName, userEmail, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isEmailSuccess = jsonObject.getBoolean("success");
                    String userID1 = jsonObject.getString("userID");
                    String userName1 = jsonObject.getString("userName");
                    String userEmail1 = jsonObject.getString("userEmail");
                    if (isEmailSuccess) {
                        Log.d("디비에 정보가 있음 확인", isEmailSuccess + "");
                        Log.d("디비에 정보가 있음 확인", userID1 + ", " + userName1 + ", " + userEmail1);
                        enableTextInput();
                        binding.tvNoDb.setVisibility(View.GONE);
                        sendEmail(userID, userEmail);
                        setTimer(userID);
                    } else {
                        Log.d("디비에 정보가 있음 확인", isEmailSuccess + "");
                        Log.d("디비에 정보가 있음 확인", userID1 + ", " + userName1 + ", " + userEmail1);
                        ableTextInput();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

}
