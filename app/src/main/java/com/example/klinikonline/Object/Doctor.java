package com.example.klinikonline.Object;

import android.net.Uri;

public class Doctor {
    private String nama,jenis,jadwal;
    private Uri uri;

    public Doctor(String nama, String jenis, String jadwal, Uri uri) {
        this.nama = nama;
        this.jenis = jenis;
        this.jadwal = jadwal;
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

}
