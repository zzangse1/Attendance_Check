package com.zzangse.attendance_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zzangse.attendance_check.R;
import com.zzangse.attendance_check.data.CheckChart;

import java.util.ArrayList;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CheckChart> chartArrayList;
    private ChartAdapterClick chartAdapterClick;

    public ChartAdapter(Context context, ArrayList<CheckChart> chartArrayList) {
        this.context = context;
        this.chartArrayList = chartArrayList;
    }


    public void setOnClick(ChartAdapterClick chartAdapterClick) {
        this.chartAdapterClick = chartAdapterClick;
    }

    public interface ChartAdapterClick {
        void onClickInfo(CheckChart chart);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_chart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckChart checkChart = chartArrayList.get(position);
        holder.bind(checkChart);
        holder.tv_infoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chartAdapterClick != null) {
                    chartAdapterClick.onClickInfo(checkChart);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chartArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_infoName;
        TextView tv_attendance;
        TextView tv_tardy;
        TextView tv_absence;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_infoName = itemView.findViewById(R.id.tv_member_name);
            this.tv_attendance = itemView.findViewById(R.id.tv_attendance);
            this.tv_tardy = itemView.findViewById(R.id.tv_tardy);
            this.tv_absence = itemView.findViewById(R.id.tv_absence);
        }

        public void bind(CheckChart chart) {
            tv_infoName.setText(chart.getInfoName());
            tv_attendance.setText(chart.getAttendanceCount());
            tv_tardy.setText(chart.getTardyCount());
            tv_absence.setText(chart.getAbsenceCount());

        }
    }
}
