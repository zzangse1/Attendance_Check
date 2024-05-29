package com.zzangse.attendance_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMemberNameAdapterClick != null) {
                    checkMemberNameAdapterClick.onClickInfo(memberInfo);
                }
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
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layout = itemView.findViewById(R.id.check_layout);
            this.tv_memberName = itemView.findViewById(R.id.tv_rv_check_name);
            this.tv_memberCheck = itemView.findViewById(R.id.tv_rv_check_result);
        }
        public void bind(MemberInfo memberInfo) {
            tv_memberName.setText(memberInfo.getInfoName());
            if (memberInfo.getInfoCheck().equals("출석")){
                tv_memberCheck.setTextColor(ContextCompat.getColor(context,R.color.check_green));
                tv_memberCheck.setText(memberInfo.getInfoCheck());
            } else if (memberInfo.getInfoCheck().equals("지각")) {
                tv_memberCheck.setTextColor(ContextCompat.getColor(context,R.color.check_orange));
                tv_memberCheck.setText(memberInfo.getInfoCheck());
            }else if (memberInfo.getInfoCheck().equals("결석")) {
                tv_memberCheck.setTextColor(ContextCompat.getColor(context, R.color.check_red));
                tv_memberCheck.setText(memberInfo.getInfoCheck());
            } else {
                tv_memberCheck.setTextColor(ContextCompat.getColor(context, R.color.black));
                tv_memberCheck.setText(memberInfo.getInfoCheck());
            }
        }
    }


}
