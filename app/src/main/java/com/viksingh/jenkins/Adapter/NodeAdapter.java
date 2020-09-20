package com.viksingh.jenkins.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.viksingh.jenkins.Model.NodeModel;
import com.viksingh.jenkins.R;

import java.util.ArrayList;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.MyViewHolder> {
    private static ArrayList<NodeModel> dataSet;
    private Context context;
    private OnItemClickListner onItemClickListner;

    public NodeAdapter(Context paramContext, ArrayList<NodeModel> paramArrayList) {
        this.context = paramContext;
        dataSet = paramArrayList;
    }

    public int getItemCount() {
        return dataSet.size();
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        NodeModel nodeModel = dataSet.get(position);
        holder.imageViewIcon.setImageResource(R.drawable.nodes);
        holder.textViewName.setText(nodeModel.getName());
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false));
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewIcon;

        public TextView textViewName;

        public MyViewHolder(View view) {
            super(view);
            this.textViewName = (TextView)view.findViewById(R.id.name);
            this.imageViewIcon = (ImageView)view.findViewById(R.id.image);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramView) {
                    if (NodeAdapter.this.onItemClickListner != null) {
                        int i = NodeAdapter.MyViewHolder.this.getAdapterPosition();
                        if (i != -1) {
                            NodeAdapter.this.onItemClickListner.onItemClick(i);
                            NodeAdapter.this.notifyDataSetChanged();
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
