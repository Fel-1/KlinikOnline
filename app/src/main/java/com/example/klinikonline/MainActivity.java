package com.example.klinikonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.klinikonline.Admin.NavDrawerAdmin;
import com.example.klinikonline.User.UserPage;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private FirebaseAuth firebaseAuth;
    private EditText EmailEdt;
    private EditText PassEdt;
    private Button signin;
    DatabaseReference dbaseref;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_KlinikOnline);
        setContentView(R.layout.activity_main);
        dbaseref = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        EmailEdt = findViewById(R.id.email_edt_text);
        PassEdt = findViewById(R.id.pass_edt_text);
        signin = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.login_loading);
        signin.setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.btn_sign_up).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            if(user.getEmail().equals("admin@admin.com")){
                Intent adminLogin = new Intent(MainActivity.this, NavDrawerAdmin.class);
                startActivity(adminLogin);
                finish();
            }else{
                Intent userLogin = new Intent(MainActivity.this, UserPage.class);
                startActivity(userLogin);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.login_btn){
            final String email = EmailEdt.getText().toString();
            final String pass = PassEdt.getText().toString();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                if(TextUtils.isEmpty(pass)){
                    PassEdt.setError("Please Fill all the field");
                    PassEdt.requestFocus();
                    PassEdt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            PassEdt.setError(null);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
                if(TextUtils.isEmpty(email)) {
                    EmailEdt.setError("Please Fill all the field");
                    EmailEdt.requestFocus();
                    EmailEdt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            EmailEdt.setError(null);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }else {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                if(user.getEmail().equals("admin@admin.com")){
                                    Intent adminLogin = new Intent(MainActivity.this, NavDrawerAdmin.class);
                                    startActivity(adminLogin);
                                    finish();
                                }else{
                                    FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());
                                    Intent userLogin = new Intent(MainActivity.this,UserPage.class);
                                    startActivity(userLogin);
                                    finish();
                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid Username/Password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }else if(v.getId()==R.id.btn_sign_up){
            startActivity(new Intent(MainActivity.this,Register.class));
        }
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}