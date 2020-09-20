package com.viksingh.jenkins.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.viksingh.jenkins.Model.JobHistoryModel;
import com.viksingh.jenkins.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class JobHistoryAdapter extends RecyclerView.Adapter<JobHistoryAdapter.MyViewHolder> {
    private static ArrayList<JobHistoryModel> dataSet;

    private Context context;

    private OnItemClickListner onItemClickListner;

    public JobHistoryAdapter(Context paramContext, ArrayList<JobHistoryModel> paramArrayList) {
        this.context = paramContext;
        dataSet = paramArrayList;
    }


    public static String getHMS(long paramLong) {
        return String.format("%02d:%02d:%02d", new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toHours(paramLong)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(paramLong) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(paramLong))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(paramLong) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(paramLong))) });
    }

    public int getItemCount() {
        return dataSet.size();
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        JobHistoryModel jobHistoryModel = dataSet.get(position);
        holder.status.setText(jobHistoryModel.getResultStatus());
        System.out.println(jobHistoryModel.getResultStatus());
        if (jobHistoryModel.getResultStatus().equals("SUCCESS")) {
            holder.status.setTextColor(Color.parseColor("#008000"));
            holder.image.setImageResource(R.drawable.green);
            holder.view.setBackgroundColor(Color.parseColor("#008000"));
        } else if (jobHistoryModel.getResultStatus().equals("FAILURE")) {
            holder.status.setTextColor(Color.parseColor("#DC143C"));
            holder.image.setImageResource(R.drawable.red);
            holder.view.setBackgroundColor(Color.parseColor("#DC143C"));
        } else {
            holder.status.setTextColor(Color.parseColor("#DAA520"));
            holder.image.setImageResource(R.drawable.yellow);
            holder.view.setBackgroundColor(Color.parseColor("#DAA520"));
        }
        holder.buidNumber.setText(jobHistoryModel.getDisplayName());
        holder.jobTitle.setText(jobHistoryModel.getJobName());
        if (getHMS(jobHistoryModel.getDuration()).split(":")[0].equals("00") && getHMS(jobHistoryModel.getDuration()).split(":")[0].equals("00") && getHMS(jobHistoryModel.getDuration()).split(":")[0].equals("00"))
            holder.duration.setText("Few Seconds");
        String totalTimeTaken = String.valueOf(getHMS(jobHistoryModel.getDuration()));
        holder.duration.setText(totalTimeTaken);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        return new MyViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.build_history, paramViewGroup, false));
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner=onItemClickListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView buidNumber;
        public TextView duration;
        public ImageView image;
        public ImageView imageViewIcon;
        public TextView jobTitle;
        public TextView status;
        public View view;

        public MyViewHolder(View paramView) {
            super(paramView);
            this.status = (TextView)paramView.findViewById(R.id.status);
            this.duration = (TextView)paramView.findViewById(R.id.duration);
            this.buidNumber = (TextView)paramView.findViewById(R.id.buidNumber);
            this.jobTitle = (TextView)paramView.findViewById(R.id.jobTitle);
            this.imageViewIcon = (ImageView)paramView.findViewById(R.id.imageView);
            this.image = (ImageView)paramView.findViewById(R.id.imageView);
            this.view = paramView.findViewById(R.id.divider);
            paramView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    if (JobHistoryAdapter.this.onItemClickListner != null) {
                        int i = JobHistoryAdapter.MyViewHolder.this.getAdapterPosition();
                        if (i != -1) {
                            JobHistoryAdapter.this.onItemClickListner.onItemClick(i);
                            JobHistoryAdapter.this.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }
    public static interface OnItemClickListner {
        void onItemClick(int position);
    }
}
