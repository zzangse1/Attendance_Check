package com.zzangse.attendance_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.data.MemberInfo;

import java.util.ArrayList;

public class MemberNameAdapter extends RecyclerView.Adapter<MemberNameAdapter.viewHolder> {
    private ArrayList<MemberInfo> list;
    private Context context;
    private MemberNameAdapterClick memberAdapterClick;

    public MemberNameAdapter(Context context, ArrayList<MemberInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClick(MemberNameAdapterClick memberNameAdapterClick) {
        this.memberAdapterClick = memberNameAdapterClick;
    }

    public interface MemberNameAdapterClick {
        void onClickInfo(MemberInfo memberInfo);

        void onDelete(MemberInfo memberInfo);
    }

    @NonNull
    @Override
    public MemberNameAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_plain_name, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberNameAdapter.viewHolder holder, int position) {
        MemberInfo memberInfo = list.get(position);
        holder.bind(memberInfo);

        holder.tv_groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberAdapterClick != null) {
                    memberAdapterClick.onClickInfo(memberInfo);
                }
            }
        });

        holder.ib_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberAdapterClick != null) {
                    memberAdapterClick.onDelete(memberInfo);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tv_groupName;
        ImageButton ib_delete;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_groupName = itemView.findViewById(R.id.rv_groupName);
            this.ib_delete = itemView.findViewById(R.id.ib_delete);
        }

        public void bind(MemberInfo memberInfo) {
            tv_groupName.setText(memberInfo.getInfoName());
        }
    }
}
