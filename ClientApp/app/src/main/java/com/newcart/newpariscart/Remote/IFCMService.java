package com.newcart.newpariscart.Remote;

import com.newcart.newpariscart.Model.FCMResponse;
import com.newcart.newpariscart.Model.FCMSendData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({

            "Content-Type:application/json",
            "Authorization:key=AAAAqg3dMUg:APA91bHrzNC-cDTqNI5v1DNOJn2IzpB9CFH9dcQrkEewaOYXyHcgDt1IncyD5jw-sx43dNVJKaWGYzri3Lx98drvfNied7WvNJJSQC1UxvaP45n9NOtDhkv9iyA8frNAyXtCLRX-UT7c"


    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);
}
