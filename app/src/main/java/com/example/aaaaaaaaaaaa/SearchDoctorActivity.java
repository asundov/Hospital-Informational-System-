package com.example.aaaaaaaaaaaa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchDoctorActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Doctor> myDoctors;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);
        recyclerView= (RecyclerView)findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myDoctors = new ArrayList<>();
        recyclerView.setAdapter(new doctorAdapters(myDoctors));

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs .class);

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

                            myDoctors.add(new Doctor(fullName,phoneNumber,email,speciality,address,city));
                            Collections.sort(myDoctors);
                        }
                    }
                }, new Consumer<Throwable>() { @Override public void accept(Throwable throwable) throws Exception { } }));



    }
    class doctorAdapters extends RecyclerView.Adapter<DoctorViewHolder>{
        List<Doctor> myDoctors;
        public doctorAdapters(List<Doctor> myDoctors) {
            super();
            this.myDoctors=myDoctors;
        }
        @NonNull
        @Override
        public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DoctorViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
            holder.bind(this.myDoctors.get(position));

        }

        @Override
        public int getItemCount() {
            return myDoctors.size();
        }
    }
    class DoctorViewHolder extends RecyclerView.ViewHolder{
        private TextView fullName;
        private TextView emailAddress;
        public DoctorViewHolder(ViewGroup container)
        {
            super(LayoutInflater.from(SearchDoctorActivity.this).inflate(R.layout.item_layout, container, false));
            fullName=(TextView)itemView.findViewById(R.id.fullName);
            emailAddress=(TextView)itemView.findViewById(R.id.emailAddress);
        }
        public void bind(Doctor doctor)
        {
            fullName.setText(doctor.getFullName());
            emailAddress.setText(doctor.getEmail());
        }
    }
}

