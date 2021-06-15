package com.example.klinikonline.Admin.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.klinikonline.Object.Layanan;
import com.example.klinikonline.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private final List<Layanan> patientList;
    private OnItemClickCallback onItemClickCallback;
    public static final String TAG = "HomeAdapter";

    public HomeAdapter(List<Layanan> patientList) {
        this.patientList = patientList;
    }

    public interface OnItemClickCallback{
        void itemClicked(Layanan data);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }
    @NonNull
    @Override
    public HomeAdapter.HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_admin_home,parent,false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeHolder holder, int position) {
        Layanan layanan = patientList.get(position);
        int size = layanan.getPatientList().size()-1;
        String temp = size+" Pasien Sedang Mengantri";
        holder.showName.setText(layanan.getNama());
        holder.showPatient.setText(temp);
        logoPicker(layanan.getNama(), holder);
        Log.e(TAG,layanan.getStatus());
        if(layanan.getStatus().equals("on")){
            holder.onOrOff.setImageResource(R.color.light_green);
        }else if(layanan.getStatus().equals("off")){
            holder.onOrOff.setImageResource(R.color.red);
        }
        holder.itemView.setOnClickListener(v -> onItemClickCallback.itemClicked(patientList.get(holder.getAdapterPosition())));
    }

    private void logoPicker(String nama, HomeHolder holder) {
        switch (nama){
            case "Poli Mata":
                holder.iconCIV.setImageResource(R.drawable.ic_eye);
                break;
            case "Poli Gigi":
                holder.iconCIV.setImageResource(R.drawable.ic_tooth);
                break;
            case "Poli Umum":
                holder.iconCIV.setImageResource(R.drawable.ic_doctor__1_);
                break;
            case "Poli Penyakit Dalam":
                holder.iconCIV.setImageResource(R.drawable.ic_human_organs);
                break;
            case "Vaksin Covid-19":
                holder.iconCIV.setImageResource(R.drawable.ic_vaccine);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public static class HomeHolder extends RecyclerView.ViewHolder {
        TextView showName, showPatient;
        CircleImageView iconCIV,onOrOff;
        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            showName =itemView.findViewById(R.id.tv_poli);
            showPatient = itemView.findViewById(R.id.tv_patient);
            iconCIV = itemView.findViewById(R.id.imageView2);
            onOrOff = itemView.findViewById(R.id.circleImageView);
        }
    }
    public void clear() {
        int size = patientList.size();
        patientList.clear();
        notifyItemRangeRemoved(0, size);
    }
}
