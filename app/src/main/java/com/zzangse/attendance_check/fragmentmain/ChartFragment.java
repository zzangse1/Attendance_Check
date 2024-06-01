package com.zzangse.attendance_check.fragmentmain;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.adapter.CheckGroupNameAdapter;
import com.zzangse.attendance_check.data.DateViewModel;
import com.zzangse.attendance_check.data.GroupName;
import com.zzangse.attendance_check.data.GroupViewModel;
import com.zzangse.attendance_check.databinding.FragmentChartBinding;
import com.zzangse.attendance_check.request.LoadChartRequest;
import com.zzangse.attendance_check.request.LoadGroupRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChartFragment extends Fragment {
    private FragmentChartBinding binding;
    private GroupViewModel groupViewModel;
    private DateViewModel dateViewModel;
    private String userID;
    private ArrayList<GroupName> groupNameList = new ArrayList<>();
    private GroupName groupName;
    private ArrayList<GroupName> filterList = new ArrayList<>();
    private String choiceGroupName;
    private java.util.Date sqlDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getArgs();
        binding = FragmentChartBinding.inflate(inflater);
        groupViewModel = new ViewModelProvider(requireActivity()).get(GroupViewModel.class);
        dateViewModel = new ViewModelProvider(requireActivity()).get(DateViewModel.class);

        groupViewModel.getGroupName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvGroupName.setText(s);
            }
        });

        dateViewModel.getSelectedDate().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long dateInMillis) {
                //  binding.tvTest2.setText(aLong + "");
            }
        });

        return binding.getRoot();
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            userID = args.getString("userID");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chart();
        loadGroupNameDB();
        test();
    }

    private void test() {
        binding.tvGroupName.setOnClickListener(v->showGroupNameDialog());
        LoadChartRequest request = new LoadChartRequest(getContext());
        request.sendMemberOutputRequest(binding.tvGroupName.getText().toString(), new LoadChartRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
               try {
                   for (int i = 0; i < result.length(); i++) {
                       JSONObject jsonObject = result.getJSONObject(i);
                       String a = jsonObject.getString("출석");
                       String b = jsonObject.getString("지각");
                       String c = jsonObject.getString("결석");
                       Log.d("log", a+", "+b+", "+c);

                   }
               } catch (JSONException e) {
                   throw new RuntimeException(e);
               }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void chart() {


        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(34f, "출석"));
        entries.add(new PieEntry(12f, "지각"));
        entries.add(new PieEntry(5f, "결석"));

        int[] colorArray = new int[]
                {
                        getContext().getColor(R.color.check_green)
                        , getContext().getColor(R.color.check_orange)
                        , getContext().getColor(R.color.check_red),
                };
        Description description = new Description();
        binding.pieChart.getDescription().setEnabled(false);

        binding.pieChart.animateY(1400, Easing.EaseInOutQuad); //애니메이션


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colorArray);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        binding.pieChart.setData(data);
    }

    private void showGroupNameDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(), R.style.RoundedDialog);
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
                binding.tvGroupName.setText(choiceGroupName);
                dialog.dismiss();
            }
        });
        dialog.show();
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
                        Log.d("groupName", dbGroupName);
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
}