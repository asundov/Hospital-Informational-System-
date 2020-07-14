package com.example.aaaaaaaaaaaa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;
import com.example.aaaaaaaaaaaa.models.Appointment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class ScheduleAppointmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView time, day;
    Intent intent;

    List<Appointment> appointments;
    String emailDoctor;
    String timeOfAppointment;

    String timeFromDatabase;
    String dateFromDatabase;
    String emailPatientUser;
    String currentDateString;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_appointment);
        day = findViewById(R.id.date);
        time = findViewById(R.id.time);
        intent = getIntent();
        emailDoctor = intent.getStringExtra("email");
        appointments = new ArrayList<>();

        MyPreferences preferences = new MyPreferences(this);
        emailPatientUser = preferences.getString("emailpatient");


        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

    }

    public void chooseDay(View view) {
        DialogFragment datePicker = new MyDatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            new SweetAlertDialog(ScheduleAppointmentActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("You can't schedule an appointment on Sunday !")
                    .show();
            double number = 1.3;
            System.out.println((int) number);
            System.out.println(number - (int) number);
            return;
        }
        currentDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());


        compositeDisposable.add(myAPI.get_appointment(emailPatientUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONArray jsonArray = new JSONArray(s);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            dateFromDatabase = object.getString("date");
                            if (dateFromDatabase.equals(currentDateString)) {

                                new SweetAlertDialog(ScheduleAppointmentActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("You can't have more than one appointment with the same doctor in one day")
                                        .show();
                                return;
                            }
                        }
                            appointments.add(new Appointment(currentDateString, timeFromDatabase, emailDoctor, emailPatientUser, "OnHold"));

                            if (appointments.size() == 14) {
                                new SweetAlertDialog(ScheduleAppointmentActivity.this)
                                        .setTitleText("Scheduling an appointment!")
                                        .setContentText("Day chosen is full, please pick another day !")
                                        .show();
                                return;
                            } else {
                                double timeFull = (double) (appointments.size() * 30) / 60;
                                int hour = 9 + (int) timeFull;
                                double minutes = timeFull - (int) timeFull;
                                System.out.println(minutes);
                                if (minutes >= 0.5) {
                                    timeOfAppointment = hour + ":30";
                                } else {
                                    timeOfAppointment = hour + ":00";
                                }
                            }
                            new SweetAlertDialog(ScheduleAppointmentActivity.this)
                                    .setTitleText("Scheduling appointment")
                                    .setContentText("If you confirm below, your appointment will be scheduled the " + currentDateString + " at " + timeOfAppointment + " !")
                                    .show();
                            day.setText(currentDateString);
                            day.setVisibility(View.VISIBLE);
                            time.setText(timeOfAppointment);
                            time.setVisibility(View.VISIBLE);
                        }

                }));
    }

    public void confirm(View view) {
        if (TextUtils.isEmpty(day.getText()) && TextUtils.isEmpty(time.getText())) {
            new SweetAlertDialog(ScheduleAppointmentActivity.this)
                    .setTitleText("Scheduling appointment")
                    .setContentText("Please pick a day !")
                    .show();
            return;
        }
        compositeDisposable.add(myAPI.add_appointment(currentDateString, timeOfAppointment, emailDoctor, emailPatientUser, "OnHold")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (!s.equals("null")) {
                            new SweetAlertDialog(ScheduleAppointmentActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Congratulations")
                                    .setContentText("Your appointment is registered successfully")
                                    .show();
                        } else {
                            new SweetAlertDialog(ScheduleAppointmentActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong!")
                                    .show();
                        }
                    }
                }));
    }
}


