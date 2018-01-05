package com.pkulak.androidtest.client.model;

import java.util.List;

public interface Page<T> {
    long getPageIndex();

    int getPerPage();

    List<T> getItems();
}
