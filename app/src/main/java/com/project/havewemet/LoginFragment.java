package com.project.havewemet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LoginFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        TextView tvSignUp = view.findViewById(R.id.tv_signup_);
        tvSignUp.setOnClickListener(view1 ->
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment)
        );
        return view;
    }
}