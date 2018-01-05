package com.pkulak.androidtest;

import com.pkulak.androidtest.client.MainThread;

public class MockMainThread implements MainThread {
    @Override
    public boolean post(Runnable r) {
        r.run();
        return true;
    }
}
