package com.project.havewemet;

import static com.project.havewemet.utils.Text.getStr;
import static com.project.havewemet.utils.Text.hidePassword;
import static com.project.havewemet.utils.Text.showPassword;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.havewemet.databinding.FragmentSignUpBinding;
import com.project.havewemet.model.AppUser;
import com.project.havewemet.model.Model;

import java.util.concurrent.atomic.AtomicBoolean;


public class SignUpFragment extends Fragment {


    ActivityResultLauncher<Void> cameraLauncher;
    ActivityResultLauncher<String> galleryLauncher;
    private FragmentSignUpBinding binding;

    private boolean isAvatarSelected = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null){
                Glide.with(MyApplication.getMyContext())
                        .load(result)
                        .circleCrop()
                        .into(binding.ivAvatar);
                isAvatarSelected = true;
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Glide.with(MyApplication.getMyContext())
                    .load(result)
                    .circleCrop()
                    .into(binding.ivAvatar);
            isAvatarSelected = true;
        });
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

        binding.ivAvatar.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.getMenu().add("camera");
            popupMenu.getMenu().add("gallery");
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getTitle().equals("camera"))
                    cameraLauncher.launch(null);
                else if(menuItem.getTitle().equals("gallery"))
                    galleryLauncher.launch("image/*");
                return true;
            });
            popupMenu.show();
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
                final AppUser appUser = new AppUser(name, username);
                if (isAvatarSelected){
                    binding.ivAvatar.setDrawingCacheEnabled(true);
                    binding.ivAvatar.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) binding.ivAvatar.getDrawable()).getBitmap();
                    Model.instance().uploadImage(appUser.getUsername(), bitmap, url->{
                        if (url != null)
                            appUser.setAvatarUrl(url);
                        Model.instance().signUp(email, pass, appUser, bool -> {
                            if (bool)
                                Navigation.findNavController(mainView).navigate(R.id.action_signUpFragment_to_shareFragment);
                            else
                                Toast.makeText(getContext(), "Failed to Sign Up!", Toast.LENGTH_SHORT).show();
                            //again allow to click sign up button
                            binding.btnSignUp.setText(R.string.sign_up);
                            binding.btnSignUp.setEnabled(true);
                        });
                    });
                }else {
                    Toast.makeText(MyApplication.getMyContext(), "Avatar is not selected!", Toast.LENGTH_SHORT).show();
                    Model.instance().signUp(email, pass, appUser, bool -> {
                        Toast.makeText(MyApplication.getMyContext(), "booL: "+bool, Toast.LENGTH_SHORT).show();
                        if (bool)
                            Navigation.findNavController(mainView).navigate(R.id.action_signUpFragment_to_shareFragment);
                        else
                            Toast.makeText(getContext(), "Failed to Sign Up!", Toast.LENGTH_SHORT).show();
                        //again allow to click sign up button
                        binding.btnSignUp.setText(R.string.sign_up);
                        binding.btnSignUp.setEnabled(true);
                    });
                }
            }
        });
        return mainView;
    }
}