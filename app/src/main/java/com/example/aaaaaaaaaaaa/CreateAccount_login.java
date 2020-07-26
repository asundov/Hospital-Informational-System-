package com.example.aaaaaaaaaaaa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CreateAccount_login extends AppCompatActivity {
    //Doctor fields
    LinearLayout doctorLinearLayout;
    RadioButton doctorRadioButton;
    EditText doctorFullName, doctorCode, doctorPhoneNumber, doctorEmail, doctorCity, doctorAddress, doctorSpeciality, doctorPassword1, doctorPassword2;
    Button doctorButton;
    // Patient fields
    LinearLayout patientLinearLayout;
    RadioButton patientRadioButton;
    EditText patientFirstName, patientLastName, patientBirthDate, patientPhoneNumber, patientEmail, patientCIN, patientPassword1, patientPassword2;
    Spinner maritalStatus;
    Button patientButton;
    //Loading screen
    loadingDialog_login ld;
    //Date picker
    DatePickerDialog.OnDateSetListener DateSetListener;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Loading dialog
        ld = new loadingDialog_login(CreateAccount_login.this);

        // Patient fields
        patientFirstName = findViewById(R.id.patientFirstName);
        patientLastName = findViewById(R.id.patientLastName);
        patientBirthDate = findViewById(R.id.patientBirthDate);
        patientPhoneNumber = findViewById(R.id.patientPhoneNumber);
        patientEmail = findViewById(R.id.patientEmail);
        patientCIN = findViewById(R.id.patientCIN);
        patientPassword1 = findViewById(R.id.patientPassword1);
        patientPassword2 = findViewById(R.id.patientPassword2);
        patientRadioButton = (RadioButton) findViewById(R.id.patientRadioButton);
        patientLinearLayout = (LinearLayout) findViewById(R.id.patientLinearLayout);
        maritalStatus = (Spinner) findViewById(R.id.maritalStatus);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.marital_status, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maritalStatus.setAdapter(adapter);
        patientButton = findViewById(R.id.patientButton);

        // Doctor fields
        doctorRadioButton = (RadioButton) findViewById(R.id.doctorRadioButton);
        doctorLinearLayout = (LinearLayout) findViewById(R.id.doctorLinearLayout);
        doctorFullName = findViewById(R.id.doctorFullName);
        doctorCode = findViewById(R.id.Code);
        doctorPhoneNumber = findViewById(R.id.doctorPhoneNumber);
        doctorEmail = findViewById(R.id.doctorEmail);
        doctorCity = findViewById(R.id.City);
        doctorAddress = findViewById(R.id.Address);
        doctorSpeciality = findViewById(R.id.Speciality);
        doctorPassword1 = findViewById(R.id.doctorPassword1);
        doctorPassword2 = findViewById(R.id.doctorPassword2);
        doctorButton = findViewById(R.id.doctorButton);

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void patientRegistration(View view) {
        if (patientRadioButton.isChecked()) {
            doctorLinearLayout.setVisibility(View.GONE);
            patientLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void doctorRegistration(View view) {
        if (doctorRadioButton.isChecked()) {
            patientLinearLayout.setVisibility(View.GONE);
            doctorLinearLayout.setVisibility(View.VISIBLE);
        }
    }


    public void registerPatient() {
        final String firstName = patientFirstName.getText().toString().trim();
        final String lastName = patientLastName.getText().toString().trim();
        final String birthDate = patientBirthDate.getText().toString().trim();
        final String phoneNumber = patientPhoneNumber.getText().toString().trim();
        final String email = patientEmail.getText().toString().trim();
        final String CIN = patientCIN.getText().toString().trim();
        final String status = maritalStatus.getSelectedItem().toString().trim();
        String password = patientPassword1.getText().toString().trim();
        String password2 = patientPassword2.getText().toString().trim();
        if (
                TextUtils.isEmpty(firstName)
                        || TextUtils.isEmpty(lastName)
                        || TextUtils.isEmpty(birthDate)
                        || TextUtils.isEmpty(phoneNumber)
                        || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(CIN)
                        || TextUtils.isEmpty(status)
                        || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(password2)
        ) {
            Toast.makeText(CreateAccount_login.this, "All fields are required !", Toast.LENGTH_LONG).show();
        } else {
            if (!isEmailValid(email)) {
                patientEmail.setError("Invalid email format !");
                return;
            }
            if (!password.equals(password2)) {
                patientPassword1.setError("The two passwords are not matched");
                return;
            } else {
                if (password.length() <= 3) {
                    patientPassword1.setError("Password must be longer than three characters");
                    return;
                }

            }
            if (!isDateValid(birthDate)) {
                patientBirthDate.setError("Date should match the YYYY-MM-DD format !");
                return;
            }
            ld.startLoadingDialog();
            compositeDisposable.add(myAPI.registerPatient(firstName, lastName, birthDate, password, email, CIN, status)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                                   @Override
                                   public void accept(String s) throws Exception {
                                       ld.dismissDialog();
                                       Intent createAccountIntent = new Intent(CreateAccount_login.this, MainActivity_login.class);
                                       startActivity(createAccountIntent);
                                   }
                               }
                            , new Consumer<Throwable>() { @Override public void accept(Throwable throwable) throws Exception { } }));
        }
    }


    private void registerDoctor() {

        final String fullName = doctorFullName.getText().toString().trim();
        final String code = doctorCode.getText().toString().trim();
        final String phoneNumber = doctorPhoneNumber.getText().toString().trim();
        final String email = doctorEmail.getText().toString().trim();
        final String city = doctorCity.getText().toString().trim();
        final String address = doctorAddress.getText().toString().trim();
        final String speciality = doctorSpeciality.getText().toString().trim();
        String password = doctorPassword1.getText().toString().trim();
        String password2 = doctorPassword2.getText().toString().trim();
        if (
                TextUtils.isEmpty(fullName)
                        || TextUtils.isEmpty(code)
                        || TextUtils.isEmpty(phoneNumber)
                        || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(city)
                        || TextUtils.isEmpty(address)
                        || TextUtils.isEmpty(speciality)
                        || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(password2)
        ) {
            Toast.makeText(CreateAccount_login.this, "All fields are required !", Toast.LENGTH_LONG).show();
        } else {
            if (!isEmailValid(email)) {
                doctorEmail.setError("Invalid email format !");
                return;
            }
            if (!password.equals(password2)) {
                doctorPassword1.setError("The two passwords are not matched");
                return;
            } else {
                if (password.length() <= 3) {
                    doctorPassword1.setError("Password must be longer than three characters");
                    return;
                }
            }

            ld.startLoadingDialog();
            compositeDisposable.add(myAPI.register_doctor(fullName, code, phoneNumber, email, city, address, speciality, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                                   @Override
                                   public void accept(String s) throws Exception {
                                       ld.dismissDialog();
                                       Intent createAccountIntent = new Intent(CreateAccount_login.this, MainActivity_login.class);
                                       startActivity(createAccountIntent);
                                   }
                               }
                            , new Consumer<Throwable>() { @Override public void accept(Throwable throwable) throws Exception { } }));
        }
    }


    public void signUpPatient(View view) {
        registerPatient();
    }

    public void signUpDoctor(View view) {
        registerDoctor();
    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isDateValid(String date) {
        String expression = "^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();

    }
}
