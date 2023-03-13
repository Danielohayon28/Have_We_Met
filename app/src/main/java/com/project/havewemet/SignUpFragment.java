package com.project.havewemet;

import static com.project.havewemet.utils.Text.getStr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.havewemet.databinding.FragmentSignUpBinding;
import com.project.havewemet.model.AppUser;
import com.project.havewemet.model.Model;


public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSignUpBinding.inflate(inflater, container, false);

        //sign up Button Click listener
        binding.btnSignUp.setOnClickListener(view -> {
            String email = getStr(binding.etEmail);
            String name = getStr(binding.etFullname);
            String username = getStr(binding.etUsername);
            String pass = getStr(binding.etPass);
            String repass = getStr(binding.etRepeatPass);

            if (email.length() < 3 || !email.contains("@"))
                binding.etEmail.setError("Invalid Email");
            else if (name.length() < 3)
                binding.etFullname.setError("Invalid Name");
            else if (username.length() == 0)
                binding.etUsername.setError("Username Required");
            else if (pass.length() < 8)
                binding.etPass.setError("Password length must be 8 at least!");
            else if (!pass.equals(repass))
                binding.etPass.setError("Passwords don't match");
            else
                Model.instance().addAppUser(new AppUser(Long.toString(System.currentTimeMillis()), name, username, "", System.currentTimeMillis()),
                        data -> {
                            Log.e("test", "usercreated");});
        });

        View view = binding.getRoot();
        return view;
    }

}