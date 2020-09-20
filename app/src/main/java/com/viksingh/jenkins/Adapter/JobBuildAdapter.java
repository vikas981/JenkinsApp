package com.viksingh.jenkins.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.viksingh.jenkins.Model.JobBuildModel;
import com.viksingh.jenkins.R;

import java.util.ArrayList;

public class JobBuildAdapter extends RecyclerView.Adapter<JobBuildAdapter.MyViewHolder> {
    private static ArrayList<JobBuildModel> dataSet;
    private Context context;
    private OnItemClickListner onItemClickListner;
    public JobBuildAdapter(Context paramContext, ArrayList<JobBuildModel> paramArrayList) {
        this.context = paramContext;
        dataSet = paramArrayList;
    }

    public int getItemCount() {
        return dataSet.size();
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        JobBuildModel jobBuildModel = dataSet.get(position);
        holder.status.setText(jobBuildModel.getResultStatus());
        holder.buidNumber.setText(String.valueOf(jobBuildModel.getNumber()));
        holder.duration.setVisibility(View.GONE);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        return new MyViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.build_history, paramViewGroup, false));
    }

    public void setOnItemClickListner(OnItemClickListner paramOnItemClickListner) {
        this.onItemClickListner = paramOnItemClickListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView buidNumber;
        public TextView duration;
        public ImageView image;
        public ImageView imageViewIcon;
        public TextView jobTitle;
        public TextView status;

        public MyViewHolder(View view) {
            super(view);
            status = (TextView)view.findViewById(R.id.status);
            duration = (TextView)view.findViewById(R.id.duration);
            buidNumber = (TextView)view.findViewById(R.id.buidNumber);
            jobTitle = (TextView)view.findViewById(R.id.jobTitle);
            imageViewIcon = (ImageView)view.findViewById(R.id.imageView);
            image = (ImageView)view.findViewById(R.id.imageView);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param2View) {
                    if (JobBuildAdapter.this.onItemClickListner != null) {
                        int i = JobBuildAdapter.MyViewHolder.this.getAdapterPosition();
                        if (i != -1) {
                            JobBuildAdapter.this.onItemClickListner.onItemClick(i);
                            JobBuildAdapter.this.notifyDataSetChanged();
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
