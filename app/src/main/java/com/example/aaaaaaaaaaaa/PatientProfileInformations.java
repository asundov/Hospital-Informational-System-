package com.example.aaaaaaaaaaaa;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PatientProfileInformations extends AppCompatActivity {
    TextView fullName, cin, email, phoneNumber, maritalStatus, birthDate;
    CircleImageView circleImageView;
    String uid;
    String Uri;
    String firstNameRetrieved, lastNameRetrieved, cinRetrieved, emailRetrieved, maritalStatusRetrieved, birthdateRetrieved;

    List<com.example.aaaaaaaaaaaa.models.Uri> uriList;


    private Uri mImageUri;


    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_informations);

        fullName = findViewById(R.id.fullName);
        cin = findViewById(R.id.cin);
        email = findViewById(R.id.email);
        maritalStatus = findViewById(R.id.maritalStatus);
        birthDate = findViewById(R.id.birthDate);
        circleImageView = findViewById(R.id.profile_image);


        emailRetrieved = getIntent().getStringExtra("patient_email");

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        compositeDisposable.add(myAPI.get_patient(emailRetrieved)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONArray jsonArray = new JSONArray(s);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            firstNameRetrieved = object.getString("firstName");
                            lastNameRetrieved = object.getString("lastName");
                            cinRetrieved = object.getString("CIN");
                            birthdateRetrieved = object.getString("birthDate");
                            maritalStatusRetrieved = object.getString("maritalStatus");
                        }
                        fullName.setText(firstNameRetrieved + " " + lastNameRetrieved);
                        cin.setText(cinRetrieved);
                        email.setText(emailRetrieved);
                        birthDate.setText(birthdateRetrieved);
                        maritalStatus.setText(maritalStatusRetrieved);

                    }

                }));


//        ************* problem :) *********
//        Iz tablice izvucem uri koji bude formata (npr) content://com.android.providers.media.documents/document/image%3A142633
//        Pretvorim ga u Uri ali i dalje ga Picasso ne loada


        compositeDisposable.add(myAPI.get_uri_for_profile(emailRetrieved)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONArray jsonArray = new JSONArray(s);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Uri = object.getString("uri");
                        }
//                        Uri uri = Uri.parse(receivedImageUri);
//                        Picasso.get().load(uri).into(circleImageView);
                    }
                }));
    }


    private void startRevealActivity(View v) {
        //calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra("firstName", firstNameRetrieved);
        intent.putExtra("lastName", lastNameRetrieved);
        intent.putExtra("cin", cinRetrieved);
        intent.putExtra("email", emailRetrieved);
        intent.putExtra("birthDate", birthdateRetrieved);
        intent.putExtra("maritalStatus", maritalStatusRetrieved);
        intent.putExtra("imageUri", Uri);


        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null);

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0);
    }

    public void editProfile(View view) {
        startRevealActivity(view);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientProfileInformations.this, MenuActivity.class);
        intent.putExtra("patient_email", emailRetrieved);
        MenuActivity.setToken(1);
        startActivity(intent);
    }
}
