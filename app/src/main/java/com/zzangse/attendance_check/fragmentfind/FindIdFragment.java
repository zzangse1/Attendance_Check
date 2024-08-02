package com.zzangse.attendance_check.fragmentfind;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.FragmentFindIdBinding;
import com.zzangse.attendance_check.request.FindAccountDeleteRequest;
import com.zzangse.attendance_check.request.FindAccountRequest;
import com.zzangse.attendance_check.request.FindAccountRandomCodeRequest;
import com.zzangse.attendance_check.request.FindAccountSendRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class FindIdFragment extends Fragment {
    private FragmentFindIdBinding binding;
    private String ERROR_MSG_NO_NAME = "이름을 입력해주세요.";
    private String ERROR_MSG_NO_EMAIL = "이메일을 입력해주세요.";
    private String ERROR_MSG_NOT_MATCH_EMAIL = "가입시 입력한 이름과 이메일이 맞지 않습니다.";
    int min = 3;
    private int value;
    private CountDownTimer timer;
    private boolean isTextManuallyChanged = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFindIdBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickSendNumber();
        onClickRandCodeCheck();
        settingBtn();
    }

    private void onClickRandCodeCheck() {
        binding.btnConfirm.setOnClickListener(v -> {
            String userName = binding.etFindAccountName.getText().toString();
            String userEmail = binding.etFindAccountEmail.getText().toString();
            String randCode = binding.etFindAccountCertificationNumber.getText().toString();
            Log.d("onCLickCOde", randCode);
            checkCode(userName, userEmail, randCode);
        });
    }


    private void onClickSendNumber() {
        binding.btnCertificationNumber.setOnClickListener(v -> {
            boolean isSuccess = visibilityNameAndEmailError();
            if (isSuccess) {
                Log.d("success", "success");
                // db에 정보가 있는지 확인
                String userName = binding.etFindAccountName.getText().toString();
                String userEmail = binding.etFindAccountEmail.getText().toString();
                FindAccountDBInfo(userName, userEmail);
                binding.btnCertificationNumber.setEnabled(false);
            } else {
                Log.d("false", "false");
            }
        });
    }

    private void clearErrorLayout(TextInputLayout layout, TextView textView) {
        textView.setVisibility(View.GONE);
        layout.setError(null);
        layout.setErrorEnabled(false);
    }

    private void setErrorLayout(TextInputLayout layout, TextView textView, String errorMsg) {
        layout.setError(errorMsg);
        layout.setErrorEnabled(true);

        textView.setVisibility(View.VISIBLE);
    }

    private void settingBtn() {
//        setActivateBtn(binding.etFindAccountNumber, binding.btnTopCertificationNumber, 11);
//        setActivateBtn(binding.etFindAccountTopCertificationNumber, binding.btnTopConfirm, 6);
        setActivateBtn(binding.etFindAccountCertificationNumber, binding.btnConfirm, 6);
    }

    // 이름과 이메일 모두 확인 메서드
    private boolean visibilityNameAndEmailError() {
        boolean isNameValid = visibilityNameError();
        boolean isEmailValid = visibilityEmailError();
        return isNameValid && isEmailValid;
    }

    // 이름 오류 확인 메서드
    private boolean visibilityNameError() {
        return visibilityError(binding.etFindAccountNameLayout, binding.etFindAccountName, ERROR_MSG_NO_NAME);
    }

    // 이메일 오류 확인 메서드
    private boolean visibilityEmailError() {
        return visibilityError(binding.etFindAccountEmailLayout, binding.etFindAccountEmail, ERROR_MSG_NO_EMAIL);
    }


    // 공통 오류 확인 메서드
    private boolean visibilityError(TextInputLayout layout, TextInputEditText editText, String errorMessage) {
        if (editText.getText().toString().isEmpty()) {
            layout.setError(errorMessage);
            return false;
        } else {
            layout.setError(null);
            return true;
        }
    }

    private void dbError(boolean isEmailSuccess) {
        if (isEmailSuccess) {
            clearErrorLayout(binding.etFindAccountNameLayout, binding.tvEmailError);
            clearErrorLayout(binding.etFindAccountEmailLayout, binding.tvEmailError);
        } else {
            setErrorLayout(binding.etFindAccountNameLayout, binding.tvEmailError, ERROR_MSG_NOT_MATCH_EMAIL);
            setErrorLayout(binding.etFindAccountEmailLayout, binding.tvEmailError, ERROR_MSG_NOT_MATCH_EMAIL);
        }
    }

    private void sendEmail(String userName, String userEmail) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("finAccount", "이메일 전송");
            }
        };
        FindAccountRandomCodeRequest request = new FindAccountRandomCodeRequest(
                FindAccountRandomCodeRequest.RequestType.ID_VERIFICATION,
                userName, userEmail, listener);
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }


    private void deleteDBCode(String userName, String userEmail) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("deleteDBCode", "삭제 완료");
            }
        };
        FindAccountDeleteRequest request = new FindAccountDeleteRequest(
                FindAccountDeleteRequest.RequestType.ID_VERIFICATION,
                userName, userEmail, listener);
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    private void enableTextInput() {
        binding.btnCertificationNumber.setText(R.string.find_account_certification_number_send);
        binding.btnCertificationNumber.setEnabled(false);
        binding.etFindAccountNameLayout.setEnabled(false);
        binding.etFindAccountEmailLayout.setEnabled(false);
    }

    private void ableTextInput() {
        binding.btnCertificationNumber.setText("정보 확인");
        binding.btnCertificationNumber.setEnabled(true);
        binding.etFindAccountNameLayout.setEnabled(true);
        binding.etFindAccountEmailLayout.setEnabled(true);
    }

    private void setTimer(String userName, String userEmail) {
        binding.tvTimer.setVisibility(View.VISIBLE);
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
                    binding.btnCertificationNumber.setText(String.format("%02d:%02d", min, value));
                }
            }

            @Override
            public void onFinish() {
                binding.btnCertificationNumber.setText("종료");
                deleteDBCode(userName, userEmail);
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


    private void checkCode(String userName, String userEmail, String randCode) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    String userEmail1 = jsonObject.getString("userEmail");
                    String userrandCode1 = jsonObject.getString("randCode1");
                    String userrandCode2 = jsonObject.getString("randCode2");
                    if (isSuccess) {
                        Log.d("난수 확인 완료", isSuccess + "");
                        Log.d("난수 확인 완료", userEmail1 + "");
                        Log.d("난수 확인 완료", userrandCode1 + "");
                        Log.d("난수 확인 완료", userrandCode2 + "");
                        binding.tvRandCodeErrorMsg.setVisibility(View.GONE);
                        binding.btnConfirm.setEnabled(false);
                        binding.btnConfirm.setText("인증 완료");
                        setButtonTextManually("인증 완료");
                        deleteDBCode(userName, userEmail);
                        // 아이디 보내기()
                    } else {
                        Log.d("난수 확인 완료", isSuccess + "");
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
                FindAccountSendRequest.RequestType.ID_VERIFICATION,
                userName, userEmail, randCode, listener);
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
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


    private void FindAccountDBInfo(String userName, String userEmail) {
        FindAccountRequest request = new FindAccountRequest(
                FindAccountRequest.RequestType.ID_VERIFICATION,
                null, userName, userEmail, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isEmailSuccess = jsonObject.getBoolean("success");
                    String userName1 = jsonObject.getString("userName");
                    String userEmail1 = jsonObject.getString("userEmail");
                    dbError(isEmailSuccess);
                    if (isEmailSuccess) {
                        Log.d("디비에 정보가 있음 확인", isEmailSuccess + "");
                        Log.d("디비에 정보가 있음 확인", userName1 + ", " + userEmail1);
                        clearErrorLayout(binding.etFindAccountNameLayout, binding.tvNameError);
                        clearErrorLayout(binding.etFindAccountEmailLayout, binding.tvEmailError);
                        enableTextInput();
                        sendEmail(userName, userEmail);
                        setTimer(userName, userEmail);
                    } else {
                        Log.d("디비에 정보가 있음 확인", isEmailSuccess + "");
                        Log.d("디비에 정보가 있음 확인", userName1 + ", " + userEmail1);
                        setErrorLayout(binding.etFindAccountNameLayout, binding.tvNameError, "");
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
