package com.example.klinikonline.Admin.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.klinikonline.Object.Layanan;
import com.example.klinikonline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    private HomeAdapter homeAdapter;
    View root;
    DatabaseReference mDatabaseReference;
    private List<Layanan> patientList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        if(!patientList.isEmpty()) {
            homeAdapter.clear();
        }
        fillRecyclerList();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_home_admin);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getActivity());
        homeAdapter = new HomeAdapter(patientList);
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter.setOnItemClickCallback(new HomeAdapter.OnItemClickCallback() {
            @Override
            public void itemClicked(Layanan data) {
                Bundle bundle = new Bundle();
                bundle.putString("Nama Poli",data.getNama());
                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_queueFragment,bundle);
            }
        });
        return root;

    }


    private void fillRecyclerList() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Layanan");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientList.clear();
                for (DataSnapshot ds :
                        snapshot.getChildren()) {
                    String nama = ds.getKey();
                    String status = ds.child("status").getValue(String.class);
                    GenericTypeIndicator<List<String>> mListGenericTypeIndicator = new GenericTypeIndicator<List<String>>() {};
                    List<String> list = ds.child("queue").getValue(mListGenericTypeIndicator);
                    Layanan layanan = new Layanan(nama,status,list);
                    patientList.add(layanan);
                    homeAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}