package com.project.havewemet;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.havewemet.model.AppUser;
import com.project.havewemet.model.Model;
import com.project.havewemet.model.Status;

import java.util.List;

public class StatusRecyclerAdapter extends RecyclerView.Adapter<StatusRecyclerAdapter.StatusViewHolder> {


    private List<Status> statusList;
    private final LayoutInflater inflater;


    public StatusRecyclerAdapter(LayoutInflater inflater, List<Status> statusList) {
        this.statusList = statusList;
        this.inflater = inflater;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.status_recyclerview_item, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        Status status = statusList.get(position);
        holder.itemView.setOnClickListener(view -> listener.onItemClick(position));
        Model.instance().getUserById(status.getUserId(), author -> {
            if (author ==  null)
                author = new AppUser("unknown user", "unknown");
            holder.bind(status, author, position);
        });
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
        private Handler handler = new Handler(Looper.getMainLooper());

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar_rv);
            tvName = itemView.findViewById(R.id.tv_name_rv);
            tvTime = itemView.findViewById(R.id.tv_time_rv);
            tvStatus = itemView.findViewById(R.id.tv_status_rv);
        }

        public void bind(Status status, AppUser author, int position) {
            handler.post(()->{
                tvName.setText(author.getName());
                tvTime.setText(status.getTime());
                tvStatus.setText(status.getContent());
            });
            if (!author.avatarUrl.equals(""))
                handler.post(()->{
                    Glide.with(MyApplication.getMyContext())
                            .load(author.avatarUrl)
                            .circleCrop()
                            .into(ivAvatar);
                });
        }
    }
}
