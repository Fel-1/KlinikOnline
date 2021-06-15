package com.example.klinikonline.Object;

import java.util.ArrayList;
import java.util.List;

public class Layanan {
    private String nama,status;
    private List<String> patientList= new ArrayList<>();

    public Layanan(String nama, List<String> patientList) {
        this.nama = nama;
        this.patientList = patientList;
    }

    public Layanan() {
    }

    public Layanan(String nama, String status, List<String> patientList) {
        this.nama = nama;
        this.status = status;
        this.patientList = patientList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public List<String> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<String> patientList) {
        this.patientList = patientList;
    }
}
