package com.pkulak.androidtest.client;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Response;

// Replace our API key placeholder in the path with the auth key. Bit hacky, but it gets the
// auth stuff out of the clients.
public class AuthInterceptor implements Interceptor {
    private final String apiKey;

    public AuthInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        int keyIndex = chain.request().url().pathSegments().indexOf(":key");

        if (keyIndex < 0) {
            // nothing to do here
            return chain.proceed(chain.request());
        }

        // do the path replacement of the api key
        HttpUrl newUrl = chain.request().url().newBuilder()
                .setPathSegment(keyIndex, apiKey)
                .build();

        return chain.proceed(chain.request().newBuilder()
                .url(newUrl)
                .build());
    }
}
