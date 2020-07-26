package com.example.aaaaaaaaaaaa;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;
import com.example.aaaaaaaaaaaa.models.Hospitalisation;

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

public class HospitalisationFragment extends Fragment {
    ListView hospitalisations;
    HospitalisationAdapter adapter;
    ArrayList<Hospitalisation> hospitalisationList = new ArrayList<>();
    List<Hospitalisation> myHospitalisations;
    String emailPatient;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    MyPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hospitalisation_fragment, container, false);
        hospitalisations = view.findViewById(R.id.hospitalisations);
        myHospitalisations = new ArrayList<>();

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        MedicalFolderActivity activity = (MedicalFolderActivity) getActivity();
        String emailPatient = activity.getMyData();



        compositeDisposable.add(myAPI.get_hospitalisation(emailPatient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        String hospitalName = "";
                        String email = "";
                        String date = "";
                        String disease = "";
                        String price = "";

                        JSONArray jsonArray = new JSONArray(s);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            hospitalName = object.getString("hospitalName");
                            email = object.getString("emailPatient");
                            date = object.getString("date");
                            disease = object.getString("disease");
                            price = object.getString("price");
                            if (emailPatient.equals(email)) {
                                myHospitalisations.add(new Hospitalisation(hospitalName, email, date, disease, price));
                                Collections.sort(myHospitalisations);
                                if (getActivity() != null) {
                                    adapter = new HospitalisationAdapter(getActivity(), myHospitalisations);
                                    hospitalisations.setAdapter(adapter);
                                }
                            }
                        }
                    }
                }, new Consumer<Throwable>() { @Override public void accept(Throwable throwable) throws Exception { } }));

//        user = FirebaseAuth.getInstance().getCurrentUser();
//        emailPatient = user.getEmail();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hospitalisations");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                myHospitalisations.clear();
//                for(DataSnapshot data : dataSnapshot.getChildren())
//                {
//                    Hospitalisation hospitalisation = data.getValue(Hospitalisation.class);
//                    if(hospitalisation.getEmailPatient().equals(emailPatient)) {
//                        myHospitalisations.add(hospitalisation);
//                        Collections.sort(myHospitalisations);
//                        if (getActivity()!=null){
//                            adapter = new HospitalisationAdapter(getActivity(), myHospitalisations);
//                            hospitalisations.setAdapter(adapter);
//                        }
//                    }
//
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });
        return view;
    }
}
