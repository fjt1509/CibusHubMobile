package com.example.cibushub.Interfaces;

import com.example.cibushub.BE.Post;

import java.util.ArrayList;

public interface IMainCallback {

    void setResult(ArrayList<Post> postList);
    void startLoad();
    void stopLoad();
    void setError(String error);


}
