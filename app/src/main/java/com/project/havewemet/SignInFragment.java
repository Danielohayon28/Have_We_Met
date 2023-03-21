package com.project.havewemet;

import static com.project.havewemet.utils.Text.getStr;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.havewemet.databinding.FragmentSignInBinding;
import com.project.havewemet.model.AppUser;
import com.project.havewemet.model.Model;
import com.project.havewemet.model.Status;

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
        View mainView = binding.getRoot();

        //set buttons click listeners
        binding.tvSignup.setOnClickListener(view1 ->
                Navigation.findNavController(mainView).navigate(R.id.action_loginFragment_to_signUpFragment)
        );

        binding.btnSignIn.setOnClickListener(view ->{
            String email = getStr(binding.etEmail);
            String pass = getStr(binding.etPass);
            if (email.length() == 0 || !email.contains("@"))
                binding.etEmail.setError("Required!");
            else if(pass.length() == 0)
                binding.etPass.setError("Required!");
            else{
                //disable the sign in button until signing in result listener is called
                binding.btnSignIn.setEnabled(false);
                binding.btnSignIn.setText(R.string.signing_in);
                Model.instance().signIn(email, pass, bool -> {
                    // now user can share the status
                    if (bool)
                        moveToNextFragment();
                    else
                        Toast.makeText(MyApplication.getMyContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    binding.btnSignIn.setEnabled(true);
                    binding.btnSignIn.setText(R.string.sign_in);
                });
            }
        });

        //as user first lands on this fragment, so skip this fragment to share fragment if user already signed in
        //if user is already signed he/she does not need to signin again

        new Handler(Looper.getMainLooper()).post(()->{
            if (Model.instance().isSignedIn())
                moveToNextFragment();
        });
        //to complete this function we have passed the navigation on Handler thread

        return mainView;
    }

    private void moveToNextFragment(){
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.popBackStack(R.id.signinFragment, true);
        navController.navigate(R.id.shareFragment);
    }
}

//    lse {
//        Toast.makeText(MyApplication.getMyContext(), "Authentication succeeded", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(MyApplication.getMyContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        getActivity().finish();
//    }