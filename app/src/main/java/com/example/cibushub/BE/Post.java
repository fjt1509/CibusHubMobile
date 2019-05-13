package com.example.cibushub.BE;

import android.net.Uri;

public class Post {
    private String Id;
    private String PictureId;
    private String PostName;
    private String postDescription;
    private String PostTime;
    private Uri PostImage;

    public Post() {
    }

    public String getId() {
        return Id;
    }

    public String getPictureId() {
        return PictureId;
    }

    public String getPostName() {
        return PostName;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public String getPostTime() {
        return PostTime;
    }

    public void setPictureId(String pictureId) {
        PictureId = pictureId;
    }

    public void setPostName(String postName) {
        PostName = postName;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public Uri getPostImage() {
        return PostImage;
    }

    public void setPostImage(Uri postImage) {
        PostImage = postImage;
    }

    public void setPostTime(String postTime) {
        PostTime = postTime;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString()
    {
        return "ID: " + Id + "\nPostName:" + PostName + "\nPostDescription" + postDescription;
    }

}
