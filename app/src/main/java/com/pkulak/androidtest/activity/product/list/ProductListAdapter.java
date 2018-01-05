package com.pkulak.androidtest.activity.product.list;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pkulak.androidtest.R;
import com.pkulak.androidtest.activity.product.view.ProductViewActivity;
import com.pkulak.androidtest.client.PagingClient;
import com.pkulak.androidtest.client.ProductClient;
import com.pkulak.androidtest.client.model.Product;
import com.pkulak.androidtest.view.Stars;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private final Fragment fragment;
    private final PagingClient<Product> productPages;

    public ProductListAdapter(ProductClient client, Fragment fragment) {
        this.fragment = fragment;

        this.productPages = new PagingClient<>(25, client, range -> {
            notifyItemRangeInserted(range.start, range.count);
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_activity_cell, parent, false);

        layout.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("index", (Integer) view.getTag());
            bundle.putInt("perPage", productPages.getPerPage());
            bundle.putParcelableArrayList("items", new ArrayList<>(productPages.getItems()));

            Intent intent = new Intent(fragment.getContext(), ProductViewActivity.class);
            intent.putExtras(bundle);

            fragment.startActivity(intent);
        });

        return new ViewHolder(layout);
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = productPages.getItem(position);

        holder.priceText.setText(product.price);
        holder.nameText.setText(product.name);
        holder.stars.setRating((float) product.reviewRating);
        holder.reviewCountText.setText("(" + Long.toString(product.reviewCount) + ")");

        Glide.with(fragment)
                .load(product.imageUrl.toString())
                .apply(new RequestOptions().fitCenter())
                .into(holder.image);

        // tag so we know the index on click
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return productPages.getItemCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView priceText;
        final TextView nameText;
        final ImageView image;
        final Stars stars;
        final TextView reviewCountText;

        public ViewHolder(View layout) {
            super(layout);

            this.priceText = layout.findViewById(R.id.price_text);
            this.nameText = layout.findViewById(R.id.name_text);
            this.image = layout.findViewById(R.id.image);
            this.stars = layout.findViewById(R.id.stars);
            this.reviewCountText = layout.findViewById(R.id.review_count_text);
        }
    }
}
