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

    @POST("get_doctor_list")
    @FormUrlEncoded
    Observable<String> get_doctor_list(@Field("emailDoctor") String emailDoctor);

    @POST("get_all_doctors")
    @FormUrlEncoded
    Observable<String> get_all_doctors(@Field("fullName") String doctors);

    @POST("add_appointment")
    @FormUrlEncoded
    Observable<String> add_appointment(@Field("date") String date,
                                       @Field("time") String time,
                                       @Field("emailDoctor") String emailDoctor,
                                       @Field("emailPatient") String emailPatient,
                                       @Field("status") String status);

    @POST("get_hospitalisation")
    @FormUrlEncoded
    Observable<String> get_hospitalisation(@Field("emailPatient") String emailPatient);

    @POST("get_consultation")
    @FormUrlEncoded
    Observable<String> get_consultation(@Field("emailPatient") String emailPatient);

    @POST("get_consultation_doctor")
    @FormUrlEncoded
    Observable<String> get_consultation_doctor(@Field("emailPatient") String emailPatient);

    @POST("relationship")
    @FormUrlEncoded
    Observable<String> relationship(@Field("emailPatient") String emailPatient);

    @POST("relationship2")
    @FormUrlEncoded
    Observable<String> relationship2(@Field("emailPatient") String emailPatient);

    @POST("get_patient")
    @FormUrlEncoded
    Observable<String> get_patient(@Field("emailPatient") String emailPatient);

    @POST("get_uri")
    @FormUrlEncoded
    Observable<String> get_uri(@Field("emailPatient") String emailPatient,
                              @Field("uri") String uri);

    @POST("get_uri_for_profile")
    @FormUrlEncoded
    Observable<String> get_uri_for_profile(@Field("emailPatient") String emailPatient);

    @POST("update_patient")
    @FormUrlEncoded
    Observable<String> update_patient(@Field("firstName") String firstName,
                                       @Field("lastName") String lastName,
                                       @Field("birthDate") String birthDate,
                                       @Field("email") String email,
                                       @Field("CIN") String CIN,
                                       @Field("maritalStatus") String maritalStatus);
}