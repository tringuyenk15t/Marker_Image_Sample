package com.example.tringuyen.marker_image_load_sample.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tri Nguyen on 8/1/2016.
 */
public class ServiceGenerator {

    public static final String API_KEY = "AIzaSyAaXtZyeFJZC4kxnX9x_Ykqk_l9pYjsLCE";
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                HttpUrl.Builder builder = chain.request().url().newBuilder();
//                builder.addQueryParameter("key",API_KEY);
//                HttpUrl url = builder.build();
//                Request.Builder reqBuilder = chain.request().newBuilder();
//                reqBuilder.url(url);
//                return chain.proceed(reqBuilder.build());
//            }
//        });
        httpClient
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
