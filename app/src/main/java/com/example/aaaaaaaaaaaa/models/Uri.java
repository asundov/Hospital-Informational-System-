package com.example.aaaaaaaaaaaa.models;

public class Uri {
    String emailPatient;
    String uri;

    public Uri(String emailPatient, String uri) {
        this.emailPatient = emailPatient;
        this.uri = uri;
    }

    public Uri(String uri) {
        this.uri = uri;
    }

    public String getEmailPatient() {
        return emailPatient;
    }

    public void setEmailPatient(String emailPatient) {
        this.emailPatient = emailPatient;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
