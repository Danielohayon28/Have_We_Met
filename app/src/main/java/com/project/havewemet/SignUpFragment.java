package com.project.havewemet;

import static com.project.havewemet.utils.Text.getStr;
import static com.project.havewemet.utils.Text.hidePassword;
import static com.project.havewemet.utils.Text.showPassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.havewemet.databinding.FragmentSignUpBinding;
import com.project.havewemet.model.AppUser;
import com.project.havewemet.model.Model;

import java.util.concurrent.atomic.AtomicBoolean;


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

        View mainView = binding.getRoot();


        AtomicBoolean passwordVisible = new AtomicBoolean(false);
        binding.ivEyePass.setOnClickListener(view -> {
            if (passwordVisible.get()){
                passwordVisible.set(true);
                showPassword(binding.etPass);
            }else{
                passwordVisible.set(false);
                hidePassword(binding.etPass);
            }
        });

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
            else{
                binding.btnSignUp.setText(R.string.signing_in);
                binding.btnSignUp.setEnabled(false);
                Model.instance().signUp(email, pass, new AppUser(Long.toString(System.currentTimeMillis()), name, username, "", System.currentTimeMillis()), bool -> {
                    if (bool)
                        Navigation.findNavController(mainView).navigate(R.id.action_signUpFragment_to_shareFragment);
                    else
                        Toast.makeText(getContext(), "Failed to Sign Up!", Toast.LENGTH_SHORT).show();
                    //again allow to click sign up button
                    binding.btnSignUp.setText(R.string.sign_up);
                    binding.btnSignUp.setEnabled(true);
                });
            }
        });
        return mainView;
    }

}