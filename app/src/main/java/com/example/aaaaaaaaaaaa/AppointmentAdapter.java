package com.example.aaaaaaaaaaaa;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.aaaaaaaaaaaa.models.Appointment;
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

public class AppointmentAdapter extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<Appointment> appointmentList;
    ArrayList<Appointment> arrayList;
    String emailDoctor, emailPatient;


    public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
        mContext = context;
        this.appointmentList = appointmentList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Appointment>();
        this.arrayList.addAll(appointmentList);
    }

    @Override
    public int getCount() {
        return appointmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return appointmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.appointment_row, null);

        final CircleImageView doctorPicture = convertView.findViewById(R.id.profile_image);


        final TextView doctorFullName = convertView.findViewById(R.id.fullName);
        final TextView day = convertView.findViewById(R.id.day);
        final TextView time = convertView.findViewById(R.id.time);
        INodeJs myAPI;
        CompositeDisposable compositeDisposable = new CompositeDisposable();


        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        final Appointment appointment = appointmentList.get(position);
        emailDoctor = appointment.getEmailDoctor();


        //ide u tablicu doktor i gleda jel email iz appointmenta isti s emailom i doktorskoj tablici. AKo je, pispisi ovo troje


            compositeDisposable.add(myAPI.get_doctor(emailDoctor)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {

                            JSONArray jsonArray = new JSONArray(s);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(0);

                                String fullName = object.getString("fullName");

                                    doctorFullName.setText(fullName);
                                    day.setText(appointment.getDate());
                                    time.setText(appointment.getTime());


                            }
                        }
                    }, new Consumer<Throwable>() { @Override public void accept(Throwable throwable) throws Exception { } }));



                    /// ****** PROFILNA!!!
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
