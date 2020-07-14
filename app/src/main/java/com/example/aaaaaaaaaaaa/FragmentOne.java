package com.example.aaaaaaaaaaaa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class FragmentOne extends Fragment {
    static ListView listView;
    static ListViewAdapter adapter;
    List<Doctor> myDoctors;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static ListViewAdapter getAdapter() {
        return adapter;
    }

    public static ListView getListView() {
        return listView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentone_layout, container, false);
        listView = view.findViewById(R.id.myListView);
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

                                myDoctors.add(new Doctor(fullName,phoneNumber,email,speciality,address,city));
                                Collections.sort(myDoctors);
                                adapter = new ListViewAdapter(getContext(), myDoctors);
                                listView.setAdapter(adapter);

                        }
                    }
                }));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DisplayDoctorInfo.class);
                intent.putExtra("fullName", myDoctors.get(position).getFullName());
                intent.putExtra("email", myDoctors.get(position).getEmail());
                intent.putExtra("speciality", myDoctors.get(position).getSpeciality());
                intent.putExtra("phoneNumber", myDoctors.get(position).getPhoneNumber());
                intent.putExtra("address", myDoctors.get(position).getAddress());
                intent.putExtra("city", myDoctors.get(position).getCity());
                startActivity(intent);
            }
        });


        return view;
    }
}
