package com.pkulak.androidtest.client;

import android.os.Handler;
import android.os.Looper;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class ClientModule {
    private final String apiKey;
    private final String baseUrl;

    public ClientModule() {
        this.apiKey = "superSecretKeyCommittedToGithub";
        this.baseUrl = "https://teserapp.appspot.com/_ah/api/test/v1";
    }

    public ClientModule(String apiKey, String baseUrl) {
        // we can't have trailing slashes in our base url
        while (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    @Provides
    String provideBaseUrl() {
        return baseUrl;
    }

    @Provides
    @Singleton
    OkHttpClient provideClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(apiKey))
                .build();
    }

    @Provides
    @Singleton
    ObjectMapper provideMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    MainThread provideMainThread() {
        return new AndroidMainThread();
    }

    private static class AndroidMainThread implements MainThread {
        Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public boolean post(Runnable r) {
            return handler.post(r);
        }
    }
}
