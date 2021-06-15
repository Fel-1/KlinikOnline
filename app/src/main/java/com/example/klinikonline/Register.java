package com.example.klinikonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.klinikonline.User.UserPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Register";
    EditText EdtNama,EdtEmail,EdtPass,EdtRePass;
    private boolean isEmpty;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //resource variable

        Button BtnRegister = findViewById(R.id.signup_btn);
        EdtNama = findViewById(R.id.name_edt_text);
        EdtPass = findViewById(R.id.pass_edt_text);
        EdtEmail = findViewById(R.id.email_edt_text);
        EdtRePass = findViewById(R.id.repass_edt_text);
        //firebase variable
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //onClickListener
        BtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signup_btn){
            isEmpty = false;
            String Nama = EdtNama.getText().toString();
            String Email = EdtEmail.getText().toString();
            String Pass = EdtPass.getText().toString();
            String Repass = EdtRePass.getText().toString();
            if(!Repass.equals(Pass)){
                EdtRePass.setError("Password Tidak Sesuai");
                EdtRePass.requestFocus();
                EdtRePass.addTextChangedListener(ConfirmPassword);
                EdtPass.addTextChangedListener(ConfirmPassword);
                isEmpty=true;
            }
            if(TextUtils.isEmpty(Repass)){
                showEmptyError(EdtRePass);
            }
            if(TextUtils.isEmpty(Pass)){
                showEmptyError(EdtPass);
            }
            if(TextUtils.isEmpty(Email)){
                showEmptyError(EdtEmail);
            }
            if(TextUtils.isEmpty(Nama)){
                showEmptyError(EdtNama);
            }

            if(!isEmpty){
                mFirebaseAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Akun Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                        mFirebaseAuth.signInWithEmailAndPassword(Email, Pass);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileupdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(Nama)
                                .build();
                        if (user != null) {
                            user.updateProfile(profileupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(Register.this, UserPage.class);
                                    startActivity(intent);
                                    finishAffinity();
                                    finish();
                                }
                            });
                        }

                    }else{
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            EdtPass.setError("Panjang Password harus lebih dari 6 karakter");
                            EdtPass.requestFocus();
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            EdtEmail.setError("Mohon periksa kembali E-mail yang dimasukkan");
                            EdtEmail.requestFocus();
                        } catch(FirebaseAuthUserCollisionException e) {
                            EdtEmail.setError("E-mail sudah terdaftar, Silahkan Login");
                            EdtEmail.requestFocus();
                        } catch(Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
            }
        }
    }
    TextWatcher ConfirmPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            EdtRePass.setError(null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private void showEmptyError(EditText edtText) {
        edtText.setError("Lengkapi Data");
        edtText.requestFocus();
        isEmpty=true;
        edtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}