package com.zzangse.attendance_check.fragmentmore;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.databinding.FragmentWithdrawalAppBinding;

public class WithdrawalAppFragment extends Fragment {
    private FragmentWithdrawalAppBinding binding;
    private String userNickName;

    public static WithdrawalAppFragment newInstance() {
        WithdrawalAppFragment fragment = new WithdrawalAppFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWithdrawalAppBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        onClickClose();
        setNameLabel();
        onClickCheckBox();
        onClickWithDrawalBtn();
    }

    private void getBundle() {
        if (getArguments() != null) {
            userNickName = getArguments().getString("userNickName", userNickName);
        }
    }

    private void setNameLabel() {
        String asdf = userNickName + getResources().getString(R.string.withdrawal_app_top_label);
        binding.tvWithdrawalName.setText(asdf);
    }

    private void onClickWithDrawalBtn() {
        binding.btnWithdrawal.setOnClickListener(v -> {
            if (!binding.cbWithdrawal.isChecked()) {
                binding.tvErrorCheckbox.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onClickClose() {
        binding.toolbarWithdrawal.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                requireActivity().finish();
                return true;
            }
        });
    }

    private void onClickCheckBox() {
        binding.cbWithdrawal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.cbWithdrawal.setTextColor(Color.GRAY);
                binding.tvErrorCheckbox.setVisibility(View.GONE);
            } else {
                binding.cbWithdrawal.setTextColor(ContextCompat.getColor(getContext(), R.color.iconPrimary));
            }
        });
    }


}
