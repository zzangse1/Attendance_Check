package com.zzangse.attendance_check.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.data.MemberInfo;
import com.zzangse.attendance_check.databinding.FragmentMemberInfoBinding;
import com.zzangse.attendance_check.request.LoadMemberViewRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemberInfoFragment extends Fragment {
    private FragmentMemberInfoBinding binding;
    private int priNum;
    private MemberInfo memberInfo;

    public static MemberInfoFragment newInstance(int priNum) {
        MemberInfoFragment fragment = new MemberInfoFragment();
        Bundle args = new Bundle();
        args.putInt("priNum", priNum);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            priNum = getArguments().getInt("priNum");
            Log.d("asd", priNum + "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMemberInfoBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickBack();
        onClickModifyBtn();
        loadMemberInfo(priNum);
    }


    private void onClickBack() {
        binding.toolbarMemberInfo.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    // 질문
    private void onClickModifyBtn() {
        binding.btnModify.setOnClickListener(v -> {
            moveToFragmentModify();
            // this::moveToFragmentModify);
        });

    }

    private void setMemberInfo(MemberInfo memberInfo) {
            binding.tvInfoGroupName.setText(memberInfo.getInfoGroupName());
            binding.tvInfoPersonName.setText(memberInfo.getInfoName());
            binding.tvInfoPersonNumber.setText(memberInfo.getInfoNumber());
            binding.tvInfoPersonNumber2.setText(memberInfo.getInfoNumber2());
            binding.tvInfoPersonAddress.setText(memberInfo.getInfoAddress());
            binding.tvInfoPersonMemo.setText(memberInfo.getInfoMemo());
    }

    private void loadMemberInfo(int priNum) {
        Log.d("asdf", priNum + "");
        LoadMemberViewRequest loadMemberViewRequest = new LoadMemberViewRequest(getContext());
        loadMemberViewRequest.loadMemberViewRequest(priNum, new LoadMemberViewRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        String groupName = jsonObject.getString("groupName");
                        String infoName = jsonObject.getString("infoName");
                        String infoPhoneNumber = jsonObject.getString("infoPhoneNumber");
                        String infoPhoneNumber2 = jsonObject.getString("infoPhoneNumber2");
                        String infoAddress = jsonObject.getString("infoAddress");
                        String infoMemo = jsonObject.getString("infoMemo");
                        Log.d("TEST", groupName + ", " + infoName);
                        memberInfo = new MemberInfo(groupName, infoName, infoPhoneNumber, infoPhoneNumber2, infoAddress, infoMemo);
                    }
                } catch (JSONException e) {
                    //throw new RuntimeException(e);
                    e.printStackTrace();
                }
                setMemberInfo(memberInfo);
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("TEST", errorMessage);
            }
        });
    }

    private void moveToFragmentModify() {//View v) {
        if (getActivity() instanceof SettingActivity) {
            Log.d("view -> info", priNum + "");
            ((SettingActivity) getActivity()).onFragmentChanged(2, priNum);
        }
    }
}
