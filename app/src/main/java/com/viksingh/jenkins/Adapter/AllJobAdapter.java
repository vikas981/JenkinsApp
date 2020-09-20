package com.viksingh.jenkins.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.viksingh.jenkins.Model.AllJobModel;
import com.viksingh.jenkins.R;

import java.util.ArrayList;

public class AllJobAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater;
    private ArrayList<AllJobModel> jobList;

    public AllJobAdapter(Context mContext, ArrayList<AllJobModel> allJobModelList) {
        this.context = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.jobList = allJobModelList;
    }

    public int getCount() {
        return this.jobList.size();
    }

    public Object getItem(int position) {
        return this.jobList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View paramView, ViewGroup ViewGroup) {
        MyViewHolder holder;
        View view = paramView;
        if (view == null) {
            view = ((LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_custom_item, null);
            holder = new MyViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder)view.getTag();
        }

        holder.countryName.setText(jobList.get(position).getJob());
        holder.imageView.setImageResource(R.drawable.logout_icon);
        return view;
    }

    class MyViewHolder {
        TextView countryName;
        ImageView imageView;
        MyViewHolder(View paramView) {
            this.countryName = (TextView)paramView.findViewById(R.id.country);
            this.imageView = (ImageView)paramView.findViewById(R.id.flag);
        }
    }
}
