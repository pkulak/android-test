package com.pkulak.androidtest;

import com.pkulak.androidtest.client.ClientModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AndroidInjectionModule.class, TestApplicationModule.class, ClientModule.class})
public interface TestApplicationComponent extends AndroidInjector<TestApplication> {
}
