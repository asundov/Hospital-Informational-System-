package com.example.aaaaaaaaaaaa;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MenuActivity extends AppCompatActivity {
    TextView fullName, cin;
    CircleImageView profilePicture;
    SharedPreferences sp;
    String uid;
    NotificationCompat.Builder builder;
    String patientFullName, patientCin, todaysDate;
    int numberOfAppointments;
    static int token = 0;
    String[] numbers = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};


    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void setToken(int number) {
        token = number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sp = getSharedPreferences("login", MODE_PRIVATE);
//        user = FirebaseAuth.getInstance().getCurrentUser();
        fullName = findViewById(R.id.fullName);
        cin = findViewById(R.id.cin);
        profilePicture = findViewById(R.id.profile_image);
        //     uid = user.getUid();
        Calendar c = Calendar.getInstance();
        todaysDate = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());


        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        String patientEmail = getIntent().getStringExtra("patient_email");



        compositeDisposable.add(myAPI.get_appointment(patientEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        String status = "";
                        String date;
                        numberOfAppointments = 0;
                        JSONObject object;
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            status = object.getString("status");
                            date = object.getString("date");

                            if (status.equals("Accepted") && date.equals(todaysDate))
                                numberOfAppointments++;
                        }

//                        StringBuffer buffer = new StringBuffer();
//                        buffer.append("String je  :" + aaa + "\n");
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
//                        builder.setCancelable(true);
//                        builder.setTitle("data");
//                        builder.setMessage(buffer.toString());
//                        builder.show();
                    }
                }));


        if (numberOfAppointments == 1) {
            builder = new NotificationCompat.Builder(MenuActivity.this, "appointment")
                    .setSmallIcon(R.drawable.ic_heart_beats)
                    .setContentTitle("Daily appointments")
                    .setContentText("You have one appointment today")
                    .setAutoCancel(true)
                    .setColor(Color.parseColor("#33AEB6"))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        if (numberOfAppointments > 1) {
            builder = new NotificationCompat.Builder(MenuActivity.this, "appointment")
                    .setSmallIcon(R.drawable.ic_heart_beats)
                    .setContentTitle("Daily appointments")
                    .setContentText("You have " + numbers[numberOfAppointments - 1] + " appointments today")
                    .setAutoCancel(true)
                    .setColor(Color.parseColor("#33AEB6"))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

        }
        if (numberOfAppointments == 0) {
            builder = new NotificationCompat.Builder(MenuActivity.this, "appointment")
                    .setSmallIcon(R.drawable.ic_heart_beats)
                    .setContentTitle("Daily appointments")
                    .setContentText("You have no appointments today")
                    .setAutoCancel(true)
                    .setColor(Color.parseColor("#33AEB6"))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

        }
//        if (token == 0) {
//            Intent intent = new Intent(MenuActivity.this, AppointmentsActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(MenuActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            builder.setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(
//                    Context.NOTIFICATION_SERVICE
//            );
//            notificationManager.notify(0, builder.build());
//        }
    }


    //                                      DODAJ PROFILNU SLIKU I INFO NA VRHU!!!


//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Patients");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                patientFullName = dataSnapshot.child(uid).child("lastName").getValue(String.class) + " " + dataSnapshot.child(uid).child("firstName").getValue(String.class);
//                patientCin = dataSnapshot.child(uid).child("cin").getValue(String.class);
//                fullName.setText(patientFullName);
//                cin.setText(patientCin);
//                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//                StorageReference profileRef = storageReference.child("Profile pictures").child(FirebaseAuth.getInstance().getCurrentUser().getEmail() + ".jpg");
//                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Picasso.get().load(uri).into(profilePicture);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    public void searchDoctor(View view) {
        Intent intent = new Intent(MenuActivity.this, SearchDoctorSpecialityActivity.class);
        startActivity(intent);
    }

    public void Appointments(View view) {
        Intent intent = new Intent(MenuActivity.this, AppointmentsActivity.class);
        intent.putExtra("patient_email", getIntent().getStringExtra("patient_email"));
        startActivity(intent);
    }

    public void profileInfo(View view) {
        Intent intent = new Intent(MenuActivity.this, PatientProfileInfoSearchInformations.class);
        startActivity(intent);
    }

    public void openMedicalFolder(View view) {
        Intent intent = new Intent(MenuActivity.this, MedicalFolderActivity.class);
        startActivity(intent);
    }

    public void logOut(View view) {
        sp.edit().putBoolean("loggedPatient", false).apply();
        finish();
        startActivity(new Intent(MenuActivity.this, MainActivity_login.class));

    }

    @Override
    public void onBackPressed() {

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setConfirmText("Yes");
        dialog.setCancelText("No");
        dialog.setContentText("Are you sure want to close HealthCare ?");
        dialog.setTitleText("Close application");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finishAffinity();
                System.exit(0);
            }
        });
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
            }
        });

        dialog.show();
    }

    public void myDoctors(View view) {
        Intent intent = new Intent(MenuActivity.this, MyDoctorsActivity.class);
        startActivity(intent);
    }
}

