package com.pkulak.androidtest.activity.product.list;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pkulak.androidtest.R;
import com.pkulak.androidtest.client.ProductClient;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ProductListFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Inject
    ProductClient productClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        AndroidInjection.inject(this);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ProductListAdapter(productClient, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        if (recyclerView == null) {
            recyclerView = (RecyclerView) inflater.inflate(
                    R.layout.product_list_activity_fragment, container, false);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        return recyclerView;
    }
}
