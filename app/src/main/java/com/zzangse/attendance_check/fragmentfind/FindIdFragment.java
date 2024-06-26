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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.FragmentFindIdBinding;
import com.zzangse.attendance_check.request.FindAccountIdRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class FindIdFragment extends Fragment {
    private FragmentFindIdBinding binding;

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
          //  String userID = binding.etFindAccountEmail.getText().toString();
            findAccountDB(userName, userPhoneNumber, "null");

        });
        binding.btnBottomCertificationNumber.setOnClickListener(v->{
            String userName = binding.etFindBottomAccountName.getText().toString();
            String userID = binding.etFindAccountEmail.getText().toString();
            findAccountDB(userName, "null", userID);
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
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
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

    private void settingBtn() {
        setActivateBtn(binding.etFindAccountNumber, binding.btnTopCertificationNumber, 11);
        setActivateBtn(binding.etFindAccountTopCertificationNumber, binding.btnTopConfirm, 4);
        setActivateBtn(binding.etFindAccountBottomCertificationNumber,binding.btnBottomConfirm,4);
    }



    private void findAccountDB(String userName,String userPhoneNumber,String userID) {
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isEmailSuccess = jsonObject.getBoolean("email_success");
                    boolean isNumberSuccess = jsonObject.getBoolean("phone_success");
                    if (isEmailSuccess) {
                        Log.d("isEmailSuccess", isEmailSuccess + "");

                    } else {
                        Log.d("isEmailSuccess", isEmailSuccess+"");
                    }
                    if (isNumberSuccess) {
                        Log.d("isNumberSuccess", isNumberSuccess + "");

                    } else {
                        Log.d("isNumberSuccess", isNumberSuccess+"");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        FindAccountIdRequest request = new FindAccountIdRequest(userName, userPhoneNumber, userID, listener);
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }
}
