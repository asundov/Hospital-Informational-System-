package com.example.aaaaaaaaaaaa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;
import com.example.aaaaaaaaaaaa.models.Doctor;
import com.example.aaaaaaaaaaaa.models.Relationship;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MyDoctorsActivity extends AppCompatActivity {
    ListView myDoctorsListView;
    List<Doctor> Doctors;
    List<Doctor> myDoctors;
    List<Relationship> relationship;
    MyDoctorsAdapter adapter;
    String patientEmail;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctors);
        myDoctorsListView = findViewById(R.id.myDoctors);
        Doctors = new ArrayList<>();
        myDoctors = new ArrayList<>();
        relationship = new ArrayList<>();

        patientEmail = getIntent().getStringExtra("patient_email");


        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        compositeDisposable.add(myAPI.relationship(patientEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        myDoctors.clear();

                        String emailDoctor = "";

                        JSONArray jsonArray = new JSONArray(s);

                        for (int count = 0; count < jsonArray.length(); count++) {
                            JSONObject object = jsonArray.getJSONObject(count);
                            emailDoctor = object.getString("emailDoctor");
                            Toast.makeText(MyDoctorsActivity.this, ""+emailDoctor, Toast.LENGTH_SHORT).show();

                            compositeDisposable.add(myAPI.get_doctor(emailDoctor)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<String>() {
                                        @Override
                                        public void accept(String s) throws Exception {

                                            String fullName = "";
                                            String speciality = "";

                                            JSONArray jsonArray = new JSONArray(s);

                                            for (int count = 0; count < jsonArray.length(); count++) {
                                                JSONObject object = jsonArray.getJSONObject(count);
                                                fullName = object.getString("fullName");
                                                speciality = object.getString("speciality");

                                                myDoctors.add(new Doctor(fullName,speciality));
                                                adapter = new MyDoctorsAdapter(MyDoctorsActivity.this, myDoctors);
                                                myDoctorsListView.setAdapter(adapter);

                                            }


                                        }

                                    }));
                        }
                    }
                }));


        myDoctorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyDoctorsActivity.this, MyDoctorInfoActivity.class);
                intent.putExtra("fullName", myDoctors.get(position).getFullName());
                intent.putExtra("email", myDoctors.get(position).getEmail());
                intent.putExtra("speciality", myDoctors.get(position).getSpeciality());
                intent.putExtra("phoneNumber", myDoctors.get(position).getPhoneNumber());
                intent.putExtra("address", myDoctors.get(position).getAddress());
                intent.putExtra("city", myDoctors.get(position).getCity());
                startActivity(intent);
            }
        });


    }
}




//        compositeDisposable.add(myAPI.get_all_doctors("")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//@Override
//public void accept(String s) throws Exception {
//
//        String fullName = "";
//        String phoneNumber = "";
//        String email = "";
//        String speciality = "";
//        String address = "";
//        String city = "";
//
//
//        JSONArray jsonArray = new JSONArray(s);
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//        JSONObject object = jsonArray.getJSONObject(i);
//        fullName = object.getString("fullName");
//        phoneNumber = object.getString("phoneNumber");
//        email = object.getString("email");
//        speciality = object.getString("speciality");
//        address = object.getString("address");
//        city = object.getString("city");
//
//        myDoctors.add(new Doctor(fullName, phoneNumber, email, speciality, address, city));
//
//        }
//        }
//        }));
//
//
//        compositeDisposable.add(myAPI.relationship("")
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Consumer<String>() {
//@Override
//public void accept(String s) throws Exception {
//
//        String emailDoctor = "";
//        String emailPatient = "";
//
//
//        JSONArray jsonArray = new JSONArray(s);
//
//        for (int count = 0; count < jsonArray.length(); count++) {
//        JSONObject object = jsonArray.getJSONObject(count);
//        emailDoctor = object.getString("emailDoctor");
//        emailPatient = object.getString("emailPatient");
//
//        for (int i = 0; i < Doctors.size(); i++) {
//        Doctor doc = Doctors.get(i);
//        if (emailDoctor.equals(doc.getEmail()) && emailPatient.equals(patientEmail))
//        {
//        myDoctors.add(doc);
//        adapter = new MyDoctorsAdapter(MyDoctorsActivity.this, myDoctors);
//        myDoctorsListView.setAdapter(adapter);
//        }
//        }
//        }
//
//
//        }
//
//        }));