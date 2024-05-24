package com.zzangse.attendance_check.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.adapter.CheckGroupNameAdapter;
import com.zzangse.attendance_check.adapter.CheckMemberNameAdapter;
import com.zzangse.attendance_check.data.GroupName;
import com.zzangse.attendance_check.data.GroupViewModel;
import com.zzangse.attendance_check.data.MemberInfo;
import com.zzangse.attendance_check.databinding.FragmentCheckBinding;
import com.zzangse.attendance_check.request.LoadGroupRequest;
import com.zzangse.attendance_check.request.LoadMemberRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CheckFragment extends Fragment {
    private GroupViewModel groupViewModel;
    private FragmentCheckBinding binding;
    private Calendar calendar;
    private Date date = new Date();
    private String userID;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy년 M월 dd일");
    private ArrayList<GroupName> groupNameList = new ArrayList<>();
    private ArrayList<GroupName> filterList = new ArrayList<>();
    private ArrayList<MemberInfo> memberInfoList = new ArrayList<>();
    private CheckMemberNameAdapter memberAdapter;
    private GroupName groupName;
    private MemberInfo memberInfo;
    private String choiceGroupName;
    private boolean viewModel = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        binding = FragmentCheckBinding.inflate(inflater);
        getArgs();
        getViewModel();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCalendar();
        onClickDate();
        initScreen();
        loadGroupNameDB();
        onClickGroupName();
    }


    private void getViewModel() {
        groupViewModel = new ViewModelProvider(requireActivity()).get(GroupViewModel.class);
        groupViewModel.getGroupName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.isEmpty()) {
                    Log.d("viewModel 작동", "그룹이룹: " + s);
                    binding.tvGroupName.setText(s);
                    loadMemberNameDB();
                    isNullMemberInfoList(memberInfoList);
                    initRecycler();
                } else {
                    Log.d("viewModel 비작동", "그룹이룹: " + s);
                }
            }
        });
    }

    private void isNullMemberInfoList(ArrayList<MemberInfo> memberInfoList) {
        if (memberInfoList.isEmpty()) {
            if (binding.tvGroupName.getText().equals("그룹 이름(수정)")) {
                binding.tvMemberNull.setText("그룹을 선택해주세요.");
                binding.tvMemberNull.setVisibility(View.VISIBLE);
            } else {
                binding.tvMemberNull.setText("멤버가 없습니다.");
                binding.tvMemberNull.setVisibility(View.VISIBLE);
            }
        } else {
            binding.tvMemberNull.setVisibility(View.GONE);
        }
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            userID = args.getString("userID");
            Log.d("테스트아이디", userID);
        } else {
            Log.d("테스트아이디", "못가져옴");
        }
    }


    private void initScreen() {
        if (!viewModel) {
            binding.tvMemberNull.setText("그룹을 선택해주세요");
            binding.tvMemberNull.setVisibility(View.VISIBLE);
        }
    }

    private void initRecycler() {
        RecyclerView recyclerView = binding.rvCheck;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        memberAdapter = new CheckMemberNameAdapter(getContext(), memberInfoList);
        recyclerView.setAdapter(memberAdapter);
        memberAdapter.setOnClick(new CheckMemberNameAdapter.CheckMemberNameAdapterClick() {
            @Override
            public void onClickInfo(MemberInfo memberInfo) {
                Log.d("memberInfo", memberInfo.getInfoName());
            }
        });
    }

    private void setCalendar() {
        simpleDateFormat = new SimpleDateFormat("yy년 M월 dd일");
        String today = simpleDateFormat.format(date);
        binding.tvDate.setText(today);
    }

    private void onClickDate() {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        // today
        Long today = MaterialDatePicker.todayInUtcMilliseconds();
        binding.tvDate.setOnClickListener(v -> {
            MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("")
                    .setSelection(today).build();

            datePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    simpleDateFormat = new SimpleDateFormat("yy년 M월 d일");
                    date.setTime(selection);
                    String dateString = simpleDateFormat.format(date);
                    binding.tvDate.setText(dateString);

                }
            });
        });

    }

    private void loadMemberNameDB() {
        LoadMemberRequest loadMemberRequest = new LoadMemberRequest(getContext());
        loadMemberRequest.sendMemberOutputRequest(userID, choiceGroupName, new LoadMemberRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    memberInfoList.clear();
                    for (int i = 0; i < result.length(); i++) {
                        //  memberInfo = new MemberInfo();
                        JSONObject jsonObject = result.getJSONObject(i);
                        String dbMemberName = jsonObject.getString("infoName");
                        Log.d("memberName", i + "번째: " + dbMemberName);
                        // memberInfo.setInfoName(dbMemberName);
                        memberInfo = new MemberInfo(dbMemberName);
                        memberInfoList.add(memberInfo);
                    }
                    memberAdapter.notifyDataSetChanged();
                    isNullMemberInfoList(memberInfoList);
//                    if (memberInfoList.isEmpty()) {
//                        Log.d("memberInfoList Null 체크", "memberInfoList isNull");
//                        binding.tvMemberNull.setText("멤버가 없습니다.");
//                        binding.tvMemberNull.setVisibility(View.VISIBLE);
//                    } else {
//                        binding.tvMemberNull.setVisibility(View.GONE);
//                        Log.d("memberInfoList Null 체크", "memberInfoList NotNull");
//                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void loadGroupNameDB() {
        LoadGroupRequest loadGroupRequest = new LoadGroupRequest(getContext());
        loadGroupRequest.sendGroupOutputRequest(userID, new LoadGroupRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    groupNameList.clear();
                    for (int i = 0; i < result.length(); i++) {
                        groupName = new GroupName();
                        JSONObject jsonObject = result.getJSONObject(i);
                        String dbGroupName = jsonObject.getString("groupName");
                        groupName.setGroupName(dbGroupName);
                        groupNameList.add(groupName);
                    }
                    // adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("userData", "error " + errorMessage);
            }
        });
    }

    private void onClickGroupName() {
        binding.tvGroupName.setOnClickListener(v -> {
            showGroupNameDialog();
        });
    }

    private void showGroupNameDialog() {
        loadGroupNameDB();
        boolean isGroup = false;
        for (GroupName str : groupNameList) {
            Log.d("확인", str.getGroupName());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundedDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        TextView btnOk = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);
        TextInputEditText textInputEditText = dialogView.findViewById(R.id.et_group_name);
        TextView tvError = dialogView.findViewById(R.id.tv_error);
        TextView tvError2 = dialogView.findViewById(R.id.tv_error2);

        RecyclerView innerRecyclerView = dialogView.findViewById(R.id.rv_group_name_list);
        innerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CheckGroupNameAdapter innerAdapter = new CheckGroupNameAdapter(getContext(), groupNameList);
        innerRecyclerView.setAdapter(innerAdapter);


        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 리싸이클러뷰 필터링
                String searchText = textInputEditText.getText().toString();
                filterList.clear();
                for (int i = 0; i < groupNameList.size(); i++) {
                    if (groupNameList.get(i).getGroupName().toLowerCase().contains(searchText.toLowerCase())) {
                        filterList.add(groupNameList.get(i));
                    }
                    innerAdapter.listFilter(filterList);
                    // 리싸이클러뷰가 비어있으면 경고 문구
                    if (innerAdapter.listNull(filterList)) {
                        tvError.setText("\" " + searchText + " \" 그룹이 없습니다.");
                        tvError.setVisibility(View.VISIBLE);
                        tvError2.setVisibility(View.VISIBLE);
                        //isGroup = false;
                    } else {
                        tvError.setVisibility(View.GONE);
                        tvError2.setVisibility(View.GONE);
                        //     isGroup = true;
                    }
                }
            }
        });

        innerAdapter.setOnClick(new CheckGroupNameAdapter.GroupNameAdapterClick() {
            // 리싸이클러뷰 아이템 클릭 이벤트
            @Override
            public void onClickInfo(GroupName groupName) {
                choiceGroupName = groupName.getGroupName();
                textInputEditText.setText(choiceGroupName); // editText에 그룹이름 삽입
                textInputEditText.setSelection(choiceGroupName.length());    // 커서 위치 맨 뒤로
            }
        });

        AlertDialog dialog = builder.setView(dialogView)
                .setCancelable(false)
                .create();
        // 취소 버튼 이벤트
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // 선택 버튼 이벤트
        btnOk.setOnClickListener(v -> {
            boolean isGroupName = textInputEditText.getText().toString().isEmpty();
            if (isGroupName || filterList.isEmpty()) {
                Toast.makeText(getContext(), "그룹 이름을 확인해주세요..", Toast.LENGTH_SHORT).show();
            } else if (textInputEditText.getText().toString().equals(filterList.toString())) {
                Toast.makeText(getContext(), "same.", Toast.LENGTH_SHORT).show();
            } else {
                viewModel = true;
                choiceGroupName = textInputEditText.getText().toString();
                binding.tvGroupName.setText(choiceGroupName); //checkFragment의 textView에 선택한 그룹이름 넣어줌
                groupViewModel.setGroupName(choiceGroupName);
                loadMemberNameDB();
                initRecycler();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
