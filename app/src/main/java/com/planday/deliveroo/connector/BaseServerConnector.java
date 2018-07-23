package com.planday.deliveroo.connector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.planday.deliveroo.BuildConfig;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by longnguyen on 9:13 PM, 7/19/18.
 */
public abstract class BaseServerConnector<T> {

    final String TAG                          = "BaseServerConnector";
    final String GOOGLE_API                   = "https://maps.googleapis.com/maps/api/";
    final String EXAMPLE_URL                  = "https://mycompany.com";
    final String CONTENT_TYPE                 = "application/json; charset=utf-8";
    protected T                       mApi;
    protected Retrofit                mRetrofit;
    protected OkHttpClient            mHttpClient;
    protected Gson                    mGson;

    public BaseServerConnector(Class<T> clazz) {
        setApi(getRetrofit().create(clazz));
    }

    protected abstract Retrofit getRetrofit();

    protected T getApi() {
        return mApi;
    }

    private void setApi(T api) {
        this.mApi = api;
    }

    protected Gson getGson() {
        return mGson;
    }

    private OkHttpClient.Builder getClientWithHeaderInfo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(4000, TimeUnit.SECONDS)
                                                                 .readTimeout(4000, TimeUnit.SECONDS);

        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.header("Example-Client-Version", "Android-" + BuildConfig.VERSION_NAME);
                requestBuilder.header("Content-Type", CONTENT_TYPE);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return builder;
    }

    protected Retrofit getRetrofit(String baseURL) {
        OkHttpClient.Builder builder = getClientWithHeaderInfo();

        mHttpClient = builder.build();

        mGson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder().baseUrl(baseURL)
                                                  .addConverterFactory(GsonConverterFactory.create(mGson))
                                                  .callbackExecutor(Executors.newSingleThreadExecutor())
                                                  .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                                                  .client(mHttpClient)
                                                  .build();
    }
}
