package com.zzangse.attendance_check.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.GroupAddActivity;
import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.databinding.FragmentEditBinding;

public class EditFragment extends Fragment {
    private FragmentEditBinding fragmentEditBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       fragmentEditBinding = FragmentEditBinding.inflate(inflater);
       return fragmentEditBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 구현
        onClickGroupAdd();
        onClickTest();
    }

    private void onClickGroupAdd() {
        fragmentEditBinding.ibGroupAdd.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), GroupAddActivity.class);
            startActivity(intent);
        });
    }

    private void onClickTest() {
        fragmentEditBinding.btnTest.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });
    }
}