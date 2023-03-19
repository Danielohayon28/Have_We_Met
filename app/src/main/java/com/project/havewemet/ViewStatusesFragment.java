package com.project.havewemet;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.havewemet.databinding.FragmentViewProfileBinding;
import com.project.havewemet.databinding.FragmentViewStatusesBinding;
import com.project.havewemet.model.Model;
import com.project.havewemet.model.Status;

import java.util.LinkedList;
import java.util.List;

public class ViewStatusesFragment extends Fragment {

    private StatusRecyclerAdapter adapter;
    private FragmentViewStatusesBinding binding;

    private StatusListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewStatusesBinding.inflate(inflater, container, false);
        View mainView = binding.getRoot();

        binding.rvStatuses.setHasFixedSize(true);
        binding.rvStatuses.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StatusRecyclerAdapter(getLayoutInflater(), viewModel.getStatusData().getValue());
        binding.rvStatuses.setAdapter(adapter);

        binding.progressBar.setVisibility(View.GONE);

        viewModel.getStatusData().observe(getViewLifecycleOwner(),list->{
            adapter.setData(list);
        });

        Model.instance().EventStatusesListLoadingState.observe(getViewLifecycleOwner(),status->{
            binding.srlStatuses.setRefreshing(status == Model.LoadingState.LOADING);
        });

        binding.srlStatuses.setOnRefreshListener(()->{
            reloadData();
        });
            // todo: write code similar to this one as commented out
//        LiveData<List<Movie>> data = MovieModel.instance.searchMoviesByTitle("avatar");
//        data.observe(getViewLifecycleOwner(),list->{
//            list.forEach(item->{
//                Log.d("TAG","got movie: " + item.getTitle() + " " + item.getPoster());
//            });
//        });


        return mainView;
    }

    void reloadData(){
        Model.instance().refreshStatuses();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        viewModel = new ViewModelProvider(this).get(StatusListViewModel.class);
    }
}