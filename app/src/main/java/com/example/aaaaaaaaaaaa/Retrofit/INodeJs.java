package com.example.aaaaaaaaaaaa.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface INodeJs {

    @POST("register_doctor")
    @FormUrlEncoded
    Observable<String> register_doctor(@Field("fullName") String fullName,
                                       @Field("code") String code,
                                       @Field("phoneNumber") String phoneNumber,
                                       @Field("email") String email,
                                       @Field("city") String city,
                                       @Field("address") String address,
                                       @Field("speciality") String speciality,
                                       @Field("password") String password);

    @POST("register_patient")
    @FormUrlEncoded
    Observable<String> registerPatient(@Field("firstName") String firstName,
                                        @Field("lastName") String lastName,
                                        @Field("birthDate") String birthDate,
                                       @Field("password") String password,
                                       @Field("email") String email,
                                        @Field("CIN") String CIN,
                                        @Field("maritalStatus") String maritalStatus);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                 @Field("password") String password);

    @POST("reset_password")
    @FormUrlEncoded
    Observable<String> reset_password(@Field("email") String email,
                                 @Field("new_password") String new_password);

    @POST("get_appointment")
    @FormUrlEncoded
    Observable<String> get_appointment(@Field("email") String email);

    @POST("get_doctor")
    @FormUrlEncoded
    Observable<String> get_doctor(@Field("emailDoctor") String emailDoctor);

    @POST("get_all_doctors")
    @FormUrlEncoded
    Observable<String> get_all_doctors(@Field("doctors") String doctors);
}