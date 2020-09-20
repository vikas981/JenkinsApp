/*
package com.viksingh.jenkins.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.viksingh.jenkins.Model.JobModel;
import com.viksingh.jenkins.R;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends ArrayAdapter<JobModel> {
    private Context context;
    private List<JobModel> jobModelFilteredList;
    private List<JobModel> jobModelList;

    public JobAdapter(@NonNull Context context, int resource, Context context1, List<JobModel> jobModelFilteredList, List<JobModel> jobModelList) {
        super(context, resource);
        this.context = context1;
        this.jobModelFilteredList = jobModelFilteredList;
        this.jobModelList = jobModelList;
    }

    public int getCount() {
        return this.jobModelFilteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            protected Filter.FilterResults performFiltering(CharSequence paramCharSequence) {
                Filter.FilterResults filterResults = new Filter.FilterResults();
                if (paramCharSequence == null || paramCharSequence.length() == 0) {
                    filterResults.count = JobAdapter.this.jobModelFilteredList.size();
                    filterResults.values = JobAdapter.this.jobModelList;
                    return filterResults;
                }
                ArrayList<JobModel> arrayList = new ArrayList();
                paramCharSequence = paramCharSequence.toString().toLowerCase();
                for (JobModel jobModel : JobAdapter.this.jobModelList) {
                    if (jobModel.getName().toLowerCase().contains(paramCharSequence))
                        arrayList.add(jobModel);
                    filterResults.count = arrayList.size();
                    filterResults.values = arrayList;
                }
                return filterResults;
            }

            protected void publishResults(CharSequence param1CharSequence, Filter.FilterResults paramFilterResults) {
                JobAdapter.(JobAdapter.this, (List)paramFilterResults.values);
                JenkinsJob.jobModelList = (List)paramFilterResults.values;
                JobAdapter.this.notifyDataSetChanged();
            }
        };
    }

    public JobModel getItem(int paramInt) {
        return this.jobModelFilteredList.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        View view = LayoutInflater.from(paramViewGroup.getContext()).inflate(2131558467, null, true);
        TextView textView = (TextView)view.findViewById(R.id.name);
        ImageView imageView = (ImageView)view.findViewById(R.id.);
        textView.setText(((JobModel)this.jobModelFilteredList.get(paramInt)).getName());
        imageView.setImageResource(2131230832);
        return view;
    }
}
*/
