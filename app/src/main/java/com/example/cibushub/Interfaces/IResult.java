package com.example.cibushub.Interfaces;

import com.example.cibushub.BE.Post;

import java.util.ArrayList;

public interface IResult {

    void setResult(Post post);
    void setResult(ArrayList<Post> postList);
    void startLoad();
    void stopLoad();

}
