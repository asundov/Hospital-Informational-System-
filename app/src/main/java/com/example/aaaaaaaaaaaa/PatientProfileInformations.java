package com.example.aaaaaaaaaaaa;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

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
    String firstNameRetrieved, lastNameRetrieved, cinRetrieved, emailRetrieved, phoneNumberRetrieved, maritalStatusRetrieved, birthdateRetrieved;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_informations);

        fullName = findViewById(R.id.fullName);
        cin = findViewById(R.id.cin);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
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

                    }

                }));
        fullName.setText(firstNameRetrieved + " " + lastNameRetrieved);
        cin.setText(cinRetrieved);
        email.setText(emailRetrieved);
        birthDate.setText(birthdateRetrieved);
        maritalStatus.setText(maritalStatusRetrieved);


//                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//                StorageReference profileRef = storageReference.child("Profile pictures").child(emailRetrieved + ".jpg");
//                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Uri = uri.toString();
//                        Picasso.get().load(uri).into(circleImageView);
//                    }
//                });


    }


private void startRevealActivity(View v){
        //calculates the center of the View v you are passing
        int revealX=(int)(v.getX()+v.getWidth()/2);
        int revealY=(int)(v.getY()+v.getHeight()/2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent=new Intent(this,EditProfileActivity.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X,revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y,revealY);
        intent.putExtra("fullName",firstNameRetrieved+" "+lastNameRetrieved);
        intent.putExtra("cin",cinRetrieved);
        intent.putExtra("email",emailRetrieved);
        intent.putExtra("phoneNumber",phoneNumberRetrieved);
        intent.putExtra("birthDate",birthdateRetrieved);
        intent.putExtra("maritalStatus",maritalStatusRetrieved);
        intent.putExtra("imageUri",Uri);

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this,intent,null);

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0,0);
        }

public void editProfile(View view){
        startRevealActivity(view);
        }

@Override
public void onBackPressed(){
        Intent intent=new Intent(PatientProfileInformations.this,MenuActivity.class);
        MenuActivity.setToken(1);
        startActivity(intent);
        }
        }