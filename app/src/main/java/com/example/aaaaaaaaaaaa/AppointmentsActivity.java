package com.example.aaaaaaaaaaaa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

//import com.example.aaaaaaaaaaaa.ui.main.SectionsPagerAdapterAppointments;
import com.example.aaaaaaaaaaaa.models.Appointment;
import com.example.aaaaaaaaaaaa.ui.main.SectionsPagerAdapterAppointments;
import com.google.android.material.tabs.TabLayout;

public class AppointmentsActivity extends AppCompatActivity {


    SharedPreferences sp;
    String patientEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        SectionsPagerAdapterAppointments sectionsPagerAdapter = new SectionsPagerAdapterAppointments(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        patientEmail = getIntent().getStringExtra("patient_email");
    }

    public String getMyData() {
        return patientEmail;
    }
}