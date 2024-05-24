package com.zzangse.attendance_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.data.MemberInfo;

import java.util.ArrayList;

public class CheckMemberNameAdapter extends RecyclerView.Adapter<CheckMemberNameAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MemberInfo> list;
    private CheckMemberNameAdapterClick checkMemberNameAdapterClick;

    public CheckMemberNameAdapter(Context context, ArrayList<MemberInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClick(CheckMemberNameAdapterClick checkMemberNameAdapterClick) {
        this.checkMemberNameAdapterClick = checkMemberNameAdapterClick;
    }

    public interface CheckMemberNameAdapterClick {
        void onClickInfo(MemberInfo memberInfo);

    }

    @NonNull
    @Override
    public CheckMemberNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_check, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckMemberNameAdapter.ViewHolder holder, int position) {
        MemberInfo memberInfo = list.get(position);
        holder.bind(memberInfo);

        holder.tv_memberName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_memberName;
        TextView tv_memberCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_memberName = itemView.findViewById(R.id.tv_rv_check_name);
            this.tv_memberCheck = itemView.findViewById(R.id.tv_rv_check_result);
        }
        public void bind(MemberInfo memberInfo) {
            tv_memberName.setText(memberInfo.getInfoName());
            tv_memberCheck.setText(memberInfo.getInfoCheck());
        }
    }


}
