package com.zzangse.attendance_check.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zzangse.attendance_check.databinding.FragmentMemberModifyBinding;

public class MemberModifyFragment extends Fragment {
    private FragmentMemberModifyBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMemberModifyBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        test();
    }

    private void test() {
        binding.btnSave.setOnClickListener(v -> {
            Toast.makeText(getActivity(),"저장되었습니다.",Toast.LENGTH_SHORT).show();
        });
    }
}
