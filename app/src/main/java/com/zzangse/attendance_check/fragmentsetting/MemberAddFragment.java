package com.zzangse.attendance_check.fragmentsetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.SearchAddressActivity;
import com.zzangse.attendance_check.databinding.FragmentMemberAddBinding;
import com.zzangse.attendance_check.request.InsertMemberRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MemberAddFragment extends Fragment {
    private FragmentMemberAddBinding binding;
    private String groupName;
    private String userID;
    private int COLOR_RED;
    private int COLOR_NAVY;
    private ActivityResultLauncher<Intent> activityResultLauncher;

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
        COLOR_NAVY = ContextCompat.getColor(requireContext(), R.color.navy_300);
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
        getAddress();
        moveToSearchAddress();
       // showAD();
    }

    private void showAD() {
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        AdRequest request = new AdRequest.Builder().build();
        binding.adView.loadAd(request);
    }

    private boolean checkName(String name) {
        if (name.isEmpty()) {
            binding.etMemberAddNameLayout.setBoxStrokeColor(COLOR_RED);
            binding.tvErrorName.setText(R.string.error_msg_no_name);
            binding.tvErrorName.setVisibility(View.VISIBLE);
            return false;
        } else if (name.length() > 10) {
            binding.etMemberAddNameLayout.setBoxStrokeColor(COLOR_RED);
            binding.tvErrorName.setText(R.string.error_msg_over_name);
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
        if (number.isEmpty()) {
            binding.etMemberAddNumberLayout.setBoxStrokeColor(COLOR_RED);
            binding.tvErrorNumber.setVisibility(View.VISIBLE);
            return false;
        } else {
            binding.etMemberAddNumberLayout.setBoxStrokeColor(COLOR_NAVY);
            binding.tvErrorNumber.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean checkAddress(String address) {
        if (address.isEmpty()){
            binding.tvErrorAddress.setVisibility(View.VISIBLE);
            return false;
        } else {
            binding.tvErrorAddress.setVisibility(View.GONE);
            return true;
        }

    }

    private String checkAddress2(String address2) {
        if (address2.isEmpty()) {
            return "";
        } else {
            return address2;
        }
    }

    private void onClickSaveBtn() {
        binding.btnSave.setOnClickListener(v -> {
            Log.d("저장버튼누름", "저장버튼누름");
            getMemberInfo();
        });
    }

    private void getMemberInfo() {
        String name = binding.etMemberAddName.getText().toString();
        String number = binding.etMemberAddNumber.getText().toString();
        String number2 = binding.etMemberAddNumber2.getText().toString();
        String address = binding.tvAddress.getText().toString();
        String address2 = binding.etMemberAddAddress2.getText().toString();
        String memo = binding.etMemberAddMemo.getText().toString();

        boolean isCheckName = checkName(name);
        boolean isCheckNumber = checkNumber(number);
        boolean isCheckAddress = checkAddress(address);
        String isCheckAddress2 = checkAddress2(address2);
        isValidMemberInfo(name, number, number2, address, isCheckAddress2, memo,
                isCheckName, isCheckNumber, isCheckAddress);
    }

    private void isValidMemberInfo(String name, String number, String number2, String address, String address2, String memo,
                                   boolean isCheckName, boolean isCheckNumber, boolean isCheckAddress) {
        if (isCheckName && isCheckNumber && isCheckAddress) {
            insertAddMember(name, number, number2, address, address2, memo);
        } else {
            Log.d("멤버추가 공백 확인 이름", name + ", " + name.length());
            Log.d("멤버추가 공백 확인 번호", number + ", " + number.length());
            Log.d("멤버추가 공백 확인 비상연락망", number2 + ", " + number2.length());
            Log.d("멤버추가 공백 주소", address + ", " + address.length());
            Log.d("멤버추가 공백 상세주소", address2 + ", " + address.length());
            Log.d("멤버추가 공백 메모", memo + ", " + memo.length());
        }
    }

    private void setInfo() {
        groupName = getArguments().getString("groupName");
        userID = getArguments().getString("userID");
    }

    private void insertAddMember(String name, String number, String number2, String address, String address2, String memo) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        Toast.makeText(getActivity(), "[ "+name+" ] 추가", Toast.LENGTH_SHORT).show();
                        Log.d("DB접속 ", "저장완료");
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), "추가 실패", Toast.LENGTH_SHORT).show();
                        Log.d("DB접속 ", "저장실패");
                    }
                } catch (JSONException e) {
                    Log.e("DB에러", "JSON예외 발생: " + e);
                    throw new RuntimeException(e);
                }
            }
        };
        InsertMemberRequest request = new InsertMemberRequest(userID, groupName, name, number, number2,
                address, address2, memo, listener);
        if (getActivity() != null) {
            Log.d("저장버튼", "userID: " + userID);
            Log.d("저장버튼", "groupName: " + groupName);
            Log.d("저장버튼", "name: " + name);
            Log.d("저장버튼", "number: " + number);
            Log.d("저장버튼", "number2: " + number2);
            Log.d("저장버튼", "address: " + address);
            Log.d("저장버튼", "address2: " + address2);
            Log.d("저장버튼", "memo: " + memo);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    private void onClickBack() {
        binding.toolbarMemberAdd.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void moveToSearchAddress() {
        binding.tvAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchAddressActivity.class);
            activityResultLauncher.launch(intent);

        });
    }

    private void getAddress() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String test = data.getStringExtra("data");
                            if (test != null) {
                                Log.d("test", test);
                                binding.tvAddress.setText(test);
                            }
                        }
                    }
                });
    }
}
