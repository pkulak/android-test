package com.pkulak.androidtest.client;

// Basically a MainLooper Handler, but an interface so we can mock it.
public interface MainThread {
    boolean post(Runnable r);
}
