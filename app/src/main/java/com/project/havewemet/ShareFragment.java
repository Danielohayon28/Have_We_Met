package com.project.havewemet;

import static com.project.havewemet.utils.Text.getStr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.havewemet.databinding.FragmentShareBinding;
import com.project.havewemet.model.Model;
import com.project.havewemet.model.Status;

public class ShareFragment extends Fragment {

    private FragmentShareBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShareBinding.inflate(inflater, container, false);
        View thisView = binding.getRoot();

        binding.tvStatus.setOnClickListener(view -> {
            String statusText = getStr(binding.etStatus);
            if (statusText.length() == 0)
                binding.etStatus.setError("Enter your status here!");
            else{
                //share and move to the next page
                Status status = new Status(Long.toString(System.currentTimeMillis()),
                        Model.instance().getUserId(), statusText);
                Model.instance().addStatus(status, nothing ->{
                    Navigation.findNavController(thisView).navigate(R.id.action_shareFragment_to_viewStatusesFragment);
                });
            }
        });

        return thisView;
    }
}