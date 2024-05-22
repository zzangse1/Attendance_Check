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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.adapter.CheckGroupNameAdapter;
import com.zzangse.attendance_check.data.GroupName;
import com.zzangse.attendance_check.databinding.FragmentCheckBinding;
import com.zzangse.attendance_check.request.LoadGroupRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CheckFragment extends Fragment {
    private FragmentCheckBinding binding;
    private Calendar calendar;
    private Date date = new Date();
    private String userID;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy년 M월 dd일");
    private ArrayList<GroupName> groupNameList = new ArrayList<>();
    private ArrayList<GroupName> filterList = new ArrayList<>();
    private GroupName groupName;
    private String choiceGroupName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        binding = FragmentCheckBinding.inflate(inflater);
        getArgs();
        return binding.getRoot();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDate();
        test();
        onClickDate();
        dataLoad();
        initRecycler();
        onClickGroupName();
    }

    private void initRecycler() {

    }

    private void setDate() {
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

    private void dataLoad() {
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
            showDialog();
        });
    }

    private void test() {
        binding.text.setText(userID + " 2");
    }

    private void showDialog() {
        dataLoad();
        for (GroupName str : groupNameList) {
            Log.d("확인", str.getGroupName());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundedDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search, null);
        TextView btnOk = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);
        TextInputLayout textInputLayout = dialogView.findViewById(R.id.et_group_name_layout);
        TextInputEditText textInputEditText = dialogView.findViewById(R.id.et_group_name);

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
                }
            }
        });

        innerAdapter.setOnClick(new CheckGroupNameAdapter.GroupNameAdapterClick() {
            @Override
            public void onClickInfo(GroupName groupName) {
                choiceGroupName = groupName.getGroupName();
                textInputEditText.setText(choiceGroupName);
                textInputEditText.setSelection(choiceGroupName.length());
            }
        });

        AlertDialog dialog = builder.setView(dialogView)
                .setCancelable(false)
                .create();
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnOk.setOnClickListener(v -> {
            // btn 눌럿을 때 이벤트
            binding.tvGroupName.setText(choiceGroupName);
            dialog.dismiss();
        });
        dialog.show();

    }


}
