package com.pkulak.androidtest.activity.product.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.pkulak.androidtest.R;

public class ProductViewActivity extends Activity {
    private static final String TAG_RETAINED_FRAGMENT = ProductViewFragment.class.getName();

    ProductViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view_activity);

        FragmentManager fm = getFragmentManager();
        fragment = (ProductViewFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        if (fragment == null) {
            fragment = new ProductViewFragment();
            fragment.setArguments(getIntent().getExtras());

            fm.beginTransaction()
                    .add(R.id.product_view_activity, fragment, TAG_RETAINED_FRAGMENT)
                    .commit();
        }
    }
}
