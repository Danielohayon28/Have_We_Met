package com.project.havewemet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.havewemet.model.Status;

import java.util.List;

public class StatusRecyclerAdapter extends RecyclerView.Adapter<StatusRecyclerAdapter.StatusViewHolder> {


    private List<Status> statusList;
    private final LayoutInflater inflater;

    public StatusRecyclerAdapter(LayoutInflater inflater, List<Status> statusList) {
        this.statusList = statusList;
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatusViewHolder(inflater.inflate(R.layout.status_recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        Status status = statusList.get(position);
        holder.bind(status, position);
    }

    @Override
    public int getItemCount() {
        return statusList == null? 0 : statusList.size();
    }

    public void setData(List<Status> list) {
        this.statusList = list;
        notifyDataSetChanged();
    }

    static class StatusViewHolder extends  RecyclerView.ViewHolder{

        private final ImageView ivAvatar;
        private final  TextView tvName, tvTime, tvStatus;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar_rv);
            tvName = itemView.findViewById(R.id.tv_name_rv);
            tvTime = itemView.findViewById(R.id.tv_time_rv);
            tvStatus = itemView.findViewById(R.id.tv_status_rv);
        }

        public void bind(Status status, int position) {
            tvName.setText("null");
            tvTime.setText("null");
            tvStatus.setText(status.getContent());

            //todo: update profile picture and get status's owner
        }
    }
}
