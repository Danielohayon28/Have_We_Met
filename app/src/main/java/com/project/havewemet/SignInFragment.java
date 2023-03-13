package com.project.havewemet;

import static com.project.havewemet.utils.Text.getStr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.havewemet.databinding.FragmentSignInBinding;
import com.project.havewemet.model.Model;

public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //set buttons click listeners
        binding.tvSignup.setOnClickListener(view1 ->
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment)
        );

        binding.btnSignIn.setOnClickListener(view2 ->{
            String email = getStr(binding.etEmail);
            String pass = getStr(binding.etPass);

            //just to test
            Model.instance().getUserByName(email, data -> {
                Toast.makeText(MyApplication.getMyContext(), data.username, Toast.LENGTH_SHORT).show();
            });
        });
        return view;
    }
}