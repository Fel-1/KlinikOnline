package com.example.klinikonline.User.ui.daftar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.klinikonline.Object.User;
import com.example.klinikonline.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DaftarFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "DaftarFragment" ;
    private View view;
    private boolean isEmpty;
    String[] gender = {"Jenis Kelamin","Laki-Laki","Perempuan"};
    String[] blood = {"Golongan Darah","A","B","AB","O"};
    ArrayList<String> poli = new ArrayList<>();
    Spinner SpinnerPoli,SpinnerGender,SpinnerBlood;
    EditText EdtNama,EdtTelp,EdtAlamat,EdtDesc,EdtUmur;
    TextInputLayout nomorTextInputLayout;
    DatabaseReference mDatabaseReference;
    FirebaseUser user;
    List<String> queue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_daftar, container, false);
        Button btnDaftar = view.findViewById(R.id.btn_create);
        RadioGroup radioGroup = view.findViewById(R.id.is_bpjs_group);
        RadioButton rbBPJS = view.findViewById(R.id.bpjs);
        RadioButton rbNonBPJS = view.findViewById(R.id.non_bpjs);

        SpinnerGender = view.findViewById(R.id.gender_spinner);
        SpinnerBlood = view.findViewById(R.id.blood_spinner);
        SpinnerPoli = view.findViewById(R.id.poli_spinner);
        EdtNama = view.findViewById(R.id.name_edt_text);
        EdtTelp = view.findViewById(R.id.telp_edt_text);
        EdtAlamat = view.findViewById(R.id.alamat_edt_txt);
        EdtDesc = view.findViewById(R.id.extra_edt_txt);
        EdtUmur = view.findViewById(R.id.umur_edt_text);

        nomorTextInputLayout = view.findViewById(R.id.nomor_edt_text);
        //spinner arrayadapter
        ArrayAdapter AAPoli = new ArrayAdapter(getActivity(), R.layout.custom_spinner_list ,poli);
        ArrayAdapter AAgender = new ArrayAdapter(getActivity(), R.layout.custom_spinner_list,gender);
        ArrayAdapter AAblood = new ArrayAdapter(getActivity(), R.layout.custom_spinner_list,blood);

        AAPoli.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        AAgender.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        AAblood.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        SpinnerPoli.setAdapter(AAPoli);
        SpinnerGender.setAdapter(AAgender);
        SpinnerBlood.setAdapter(AAblood);

        btnDaftar.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.bpjs:
                    nomorTextInputLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.non_bpjs:
                    nomorTextInputLayout.getEditText().setText(null);
                    nomorTextInputLayout.setVisibility(View.GONE);
                    break;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDatabaseReference.child("User").child(user.getUid()).addValueEventListener(mValueEventListener);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        poli.add("Jenis Poli");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("Layanan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds :
                        snapshot.getChildren()) {
                    String nama = ds.getKey();
                    poli.add(nama);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mDatabaseReference.child("User").child(user.getUid()).removeEventListener(mValueEventListener);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_create){
            isEmpty = false;
            String Nama = EdtNama.getText().toString().trim();
            String Telepon = EdtTelp.getText().toString().trim();
            String Alamat = EdtAlamat.getText().toString().trim();
            String Poli = SpinnerPoli.getSelectedItem().toString();
            String Blood = SpinnerBlood.getSelectedItem().toString();
            String Gender = SpinnerGender.getSelectedItem().toString();
            String Desc = EdtDesc.getText().toString().trim();
            String Nomor = nomorTextInputLayout.getEditText().getText().toString().trim();
            int Umur = Integer.parseInt(EdtUmur.getText().toString());
            if(Blood.equals("Golongan Darah")){
                Blood = "Tidak Diketahui";
            }
            if(Gender.equals("Jenis Kelamin")){
                ((TextView)SpinnerGender.getSelectedView()).setError("Pilih Jenis Kelamin Pasien");
                isEmpty=true;
            }
            if(Poli.equals("Jenis Poli")){
                ((TextView)SpinnerPoli.getSelectedView()).setError("Pilih Jenis Poli");
                isEmpty=true;
            }
            if(TextUtils.isEmpty(Alamat)){
                showEmptyError(EdtAlamat);
            }
            if(TextUtils.isEmpty(Telepon)){
                showEmptyError(EdtTelp);
            }
            if(TextUtils.isEmpty(Nama)){
                showEmptyError(EdtNama);
            }
            if(nomorTextInputLayout.getVisibility() == View.VISIBLE
                && TextUtils.isEmpty(Nomor)){
                showEmptyError(nomorTextInputLayout.getEditText());
            }
            if(!isEmpty){
                User userdata = new User(Nomor,Nama,Telepon,Alamat,Poli,Blood,Gender,Desc,Umur);

                if (user != null) {
                    mDatabaseReference.child("User").child(user.getUid()).setValue(userdata);
                    mDatabaseReference.child("Layanan").child(Poli).child("queue").get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            GenericTypeIndicator<List<String>> ListGTI = new GenericTypeIndicator<List<String>>() {};
                            queue = task.getResult().getValue(ListGTI);
                            queue.add(user.getUid());
                            mDatabaseReference.child("Layanan").child(Poli).child("queue").setValue(queue);
                        }
                    });
                }

            }
        }
    }
    ValueEventListener mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.getValue()!=null){
                Navigation.findNavController(view).navigate(R.id.action_nav_daftar_to_nav_home);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    private void showEmptyError(EditText edtText) {
        edtText.setError("Lengkapi Data Pasien");
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