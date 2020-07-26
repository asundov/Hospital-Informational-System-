package com.example.aaaaaaaaaaaa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;
import com.example.aaaaaaaaaaaa.models.Appointment;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class OnHoldFragment extends Fragment {
    ListView onHoldAppointments;
    AppointmentAdapter adapter;
    ArrayList<Appointment> appointmentList = new ArrayList<>();
    List<Appointment> myAppointments;
    SharedPreferences sp;


    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onhold_fragment, container, false);
        onHoldAppointments = view.findViewById(R.id.onhold_appointments);
        myAppointments = new ArrayList<>();



        AppointmentsActivity activity = (AppointmentsActivity) getActivity();
        String emailPatient = activity.getMyData();

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        //U TABLICU APPOINTMENTS TRAZI email pacijenta i on hold i ako je, spremi u myAppointments listu

        compositeDisposable.add(myAPI.get_appointment(emailPatient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        String status = "";
                        String emailDoctor;
                        String time;
                        String date;
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            status = object.getString("status");
                            emailDoctor= object.getString("emailDoctor");
                            time = object.getString("time");
                            date = object.getString("date");
                            if (status.equals("OnHold"))
                            {
                                myAppointments.add(new Appointment(date, time, emailDoctor, emailPatient));
                                if (getActivity()!=null){
                                    adapter = new AppointmentAdapter(getActivity(), myAppointments);
                                    onHoldAppointments.setAdapter(adapter);
                                }
                            }
                        }
                    }
                }, new Consumer<Throwable>() { @Override public void accept(Throwable throwable) throws Exception { } }));

//        user = FirebaseAuth.getInstance().getCurrentUser();
//        emailPatient = user.getEmail();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointments");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                myAppointments.clear();
//                for(DataSnapshot data : dataSnapshot.getChildren())
//                {
//                    Appointment appointment = data.getValue(Appointment.class);
//                    if(appointment.getEmailPatient().equals(emailPatient) && appointment.getStatus().equals("On hold")) {
//                        myAppointments.add(appointment);
//                        if (getActivity()!=null){
//                            adapter = new AppointmentAdapter(getActivity(), myAppointments);
//                            onHoldAppointments.setAdapter(adapter);
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });


        return view;
    }
}
