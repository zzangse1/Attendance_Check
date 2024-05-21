package com.zzangse.attendance_check.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.FragmentMemberAddBinding;
import com.zzangse.attendance_check.request.InsertMemberRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MemberAddFragment extends Fragment {
    private FragmentMemberAddBinding binding;
    private String groupName;
    private String userID;
    private static final String REGEX_PHONE_NUMBER = "^\\d{3}-?\\d{3,4}-?\\d{4}$";
    private int COLOR_RED;
    private int COLOR_NAVY;

    public static MemberAddFragment newInstance(Bundle bundle) {
        MemberAddFragment fragment = new MemberAddFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMemberAddBinding.inflate(inflater);
        COLOR_RED = ContextCompat.getColor(requireContext(), R.color.red);
        COLOR_NAVY = ContextCompat.getColor(requireContext(), R.color.navy_200);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 구현
        onClickBack();
        setInfo();
        onClickSaveBtn();
        textWatcherMemo();
    }


    private boolean checkName(String name) {
        if (name.isEmpty()) {
            binding.etMemberAddNameLayout.setBoxStrokeColor(COLOR_RED);
            binding.tvErrorName.setVisibility(View.VISIBLE);
            return false;
        } else {
            binding.etMemberAddNameLayout.setBoxStrokeColor(COLOR_NAVY);
            binding.tvErrorName.setVisibility(View.GONE);
            return true;
        }
    }

    // memo 300자 체크
    private void textWatcherMemo() {
        binding.etMemberAddMemo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int memoLength = binding.etMemberAddMemo.length();
                binding.etMemberAddMemoLayout.setHint("메모( " + memoLength + " / 300 )");
            }
        });
    }

    private boolean checkNumber(String number) {
        boolean isNumber = number.matches(REGEX_PHONE_NUMBER);
        if (number.isEmpty() || !isNumber) {
            binding.etMemberAddNumberLayout.setBoxStrokeColor(COLOR_RED);
            binding.tvErrorNumber.setVisibility(View.VISIBLE);
            return false;
        } else {
            binding.etMemberAddNumberLayout.setBoxStrokeColor(COLOR_NAVY);
            binding.tvErrorNumber.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean checkNumber2(String number2) {
        boolean isNumber = number2.matches(REGEX_PHONE_NUMBER);
        if (number2.isEmpty() || !isNumber) {
            binding.etMemberAddNumber2Layout.setBoxStrokeColor(COLOR_RED);
            binding.tvErrorNumber2.setVisibility(View.VISIBLE);
            return false;
        } else {
            binding.etMemberAddNumber2Layout.setBoxStrokeColor(COLOR_NAVY);
            binding.tvErrorNumber2.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean checkAddress(String address) {
        if (address.isEmpty()) {
            binding.etMemberAddAddressLayout.setBoxStrokeColor(COLOR_RED);
            binding.tvErrorAddress.setVisibility(View.VISIBLE);
            return false;
        } else {
            binding.etMemberAddAddressLayout.setBoxStrokeColor(COLOR_NAVY);
            binding.tvErrorAddress.setVisibility(View.GONE);
            return true;
        }

    }

    private void onClickSaveBtn() {
        binding.btnSave.setOnClickListener(v -> {
            getMemberInfo();
        });
    }

    private void getMemberInfo() {
        String name = binding.etMemberAddName.getText().toString();
        String number = binding.etMemberAddNumber.getText().toString();
        String number2 = binding.etMemberAddNumber2.getText().toString();
        String address = binding.etMemberAddAddress.getText().toString();
        String memo = binding.etMemberAddMemo.getText().toString();
        boolean isCheckName = checkName(name);
        boolean isCheckNumber = checkNumber(number);
        boolean isCheckNumber2 = checkNumber2(number2);
        boolean isCheckAddress = checkAddress(address);
        isValidMemberInfo(name, number, number2, address, memo, isCheckName, isCheckNumber, isCheckNumber2, isCheckAddress);
    }

    private void isValidMemberInfo(String name, String number, String number2, String address, String memo,
                                   boolean isCheckName, boolean isCheckNumber, boolean isCheckNumber2, boolean isCheckAddress) {
        if (isCheckName && isCheckNumber && isCheckNumber2 && isCheckAddress) {
            insertAddMember(name, number, number2, address, memo);
        } else {
            Log.d("멤버추가 공백 확인", "null");
        }
    }

    private void setInfo() {
        groupName = getArguments().getString("groupName");
        userID = getArguments().getString("userID");
    }

    private void insertAddMember(String name, String number, String number2, String address, String memo) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        Toast.makeText(getActivity(), "DB ok", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), "DB false", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        InsertMemberRequest request = new InsertMemberRequest(userID, groupName, name, number, number2,
                address, memo, listener);
        if (getActivity() != null) {
            Log.d("add", userID + groupName + name + number + number2 + address + memo);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    private void onClickBack() {
        binding.toolbarMemberAdd.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

}
