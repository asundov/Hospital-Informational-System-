package com.example.aaaaaaaaaaaa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity_login extends AppCompatActivity {
    private EditText login;
    private EditText password;
    private TextView errorMessage;
    CheckBox rememberMe;
    SharedPreferences sp;
    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        errorMessage = findViewById(R.id.errorMessage);
        rememberMe = findViewById(R.id.rememberMe);


        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);


        sp = getSharedPreferences("login", MODE_PRIVATE);


        if (sp.getBoolean("loggedPatient", false)) {
            Intent intent = new Intent(MainActivity_login.this, MenuActivity.class);
            startActivity(intent);
        }
        if (sp.getBoolean("loggedDoctor", false)) {
            Intent intent = new Intent(MainActivity_login.this, DoctorMenuActivity.class);
            startActivity(intent);
        }

    }

    public void createAccount(View view) {
        Intent createAccountIntent = new Intent(this, CreateAccount_login.class);
        startActivity(createAccountIntent);
    }


    public void login(View view) {
        errorMessage.setVisibility(View.GONE);
        String tempEmail = login.getText().toString().trim();
        String tempPassword = password.getText().toString().trim();
        if (
                TextUtils.isEmpty(tempEmail)
                        || TextUtils.isEmpty(tempPassword)
        ) {
            Toast.makeText(MainActivity_login.this, "Login or Password are empty", Toast.LENGTH_SHORT).show();
        } else {
            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#33aeb6"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();

            compositeDisposable.add(myAPI.loginUser(tempEmail, tempPassword)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {

                            if (s.contains("email")) {

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("email", tempEmail).apply();
                                editor.commit();


                                if (s.contains("CIN")) {
                                    pDialog.hide();
                                    if (rememberMe.isChecked()) {
                                        sp.edit().putBoolean("loggedPatient", true).apply();
                                    } else
                                        sp.edit().putBoolean("loggedPatient", false).apply();


                                    Intent intent = new Intent(MainActivity_login.this, MenuActivity.class);
                                    intent.putExtra("patient_email", tempEmail);
                                    MainActivity_login.this.startActivity(intent);


                                } else {
                                    pDialog.hide();
                                    if (rememberMe.isChecked()) {
                                        sp.edit().putBoolean("loggedDoctor", true).apply();
                                    } else
                                        sp.edit().putBoolean("loggedDoctor", false).apply();

                                    Intent intent = new Intent(MainActivity_login.this, DoctorMenuActivity.class);
                                    intent.putExtra("doctor_email", tempEmail);
                                    MainActivity_login.this.startActivity(intent);
                                }
                            }
                            else {
                                pDialog.hide();
                                Toast.makeText(MainActivity_login.this, "Invalid user, Try again.", Toast.LENGTH_SHORT).show();
                                login.setText("");
                                password.setText("");

                            }
                        }
                    }, new Consumer<Throwable>() { @Override public void accept(Throwable throwable) throws Exception { } }));
        }
    }


    public void passwordForgotten(View view) {
        ResetPasswordDialog_login resetPasswordDialog = new ResetPasswordDialog_login();
        resetPasswordDialog.show(getSupportFragmentManager(), "reset password dialog");

    }
}
