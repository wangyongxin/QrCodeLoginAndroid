package com.test.qrcodeloginandroid;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wangyongxin on 2018/1/30.
 */

public class HttpUtil {

    public final static String BASE_URL_GET = "http://wwwdev.ttplus.cn";


    @NonNull
    public static Retrofit getRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);

        final OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
