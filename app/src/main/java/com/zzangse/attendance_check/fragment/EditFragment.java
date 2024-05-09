package com.zzangse.attendance_check.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.adapter.GroupNameAdapter;
import com.zzangse.attendance_check.data.GroupName;
import com.zzangse.attendance_check.databinding.FragmentEditBinding;
import com.zzangse.attendance_check.request.DeleteGroupNameRequest;
import com.zzangse.attendance_check.request.GroupInputRequest;
import com.zzangse.attendance_check.request.GroupOutputRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditFragment extends Fragment {
    private FragmentEditBinding binding;
    private String userID;
    private ArrayList<GroupName> groupNameList = new ArrayList<>();
    private final static String REGEX_GROUP_NAME = "^(?!\\s{2,10}$)\\S{2,10}$";
    private GroupNameAdapter adapter;
    private RecyclerView recyclerView;
    private GroupName groupName;

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
        dataLoad();
        initRecycler();
        onClickGroupAdd();
    }

    private void onClickGroupAdd() {
        binding.toolbarEdit.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showGroupAddDialog();
                return true;
            }
        });
    }


    private void initRecycler() {
        RecyclerView recyclerView = binding.rvEdit;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupNameAdapter(getContext(), groupNameList);
        recyclerView.setAdapter(adapter);

        adapter.setOnClick(new GroupNameAdapter.GroupNameAdapterClick() {
            @Override
            public void onClickInfo(GroupName groupName) {
                Toast.makeText(getActivity(), groupName.getGroupName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("groupName", groupName.getGroupName());
                startActivity(intent);
            }

            @Override
            public void onDelete(GroupName groupName) {
                int pos = groupNameList.indexOf(groupName);
                showDeleteDialog(pos, groupName.getGroupName());
            }
        });
    }

    private void dataDelete(String groupName) {
        DeleteGroupNameRequest request = new DeleteGroupNameRequest(userID, groupName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 요청에 대한 응답 처리
                        Log.d("GroupDeleteRequest", "Response: " + response);
                    }
                });
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    private void showDeleteDialog(int pos, String groupName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundedDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_group, null);
        TextView btnOk = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = builder.setView(dialogView)
                .setCancelable(false)
                .create();
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        btnOk.setOnClickListener(v -> {
            dataDelete(groupName);
            groupNameList.remove(pos);
            adapter.notifyItemRemoved(pos);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void showGroupAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundedDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_group, null);
        TextView btnOK = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);
        TextInputLayout textInputLayout = dialogView.findViewById(R.id.et_group_name_layout);
        TextView textView = dialogView.findViewById(R.id.tv_group_name_error);
        TextInputEditText textInputEditText = dialogView.findViewById(R.id.et_group_name);

        AlertDialog dialog = builder.setView(dialogView)
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
                dialog.dismiss();
            } else {
                int colorRed = ContextCompat.getColor(getContext(), R.color.red);
                textInputLayout.setBoxStrokeColor(colorRed);
                textView.setVisibility(View.VISIBLE);
            }

        });
        dialog.show();
    }

    private boolean checkGroupName(String groupName) {
        boolean isCheck = groupName.matches(REGEX_GROUP_NAME);
        if (isCheck) {
            Toast.makeText(getActivity(), "db입력 허용", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(getActivity(), "db입력 불허", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // group을 생성
    private void setGroupDB(String userID, String groupName) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        Toast.makeText(getActivity(), "db ok", Toast.LENGTH_SHORT).show();
                        dataLoad();
                    } else {
                        Toast.makeText(getActivity(), "db no", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        if (getActivity() != null) {
            GroupInputRequest request = new GroupInputRequest(userID, groupName, listener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        } else {
            Log.d("EditFragment", "getActivity == null");
        }
    }

    // group을 리스트에 넣어줌
    private void dataLoad() {
        GroupOutputRequest groupOutputRequest = new GroupOutputRequest(getContext());
        groupOutputRequest.sendGroupOutputRequest(userID, new GroupOutputRequest.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                try {
                    groupNameList.clear();
                    for (int i = 0; i < result.length(); i++) {
                        groupName = new GroupName();
                        JSONObject jsonObject = result.getJSONObject(i);
                        String dbGroupName = jsonObject.getString("groupName");
                        groupName.setGroupName(dbGroupName);
                        groupNameList.add(groupName);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("userData", "error " + errorMessage);
            }
        });
    }
}