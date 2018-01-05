package com.pkulak.androidtest.activity.product.list;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.pkulak.androidtest.R;

public class ProductListActivity extends Activity {
    private static final String TAG_RETAINED_FRAGMENT = ProductListFragment.class.getName();

    ProductListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);

        FragmentManager fm = getFragmentManager();
        fragment = (ProductListFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        if (fragment == null) {
            fragment = new ProductListFragment();

            fm.beginTransaction()
                    .add(R.id.product_list_activity, fragment, TAG_RETAINED_FRAGMENT)
                    .commit();
        }
    }
}
