package com.example.cibushub.Interfaces;

public interface IDataAccess {

    void ReadAllPosts (IMainCallback result);
    void DeletePost (IDetailsCallback result, String id);
    void GetAllComments (ICommentCallBack result, String id);
    void AddComment (ICommentCallBack result, String id, String comment);
}
