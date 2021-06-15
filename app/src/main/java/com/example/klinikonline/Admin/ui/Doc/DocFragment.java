package com.example.klinikonline.Admin.ui.Doc;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.klinikonline.Admin.ui.Doc.DocAdapter;
import com.example.klinikonline.Object.Doctor;
import com.example.klinikonline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DocFragment extends Fragment {
    public static final String TAG = "DocFragment";
    private DocAdapter docAdapter;
    View root;
    DatabaseReference mDatabaseReference;
    private List<Doctor> doctorList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_doc, container, false);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Dokter");
        mDatabaseReference.keepSynced(true);

        if(!doctorList.isEmpty()){
            docAdapter.clear();
        }
        fillRecyclerList();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_doc_admin);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getActivity());
        docAdapter = new DocAdapter(doctorList);
        recyclerView.setAdapter(docAdapter);
        recyclerView.setLayoutManager(layoutManager);
        return root;
    }

    private void fillRecyclerList() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctorList.clear();
                for (DataSnapshot ds :
                        snapshot.getChildren()) {
                    String nama = ds.getKey();
                    String jadwal = ds.child("Jadwal").getValue().toString();
                    String jenis = ds.child("Jenis").getValue().toString();
                    Uri uri;
                    if(ds.child("Picture").getValue()==null){
                        uri = Uri.parse("android.resource://com.example.klinikonline/drawable/default_pic");
                    }else{
                        uri = Uri.parse(ds.child("Picture").getValue().toString());
                    }
                    Doctor doctor = new Doctor(nama,jenis,jadwal,uri);
                    doctorList.add(doctor);
                    docAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}