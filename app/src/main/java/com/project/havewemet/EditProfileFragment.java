package com.project.havewemet;

import static com.project.havewemet.utils.Text.getStr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.havewemet.databinding.FragmentEditProfileBinding;
import com.project.havewemet.model.AppUser;
import com.project.havewemet.model.Model;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;

    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);

        handler = new Handler(Looper.getMainLooper());
        View mainView = binding.getRoot();
        AppUser appUser = Model.instance().getSignedInAppUser();
        handler.post(()->{
            binding.etEditName.setText(appUser.getName());
            binding.etEditUsername.setText(appUser.getUsername());
            binding.etEditEmail.setText(Model.instance().getSignedInEmail());

            binding.tvSaveChangesEdit.setOnClickListener(view -> {
                String name = getStr(binding.etEditName );
                String username = getStr(binding.etEditUsername);
                String email = getStr(binding.etEditEmail);

                appUser.setName(name);
                appUser.setUsername(username); //update the appUser info to update it
                Model.instance().editProfile(appUser, email, bool -> {
                    Toast.makeText(MyApplication.getMyContext(),bool?"Profile Updated Successfully!":"Profile Update Failed!" , Toast.LENGTH_SHORT).show();
                });
            });

            Glide.with(MyApplication.getMyContext())
                    .load(appUser.avatarUrl)
                    .circleCrop()
                    .into(binding.ivAvatarEdit);

            Model.instance().refreshStatuses();
        });

        return mainView;
    }
}