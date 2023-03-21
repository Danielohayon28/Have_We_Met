package com.project.havewemet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.havewemet.databinding.FragmentViewProfileBinding;
import com.project.havewemet.model.Model;
import com.project.havewemet.model.Status;

import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProfileFragment extends Fragment {

    private StatusRecyclerAdapter adapter;
    private FragmentViewProfileBinding binding;
    private String userId;
    public static ViewProfileFragment newInstance(String userId) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putString("USER_ID", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.userId = getArguments().getString("USER_ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        userId = ViewProfileFragmentArgs.fromBundle(getArguments()).getUserId();

        Toast.makeText(MyApplication.getMyContext(), "iiid: "+userId, Toast.LENGTH_SHORT).show();

        //getting all views from inflated view
        RecyclerView rvStatuses = view.findViewById(R.id.rv_view);
        TextView tvName = view.findViewById(R.id.tv_name_view);
        TextView tvUsername = view.findViewById(R.id.tv_username_view);
        ImageView ivAvatar = view.findViewById(R.id.iv_avatar_view);

        //update the user info
        Model.instance().getUserById(userId, appUser -> {
            tvName.setText(appUser.getName());
            tvUsername.setText(appUser.getUsername());
            if (!appUser.avatarUrl.equals(""))
                new Handler(Looper.getMainLooper()).post(()->{
                    Glide.with(MyApplication.getMyContext())
                            .load(appUser.avatarUrl)
                            .circleCrop()
                            .into(ivAvatar);
                });
        });

        LiveData<List<Status>> data =  Model.instance().getAllStatusesByUser(userId);
        rvStatuses.setHasFixedSize(true);
        rvStatuses.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StatusRecyclerAdapter(getLayoutInflater(), data.getValue());
        rvStatuses.setAdapter(adapter);

        adapter.setOnItemClickListener(pos->{
            //do nothing, when item is clicked in while viewing profile
        });


        data.observe(getViewLifecycleOwner(), list->{
            adapter.setData(list);
        });

        return view;
    }

}