package com.example.aaaaaaaaaaaa;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class EditProfileActivity extends AppCompatActivity {
    private static final int Pick_Image_Request = 1;
    RevealAnimation mRevealAnimation;
    CircleImageView circleImageView;
    private Uri mImageUri;
    EditText fullName, cin, email, phoneNumber, birthDate, maritalStatus;
    String receivedFullName, receivedCin, receivedEmail, receivedPhoneNumber, receivedBirthDate, receivedMaritalStatus;
    String receivedImageUri;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullName = findViewById(R.id.fullName);
        cin = findViewById(R.id.cin);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        birthDate = findViewById(R.id.birthDate);
        maritalStatus = findViewById(R.id.maritalStatus);
        circleImageView = findViewById(R.id.profile_image);


        Intent intent = this.getIntent();   //get the intent to recieve the x and y coords, that you passed before

        receivedFullName = intent.getStringExtra("fullName");
        receivedCin = intent.getStringExtra("cin");
        receivedEmail = intent.getStringExtra("email");
        receivedPhoneNumber = intent.getStringExtra("phoneNumber");
        receivedBirthDate = intent.getStringExtra("birthDate");
        receivedMaritalStatus = intent.getStringExtra("maritalStatus");
        receivedImageUri = intent.getStringExtra("imageUri");
        Uri uri = Uri.parse(receivedImageUri);

        fullName.setText(receivedFullName);
        cin.setText(receivedCin);
        email.setText(receivedEmail);
        phoneNumber.setText(receivedPhoneNumber);
        birthDate.setText(receivedBirthDate);
        maritalStatus.setText(receivedMaritalStatus);
        Picasso.get().load(uri).into(circleImageView);

        FrameLayout rootLayout = findViewById(R.id.root); //there you have to get the root layout of your second activity
        mRevealAnimation = new RevealAnimation(rootLayout, intent, this);

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        compositeDisposable.add(myAPI.get_uri_for_profile(receivedEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(EditProfileActivity.this, "s je "+s, Toast.LENGTH_SHORT).show();
//                        JSONArray jsonArray = new JSONArray(s);
//
//                        String uri = "";
//
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//                            uri = object.getString("uri");
//                        }
//                        URI myURI = new URI(uri);
//                        Picasso.get().load(myURI.toString()).into(circleImageView);
                    }

                }));
    }

    public void onBackPressed() {
        mRevealAnimation.unRevealActivity();
    }

    public void editProfilePicture(View view) {
        openFileChooser();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Pick_Image_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick_Image_Request && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Toast.makeText(this, "aa" + mImageUri, Toast.LENGTH_SHORT).show();
            Picasso.get().load(mImageUri).into(circleImageView);
            uploadImage(mImageUri.toString());
        }

    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage( String uri) {

        //nit api
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        compositeDisposable.add(myAPI.get_uri(receivedEmail, getExtension(mImageUri))

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(EditProfileActivity.this, "bravo ja je "+s, Toast.LENGTH_SHORT).show();
                        }

                    }));
                           }




//        StorageReference ref = mStorageReference.child(receivedEmail + "." + getExtension(mImageUri));
//        ref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(EditProfileActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
//            }
//        });


        //  }

    public void update(View view) {


//        database = FirebaseDatabase.getInstance();
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        final String userUid = user.getUid();
//        DatabaseReference dbRef = database.getReference("Patients");
//        String[] fn = fullName.getText().toString().split(" ");
//        final Patient patient = new Patient(fn[0],fn[1],birthDate.getText().toString(),phoneNumber.getText().toString(),email.getText().toString()
//                ,cin.getText().toString(),maritalStatus.getText().toString());
//        dbRef.child(userUid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataSnapshot.getRef().setValue(patient);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        Intent intent = new Intent(EditProfileActivity.this, PatientProfileInformations.class);
//        MyPreferences preferences = new MyPreferences(this);
//
//        preferences.setString("emailpatient", receivedEmail);
//        startActivity(intent);
    }

}
