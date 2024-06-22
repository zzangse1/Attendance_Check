package com.zzangse.attendance_check.fragmentmain;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.MoreActivity;
import com.zzangse.attendance_check.databinding.FragmentMoreBinding;


public class MoreFragment extends Fragment {
    private FragmentMoreBinding binding;
    private String userID;
    private String userToken = "hello";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoreBinding.inflate(inflater);
        getArgs();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 구현
        setTextView();
        moveToChangePW();
        onClickLogout();
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            userID = args.getString("userID");
            userToken = args.getString("userToken");
            Log.d("usrToken", userToken + "");
        }
    }


    private void setTextView() {
        binding.tvId.setText(userID);
    }

    private void moveToChangePW() {
        binding.tvPasswordLabel.setOnClickListener(v -> {
            if (!userToken.equals("KAKAO")) {
                Intent intent = new Intent(getContext(), MoreActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "카카오 계정은 비밀번호 변경을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickLogout() {
        binding.tvOtherLogout.setOnClickListener(v->{
            showLogOutDialog();
        });
    }
    private void showLogOutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundedDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_logout, null);
        TextView btnOk = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = builder.setView(dialogView)
                .setCancelable(true)
                .create();
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        btnOk.setOnClickListener(v -> {
            requireActivity().finish();
        });
        dialog.show();
    }
}