package com.zzangse.attendance_check.fragmentmore;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zzangse.attendance_check.MySharedPreferences;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.LoginActivity;
import com.zzangse.attendance_check.databinding.FragmentWithdrawalAppBinding;
import com.zzangse.attendance_check.request.FeedbackRequest;
import com.zzangse.attendance_check.request.WithdrawalAppRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class WithdrawalAppFragment extends Fragment {
    private FragmentWithdrawalAppBinding binding;
    private String userNickName;
    private String userID;
    private String feedback;

    public static WithdrawalAppFragment newInstance() {
        WithdrawalAppFragment fragment = new WithdrawalAppFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWithdrawalAppBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        onClickClose();
        setNameLabel();
        onClickCheckBox();
        onClickWithDrawalBtn();
        textReasonWatch();
    }

    private void getBundle() {
        if (getArguments() != null) {
            userNickName = getArguments().getString("userNickName", userNickName);
            userID = getArguments().getString("userID", userNickName);
        }
    }

    private void setNameLabel() {
        String asdf = userNickName + getResources().getString(R.string.withdrawal_app_top_label);
        binding.tvWithdrawalName.setText(asdf);
    }

    private void onClickWithDrawalBtn() {
        binding.btnWithdrawal.setOnClickListener(v -> {
            if (!binding.cbWithdrawal.isChecked()) {
                binding.tvErrorCheckbox.setVisibility(View.VISIBLE);
            } else {
                showDialog();
            }
        });
    }

    private void textReasonWatch() {
        binding.etWithdrawalFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textCount = binding.etWithdrawalFeedback.length();
                binding.tvCount.setText("( " + textCount + " / 100 )");
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundedDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_withdrawal, null);
        EditText etInput = dialogView.findViewById(R.id.et_withdrawal_agree);
        TextView btnOk = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = builder.setView(dialogView)
                .setCancelable(false)
                .create();
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        btnOk.setOnClickListener(v -> {
            // 탈퇴
            if (etInput.getText().toString().equals(getResources().getString(R.string.fragment_withdrawal_btn_label))) {
                dialog.dismiss();
                insertFeedback();
                withdrawalApp();
            } else {
                Toast.makeText(getActivity(), "문구를 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void moveToLogin() {
        Toast.makeText(getActivity(), "탈퇴를 완료했습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        MySharedPreferences.clearUser(requireContext());
        startActivity(intent);
    }

    private void onClickClose() {
        binding.toolbarWithdrawal.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                requireActivity().finish();
                return true;
            }
        });
    }

    private void onClickCheckBox() {
        binding.cbWithdrawal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.cbWithdrawal.setTextColor(Color.GRAY);
                binding.tvErrorCheckbox.setVisibility(View.GONE);
            } else {
                binding.cbWithdrawal.setTextColor(ContextCompat.getColor(getContext(), R.color.iconPrimary));
            }
        });
    }

    private void insertFeedback() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        Log.d("피드백", "피드백");
                    } else {
                        Log.d("피드백", "실패");
                    }
                } catch (JSONException e) {
                    Log.d("DB ERROR", "JSONExceptiopn" + e);
                    throw new RuntimeException(e);
                }
            }
        };
        feedback = getFeedback();
        FeedbackRequest request = new FeedbackRequest(feedback, listener);
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    private String getFeedback() {
        String feedback = binding.etWithdrawalFeedback.getText().toString();
        if (feedback.isEmpty()) {
            feedback = "없음";
        }
        Log.d("feedback", feedback);
        return feedback;
    }
    private void withdrawalApp() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        Toast.makeText(getActivity(), "탈퇴완료", Toast.LENGTH_SHORT).show();
                        Log.d("탈퇴", "탈퇴");
                        moveToLogin();
                    } else {
                        Toast.makeText(getActivity(), "탈퇴 실패", Toast.LENGTH_SHORT).show();
                        Log.d("탈퇴", "탈퇴 실패");
                    }
                } catch (JSONException e) {
                    Log.d("DB ERROR", "JSONException" + e);
                    throw new RuntimeException(e);
                }
            }
        };
        WithdrawalAppRequest request = new WithdrawalAppRequest(userID, listener);
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }


}
