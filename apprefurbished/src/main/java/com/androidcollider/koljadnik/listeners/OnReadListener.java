package com.androidcollider.koljadnik.listeners;

public interface OnReadListener<T extends Object> {
    void onSuccess(T result);

    void onError(String error);
}