package com.example.cibushub.BE;

public class PostPicWrap {
    private Post post;
    private PictureFile postPic;

    public PostPicWrap(Post post, PictureFile postPic) {
        this.post = post;
        this.postPic = postPic;
    }

    public PostPicWrap() {
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public PictureFile getPostPic() {
        return postPic;
    }

    public void setPostPic(PictureFile postPic) {
        this.postPic = postPic;
    }
}
