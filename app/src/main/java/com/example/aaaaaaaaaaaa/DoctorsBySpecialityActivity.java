package com.example.aaaaaaaaaaaa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;
import com.example.aaaaaaaaaaaa.models.Doctor;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class DoctorsBySpecialityActivity extends AppCompatActivity {
    TextView doctorSpeciality;
    ListView myDoctorsBySpecialityListView;
    List<Doctor>  myDoctors;
    ListViewAdapter adapter;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_by_speciality);
        Intent intent = getIntent();
        String receivedSpeciality = intent.getStringExtra("speciality");
        final String speciality = intent.getStringExtra("speciality");
        myDoctorsBySpecialityListView = findViewById(R.id.myDoctorsBySpeciality);
        doctorSpeciality = findViewById(R.id.speciality);
        doctorSpeciality.setText(speciality);
        myDoctors = new ArrayList<>();

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        compositeDisposable.add(myAPI.get_all_doctors("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        String fullName = "";
                        String phoneNumber= "";
                        String email= "";
                        String speciality= "";
                        String address= "";
                        String city= "";


                        JSONArray jsonArray = new JSONArray(s);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            fullName = object.getString("fullName");
                            phoneNumber = object.getString("phoneNumber");
                            email = object.getString("email");
                            speciality = object.getString("speciality");
                            address = object.getString("address");
                            city = object.getString("city");
                            if (receivedSpeciality.equals(speciality)) {
                                myDoctors.add(new Doctor(fullName, phoneNumber, email, speciality, address, city));
                                Collections.sort(myDoctors);
                                adapter = new ListViewAdapter(getApplicationContext(), myDoctors);
                                myDoctorsBySpecialityListView.setAdapter(adapter);
                            }
                        }
                    }
                }, new Consumer<Throwable>() { @Override public void accept(Throwable throwable) throws Exception { } }));

        myDoctorsBySpecialityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DisplayDoctorInfo.class);
                intent.putExtra("fullName",myDoctors.get(position).getFullName());
                intent.putExtra("email",myDoctors.get(position).getEmail());
                intent.putExtra("speciality",myDoctors.get(position).getSpeciality());
                intent.putExtra("phoneNumber",myDoctors.get(position).getPhoneNumber());
                intent.putExtra("address", myDoctors.get(position).getAddress());
                intent.putExtra("city", myDoctors.get(position).getCity());
                startActivity(intent);
            }
        });
    }
}
