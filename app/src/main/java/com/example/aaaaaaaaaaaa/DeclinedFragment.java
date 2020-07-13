package com.example.aaaaaaaaaaaa;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class DeclinedFragment extends Fragment {
    ListView declinedAppointments;
    AppointmentAdapter adapter;
    ArrayList<Appointment> appointmentList = new ArrayList<>();
    List<Appointment> myAppointments;
    List<String> myReasons;
    SharedPreferences sp;
    Context context;


    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    //U TABLICU APPOINTMENTS TRAZI email pacijenta i declined i ako je, spremi u myAppointments listu


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.declined_fragment, container, false);
        declinedAppointments = view.findViewById(R.id.declined_appointments);
        myAppointments = new ArrayList<>();
        myReasons = new ArrayList<>();



        AppointmentsActivity activity = (AppointmentsActivity) getActivity();
        String emailPatient = activity.getMyData();

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

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
                            if (status.equals("Declined"))
                            {
                                myAppointments.add(new Appointment(date, time, emailDoctor, emailPatient));
                                if (getActivity()!=null){
                                    adapter = new AppointmentAdapter(getActivity(), myAppointments);
                                    declinedAppointments.setAdapter(adapter);
                                }

                            }
                        }
                    }
                }));


//        user = FirebaseAuth.getInstance().getCurrentUser();
//        emailPatient = user.getEmail();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointments");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                myReasons.clear();
//                myAppointments.clear();
//                for(DataSnapshot data : dataSnapshot.getChildren())
//                {
//                    Appointment appointment = data.getValue(Appointment.class);
//                    if(appointment.getEmailPatient().equals(emailPatient) && appointment.getStatus().equals("Declined")) {
//                        myReasons.add(data.child("reason").getValue(String.class));
//                        myAppointments.add(appointment);
//                        if (getActivity()!=null){
//                            adapter = new AppointmentAdapter(getActivity(), myAppointments);
//                            declinedAppointments.setAdapter(adapter);
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


        declinedAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                alertDialog.setTitleText("Reason of refusal");
                alertDialog.setContentText(myReasons.get(position));
                alertDialog.show();
                Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                btn.setBackgroundColor(Color.parseColor("#33aeb6"));

            }
        });


        return view;
    }
}
