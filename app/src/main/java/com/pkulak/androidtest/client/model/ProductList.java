package com.pkulak.androidtest.client.model;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductList implements Comparable<ProductList>, Page<Product> {
    public final List<Product> products;
    public final String id;
    public final long total;
    public final long pageIndex;
    public final int pageSize;

    @JsonCreator
    public ProductList(
            @JsonProperty("products")      Collection<Product> products,
            @JsonProperty("id")            String id,
            @JsonProperty("totalProducts") long total,
            @JsonProperty("pageNumber")    long pageNumber,
            @JsonProperty("pageSize")      int pageSize) {
        this.products = ImmutableList.copyOf(products);
        this.id = id;
        this.total = total;
        this.pageIndex = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public int compareTo(@NonNull ProductList other) {
        return (int) (this.pageIndex - other.pageIndex);
    }

    @Override
    public long getPageIndex() {
        return pageIndex;
    }

    @Override
    public int getPerPage() {
        return pageSize;
    }

    @Override
    public List<Product> getItems() {
        return products;
    }
}
