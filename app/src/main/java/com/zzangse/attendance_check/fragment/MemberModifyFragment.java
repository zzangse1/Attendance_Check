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
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.databinding.FragmentMemberModifyBinding;
import com.zzangse.attendance_check.request.ModifyMemberRequest;

public class MemberModifyFragment extends Fragment {
    private FragmentMemberModifyBinding binding;
    private int priNum;
    private String userID, groupName;
    private String modifyGroupName, modifyName, modifyNumber, modifyNumber2, modifyAddress, modifyMemo;

    public static MemberModifyFragment newInstance(Bundle bundle) {
        MemberModifyFragment fragment = new MemberModifyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMemberModifyBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        onClickSaveBtn();
        onClickBack();
        textWatcher();
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
            String infoMemo = getArguments().getString("infoMemo");
            setMemberInfo(groupName, infoName, infoPhoneNumber, infoPhoneNumber2, infoAddress, infoMemo);
        }
    }

    private void setMemberInfo(String groupName, String infoName, String infoPhoneNumber, String infoPhoneNumber2, String infoAddress, String infoMemo) {
        binding.etModifyGroupName.setText(groupName);
        binding.etModifyPersonName.setText(infoName);
        binding.etModifyPersonNumber.setText(infoPhoneNumber);
        binding.etModifyPersonNumber2.setText(infoPhoneNumber2);
        binding.etModifyPersonAddress.setText(infoAddress);
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
                binding.etModifyPersonMemoLayout.setHint("메모(수정) ( " + memoLength + " / 300 )");
            }
        });
    }

    private void onClickSaveBtn() {
        binding.btnSave.setOnClickListener(v -> {
            updateMemberInfo();
            Toast.makeText(getActivity(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
            moveToMemberViewFragment();
        });
    }

    private void updateMemberInfo() {
        modifyGroupName = binding.etModifyGroupName.getText().toString();
        modifyName = binding.etModifyPersonName.getText().toString();
        modifyNumber = binding.etModifyPersonNumber.getText().toString();
        modifyNumber2 = binding.etModifyPersonNumber2.getText().toString();
        modifyAddress = binding.etModifyPersonAddress.getText().toString();
        modifyMemo = binding.etModifyPersonMemo.getText().toString();
        ModifyMemberRequest request = new ModifyMemberRequest(priNum, modifyGroupName, modifyName,
                modifyNumber, modifyNumber2, modifyAddress, modifyMemo, new Response.Listener<String>() {
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

    private void moveToMemberViewFragment() {
        if (getActivity() instanceof SettingActivity) {
            Log.d("modify -> view", priNum + "");
            Bundle bundle = new Bundle();
            bundle.putString("groupName", groupName);
            bundle.putString("userID", userID);
            ((SettingActivity) getActivity()).onFragmentChanged(3, bundle);

        }
    }
}
