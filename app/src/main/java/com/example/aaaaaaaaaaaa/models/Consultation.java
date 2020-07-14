package com.example.aaaaaaaaaaaa.models;

public class Consultation {
    String doctorName;
    String emailDoctor;
    String emailPatient;
    String disease;
    String date;
    String price;
    String prescription;

    public Consultation() {
    }

    public Consultation(String doctorName, String emailDoctor, String emailPatient, String disease, String date, String price, String prescription) {
        this.doctorName = doctorName;
        this.emailDoctor = emailDoctor;
        this.emailPatient = emailPatient;
        this.disease = disease;
        this.date = date;
        this.price = price;
        this.prescription = prescription;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return emailDoctor;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.emailDoctor = doctorEmail;
    }

    public String getPatientEmail() {
        return emailPatient;
    }

    public void setPatientEmail(String patientEmail) {
        this.emailPatient = patientEmail;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }
}
