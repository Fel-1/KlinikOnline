package com.example.klinikonline.Object;

public class User {
    private String Nomor,Nama,Email,Telepon,Alamat,Poli,Goldar,JenisKelamin,Keterangan;
    private int umur;

    public User() {
    }
    public User(String nomor, String nama, String telepon, String alamat, String poli, String goldar, String jenisKelamin, String keterangan,int umur) {
        Nama = nama;
        Telepon = telepon;
        Alamat = alamat;
        Poli = poli;
        Goldar = goldar;
        JenisKelamin = jenisKelamin;
        Keterangan = keterangan;
        this.umur = umur;
        Nomor = nomor;
    }

    public String getNomor() {
        return Nomor;
    }

    public void setNomor(String nomor) {
        Nomor = nomor;
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    public String getPoli() {
        return Poli;
    }

    public void setPoli(String poli) {
        Poli = poli;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelepon() {
        return Telepon;
    }

    public void setTelepon(String telepon) {
        Telepon = telepon;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getGoldar() {
        return Goldar;
    }

    public void setGoldar(String goldar) {
        Goldar = goldar;
    }

    public String getJenisKelamin() {
        return JenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        JenisKelamin = jenisKelamin;
    }
}
