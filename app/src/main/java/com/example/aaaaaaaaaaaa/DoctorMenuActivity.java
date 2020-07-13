package com.example.aaaaaaaaaaaa;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_menu);

        Toast.makeText(DoctorMenuActivity.this, "odvelo me na doktora", Toast.LENGTH_SHORT).show();

    }
}
