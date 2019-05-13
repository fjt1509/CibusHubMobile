package com.example.cibushub.Interfaces;

import com.example.cibushub.BE.Comment;

import java.util.ArrayList;

public interface ICommentCallBack {

    void setResult(ArrayList<Comment> commentList);
    void startReadLoad();
    void stopReadLoad();
    void startAddLoad();
    void stopAddLoad();
    void setOnSuccess();
    void setError(String error);

}
