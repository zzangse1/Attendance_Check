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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputLayout;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.SearchAddressActivity;
import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.databinding.FragmentMemberModifyBinding;
import com.zzangse.attendance_check.request.LoadGroupRequest;
import com.zzangse.attendance_check.request.ModifyMemberRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemberModifyFragment extends Fragment {
    private FragmentMemberModifyBinding binding;
    private int priNum;
    private String userID, groupName;
    private String modifyGroupName, modifyName, modifyNumber, modifyNumber2, modifyAddress, modifyAddress2, modifyMemo;
    private int COLOR_RED;
    private int COLOR_NAVY;
    private String[] groupNameArr;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    public static MemberModifyFragment newInstance(Bundle bundle) {
        MemberModifyFragment fragment = new MemberModifyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMemberModifyBinding.inflate(inflater);
        COLOR_RED = ContextCompat.getColor(requireContext(), R.color.red);
        COLOR_NAVY = ContextCompat.getColor(requireContext(), R.color.navy_300);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        moveToSearchAddress();
        getAddress();
        onClickBack();
        textWatcher();
        setSpinner();
        onClickSaveBtn();
      //  showAD();
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

    private void getBundle() {
        if (getArguments() != null) {
            userID = getArguments().getString("userID", userID);
            priNum = getArguments().getInt("priNum");
            groupName = getArguments().getString("groupName");
            String infoName = getArguments().getString("infoName");
            String infoPhoneNumber = getArguments().getString("infoPhoneNumber");
            String infoPhoneNumber2 = getArguments().getString("infoPhoneNumber2");
            String infoAddress = getArguments().getString("infoAddress");
            String infoAddress2 = getArguments().getString("infoAddress2");
            String infoMemo = getArguments().getString("infoMemo");
            setMemberInfo(groupName, infoName, infoPhoneNumber, infoPhoneNumber2, infoAddress, infoAddress2, infoMemo);
        }
    }

    private void setBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("priNum", priNum);
        bundle.putString("userID", userID);
        bundle.putString("groupName", modifyGroupName);
        bundle.putString("infoName", modifyName);
        bundle.putString("infoPhoneNumber", modifyNumber);
        bundle.putString("infoPhoneNumber2", modifyNumber2);
        bundle.putString("infoAddress", modifyAddress);
        bundle.putString("infoAddress2", modifyAddress2);
        bundle.putString("infoMemo", modifyMemo);

        moveToMemberViewFragment(bundle);
    }

    private void setMemberInfo(String groupName, String infoName, String infoPhoneNumber, String infoPhoneNumber2, String infoAddress, String infoAddress2, String infoMemo) {
        binding.etModifyGroupName.setText(groupName);
        binding.etModifyPersonName.setText(infoName);
        binding.etModifyPersonNumber.setText(infoPhoneNumber);
        binding.etModifyPersonNumber2.setText(infoPhoneNumber2);
        binding.tvModifyPersonAddress.setText(infoAddress);
        binding.etModifyPersonAddress2.setText(infoAddress2);
        binding.etModifyPersonMemo.setText(infoMemo);
    }

    private void onClickBack() {
        binding.toolbarMemberModify.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }


    private void textWatcher() {
        binding.etModifyPersonMemo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int memoLength = binding.etModifyPersonMemo.length();
                binding.etModifyPersonMemoLayout.setHint("메모(수정) ( " + memoLength + " / 200 )");
            }
        });
    }

    private void onClickSaveBtn() {
        binding.btnSave.setOnClickListener(v -> {
            boolean isSuccess = isCheckError();
            if (isSuccess) {
                updateMemberInfo();
                Toast.makeText(getActivity(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                setBundle();
                Log.d("저장버튼", "저장");
            }
        });
    }

    private void updateMemberInfo() {
        modifyGroupName = binding.etModifyGroupName.getText().toString();
        modifyName = binding.etModifyPersonName.getText().toString();
        modifyNumber = binding.etModifyPersonNumber.getText().toString();
        modifyNumber2 = binding.etModifyPersonNumber2.getText().toString();
        modifyAddress = binding.tvModifyPersonAddress.getText().toString();
        modifyAddress2 = binding.etModifyPersonAddress2.getText().toString();
        modifyMemo = binding.etModifyPersonMemo.getText().toString();
        ModifyMemberRequest request = new ModifyMemberRequest(priNum, modifyGroupName, modifyName,
                modifyNumber, modifyNumber2, modifyAddress, modifyAddress2, modifyMemo, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("name", s);
            }
        });
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }


    private boolean visibilityError(TextView errorView, TextInputLayout layout, EditText inputField, String errorMsg) {
        if (inputField.getText().toString().isEmpty()) {
            errorView.setText(errorMsg);
            layout.setBoxStrokeColor(COLOR_RED);
            errorView.setVisibility(View.VISIBLE);
            Log.d("error", inputField + errorMsg);
            return false;
        } else {
            layout.setBoxStrokeColor(COLOR_NAVY);
            errorView.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean checkAddress(String address) {
        if (address.isEmpty()){
            binding.tvErrorAddress.setVisibility(View.VISIBLE);
            Log.d("checkAddress", address+"");
            return false;
        } else {
            binding.tvErrorAddress.setVisibility(View.GONE);
            Log.d("checkAddress", address+"");
            return true;
        }

    }

    private boolean isCheckError() {
        boolean isNameValid = isValidName();
        boolean isNumberValid = isValidNumber();
        boolean isAddressValid = isValidAddress();
        return isNameValid && isNumberValid && isAddressValid;
    }

    private boolean isValidAddress() {
        return checkAddress(binding.tvModifyPersonAddress.getText().toString());
    }

    private boolean isValidNumber() {
        return visibilityError(binding.tvErrorNumber, binding.etModifyPersonNumberLayout, binding.etModifyPersonNumber, getResources().getString(R.string.error_msg_no_number));
    }

    private boolean isValidName() {
        return visibilityError(binding.tvErrorName, binding.etModifyPersonNameLayout, binding.etModifyPersonName, getResources().getString(R.string.error_msg_no_name));
    }

    private void moveToMemberViewFragment(Bundle bundle) {
        if (getActivity() instanceof SettingActivity) {
            Log.d("modify -> view", priNum + "");
            ((SettingActivity) getActivity()).onFragmentChanged(0, bundle);

        }
    }

    private void setSpinner() {
        getGroupName();

    }

    private void moveToSearchAddress() {
        binding.tvModifyPersonAddress.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchAddressActivity.class);
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
                                binding.tvModifyPersonAddress.setText(test);
                                Log.d("getAddress()", binding.tvModifyPersonAddress.getText().toString());
                            }
                        }
                    }
                });
    }

    private void getGroupName() {
        LoadGroupRequest loadGroupRequest = new LoadGroupRequest(getContext());
        loadGroupRequest.sendGroupOutputRequest(userID, new LoadGroupRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    groupNameArr = new String[result.length()];
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        String dbGroupName = jsonObject.getString("groupName");
                        Log.d("groupName", dbGroupName);
                        groupNameArr[i] = dbGroupName;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, groupNameArr);
                    binding.etModifyGroupName.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}
