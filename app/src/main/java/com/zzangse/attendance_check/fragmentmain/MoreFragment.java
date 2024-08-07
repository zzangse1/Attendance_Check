package com.zzangse.attendance_check.fragmentmain;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.zzangse.attendance_check.MySharedPreferences;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.LoginActivity;
import com.zzangse.attendance_check.activity.MoreActivity;
import com.zzangse.attendance_check.databinding.FragmentMoreBinding;


public class MoreFragment extends Fragment {
    private FragmentMoreBinding binding;
    private String userID;
    private String userNickName;
    private String userToken = "hello";

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
        onClickLogout();
        onClickTv();
        onClickNotice();
        onClickPrivacy();
        onClickService();
        showAD();
    }

    private void showAD() {
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        AdRequest request = new AdRequest.Builder().build();
        binding.adView.loadAd(request);
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            userID = args.getString("userID");
            userNickName = args.getString("userNickName");
            userToken = args.getString("userToken");
            Log.d("moreFragment_getArgs", userID + ", " + userNickName + ", " + userToken);
        }
    }


    private void setTextView() {
        binding.tvId.setText(userID);
    }

    private void onClickTv() {
        binding.tvPasswordLabel.setOnClickListener(v -> {
            switchToFragment(0);
        });
        binding.tvWithdrawalApp.setOnClickListener(v -> {
            switchToFragment(1);
        });
    }

    private void switchToFragment(int index) {
        Intent intent = new Intent(getActivity(), MoreActivity.class);
        intent.putExtra("fragment_index", index);
        intent.putExtra("userID", userID);
        intent.putExtra("userNickName", userNickName);
        startActivity(intent);

    }

    private void onClickLogout() {
        binding.tvOtherLogout.setOnClickListener(v -> {
            showLogOutDialog();
        });
    }

    private void onClickNotice() {
        binding.tvAppNoticeLabel.setOnClickListener(v -> {
            Toast.makeText(getContext(), "공지가 없습니다.", Toast.LENGTH_SHORT).show();
        });
    }
    private void onClickPrivacy() {
        binding.tvAppPersonalInfoLabel.setOnClickListener(v->{
            String url = "https://sites.google.com/view/zzangse-privacy/%ED%99%88";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }

    private void onClickService() {
        binding.tvAppTermOfServiceLabel.setOnClickListener(v -> {
            String url = "https://sites.google.com/view/zzangse-service/%ED%99%88";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
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
            clearAutoLogin();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void clearAutoLogin() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.putExtra("LOGOUT", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 모든 기존 Activity를 제거하고 새로운 태스크로 시작
        startActivity(intent);
        requireActivity().finish(); // 현재 Activity를 종료
        MySharedPreferences.clearUser(requireContext());
    }
}