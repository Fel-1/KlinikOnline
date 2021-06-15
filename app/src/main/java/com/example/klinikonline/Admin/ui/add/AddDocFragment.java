package com.example.klinikonline.Admin.ui.add;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.klinikonline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import ca.antonious.materialdaypicker.MaterialDayPicker;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddDocFragment extends Fragment implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 22;
    private TextView addPhotoFiller;
    private EditText EdtNama,EdtJadwal,EdtSpesialis;
    private Uri uri;
    DatabaseReference mDatabaseReference;

    CircleImageView circleImageView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        Button btn_add = root.findViewById(R.id.btn_add);
        addPhotoFiller = root.findViewById(R.id.textImage);
        circleImageView = root.findViewById(R.id.civ_image);
        EdtNama = root.findViewById(R.id.doc_edt_name);
        EdtJadwal = root.findViewById(R.id.doc_edt_jadwal);
        EdtSpesialis = root.findViewById(R.id.doc_edt_spesialis);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        btn_add.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_add){
            //upload data
            String nama = EdtNama.getText().toString().trim();
            String Jadwal = EdtJadwal.getText().toString().trim();
            String Spesialis = EdtSpesialis.getText().toString().trim();
            Map<String,String> objek = new HashMap<String, String>();
            objek.put("Jadwal",Jadwal);
            objek.put("Jenis",Spesialis);
            mDatabaseReference.child("Dokter").child(nama).setValue(objek);
            uploadImage();
        }else if(v.getId()==R.id.civ_image){
            //pick image
            SelectImage();
        }
    }
    private void SelectImage()
    {
        // Open Gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here...")
                , PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getActivity().getContentResolver(), uri);
                Picasso.get().load(uri).fit().centerCrop().into(circleImageView);
                addPhotoFiller.setVisibility(View.GONE);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage()
    {
        if (uri != null) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference mStorageReference = FirebaseStorage.getInstance().getReference().child("upload").child(System.currentTimeMillis() +"."+getFileExt(uri));
            mStorageReference.putFile(uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            mDatabaseReference.child("Dokter").child(EdtNama.getText().toString().trim()).child("Picture").setValue(url);
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Berhasil Ditambahkan", Toast.LENGTH_LONG).show();
                            EdtNama.setText(null);
                            EdtJadwal.setText(null);
                            EdtSpesialis.setText(null);
                            EdtSpesialis.clearFocus();
                            addPhotoFiller.setVisibility(View.VISIBLE);
                            circleImageView.setImageResource(R.drawable.ic_baseline_person_24);
                        }
                    });
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " +(int)progress + "%");
                        }
                    });
        }else{
            EdtNama.setText(null);
            EdtJadwal.setText(null);
            EdtSpesialis.setText(null);
            EdtSpesialis.clearFocus();
            addPhotoFiller.setVisibility(View.VISIBLE);
            circleImageView.setImageResource(R.drawable.ic_baseline_person_24);
            Toast.makeText(getActivity(), "Berhasil Ditambahkan", Toast.LENGTH_LONG).show();

        }
    }
    private String getFileExt(Uri imgUri) {
        ContentResolver mContentResolver = requireActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(mContentResolver.getType(imgUri));
    }

}