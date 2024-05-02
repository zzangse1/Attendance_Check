package com.zzangse.attendance_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zzangse.attendance_check.data.GroupName;
import com.zzangse.attendance_check.R;

import java.util.ArrayList;

public class GroupNameAdapter extends RecyclerView.Adapter<GroupNameAdapter.ViewHolder> {
    private ArrayList<GroupName> list;
    private Context context;
    private GroupNameAdapterClick groupNameAdapterClick;

    public GroupNameAdapter(Context context, ArrayList<GroupName> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnClick(GroupNameAdapterClick groupNameAdapterClick) {
        this.groupNameAdapterClick = groupNameAdapterClick;
    }

    public interface GroupNameAdapterClick {
        void onClickInfo(GroupName groupName);
    }

    @NonNull
    @Override
    public GroupNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_plain_name, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupNameAdapter.ViewHolder holder, int position) {
        GroupName groupName = list.get(position);
        holder.bind(groupName);

        holder.tv_groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupNameAdapterClick != null) {
                    groupNameAdapterClick.onClickInfo(groupName);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_groupName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_groupName = itemView.findViewById(R.id.rv_groupName);
        }

        public void bind(GroupName groupName) {
            tv_groupName.setText(groupName.getGroupName());
        }
    }
}
