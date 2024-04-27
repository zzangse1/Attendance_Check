package com.zzangse.attendance_check.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.databinding.FragmentEditBinding;
import com.zzangse.attendance_check.request.GroupRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class EditFragment extends Fragment {
    private FragmentEditBinding binding;
    private String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditBinding.inflate(inflater);
        getArgs();
        return binding.getRoot();
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            userID = args.getString("userID");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 구현
        onClickGroupAdd();
        onClickTest();
    }

    private void onClickGroupAdd() {
        binding.ibGroupAdd.setOnClickListener(v -> {
            showDialog();
        });
    }

    private void onClickTest() {
        binding.btnTest.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_view, null);
        Button btnOK = dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        TextInputLayout textInputLayout = dialogView.findViewById(R.id.et_group_name_layout);
        TextView textView = dialogView.findViewById(R.id.tv_group_name_error);
        TextInputEditText textInputEditText = dialogView.findViewById(R.id.et_group_name);

        AlertDialog dialog = builder.setView(dialogView)
                .setTitle("그룹 생성")
                .setCancelable(false)
                .create();
        btnCancel.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnOK.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
            String groupName = textInputEditText.getText().toString();
            if (checkGroupName(groupName)) {
                setGroupDB(userID, groupName);
                binding.tvTest.setText(userID + ", " + groupName);
                dialog.dismiss();
            } else {
                textInputLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
                textView.setVisibility(View.VISIBLE);
            }

        });
        dialog.show();
    }

    private boolean checkGroupName(String groupName) {
        boolean isCheck = 2 <= groupName.length() && groupName.length() <= 10;
        if (isCheck) {
            Toast.makeText(getActivity(), "db입력 허용", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(getActivity(), "db입력 불허", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private void setGroupDB(String userID, String groupName) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        Toast.makeText(getActivity(), "db ok", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "db no", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        if (getActivity() != null) {
            GroupRequest request = new GroupRequest(userID, groupName, listener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        } else {
            Log.d("EditFragment", "getActivity == null");
        }
    }
}