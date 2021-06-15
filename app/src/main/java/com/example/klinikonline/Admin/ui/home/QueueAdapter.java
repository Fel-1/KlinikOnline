package com.example.klinikonline.Admin.ui.home;

import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.klinikonline.Object.User;
import com.example.klinikonline.R;

import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueHolder>{
    private final List<User> userList;
    public static final String TAG = "QueueAdapter";
    Dialog mDialog;
    TextView DialogNomor,DialogNama, DialogUmur,DialogGender,DialogGoldar,DialogAlamat,DialogPhone,DialogDesc, NomorTV;
    public QueueAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public QueueAdapter.QueueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_admin_queue,parent,false);
        mDialog = new Dialog(parent.getContext());
        mDialog.setContentView(R.layout.dialog_antrian);
        DialogNomor = mDialog.findViewById(R.id.tv_dialog_nomor);
        DialogNama = mDialog.findViewById(R.id.tv_dialog_nama);
        DialogUmur = mDialog.findViewById(R.id.tv_dialog_umur);
        DialogGender = mDialog.findViewById(R.id.tv_dialog_gender);
        DialogGoldar = mDialog.findViewById(R.id.tv_dialog_goldar);
        DialogAlamat = mDialog.findViewById(R.id.tv_dialog_alamat);
        DialogPhone = mDialog.findViewById(R.id.tv_dialog_notelp);
        DialogDesc = mDialog.findViewById(R.id.tv_dialog_desc);

        NomorTV = mDialog.findViewById(R.id.tvNomorBPJS );

        return new QueueAdapter.QueueHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueAdapter.QueueHolder holder, int position) {
        User user = userList.get(position);
        if(user!=null) {
            holder.showName.setText(user.getNama());
            holder.showDesc.setText(user.getKeterangan());
            holder.showNum.setText(String.valueOf(position + 1));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(user.getNomor().equals("")){
                        NomorTV.setVisibility(View.GONE);
                        DialogNomor.setVisibility(View.GONE);
                    }else{
                        NomorTV.setVisibility(View.VISIBLE);
                        DialogNomor.setVisibility(View.VISIBLE);
                    }
                    DialogNomor.setText(user.getNomor());
                    DialogNama.setText(user.getNama());
                    DialogAlamat.setText(user.getAlamat());
                    DialogDesc.setText(user.getKeterangan());
                    DialogGender.setText(user.getJenisKelamin());
                    DialogGoldar.setText(user.getGoldar());
                    DialogUmur.setText(String.valueOf(user.getUmur()));
                    DialogPhone.setText(user.getTelepon());
                    mDialog.show();
                }
            });
            if (position == 0) {
                holder.mCardView.setCardBackgroundColor(Color.parseColor("#48f76b"));
                holder.showStatus.setVisibility(View.VISIBLE);
            } else {
                holder.mCardView.setCardBackgroundColor(Color.WHITE);
                holder.showStatus.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class QueueHolder extends RecyclerView.ViewHolder {
        TextView showName, showDesc,showNum,showStatus;
        CardView mCardView;
        public QueueHolder(@NonNull View itemView) {
            super(itemView);
            showName = itemView.findViewById(R.id.tv_nama);
            showDesc = itemView.findViewById(R.id.tv_keterangan);
            showNum = itemView.findViewById(R.id.textView7);
            showStatus = itemView.findViewById(R.id.textView8);
            mCardView = itemView.findViewById(R.id.queue_card_view);

        }
    }
    public void clear() {
        int size = userList.size();
        userList.clear();
        notifyItemRangeRemoved(0, size);
    }
}
