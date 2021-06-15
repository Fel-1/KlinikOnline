package com.example.klinikonline.Admin.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.klinikonline.Object.User;
import com.example.klinikonline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueueFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "QueueFragment";
    private QueueAdapter queueAdapter;
    List<String> queueList = new ArrayList<>();
    DatabaseReference mDatabaseReference;
    private String layananType;
    Button nextbtn,btnOnOff;
    private List<User> userList = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_queue, container, false);
        nextbtn = view.findViewById(R.id.btn_next);
        btnOnOff = view.findViewById(R.id.btn_on_off);
        btnOnOff.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        if (getArguments() != null) {
            layananType = getArguments().getString("Nama Poli");
        }
        RecyclerView recyclerView = view.findViewById(R.id.recycler_queue_admin);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getActivity());
        queueAdapter = new QueueAdapter(userList);
        recyclerView.setAdapter(queueAdapter);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    private void fillRecyclerList() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("Layanan").child(layananType).child("status").addValueEventListener(onOffListener);
        mDatabaseReference.child("Layanan").child(layananType).child("queue").addValueEventListener(queueUID);
    }

    @Override
    public void onResume() {
        super.onResume();
        fillRecyclerList();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDatabaseReference.child("Layanan").child(layananType).child("queue").removeEventListener(queueUID);
        mDatabaseReference.child("Layanan").child(layananType).child("status").removeEventListener(onOffListener);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_next){
            if(queueList!=null&&queueList.size()>0){
                List<String> tempQueueList = queueList;
                String deletedUID = tempQueueList.get(0);
                tempQueueList.remove(0);
                if(tempQueueList.size()>0){
                    String first = tempQueueList.get(0);
                    sendNotification(first);
                }
                tempQueueList.add(0,"placehold");
                userList.remove(0);
                mDatabaseReference.child("Layanan").child(layananType).child("queue").setValue(tempQueueList);
                mDatabaseReference.child("User").child(deletedUID).setValue(null);
                queueAdapter.notifyDataSetChanged();

            }else{
                Toast.makeText(getActivity(),"Antrian Sudah Kosong",Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId()==R.id.btn_on_off){
            if(btnOnOff.getText().equals("Nyalakan Antrian")){
                mDatabaseReference.child("Layanan").child(layananType).child("status").setValue("on");
            }else{
                mDatabaseReference.child("Layanan").child(layananType).child("status").setValue("off");
            }
        }
    }

    private void sendNotification(String first) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("to","/topics/"+first);
            JSONObject notif = new JSONObject();
            notif.put("title",layananType);
            notif.put("body","Giliran Anda Telah Tiba");
            jsonObject.put("notification",notif);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("MUR","onResponse:");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR","onError : "+error.networkResponse);

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAVedAMt4:APA91bGmZrNUuy-DOKwhSkEb5xqms-Nr0CcQrsqvGIHMVpXT2PkHtvY6hGp9JArV8PdKT8UXcfuyyjgohQNcHhbB1k1K40yOx_F8RkriXDRpg1TeROTpfV5WB0GSMT8hZlT047Afwy_5");
                    return header;
                }
            };
            mRequestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    ValueEventListener queueUID= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            GenericTypeIndicator<List<String>> listGenericTypeIndicator = new GenericTypeIndicator<List<String>>() {};
            queueList = snapshot.getValue(listGenericTypeIndicator);
            if (queueList != null) {
                queueList.remove(0);
                userList.clear();
                for (String UID : queueList) {
                    mDatabaseReference.child("User").child(UID).get().addOnCompleteListener(task -> {
                        User user = task.getResult().getValue(User.class);
                        userList.add(user);
                        queueAdapter.notifyDataSetChanged();
                    });
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    ValueEventListener onOffListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String status = snapshot.getValue(String.class);
            if (status != null) {
                if(status.equals("off")){
                    nextbtn.setEnabled(false);
                    btnOnOff.setText(R.string.nyalakan_antrian);
                }else if(status.equals("on")){
                    nextbtn.setEnabled(true);
                    btnOnOff.setText(R.string.matikan_antrian);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}