package com.example.aaaaaaaaaaaa;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;
import com.example.aaaaaaaaaaaa.models.Consultation;

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

public class ConsultationFragment extends Fragment {
    ListView consultations;
    ConsultationAdapter adapter;
    ArrayList<Consultation> consultationList = new ArrayList<>();
    List<Consultation> myConsultations;
    String emailPatient;


    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consultation_fragment, container, false);
        consultations = view.findViewById(R.id.consultations);
        myConsultations = new ArrayList<>();

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        MedicalFolderActivity activity = (MedicalFolderActivity) getActivity();
        String emailPatient = activity.getMyData();

        Toast.makeText(activity, "emaaaail " + emailPatient, Toast.LENGTH_SHORT).show();

        compositeDisposable.add(myAPI.get_consultation(emailPatient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String doctorName = "";
                        String patientEmail = "";
                        String disease = "";
                        String date = "";
                        String price = "";
                        String prescription = "";
                        String doctorEmail = "";


                        JSONArray jsonArray = new JSONArray(s);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            doctorName = object.getString("doctorName");
                            doctorEmail = object.getString("emailDoctor");
                            patientEmail = object.getString("emailPatient");
                            date = object.getString("date");
                            disease = object.getString("disease");
                            price = object.getString("price");
                            prescription = object.getString("prescription");

                            if (emailPatient.equals(patientEmail)) {
                                myConsultations.add(new Consultation(doctorName, doctorEmail, patientEmail, disease, date, price, prescription));
                                //Collections.sort(myConsultations);

                                if (getActivity() != null) {
                                    adapter = new ConsultationAdapter(getActivity(), myConsultations);
                                    consultations.setAdapter(adapter);
                                }
                            }
                        }
                    }
                }));


//        user = FirebaseAuth.getInstance().getCurrentUser();
//        emailPatient = user.getEmail();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Consultations");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                myConsultations.clear();
//                for(DataSnapshot data : dataSnapshot.getChildren())
//                {
//                    Consultation consultation = data.getValue(Consultation.class);
//                    if(consultation.getPatientEmail().equals(emailPatient)) {
//                        myConsultations.add(consultation);
//                        if (getActivity()!=null){
//                            adapter = new ConsultationAdapter(getActivity(), myConsultations);
//                            consultations.setAdapter(adapter);
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
        consultations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ConsultationInfoActivity.class);
                Consultation consultation = myConsultations.get(position);
                intent.putExtra("doctorName", consultation.getDoctorName());
                intent.putExtra("doctorEmail", consultation.getDoctorEmail());
                intent.putExtra("date", consultation.getDate());
                intent.putExtra("price", consultation.getPrice());
                intent.putExtra("prescription", consultation.getPrescription());
                intent.putExtra("disease", consultation.getDisease());
                startActivity(intent);
            }
        });


        return view;
    }
}
