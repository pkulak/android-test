package com.pkulak.androidtest.activity.product.view;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pkulak.androidtest.R;
import com.pkulak.androidtest.client.PagingClient;
import com.pkulak.androidtest.client.ProductClient;
import com.pkulak.androidtest.client.model.Product;
import com.pkulak.androidtest.view.Stars;

import java.util.Collection;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ViewHolder> {
    private final Fragment fragment;
    private final PagingClient<Product> productPages;

    public ProductViewAdapter(int perPage, Collection<Product> items, ProductClient client,
                              Fragment fragment) {
        this.fragment = fragment;

        this.productPages = new PagingClient<>(perPage, items, client, range -> {
            notifyItemRangeInserted(range.start, range.count);
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_view_activity_cell, parent, false);

        return new ViewHolder(layout);
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = productPages.getItem(position);

        holder.nameText.setText(product.name);
        holder.stars.setRating((float) product.reviewRating);

        if (product.longDescription == null) {
            holder.descriptionText.setText(null);
        } else {
            holder.descriptionText.setText(
                    Html.fromHtml(product.longDescription, Html.FROM_HTML_MODE_COMPACT));
        }

        Glide.with(fragment)
                .load(product.imageUrl.toString())
                .apply(new RequestOptions().fitCenter())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return productPages.getItemCount();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final Stars stars;
        final TextView nameText;
        final TextView descriptionText;

        public ViewHolder(View layout) {
            super(layout);

            this.image = layout.findViewById(R.id.image);
            this.stars = layout.findViewById(R.id.stars);
            this.nameText = layout.findViewById(R.id.name_text);
            this.descriptionText = layout.findViewById(R.id.description_text);
        }
    }
}
