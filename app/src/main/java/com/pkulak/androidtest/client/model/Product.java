package com.pkulak.androidtest.client.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.MalformedURLException;
import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Parcelable {
    public final String id;
    public final String name;
    public final String shortDescription;
    public final String longDescription;
    public final String price;
    public final URL imageUrl;
    public final double reviewRating;
    public final long reviewCount;
    public final boolean inStock;

    @JsonCreator
    public Product(
            @JsonProperty("productId")        String id,
            @JsonProperty("productName")      String name,
            @JsonProperty("shortDescription") String shortDescription,
            @JsonProperty("longDescription")  String longDescription,
            @JsonProperty("price")            String price,
            @JsonProperty("productImage")     URL imageUrl,
            @JsonProperty("reviewRating")     double reviewRating,
            @JsonProperty("reviewCount")      long reviewCount,
            @JsonProperty("inStock")          boolean inStock) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.price = price;
        this.imageUrl = imageUrl;
        this.reviewRating = reviewRating;
        this.reviewCount = reviewCount;
        this.inStock = inStock;
    }

    private Product(Parcel parcel) {
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.shortDescription = parcel.readString();
        this.longDescription = parcel.readString();
        this.price = parcel.readString();
        this.imageUrl = parseUrl(parcel.readString());
        this.reviewRating = parcel.readDouble();
        this.reviewCount = parcel.readLong();
        this.inStock = parcel.readInt() == 1;
    }

    private URL parseUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(shortDescription);
        dest.writeString(longDescription);
        dest.writeString(price);
        dest.writeString(imageUrl.toString());
        dest.writeDouble(reviewRating);
        dest.writeLong(reviewCount);
        dest.writeInt(inStock ? 1 : 0);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
