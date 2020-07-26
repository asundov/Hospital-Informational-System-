package com.example.aaaaaaaaaaaa;


import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;
import com.example.aaaaaaaaaaaa.models.Consultation;
import com.example.aaaaaaaaaaaa.models.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ConsultationAdapter extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<Consultation> consultationList;
    ArrayList<Consultation> arrayList;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    public ConsultationAdapter(Context context, List<Consultation> consultationList) {
        mContext = context;
        this.consultationList = consultationList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Consultation>();
        this.arrayList.addAll(consultationList);
    }

    @Override
    public int getCount() {
        return consultationList.size();
    }

    @Override
    public Object getItem(int position) {
        return consultationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.consultation_row, null);

        final CircleImageView doctorPicture = convertView.findViewById(R.id.profile_image);


        final TextView doctorFullName = convertView.findViewById(R.id.fullName);
        final TextView day = convertView.findViewById(R.id.day);

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);



        final Consultation Consultation = consultationList.get(position);
        final String emailDoctor = Consultation.getDoctorEmail();



        compositeDisposable.add(myAPI.get_consultation_doctor(emailDoctor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONArray jsonArray = new JSONArray(s);
                        String fullName="";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(0);

                            fullName = object.getString("fullName");

                        }
                        doctorFullName.setText("Dr. "+fullName);
                        day.setText(Consultation.getDate());


    }
}, new Consumer<Throwable>() { @Override public void accept(Throwable throwable) throws Exception { } }));


//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot data : dataSnapshot.getChildren())
//                {
//                    Doctor doctor = data.getValue(Doctor.class);
//                    if(doctor.getEmail().equals(emailDoctor))
//                    {
//                        doctorFullName.setText("Dr. "+doctor.getFullName());
//                        day.setText(Consultation.getDate());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });

//        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//        final StorageReference profileRef = storageReference.child("Profile pictures").child(emailDoctor+".jpg");
//        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(mContext).load(uri).into(doctorPicture);
//
//            }
//        });
        return convertView;
                    }
                }
