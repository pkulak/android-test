package com.pkulak.androidtest;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasFragmentInjector;

public class TestApplication extends Application implements HasActivityInjector, HasFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerTestApplicationComponent.create().inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return dispatchingFragmentInjector;
    }
}
