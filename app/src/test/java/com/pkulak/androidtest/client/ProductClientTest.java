package com.pkulak.androidtest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkulak.androidtest.MockMainThread;
import com.pkulak.androidtest.ProductListResponse;
import com.pkulak.androidtest.client.model.ProductList;

import org.junit.Test;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.*;

public class ProductClientTest {
    @Test
    public void productFetchingWorks() throws Exception {
        MockWebServer server = new MockWebServer();

        // queue up a response
        server.enqueue(new MockResponse().setBody(ProductListResponse.generate(0, 10, 10)));

        server.start();

        ProductClient client = new ProductClient(
                "http://localhost:" + server.getPort(),
                new OkHttpClient.Builder()
                        .addInterceptor(new AuthInterceptor("superSekretKey"))
                        .build(),
                new ResponseMapper(new ObjectMapper(), new MockMainThread()));

        ProductList productList = (ProductList) client.getPage(0, 10).get();

        assertEquals(productList.products.size(), 10);
        assertEquals(productList.products.get(0).reviewRating, 4.5, 0);

        // make sure we sent the right request
        RecordedRequest request = server.takeRequest();
        assertEquals(request.getPath(), "/products/superSekretKey/0/10");

        server.shutdown();
    }
}
