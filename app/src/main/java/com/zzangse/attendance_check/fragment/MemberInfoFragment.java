package com.zzangse.attendance_check.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.databinding.FragmentMemberInfoBinding;

public class MemberInfoFragment extends Fragment {
    private FragmentMemberInfoBinding binding;

    public static MemberInfoFragment newInstance() {
        MemberInfoFragment fragment = new MemberInfoFragment();
        Bundle args = new Bundle();
        args.putString("groupName", "value");
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMemberInfoBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        test();
        onClickBack();
    }

    private void onClickBack() {
        binding.toolbarMemberInfo.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void test() {
        binding.btnModify.setOnClickListener(v->{
            if (getActivity() instanceof SettingActivity) {
                ((SettingActivity)getActivity()).onFragmentChanged(2);
            }
        });
    }
}
