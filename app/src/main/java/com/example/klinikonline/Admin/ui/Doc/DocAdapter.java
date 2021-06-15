package com.example.klinikonline.Admin.ui.Doc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.klinikonline.Object.Doctor;
import com.example.klinikonline.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.DocHolder> {
    private final List<Doctor> doctorList;

    public DocAdapter(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public DocAdapter.DocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_admin_doc,parent,false);
        return new DocHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocAdapter.DocHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.showName.setText(doctor.getNama());
        holder.showJob.setText(doctor.getJenis());
        holder.showSchedule.setText(doctor.getJadwal());
        Picasso.get()
                .load(doctor.getUri()).fit()
                .into(holder.showImage);
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class DocHolder extends RecyclerView.ViewHolder {
        TextView showName, showJob,showSchedule;
        CircleImageView showImage;
        public DocHolder(@NonNull View itemView) {
            super(itemView);
            showName =itemView.findViewById(R.id.tv_name);
            showJob = itemView.findViewById(R.id.tv_job);
            showSchedule = itemView.findViewById(R.id.tv_schedule);
            showImage = itemView.findViewById(R.id.iv_image);
        }
    }
    public void clear(){
        int size = doctorList.size();
        doctorList.clear();
        notifyItemRangeRemoved(0, size);
    }
}
