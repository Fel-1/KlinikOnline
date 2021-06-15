package com.example.klinikonline.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.klinikonline.MainActivity;
import com.example.klinikonline.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class UserPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavController navController;
    FirebaseUser mFirebaseUser;
    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.user_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
               //fragment here
                R.id.nav_home,R.id.nav_daftar,R.id.nav_doc,R.id.nav_map)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.nav_header_name);
        TextView email = header.findViewById(R.id.nav_header_email);
        if (mFirebaseUser != null) {
            name.setText(mFirebaseUser.getDisplayName());
            email.setText(mFirebaseUser.getEmail());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout :
                FirebaseMessaging.getInstance().unsubscribeFromTopic(mFirebaseUser.getUid());
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserPage.this, MainActivity.class));
                finish();
                break;
            default :
                NavigationUI.onNavDestinationSelected(item,navController);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.user_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.user_drawer_layout);

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}