package com.example.cibushub.Interfaces;

import com.example.cibushub.BE.PictureFile;
import com.example.cibushub.BE.Post;

public interface IDataAccess {

    void ReadAllPosts (IMainCallback result);
    void DeletePost (IDetailsCallback result, String id);
    void GetAllComments (ICommentCallBack result, String id);
    void AddComment (ICommentCallBack result, String id, String comment);
    void AddPost (IAddPostCallback result, Post post, PictureFile pic);
}
