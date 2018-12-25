package com.androidcollider.koljadnik.listeners;

public interface OnWriteListener {
    void onSuccess();

    void onError(String error);
}