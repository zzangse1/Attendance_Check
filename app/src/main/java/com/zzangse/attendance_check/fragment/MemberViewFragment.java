package com.zzangse.attendance_check.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.databinding.FragmentMemberModifyBinding;
import com.zzangse.attendance_check.databinding.FragmentMemberViewBinding;

public class MemberViewFragment extends Fragment {
    private FragmentMemberViewBinding binding;
    private MemberInfoFragment memberInfoFragment;

    public static MemberViewFragment newInstance() {
        return new MemberViewFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMemberViewBinding.inflate(inflater);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 구현
        test();
    }

    private void test() {
        binding.btnViewTest.setOnClickListener(v->{
            ((SettingActivity)getActivity()).replace(memberInfoFragment);
        });
    }
}
