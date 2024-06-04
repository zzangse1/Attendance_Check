package com.zzangse.attendance_check.data;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import com.zzangse.attendance_check.databinding.DialogDatePickerBinding;

import java.util.Calendar;

public class YearMonthPickerDialog extends Dialog {
    private OnDateSetListener listener;
    private DialogDatePickerBinding binding;

    public interface OnDateSetListener {
        void onDateSet(int year, int month);
    }

    public YearMonthPickerDialog(@NonNull Context context, OnDateSetListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogDatePickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // NumberPicker를 binding을 통해 초기화
        NumberPicker yearPicker = binding.yearPicker;
        NumberPicker monthPicker = binding.monthPicker;

        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(2100);
        yearPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(Calendar.getInstance().get(Calendar.MONTH) + 1); // 0월부터 시작 +1

        binding.setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDateSet(yearPicker.getValue(), monthPicker.getValue());
                dismiss();
            }
        });

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
    }
}
