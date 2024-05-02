package com.zzangse.attendance_check.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.databinding.FragmentMemberViewBinding;

public class MemberViewFragment extends Fragment {

    private FragmentMemberViewBinding binding;
    public static MemberViewFragment newInstance(String groupName) {
        MemberViewFragment fragment = new MemberViewFragment();
        Bundle args = new Bundle();
        args.putString("groupName", groupName);
        fragment.setArguments(args);
        return fragment;
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
        onClickBack();
        setGroupName();
        onClickBtnAdd();
    }
    
    private void onClickBtnAdd() {
        binding.toolbarMemberView.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.ib_add) {
                    Toast.makeText(getActivity(), "ib_add", Toast.LENGTH_SHORT).show();
                    test2();
                    return true;
                }
     
                return false;
            }
            
        });
    }
    private void setGroupName() {
        String groupName = getArguments().getString("groupName");
        binding.tvGroupName.setText(groupName);
    }
    private void onClickBack() {
        binding.toolbarMemberView.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void test2() {
        if (getActivity() instanceof SettingActivity) {
            ((SettingActivity) getActivity()).onFragmentChanged(1);
        }
    }
    private void test() {
        binding.btnViewTest.setOnClickListener(v -> {
            if (getActivity() instanceof SettingActivity) {
                ((SettingActivity) getActivity()).onFragmentChanged(0);
            }
        });
    }

}
