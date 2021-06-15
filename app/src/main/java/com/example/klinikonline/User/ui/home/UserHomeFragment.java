package com.example.klinikonline.User.ui.home;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.klinikonline.Object.Layanan;
import com.example.klinikonline.Object.User;
import com.example.klinikonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHomeFragment extends Fragment {
    View view;
    DatabaseReference mDatabaseReference;
    public static final String TAG = "UserHomeFragment";
    TextView title, queueNumber,placeHolder,queueStatus;
    FirebaseUser mFirebaseUser;
    private boolean dialogShown; //check have the dialog been shown or not
    private List<String> queueList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_home, container, false);
        title = view.findViewById(R.id.tv_jenis_antrian);
        placeHolder = view.findViewById(R.id.placeholder);
        queueNumber = view.findViewById(R.id.tv_nomor_antrian);
        queueStatus = view.findViewById(R.id.QueueStatus);
        placeHolder.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDatabaseReference.child("User").child(mFirebaseUser.getUid()).addValueEventListener(mValueEventListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        dialogShown = false;
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }
    ValueEventListener mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.getValue()!=null){
                User user = snapshot.getValue(User.class);
                String foo = "Antrian " +user.getPoli();
                title.setText(foo);

                if (user.getPoli() != null) {
                    mDatabaseReference.child("Layanan").child(user.getPoli()).child("queue").addValueEventListener(queueNumberListener);
                    mDatabaseReference.child("Layanan").child(user.getPoli()).child("status").addValueEventListener(queueStatusListener);
                }else{
                    Log.e(TAG,"Polinya Null");
                }
            }else{
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_daftar);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mDatabaseReference.child("User").child(mFirebaseUser.getUid()).removeEventListener(mValueEventListener);
    }
    ValueEventListener queueNumberListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            //Make GenericTypeIndicator and take data from database then convert to Generic Type Indicator
            GenericTypeIndicator<List<String>> objectsGTypeInd = new GenericTypeIndicator<List<String>>() {};
            queueList = snapshot.getValue(objectsGTypeInd);

            int antri = queueList.indexOf(mFirebaseUser.getUid());
            queueNumber.setText(String.valueOf(antri));
            placeHolder.setVisibility(View.GONE);
            if(antri==1){
                if(getActivity()!=null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage("GILIRAN ANDA TELAH TIBA");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = alert.create();
                    if (!getActivity().isFinishing()) {
                        if(!dialogShown){
                            dialog.show();
                        }
                        dialogShown=true;
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    ValueEventListener queueStatusListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String status = snapshot.getValue(String.class);
            if (status != null) {
                if(status.equals("on")){
                    queueStatus.setText(R.string.antrian_sedang_aktif);
                }else if(status.equals("off")){
                    queueStatus.setText(R.string.antrian_sedang_tidak_aktif);

                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}