package com.zzangse.attendance_check.fragmentsetting;

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
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.data.MemberInfo;
import com.zzangse.attendance_check.databinding.FragmentMemberInfoBinding;
import com.zzangse.attendance_check.request.LoadMemberViewRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemberInfoFragment extends Fragment {
    private FragmentMemberInfoBinding binding;
    private int priNum;
    private String userID;
    private MemberInfo memberInfo;

    public static MemberInfoFragment newInstance(Bundle bundle) {
        MemberInfoFragment fragment = new MemberInfoFragment();
        fragment.setArguments(bundle);
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
        if (getArguments() != null) {
            userID = getArguments().getString("userID", userID);
            priNum = getArguments().getInt("priNum");
            Log.d("정보에서 priNum", priNum + "");
        }
        onClickBack();
        onClickModifyBtn();
        loadMemberInfo(priNum);
        onClickPhoneNumber();
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


    private void onClickBack() {
        binding.toolbarMemberInfo.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    // 질문
    private void onClickModifyBtn() {
        binding.btnModify.setOnClickListener(v -> {
            setBundle();
        });

    }

    private void showDeleteDialog(String number) {
        String dialogTitle = "전화 걸기";
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(), R.style.RoundedDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete, null);
        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvTarget = dialogView.findViewById(R.id.tv_delete_target);
        TextView tvLabel = dialogView.findViewById(R.id.tv_delete_label);
        TextView btnOk = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);

        tvTarget.setText("전화창으로 이동합니다.");
        tvLabel.setVisibility(View.GONE);
        btnOk.setText("이동");
        tvTitle.setText(dialogTitle);

        AlertDialog dialog = builder.setView(dialogView)
                .setCancelable(false)
                .create();
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        btnOk.setOnClickListener(v -> {
            moveToCall(number);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void moveToCall(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    public void onClickPhoneNumber() {
        binding.tvInfoPersonNumber.setOnClickListener(v -> {
            String number = binding.tvInfoPersonNumber.getText().toString();
            Toast.makeText(getContext(), "전화번호 이동", Toast.LENGTH_SHORT).show();
            showDeleteDialog(number);
        });
        binding.tvInfoPersonNumber2.setOnClickListener(v -> {
            Toast.makeText(getContext(), "전화번호 이동", Toast.LENGTH_SHORT).show();
            String number = binding.tvInfoPersonNumber2.getText().toString(); // 수정된 부분
            showDeleteDialog(number);
        });
    }


    private void setMemberInfo(MemberInfo memberInfo) {
        binding.tvInfoGroupName.setText(memberInfo.getInfoGroupName());
        binding.tvInfoPersonName.setText(memberInfo.getInfoName());
        binding.tvInfoPersonNumber.setText(memberInfo.getInfoNumber());
        binding.tvInfoPersonNumber2.setText(memberInfo.getInfoNumber2());
        binding.tvInfoPersonAddress.setText(memberInfo.getInfoAddress());
        binding.tvInfoPersonAddress2.setText(memberInfo.getInfoAddress2());
        binding.tvInfoPersonMemo.setText(memberInfo.getInfoMemo());
    }

    private void loadMemberInfo(int priNum) {
        LoadMemberViewRequest loadMemberViewRequest = new LoadMemberViewRequest(getContext());
        loadMemberViewRequest.loadMemberViewRequest(priNum, new LoadMemberViewRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        String groupName = jsonObject.getString("groupName");
                        String infoName = jsonObject.getString("infoName");
                        String infoPhoneNumber = jsonObject.getString("infoPhoneNumber");
                        String infoPhoneNumber2 = jsonObject.getString("infoPhoneNumber2");
                        String infoAddress = jsonObject.getString("infoAddress");
                        String infoAddress2 = jsonObject.getString("infoAddress2");
                        String infoMemo = jsonObject.getString("infoMemo");
                        Log.d("TEST", groupName + ", " + infoName);
                        memberInfo = new MemberInfo(groupName, infoName, infoPhoneNumber, infoPhoneNumber2, infoAddress,infoAddress2, infoMemo);
                    }
                } catch (JSONException e) {
                    //throw new RuntimeException(e);
                    e.printStackTrace();
                }
                setMemberInfo(memberInfo);
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("TEST", errorMessage);
            }
        });
    }

    private void setBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("priNum",priNum);
        bundle.putString("userID", userID);
        bundle.putString("groupName", memberInfo.getInfoGroupName());
        bundle.putString("infoName", memberInfo.getInfoName());
        bundle.putString("infoPhoneNumber", memberInfo.getInfoNumber());
        bundle.putString("infoPhoneNumber2", memberInfo.getInfoNumber2());
        bundle.putString("infoAddress", memberInfo.getInfoAddress());
        bundle.putString("infoAddress2", memberInfo.getInfoAddress2());
        bundle.putString("infoMemo", memberInfo.getInfoMemo());

        Log.d("테스트", memberInfo.getInfoGroupName());
        moveToFragmentModify(bundle);
    }

    private void moveToFragmentModify(Bundle bundle) {
        if (getActivity() instanceof SettingActivity) {
            Log.d("view -> info", priNum + "");
            ((SettingActivity) getActivity()).onFragmentChanged(2, bundle);
        }
    }
}
