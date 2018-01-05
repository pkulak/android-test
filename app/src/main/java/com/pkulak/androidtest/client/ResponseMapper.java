package com.pkulak.androidtest.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseMapper {
    private final ObjectMapper mapper;
    private final Executor pool = Executors.newSingleThreadExecutor();
    private final MainThread mainThread;

    @Inject
    public ResponseMapper(ObjectMapper mapper, MainThread mainThread) {
        this.mapper = mapper;
        this.mainThread = mainThread;
    }

    public <T> Callback map(CompletableFuture<T> future, Class<? extends T> type) {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() != 200) {
                    completeExceptionally(
                            future,
                            new InvalidHttpStatusCodeException(response.code()));
                    return;
                }

                // do the response reading and mapping off the main thread
                pool.execute(() -> {
                    try (ResponseBody body = response.body()) {
                        if (body == null) {
                            complete(future, null);
                            return;
                        }

                        try {
                            complete(future, mapper.readValue(body.byteStream(), type));
                        } catch (IOException e) {
                            completeExceptionally(future, e);
                        }}
                });
            }
        };
    }

    private <T> void completeExceptionally(CompletableFuture<T> future, Throwable ex) {
        mainThread.post(() -> future.completeExceptionally(ex));
    }

    private <T> void complete(CompletableFuture<T> future, T value) {
        mainThread.post(() -> future.complete(value));
    }

    public static class InvalidHttpStatusCodeException extends RuntimeException {
        public final int code;

        public InvalidHttpStatusCodeException(int code) {
            super("Invalid http status code: " + code);
            this.code = code;
        }
    }
}
