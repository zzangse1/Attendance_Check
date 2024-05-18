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
import com.zzangse.attendance_check.data.MemberInfo;
import com.zzangse.attendance_check.databinding.FragmentMemberModifyBinding;
import com.zzangse.attendance_check.request.LoadMemberViewRequest;
import com.zzangse.attendance_check.request.ModifyMemberRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemberModifyFragment extends Fragment {
    private FragmentMemberModifyBinding binding;
    private int priNum;
    private MemberInfo memberInfo;
    private String modifyGroupName, modifyName, modifyNumber, modifyNumber2, modifyAddress, modifyMemo;

    public static MemberModifyFragment newInstance(int priNum) {
        MemberModifyFragment fragment = new MemberModifyFragment();
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
        }
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
        onClickSaveBtn();
        onClickBack();
        loadMemberData();
        textWatcher();

        //  test();
    }

    private void getMemberInfo() {
        binding.tvPri.setText(priNum + "");
        String newGroupName = binding.etModifyGroupName.getText().toString();
        // newName = binding.etModifyPersonName.getText().toString();
        String newPhoneNumber = binding.etModifyPersonNumber.getText().toString();
        String newPhoneNumber2 = binding.etModifyPersonNumber2.getText().toString();
        String newAddress = binding.etModifyPersonAddress.getText().toString();
        String newMemo = binding.etModifyPersonMemo.getText().toString();
    }

    private void onClickBack() {
        binding.toolbarMemberModify.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void setMemberInfo(MemberInfo memberInfo) {
        binding.etModifyGroupName.setText(memberInfo.getInfoGroupName());
        binding.etModifyPersonName.setText(memberInfo.getInfoName());
        binding.etModifyPersonNumber.setText(memberInfo.getInfoNumber());
        binding.etModifyPersonNumber2.setText(memberInfo.getInfoNumber2());
        binding.etModifyPersonAddress.setText(memberInfo.getInfoAddress());
        binding.etModifyPersonMemo.setText(memberInfo.getInfoMemo());
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

    private void loadMemberData() {
        LoadMemberViewRequest loadMemberViewRequest = new LoadMemberViewRequest(getContext());
        loadMemberViewRequest.loadMemberViewRequest(priNum, new LoadMemberViewRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                for (int i = 0; i < result.length(); i++) {
                    try {
                        JSONObject jsonObject = result.getJSONObject(i);
                        String groupName = jsonObject.getString("groupName");
                        String infoName = jsonObject.getString("infoName");
                        String infoPhoneNumber = jsonObject.getString("infoPhoneNumber");
                        String infoPhoneNumber2 = jsonObject.getString("infoPhoneNumber2");
                        String infoAddress = jsonObject.getString("infoAddress");
                        String infoMemo = jsonObject.getString("infoMemo");
                        memberInfo = new MemberInfo(groupName, infoName, infoPhoneNumber, infoPhoneNumber2, infoAddress, infoMemo);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    setMemberInfo(memberInfo);
                }
            }

            @Override
            public void onError(String errorMessage) {

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

//    private void test() {
//        getParentFragmentManager().setFragmentResultListener("memberInfo", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                String groupName = result.getString("groupName");
//                String infoName = result.getString("infoName");
//                String infoPhoneNumber = result.getString("infoPhoneNumber");
//                String infoPhoneNumber2 = result.getString("infoPhoneNumber2");
//                String infoAddress = result.getString("infoAddress");
//                String infoMemo = result.getString("infoMemo");
//                Log.d("AAA", groupName);
//                Log.d("AAA", infoName);
//                Log.d("AAA", infoPhoneNumber);
//                Log.d("AAA", infoPhoneNumber2);
//                Log.d("AAA", infoAddress);
//                Log.d("AAA", infoMemo);
//            }
//        });
//    }

    private void moveToMemberViewFragment() {
        if (getActivity() instanceof SettingActivity) {
            Log.d("modify -> view", priNum + "");
            ((SettingActivity) getActivity()).onFragmentChanged(3, 0);

        }
    }
}
