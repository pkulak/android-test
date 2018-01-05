package com.pkulak.androidtest;

import com.pkulak.androidtest.activity.product.list.ProductListFragment;
import com.pkulak.androidtest.activity.product.view.ProductViewFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TestApplicationModule {
    @ContributesAndroidInjector
    abstract ProductListFragment contributeProductListFragment();

    @ContributesAndroidInjector
    abstract ProductViewFragment contributeProductViewFragment();
}
