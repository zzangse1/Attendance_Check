package com.zzangse.attendance_check.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.FragmentChartBinding;


public class ChartFragment extends Fragment {
    private FragmentChartBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChartBinding.inflate(inflater);
        return binding.getRoot();
    }
}