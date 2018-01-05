package com.pkulak.androidtest.client;

import android.annotation.SuppressLint;

import com.pkulak.androidtest.client.model.Page;
import com.pkulak.androidtest.client.model.Product;
import com.pkulak.androidtest.client.model.ProductList;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ProductClient implements PagingClient.PageFetcher<Product> {
    private final String baseUrl;
    private final OkHttpClient client;
    private final ResponseMapper mapper;

    @Inject
    public ProductClient(String baseUrl, OkHttpClient client, ResponseMapper mapper) {
        this.baseUrl = baseUrl;
        this.client = client;
        this.mapper = mapper;
    }

    /**
     * Get a page of products
     *
     * @param index the index of the page (0-indexed)
     * @param perPage how many results, at maximum, to include
     * @return a list of products
     */
    @Override
    @SuppressLint("DefaultLocale")
    public CompletableFuture<Page<Product>> getPage(int index, int perPage) {
        CompletableFuture<Page<Product>> ret = new CompletableFuture<>();

        Request request = new Request.Builder()
                .url(String.format("%s/products/:key/%d/%d", baseUrl, index, perPage))
                .build();

        client.newCall(request).enqueue(mapper.map(ret, ProductList.class));

        return ret;
    }
}
