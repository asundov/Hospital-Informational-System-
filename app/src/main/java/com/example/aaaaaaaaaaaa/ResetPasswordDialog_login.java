package com.example.aaaaaaaaaaaa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.aaaaaaaaaaaa.Retrofit.INodeJs;
import com.example.aaaaaaaaaaaa.Retrofit.RetrofitClient;

import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ResetPasswordDialog_login extends AppCompatDialogFragment {
    private EditText email;
    private Button resetEmailButton;
    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        email = view.findViewById(R.id.email);
        resetEmailButton = view.findViewById(R.id.resetButton);

        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        resetEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText())) {
                    Toast.makeText(getActivity(), "Email field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Random generator = new Random();
                    StringBuilder randomStringBuilder = new StringBuilder();
                    int randomLength = generator.nextInt(30);
                    char tempChar;
                    for (int i = 0; i < randomLength; i++) {
                        tempChar = (char) (generator.nextInt(10) + 32);
                        randomStringBuilder.append(tempChar);
                    }
                    reset_password(email.getText().toString(), randomStringBuilder.toString());
                    Intent mojintent = new Intent(getActivity(), MainActivity_login.class);
                    startActivity(mojintent);
                }
            }
        });
        builder.setView(view);
        return builder.create();
    }


    private void reset_password(String email, String password) {
        compositeDisposable.add(myAPI.reset_password(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                    }
                }));
    }
}
