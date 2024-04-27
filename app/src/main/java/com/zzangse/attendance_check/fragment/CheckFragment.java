package com.zzangse.attendance_check.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.zzangse.attendance_check.databinding.FragmentCheckBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CheckFragment extends Fragment {
    private FragmentCheckBinding checkBinding;
    private Calendar calendar;
    private Date date = new Date();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy년 M월 dd일");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        checkBinding = FragmentCheckBinding.inflate(inflater);
        return checkBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDate();
        onClickDate();
    }

    private void setDate() {
        simpleDateFormat = new SimpleDateFormat("yy년 M월 dd일");
        String today = simpleDateFormat.format(date);
        checkBinding.tvDate.setText(today);
    }

    private void onClickDate() {
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        // today
        Long today = MaterialDatePicker.todayInUtcMilliseconds();
        checkBinding.tvDate.setOnClickListener(v -> {
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
                    checkBinding.tvDate.setText(dateString);

                }
            });
        });

    }
}