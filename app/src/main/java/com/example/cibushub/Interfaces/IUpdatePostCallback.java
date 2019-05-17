package com.example.cibushub.Interfaces;

public interface IUpdatePostCallback {
    void setOnSuccess();
    void setOnError(String error);
    void startLoading();
    void stopLoading();
}
