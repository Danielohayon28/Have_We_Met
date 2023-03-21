package com.project.havewemet;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.project.havewemet.model.AppUser;
import com.project.havewemet.model.Model;
import com.project.havewemet.model.Status;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //    FirebaseApp.initializeApp(this);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.main_navhost);

        ImageView ivMenu = findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.inflate(R.menu.main_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.menu_signout:
                        Model.instance().signout();
                        new Handler(Looper.getMainLooper()).postDelayed(()->{
                            Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }, 500);
                        break;
                    case R.id.menu_myprofile:
                        navHostFragment.getNavController().navigate(R.id.editProfileFragment);
                    case R.id.menu_api:
                        navHostFragment.getNavController().navigate(R.id.ExternalApiFragment);
                        Log.d("TAG","dsadsadsa");
                }
                return true;
            }); // end of popup menu click listener
            popupMenu.show();
        }); //end of ivMenu click listener,(lambda)

    }
}