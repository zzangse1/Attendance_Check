package com.zzangse.attendance_check.fragmentsetting;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.adapter.MemberNameAdapter;
import com.zzangse.attendance_check.data.MemberInfo;
import com.zzangse.attendance_check.databinding.FragmentMemberViewBinding;
import com.zzangse.attendance_check.request.LoadMemberRequest;
import com.zzangse.attendance_check.request.RemoveMemberNameRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MemberViewFragment extends Fragment {

    private FragmentMemberViewBinding binding;
    private String groupName;
    private String userID;
    private MemberNameAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<MemberInfo> memberNameList = new ArrayList<>();
    private MemberInfo memberInfo;
    private int priNum;

    public static MemberViewFragment newInstance(Bundle bundle) {
        MemberViewFragment fragment = new MemberViewFragment();
        fragment.setArguments(bundle);
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
        //moveToMemberInfoFragment();
        if (getArguments() != null) {
            priNum = getArguments().getInt("priNum");
            groupName = getArguments().getString("groupName");
        }
        onClickBack();
        initRecycler();
        setGroupName();
        onClickBtnAdd();
        dataLoad();
    }

    private void initRecycler() {
        recyclerView = binding.rvMemberView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MemberNameAdapter(getContext(), memberNameList);
        recyclerView.setAdapter(adapter);

        adapter.setOnClick(new MemberNameAdapter.MemberNameAdapterClick() {
            @Override
            public void onClickInfo(MemberInfo memberInfo) {
                Log.d("onClickInfo", memberInfo.getPriNum() + ", " + memberInfo.getInfoName());
                priNum = memberInfo.getPriNum();
              //  moveToMemberInfoFragment(memberInfo.getPriNum());
                moveToMemberInfoFragment();
            }

            @Override
            public void onDelete(MemberInfo memberInfo) {
                Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
                int pos = memberNameList.indexOf(memberInfo);
                showDeleteDialog(memberInfo.getPriNum(), pos, memberInfo.getInfoName());
            }
        });
    }

    private void onClickBtnAdd() {
        binding.toolbarMemberView.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.ib_add) {
                    //Toast.makeText(getActivity(), "멤버추가", Toast.LENGTH_SHORT).show();
                    moveToMemberAddFragment();
                    return true;
                }
                return false;
            }

        });
    }

    private void showDeleteDialog(int priNum, int pos, String memberName) {
        String dialogTitle = "멤버 삭제하기";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundedDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete, null);
        TextView tvTittle = dialogView.findViewById(R.id.tv_title);
        TextView tvMember = dialogView.findViewById(R.id.tv_delete_target);
        TextView btnOK = dialogView.findViewById(R.id.btn_ok);
        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);

        tvMember.setText(String.format("[ %s ] 삭제 하시겠습니까?", memberName));
        tvTittle.setText(dialogTitle);

        AlertDialog dialog = builder.setView(dialogView)
                .setCancelable(false)
                .create();
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        btnOK.setOnClickListener(v -> {
            dataDelete(priNum);
            memberNameList.remove(pos);
            adapter.notifyItemRemoved(pos);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void dataDelete(int priNum) {
        Toast.makeText(getActivity(), priNum + "멤버삭제", Toast.LENGTH_SHORT).show();
        RemoveMemberNameRequest request = new RemoveMemberNameRequest(priNum, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("GroupDeleteRequest", "Response: " + s);
            }
        });
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    private void setGroupName() {
        groupName = getArguments().getString("groupName");
        userID = getArguments().getString("userID");
        binding.tvGroupName.setText("그룹 [ " + groupName + " ]");
    }


    private void dataLoad() {
        Log.d("데이터 로드  ", userID + ", " + groupName);
        LoadMemberRequest loadMemberRequest = new LoadMemberRequest(getContext());
        loadMemberRequest.sendMemberOutputRequest(userID, groupName, new LoadMemberRequest.VolleyCallback() {

            @Override
            public void onSuccess(JSONArray result) {
                try {
                    memberNameList.clear();
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        String dbMemberName = jsonObject.getString("infoName");
                        int dbPriNum = jsonObject.getInt("priNum");
                        Log.d("data", dbPriNum + ", " + dbMemberName);
                        memberInfo = new MemberInfo(dbPriNum, dbMemberName);
                        memberNameList.add(memberInfo);
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

    private void onClickBack() {
        binding.toolbarMemberView.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void moveToMemberAddFragment() {
        if (getActivity() instanceof SettingActivity) {
            Bundle bundle = new Bundle();
            bundle.putString("groupName", groupName);
            bundle.putString("userID", userID);
            Log.d("추가로 이동", groupName + ", " + userID);
            ((SettingActivity) getActivity()).onFragmentChanged(1, bundle);
        }
    }

    private void moveToMemberInfoFragment() {
        if (getActivity() instanceof SettingActivity) {
            Bundle bundle = new Bundle();
            bundle.putInt("priNum", priNum);
            bundle.putString("userID",userID);
            Log.d("정보로 이동", priNum + "");
            ((SettingActivity) getActivity()).onFragmentChanged(0, bundle);
        }
    }
}
