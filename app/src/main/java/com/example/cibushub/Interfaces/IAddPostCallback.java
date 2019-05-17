package com.example.cibushub.Interfaces;

public interface IAddPostCallback {
    void setOnSuccess();
    void setOnError(String error);
    void startLoading();
    void stopLoading();
}
