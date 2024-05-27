package com.zzangse.attendance_check.fragmentmain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import com.zzangse.attendance_check.request.InsertCheckRequest;
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
    private String today, choiceDay;
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

    private void showSheet(int priNum, String infoName, String infoNumber) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext());
        sheetDialog.setContentView(R.layout.dialog_bottom_sheet);
        TextView tv_infoName = sheetDialog.findViewById(R.id.tv_bottom_name);
        TextView tv_infoNumber = sheetDialog.findViewById(R.id.tv_bottom_number);
        Button btn_1 = sheetDialog.findViewById(R.id.btn_bottom_check_present);

        btn_1.setOnClickListener(v->{
            String aaa = "출석";
            insertCheckDB(priNum, aaa, today);
            Log.d("저장되었습니다.", "priNum" + priNum + "출석: " + aaa + "날짜" + today);
            Toast.makeText(getContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
            sheetDialog.dismiss();
        });
        tv_infoName.setText(infoName);
        tv_infoNumber.setText(infoNumber);

        String number = tv_infoNumber.getText().toString();
        tv_infoNumber.setOnClickListener(v -> {
            showDeleteDialog(number);
        });
        sheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
              //  dialog.dismiss();
            }
        });
        sheetDialog.setCanceledOnTouchOutside(true);
        sheetDialog.create();
        sheetDialog.show();
    }

    private void moveToCall(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void showDeleteDialog(String number) {
        String dialogTitle = "전화 걸기";
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(), R.style.RoundedDialog);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete, null);
        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvTarget = dialogView.findViewById(R.id.tv_delete_target);
        TextView tvLabel = dialogView.findViewById(R.id.tv_delete_label);
        TextView btnOk = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);

        tvTarget.setText("전화창으로 이동합니다.");
        tvLabel.setVisibility(View.GONE);
        btnOk.setText("이동");
        tvTitle.setText(dialogTitle);

        AlertDialog dialog = builder.setView(dialogView)
                .setCancelable(false)
                .create();
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        btnOk.setOnClickListener(v -> {
            moveToCall(number);
            dialog.dismiss();
        });
        dialog.show();
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
        Log.d("리싸이클러 ", "init");
        RecyclerView recyclerView = binding.rvCheck;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        memberAdapter = new CheckMemberNameAdapter(getContext(), memberInfoList);
        recyclerView.setAdapter(memberAdapter);
        memberAdapter.setOnClick(new CheckMemberNameAdapter.CheckMemberNameAdapterClick() {
            @Override
            public void onClickInfo(MemberInfo memberInfo) {
                Log.d("클릭 멤버 이름:", memberInfo.getInfoName());
                Log.d("클릭 멤버 기본키: ", memberInfo.getPriNum() + "");
                Toast.makeText(getActivity(), memberInfo.getInfoName(), Toast.LENGTH_SHORT).show();
                showSheet(memberInfo.getPriNum(), memberInfo.getInfoName(), memberInfo.getInfoNumber());
            }
        });
    }

    private void setCalendar() {
        simpleDateFormat = new SimpleDateFormat("yy년 M월 dd일");
        today = simpleDateFormat.format(date);
        binding.tvDate.setText(today);
        Log.d("오늘 날짜", today);
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
                    choiceDay = simpleDateFormat.format(date);
                    binding.tvDate.setText(choiceDay);
                    Log.d("선택 날짜", choiceDay);
                }
            });
        });

    }

    private void insertCheckDB(int priNum, String infoCheck, String infoDate) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        Toast.makeText(getActivity(), "DB ok", Toast.LENGTH_SHORT).show();
                        Log.d("DB접속 ", "저장완료");
                    } else {
                        Toast.makeText(getActivity(), "DB false", Toast.LENGTH_SHORT).show();
                        Log.d("DB접속 ", "저장실패");
                    }
                } catch (JSONException e) {
                    Log.e("DB에러", "JSON예외 발생: " + e);
                    throw new RuntimeException(e);
                }
            }
        };
       // InsertCheckRequest request = new InsertCheckRequest(priNum, infoCheck, infoDate, listener);
        InsertCheckRequest request = new InsertCheckRequest(priNum, infoCheck, listener);
        if (getActivity() != null) {
            Log.d("저장버튼", "priNum: " + priNum);
            Log.d("저장버튼", "infoCheck: " + infoCheck);
            Log.d("저장버튼", "infoDate: " + infoDate);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
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
                        // priNum도 가져와야함
                        int priNum = jsonObject.getInt("priNum");
                        String dbInfoName = jsonObject.getString("infoName");
                        String dbInfoNumber = jsonObject.getString("infoPhoneNumber");
                        Log.d("memberName", i + "번째: " + dbInfoName);
                        Log.d("dbInfoNumber", i + "번째: " + dbInfoNumber);
                        Log.d("priNum", i + "번째: " + priNum);
                        // memberInfo.setInfoName(dbMemberName);
                        memberInfo = new MemberInfo(priNum, dbInfoName, dbInfoNumber);
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
