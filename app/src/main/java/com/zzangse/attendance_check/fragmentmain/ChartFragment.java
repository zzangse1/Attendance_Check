package com.zzangse.attendance_check.fragmentmain;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.textfield.TextInputEditText;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.adapter.ChartAdapter;
import com.zzangse.attendance_check.adapter.CheckGroupNameAdapter;
import com.zzangse.attendance_check.data.CheckChart;
import com.zzangse.attendance_check.data.DateViewModel;
import com.zzangse.attendance_check.data.GroupName;
import com.zzangse.attendance_check.data.GroupViewModel;
import com.zzangse.attendance_check.data.YearMonthPickerDialog;
import com.zzangse.attendance_check.databinding.FragmentChartBinding;
import com.zzangse.attendance_check.request.LoadChartListRequest;
import com.zzangse.attendance_check.request.LoadChartRequest;
import com.zzangse.attendance_check.request.LoadGroupRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class ChartFragment extends Fragment {
    private FragmentChartBinding binding;
    private GroupViewModel groupViewModel;
    private DateViewModel dateViewModel;
    private String userID;
    private ChartAdapter adapter;
    private ArrayList<GroupName> groupNameList = new ArrayList<>();
    private GroupName groupName;
    private ArrayList<GroupName> filterList = new ArrayList<>();
    private String choiceGroupName;
    private CheckChart chart;
    private SimpleDateFormat simpleDateFormat;
    private ArrayList<CheckChart> chartPieArrayList = new ArrayList<>();
    private ArrayList<CheckChart> chartRvArrayList = new ArrayList<>();
    private String date, firstDay, lastDay;
    private int currentYear, currentMonth;
    private boolean isRecycler = false;

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
                choiceGroupName = s;
                loadCheckChart();
            }
        });

        dateViewModel.getSelectedDate().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long dateInMillis) {
                Log.d("date", dateInMillis + "");
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
        initDate();
        loadGroupNameDB();
        onClickGroupName();
        onClickDate();
        setupCheckBox();
        initRecycler();
        onClickMonthBtn();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.checkBox.setChecked(false);
    }

    private void initDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 현재 날짜를 가져옴
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            YearMonth yearMonth = YearMonth.from(today);
            LocalDate firstDayOfMonth = yearMonth.atDay(1);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            String formattedToday = today.format(formatter);
            firstDay = firstDayOfMonth.format(formatter);
            lastDay = lastDayOfMonth.format(formatter);
            date = today.format(formatter);

            int year = today.getYear();
            int month = today.getMonthValue();
            currentMonth = today.getMonthValue();
            currentYear = today.getYear();

            Log.d("java8 up", date);
            Log.d("java8 up | formattedToday", formattedToday);
            Log.d("java8 up | formattedFirstDay", firstDay);
            Log.d("java8 up | formattedLastDay", lastDay);
            binding.tvDate.setText(year + "년 " + month + "월");
        } else {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            String formattedToday = formatter.format(calendar.getTime());

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            firstDay = formatter.format(calendar.getTime());

            int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
            lastDay = formatter.format(calendar.getTime());

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;

            currentYear = calendar.get(Calendar.YEAR);
            currentMonth = calendar.get(Calendar.MONTH) + 1;

            binding.tvDate.setText(year + "년 " + month + "월");
            Log.d("CurrentDate", "Today: " + formattedToday);
            Log.d("FirstDay", "First day of the month: " + firstDay);
            Log.d("LastDay", "Last day of the month: " + lastDay);

            binding.tvDate.setText(formattedToday); // 예제 TextView 업데이트
        }
        loadCheckChart();
    }

    private void onClickGroupName() {
        binding.tvGroupName.setOnClickListener(v -> showGroupNameDialog());
    }


    private void initRecycler() {
        RecyclerView recyclerView = binding.rvChart;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChartAdapter(getContext(), chartRvArrayList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(new ChartAdapter.ChartAdapterClick() {
            @Override
            public void onClickInfo(CheckChart chart) {
                Toast.makeText(getContext(), chart.getInfoName() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCheckBox() {
        binding.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = binding.checkBox.isChecked();
                if (isChecked) {
                    binding.pieChart.setVisibility(View.GONE);
                } else {
                    binding.pieChart.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadCheckList() {
        LoadChartListRequest request = new LoadChartListRequest(getContext());
        request.sendMemberOutputRequest(firstDay, lastDay, userID, binding.tvGroupName.getText().toString(),
                new LoadChartListRequest.VolleyCallback() {
                    @Override
                    public void onSuccess(JSONArray result) {
                        try {
                            chartRvArrayList.clear();
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject jsonObject = result.getJSONObject(i);
                                String infoName = jsonObject.getString("infoName");
                                String check1 = jsonObject.getString("출석_개수");
                                String check2 = jsonObject.getString("지각_개수");
                                String check3 = jsonObject.getString("결석_개수");
                                chart = new CheckChart(infoName, check1, check2, check3);
                                chartRvArrayList.add(chart);
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
    }

    private void loadCheckChart() {
        if (binding.tvGroupName.getText().equals("그룹 이름")) {
            chartRvArrayList.clear();
            binding.tvGroupNull.setText(R.string.common_choice_group);
            binding.tvGroupNull.setVisibility(View.VISIBLE);
            binding.pieChart.setVisibility(View.GONE);
        } else {
            binding.tvGroupNull.setVisibility(View.GONE);
            LoadChartRequest request = new LoadChartRequest(getContext());
            request.sendMemberOutputRequest(
                    firstDay, lastDay,
                    userID, binding.tvGroupName.getText().toString(),
                    new LoadChartRequest.VolleyCallback() {
                        @Override
                        public void onSuccess(JSONArray result) {
                            try {
                                chartPieArrayList.clear();
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject jsonObject = result.getJSONObject(i);
                                    String a = jsonObject.getString("infoCheck");
                                    String b = jsonObject.getString("개수");
                                    Log.d("log", a + "," + b);
                                    chart = new CheckChart(a, b);
                                    chartPieArrayList.add(chart);
                                }
                                setChart();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
        }

    }

    private void setChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Log.d("chartArrayList.size()", chartPieArrayList.size() + "");
        int count = 0;
        for (CheckChart c : chartPieArrayList) {
            count += Integer.parseInt(c.getCheckCount());
            if (count == 0) {
                isRecycler = false;
                Log.d("count", count + "");
                binding.pieChart.setVisibility(View.GONE);
                binding.tvGroupNull.setText("해당 날짜는 기록이 없거나\n멤버가 없습니다.");
                binding.tvGroupNull.setVisibility(View.VISIBLE);
            } else {
                isRecycler = true;
                entries.add(new PieEntry(Float.parseFloat(c.getCheckCount()), c.getInfoCheck()));
                binding.pieChart.setVisibility(View.VISIBLE);
                binding.tvGroupNull.setVisibility(View.GONE);
            }
        }
        // 차트가 있으면 리싸이클러뷰 업데이트
        if (isRecycler) {
            loadCheckList();
        } else {
            chartRvArrayList.clear();
        }
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
        int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            data.setValueTextColor(Color.WHITE);
        } else {
            data.setValueTextColor(Color.BLACK);
        }
        Legend legend = binding.pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(nightModeFlags == Configuration.UI_MODE_NIGHT_YES ? Color.WHITE : Color.BLACK);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        //data.setValueTextColor(R.color.iconPrimary);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%d", (int) value);
            }
        });

        binding.pieChart.setData(data);
        binding.pieChart.invalidate();
    }

    private void onClickDate() {
        binding.tvDate.setOnClickListener(v -> showYearMonthPicker());
    }

    private void showYearMonthPicker() {
        YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(getContext(), new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int month) {
                Log.d("onDateSet", year + "," + month);
                calculateFirstAndLastDayOfMonth(year, month);
            }
        });
        yearMonthPickerDialog.show();
    }

    private void changeMonth(boolean increase) {
        if (increase) {
            if (currentMonth == Calendar.DECEMBER) {
                currentMonth = Calendar.JANUARY;
                currentYear++;
            } else {
                currentMonth++;
            }
        } else {
            if (currentMonth == Calendar.JANUARY) {
                currentMonth = Calendar.DECEMBER;
                currentYear--;
            } else {
                currentMonth--;
            }
        }
        calculateFirstAndLastDayOfMonth(currentYear, currentMonth);
    }

    private void onClickMonthBtn() {
        binding.btnRight.setOnClickListener(v -> {
            changeMonth(true);
        });
        binding.btnLeft.setOnClickListener(v -> {
            changeMonth(false);
        });
    }

    private void calculateFirstAndLastDayOfMonth(int year, int month) {
        YearMonth yearMonth = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            yearMonth = YearMonth.of(year, month);
            LocalDate firstDayOfMonth = yearMonth.atDay(1);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            firstDay = firstDayOfMonth.format(formatter);
            lastDay = lastDayOfMonth.format(formatter);
            binding.tvDate.setText(year + "년 " + month + "월");
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            // 첫날
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            firstDay = String.format("%d-%02d-%02d", calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            // 마지막 날
            int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
            lastDay = String.format("%d-%02d-%02d", calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            binding.tvDate.setText(year + "년 " + month + "월");
        }
        // 결과 출력
        Log.d("Date", "First day: " + firstDay);
        Log.d("Date", "Last day: " + lastDay);
        loadCheckChart();
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
                loadCheckChart();
                setChart();
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