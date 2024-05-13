package com.zzangse.attendance_check.fragment;

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

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.activity.SettingActivity;
import com.zzangse.attendance_check.adapter.MemberNameAdapter;
import com.zzangse.attendance_check.data.MemberInfo;
import com.zzangse.attendance_check.databinding.FragmentMemberViewBinding;
import com.zzangse.attendance_check.request.MemberOutputRequest;

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

    public static MemberViewFragment newInstance(String groupName, String userID) {
        MemberViewFragment fragment = new MemberViewFragment();
        Bundle args = new Bundle();
        args.putString("groupName", groupName);
        args.putString("userID", userID);
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
        //moveToMemberInfoFragment();
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
                Toast.makeText(getActivity(),memberInfo.getInfoName(),Toast.LENGTH_SHORT).show();
                moveToMemberInfoFragment();
            }

            @Override
            public void onDelete(MemberInfo memberInfo) {
                Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
                int pos = memberNameList.indexOf(memberInfo);
                showDeleteDialog(pos,memberInfo.getInfoGroupName());
            }
        });
    }

    private void onClickBtnAdd() {
        binding.toolbarMemberView.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.ib_add) {
                    Toast.makeText(getActivity(), "멤버추가", Toast.LENGTH_SHORT).show();
                    moveToMemberAddFragment();
                    return true;
                }
                return false;
            }

        });
    }

    private void showDeleteDialog(int pos,String memberName) {
        String dialogTitle = "멤버 삭제하기";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.RoundedDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete, null);
        TextView tvTittle = dialogView.findViewById(R.id.tv_title);
        TextView btnOK = dialogView.findViewById(R.id.btn_ok);
        TextView btnCanvel = dialogView.findViewById(R.id.btn_cancel);

        tvTittle.setText(dialogTitle);
        AlertDialog dialog = builder.setView(dialogView)
                .setCancelable(false)
                .create();
        btnCanvel.setOnClickListener(v->{
            dialog.dismiss();
        });
        btnOK.setOnClickListener(v->{
            dataDelete(memberName);
            memberNameList.remove(pos);
            adapter.notifyItemRemoved(pos);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void dataDelete(String memberName) {
        Toast.makeText(getActivity(),"멤버삭제",Toast.LENGTH_SHORT).show();
    }

    private void setGroupName() {
        groupName = getArguments().getString("groupName");
        userID = getArguments().getString("userID");
        binding.tvGroupName.setText("그룹 [ "+groupName+" ]");
    }

    private void dataLoad() {
        MemberOutputRequest memberOutputRequest = new MemberOutputRequest(getContext());
        memberOutputRequest.sendMemberOutputRequest(userID,groupName,new MemberOutputRequest.VolleyCallback(){

            @Override
            public void onSuccess(JSONArray result) {
                try {
                    memberNameList.clear();
                    for (int i = 0; i < result.length(); i++) {
                        memberInfo = new MemberInfo();
                        JSONObject jsonObject = result.getJSONObject(i);
                        String dbMemberName = jsonObject.getString("infoName");
                        memberInfo.setInfoName(dbMemberName);
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
            ((SettingActivity) getActivity()).onFragmentChanged(1);
        }
    }

    private void moveToMemberInfoFragment() {
        if (getActivity() instanceof SettingActivity) {
            ((SettingActivity) getActivity()).onFragmentChanged(0);
        }
    }
}
