package com.zzangse.attendance_check.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzangse.attendance_check.data.GroupViewModel;
import com.zzangse.attendance_check.databinding.FragmentChartBinding;


public class ChartFragment extends Fragment {
    private FragmentChartBinding binding;
    private GroupViewModel groupViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChartBinding.inflate(inflater);
        groupViewModel = new ViewModelProvider(requireActivity()).get(GroupViewModel.class);
        groupViewModel.getGroupName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvTest.setText(s);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void test() {

    }
}