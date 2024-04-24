package com.zzangse.attendance_check.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzangse.attendance_check.databinding.FragmentCheckBinding;

public class CheckFragment extends Fragment {
    private FragmentCheckBinding checkBinding;


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
      //  test();
    }

//    private void test() {
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String id = bundle.getString("userID");
//            String ps = bundle.getString("userPW");
//            String nickName = bundle.getString("nickName");
//            checkBinding.tvId.setText(id);
//            checkBinding.tvPw.setText(ps);
//            checkBinding.tvNickname.setText(nickName);
//        }
//    }
}