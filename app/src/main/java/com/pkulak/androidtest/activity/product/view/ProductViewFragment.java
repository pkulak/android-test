package com.pkulak.androidtest.activity.product.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pkulak.androidtest.R;
import com.pkulak.androidtest.client.ProductClient;
import com.pkulak.androidtest.client.model.Product;

import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ProductViewFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();

    @Inject
    ProductClient productClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        AndroidInjection.inject(this);

        //noinspection ConstantConditions
        this.adapter = new ProductViewAdapter(
                getArguments().getInt("perPage", 25),
                getArguments().getParcelableArrayList("items").stream()
                        .map(p -> (Product) p)
                        .collect(Collectors.toList()),
                productClient,
                this);

        this.layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        if (recyclerView == null) {
            recyclerView = (RecyclerView) inflater.inflate(
                    R.layout.product_view_activity_fragment, container, false);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(getArguments().getInt("index"));
            pagerSnapHelper.attachToRecyclerView(recyclerView);
        } else {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        return recyclerView;
    }
}
