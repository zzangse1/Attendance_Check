package com.zzangse.attendance_check.fragmentmain;

import android.app.AlertDialog;
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
import com.zzangse.attendance_check.data.DateViewModel;
import com.zzangse.attendance_check.data.GroupName;
import com.zzangse.attendance_check.data.GroupViewModel;
import com.zzangse.attendance_check.data.MemberInfo;
import com.zzangse.attendance_check.databinding.FragmentCheckBinding;
import com.zzangse.attendance_check.request.InsertCheckRequest;
import com.zzangse.attendance_check.request.LoadGroupRequest;
import com.zzangse.attendance_check.request.LoadMemberCheckPastRequest;
import com.zzangse.attendance_check.request.LoadMemberCheckRequest;
import com.zzangse.attendance_check.request.LoadMemberRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CheckFragment extends Fragment {
    private GroupViewModel groupViewModel;
    private DateViewModel dateViewModel;
    private FragmentCheckBinding binding;
    private Calendar calendar;
    private Date date = new Date();
    private String userID;
    private SimpleDateFormat simpleDateFormat;
    private ArrayList<GroupName> groupNameList = new ArrayList<>();
    private ArrayList<GroupName> filterList = new ArrayList<>();
    private ArrayList<MemberInfo> memberInfoList = new ArrayList<>();
    private CheckMemberNameAdapter memberAdapter;
    private GroupName groupName;
    private MemberInfo memberInfo;
    private String choiceGroupName;
    private String today, choiceDay;
    java.sql.Date sqlDate;
    private boolean isInitViewModel = false;
    private long selectedDateInMillis = -1; // 사용자가 선택한 날짜를 저장할 변수


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        binding = FragmentCheckBinding.inflate(inflater);
        getArgs();
        initViewModel();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCalendar();
        onClickDate();
        initScreen();
        loadGroupNameDB();
        onClickDateBtn();
        onClickGroupName();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isInitViewModel) {
            loadGroupNameDB();
            loadMemberNameDB();
        }
        if (selectedDateInMillis != -1) {
            calendar.setTimeInMillis(selectedDateInMillis);
            sqlDate = new java.sql.Date(selectedDateInMillis);
            choiceDay = simpleDateFormat.format(sqlDate);
            binding.tvDate.setText(choiceDay);
            loadPastMemberCheckDB();
        }
    }

    private void updateAttendanceCount() {
        int presentCount = 0, tardyCount = 0, absentCount = 0;
        for (MemberInfo member : memberInfoList) {
            if (member.getInfoCheck().equals("출석")) {
                presentCount++;
            } else if (member.getInfoCheck().equals("지각")) {
                tardyCount++;
            } else if (member.getInfoCheck().equals("결석")) {
                absentCount++;
            }
        }
        binding.tvGreen.setText(String.valueOf(presentCount));
        binding.tvYellow.setText(String.valueOf(tardyCount));
        binding.tvRed.setText(String.valueOf(absentCount));
    }

    private void onClickDateBtn() {
        binding.btnLeft.setOnClickListener(v -> {
            updateDateBy(-1);
        });

        binding.btnRight.setOnClickListener(v -> {
            updateDateBy(1);

        });
    }


    private void updateDateBy(int days) {
        if (selectedDateInMillis == -1) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            calendar.setTimeInMillis(selectedDateInMillis);
        }
        calendar.add(Calendar.DAY_OF_YEAR, days);
        selectedDateInMillis = calendar.getTimeInMillis();

        dateViewModel.setSelectedDate(selectedDateInMillis);

        sqlDate = new java.sql.Date(selectedDateInMillis);
        choiceDay = simpleDateFormat.format(sqlDate);
        binding.tvDate.setText(choiceDay);

        loadPastMemberCheckDB();
        Log.d("오늘날짜", today);
    }

    private void showSheet(int priNum, String infoName, String infoNumber) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext());
        sheetDialog.setContentView(R.layout.dialog_bottom_sheet);
        TextView tv_infoName = sheetDialog.findViewById(R.id.tv_bottom_name);
        TextView tv_infoNumber = sheetDialog.findViewById(R.id.tv_bottom_number);
        Button btnPresent = sheetDialog.findViewById(R.id.btn_bottom_check_present);
        Button btnTardy = sheetDialog.findViewById(R.id.btn_bottom_check_tardy);
        Button btnAbsent = sheetDialog.findViewById(R.id.btn_bottom_check_absent);
        Button btnCancel = sheetDialog.findViewById(R.id.btn_bottom_check_cancel);

        View.OnClickListener onClickListener = v -> {
            String check = ((Button) v).getText().toString();
            insertCheckDB(priNum, check, sqlDate, new DBCallback() {
                @Override
                public void onSuccess() {
                    Log.d("check", check + "실행");
                    loadMemberCheckDB(priNum, sqlDate);
                    sheetDialog.dismiss();
                }

                @Override
                public void onError(String errorMessage) {
                    // 에러 처리
                    Log.d("onError", "Failed to update status");
                }
            });
        };
        btnPresent.setOnClickListener(onClickListener);
        btnTardy.setOnClickListener(onClickListener);
        btnAbsent.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);

        tv_infoName.setText(infoName);
        tv_infoNumber.setText(infoNumber);

        tv_infoNumber.setOnClickListener(v -> showDeleteDialog(infoNumber));

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


    private void initViewModel() {
        groupViewModel = new ViewModelProvider(requireActivity()).get(GroupViewModel.class);
        dateViewModel = new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        groupViewModel.getGroupName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.isEmpty()) {
                    binding.tvGroupName.setText(s);
                    isInitViewModel = true;
                    loadMemberNameDB();
//                    loadPastMemberCheckDB();
                    initRecycler();
                }
            }
        });
        dateViewModel.getSelectedDate().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long dateInMillis) {
                if (dateInMillis != null) {
                    isInitViewModel = true;
                    selectedDateInMillis = dateInMillis;
                    Date selectedDate = new Date(dateInMillis);
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    choiceDay = simpleDateFormat.format(selectedDate);
                    binding.tvDate.setText(choiceDay);
                    sqlDate = new java.sql.Date(selectedDate.getTime());
                    //loadPastMemberCheckDB();
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
        }
    }


    private void initScreen() {
        if (!isInitViewModel) {
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
                //Toast.makeText(getActivity(), memberInfo.getInfoName(), Toast.LENGTH_SHORT).show();
                showSheet(memberInfo.getPriNum(), memberInfo.getInfoName(), memberInfo.getInfoNumber());
            }
        });
    }

    private void setCalendar() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        today = simpleDateFormat.format(new Date());
        binding.tvDate.setText(today);
        try {
            Date parsedDate = simpleDateFormat.parse(today);
            if (parsedDate != null) {
                sqlDate = new java.sql.Date(parsedDate.getTime());
                selectedDateInMillis = parsedDate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void onClickDate() {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Long todayInMillis = MaterialDatePicker.todayInUtcMilliseconds();
        binding.tvDate.setOnClickListener(v -> {
            Long initialSelection = (selectedDateInMillis != -1) ? selectedDateInMillis : todayInMillis;

            MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("")
                    .setSelection(initialSelection)
                    .build();

            datePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    dateViewModel.setSelectedDate(selection);
                    selectedDateInMillis = selection;
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date selectedDate = new Date(selection);
                    choiceDay = simpleDateFormat.format(selectedDate);
                    binding.tvDate.setText(choiceDay);
                    sqlDate = new java.sql.Date(selectedDate.getTime());
                    loadPastMemberCheckDB();
                }
            });
        });

    }


    private void insertCheckDB(int priNum, String infoCheck, java.sql.Date infoDate, DBCallback dbCallback) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                       // Toast.makeText(getActivity(), "DB ok", Toast.LENGTH_SHORT).show();
                        dbCallback.onSuccess();
                    } else {
                        //Toast.makeText(getActivity(), "DB false", Toast.LENGTH_SHORT).show();
                        dbCallback.onError("failed to insert");
                    }

                } catch (JSONException e) {
                    dbCallback.onError("JSON parsing error");
                    throw new RuntimeException(e);
                }
            }
        };
        InsertCheckRequest request = new InsertCheckRequest(priNum, infoCheck, infoDate, listener);
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }

    }

    interface DBCallback {
        void onSuccess();

        void onError(String errorMessage);
    }

    private void loadMemberCheckDB(int priNum, java.util.Date infoDate) {
        LoadMemberCheckRequest loadMemberCheckRequest = new LoadMemberCheckRequest(getContext());
        loadMemberCheckRequest.sendMemberOutputRequest(priNum, infoDate, new LoadMemberCheckRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    boolean priNumFound = false;

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        String infoCheck = jsonObject.getString("infoCheck");
                        for (MemberInfo member : memberInfoList) {
                            if (member.getPriNum() == priNum) {
                                member.setInfoCheck(infoCheck);
                                priNumFound = true;
                                Log.d("member"+i, member.getInfoCheck());
                                break;
                            }
                        }
                    }
                    if (!priNumFound) {
                        for (MemberInfo member : memberInfoList) {
                            if (member.getPriNum() == priNum) {
                                member.setInfoCheck("");
                                Log.d("member", "No match found, set to empty");
                                break;
                            }
                        }
                    }
                    updateAttendanceCount();
                    memberAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(String errorMessage) {
                Log.e("loadMemberCheckDB", "Error: " + errorMessage);
            }
        });
    }

    private void loadPastMemberCheckDB() {
        if (choiceGroupName == null || sqlDate == null) return;
        LoadMemberCheckPastRequest loadMemberCheckPastRequest = new LoadMemberCheckPastRequest(getContext());
        loadMemberCheckPastRequest.sendMemberOutputRequest(choiceGroupName, sqlDate, new LoadMemberCheckPastRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (MemberInfo member : memberInfoList) {
                        member.setInfoCheck(" ");
                    }
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        String dbInfoCheck = jsonObject.getString("infoCheck");
                        int dbPriNum = jsonObject.getInt("priNum");

                        for (MemberInfo member : memberInfoList) {
                            if (member.getPriNum() == dbPriNum) {
                                member.setInfoCheck(dbInfoCheck);
                                break;
                            }
                        }
                    }
                    updateAttendanceCount();
                    memberAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void loadMemberNameDB() {
        if (userID == null || choiceGroupName == null) return;

        LoadMemberRequest loadMemberRequest = new LoadMemberRequest(getContext());
        loadMemberRequest.sendMemberOutputRequest(userID, choiceGroupName, new LoadMemberRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    memberInfoList.clear();
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        int priNum = jsonObject.getInt("priNum");
                        String dbInfoName = jsonObject.getString("infoName");
                        String dbInfoNumber = jsonObject.getString("infoPhoneNumber");
                        memberInfo = new MemberInfo(priNum, dbInfoName, dbInfoNumber);
                        memberInfoList.add(memberInfo);
                        // loadMemberCheckDB(priNum, sqlDate);
                    }
                    loadPastMemberCheckDB();
                    isNullMemberInfoList(memberInfoList);
                    memberAdapter.notifyDataSetChanged();
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
        binding.tvGroupName.setOnClickListener(v -> showGroupNameDialog());
    }

    private void showGroupNameDialog() {
        //loadGroupNameDB();
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
                String searchText = textInputEditText.getText().toString();
                filterList.clear();
                for (GroupName groupName : groupNameList) {
                    if (groupName.getGroupName().toLowerCase().contains(searchText)) {
                        filterList.add(groupName);
                    }

                    innerAdapter.listFilter(filterList);
                    // 리싸이클러뷰가 비어있으면 경고 문구
                    if (innerAdapter.listNull(filterList)) {
                        tvError.setText("\" " + searchText + " \" 그룹이 없습니다.");
                        tvError.setVisibility(View.VISIBLE);
                        tvError2.setVisibility(View.VISIBLE);
                    } else {
                        tvError.setVisibility(View.GONE);
                        tvError2.setVisibility(View.GONE);
                    }
                }
            }
        });

        innerAdapter.setOnClick(groupName -> {
            // 리싸이클러뷰 아이템 클릭 이벤트
            choiceGroupName = groupName.getGroupName();
            textInputEditText.setText(choiceGroupName); // editText에 그룹이름 삽입
            textInputEditText.setSelection(choiceGroupName.length());    // 커서 위치 맨 뒤로
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
            if (textInputEditText.getText().toString().isEmpty() || filterList.isEmpty()) {
                Toast.makeText(getContext(), "그룹 이름을 확인해주세요..", Toast.LENGTH_SHORT).show();
            } else {
                groupViewModel.setGroupName(choiceGroupName);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
