package com.example.parkme.utils;

import android.content.Context;

import com.example.parkme.constants.ApiConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.parkme.constants.ApiConstants.API_BASE_URL;

public class RetrofitHelper {

    private static final int TIMEOUT_SECONDS = 120;

    private WeakReference<Retrofit> mWeakReference;
    public RetrofitHelper() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client =
                new OkHttpClient
                        .Builder()
                        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                        .addInterceptor(httpLoggingInterceptor)
                        .build();

        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit =
                new Retrofit
                        .Builder()
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .baseUrl(API_BASE_URL)
                        .client(client)
                        .build();

        mWeakReference = new WeakReference<>(retrofit);
    }

    /**
     * To get retrofit instance.
     *
     * @return retrofit weak reference instance.
     */
    public WeakReference<Retrofit> getRetrofit() {
        return mWeakReference;
    }

    public <T> T getService(Class<T> tClass) {
        return mWeakReference.get().create(tClass);
    }



    /**
     * For parsing error response
     */
    public static void showErrorMessage(retrofit2.Response response, Context context) {

        try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            Helper.showToast(context,jObjError.getString(ApiConstants.KEY_ERROR));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
