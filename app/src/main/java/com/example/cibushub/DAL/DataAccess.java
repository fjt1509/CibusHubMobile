package com.example.cibushub.DAL;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.cibushub.BE.Comment;
import com.example.cibushub.BE.PictureFile;
import com.example.cibushub.BE.Post;
import com.example.cibushub.BE.PostPicWrap;
import com.example.cibushub.Interfaces.IAddPostCallback;
import com.example.cibushub.Interfaces.ICommentCallBack;
import com.example.cibushub.Interfaces.IDataAccess;
import com.example.cibushub.Interfaces.IDetailsCallback;
import com.example.cibushub.Interfaces.IMainCallback;
import com.example.cibushub.Interfaces.IUpdatePostCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.functions.FirebaseFunctions;


import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.cibushub.MainActivity.TAG;

class DataAccess implements IDataAccess {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFunctions functions = FirebaseFunctions.getInstance();
    private IAddPostCallback addPostCallback;
    private IUpdatePostCallback updatePostCallback;

    public DataAccess(Context c)
    {

    }

    @Override
    public void ReadAllPosts(final IMainCallback result) {
        result.startLoad();
        db.collection("post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    ArrayList<Post> postList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Post post = document.toObject(Post.class);
                        post.setId(document.getId());
                        postList.add(post);
                    }
                    result.setResult(postList);
                    result.stopLoad();
                }
                else {
                    result.stopLoad();
                    result.setError(task.getException().getMessage());
                }
            }

        });
    }

    @Override
    public void DeletePost(final IDetailsCallback result, String id) {
        result.startLoad();
        db.collection("post").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    result.stopLoad();
                    result.finishActivity();
                }
                else {
                    result.stopLoad();
                    result.setError(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void GetAllComments(final ICommentCallBack result, String id) {
        result.startReadLoad();
        Log.d(TAG, id);
        db.collection("post").document(id).collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    ArrayList<Comment> commentList = new ArrayList<>();
                    for (QueryDocumentSnapshot document: task.getResult())
                    {
                        Comment comment = document.toObject(Comment.class);
                        comment.setId(document.getId());
                        commentList.add(comment);
                    }

                    result.stopReadLoad();
                    result.setResult(commentList);
                } else {
                    result.stopReadLoad();
                    result.setError(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void AddComment(final ICommentCallBack result, String id, String commentMessage) {
        result.startAddLoad();



        Date date = new Date();
        Comment comment = new Comment(commentMessage, date);

        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("comment", commentMessage);
        commentMap.put("time", date);
        commentMap.put("uId", comment.getuId());
        commentMap.put("userDisplayName", comment.getUserDisplayName());
        commentMap.put("userDisplayUrl", comment.getUserDisplayUrl());

        db.collection("post").document(id).collection("comments").add(commentMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    result.stopAddLoad();
                    result.setOnSuccess();
                } else {
                    result.stopAddLoad();
                    result.setError(task.getException().getMessage());
                }
            }
        });




    }

    @Override
    public void AddPost(IAddPostCallback result, Post post, PictureFile pic) {

        addPostCallback = result;
        PostPicWrap payload = new PostPicWrap(post, pic);
        addPostCallback.startLoading();
        new AddPostFunction().execute(payload);
    }

    @Override
    public void UpdatePostWithoutPic(final IUpdatePostCallback result, Post post) {
        result.startLoading();
        Map<String, Object> newPost = new HashMap<>();
        newPost.put("postName", post.getPostName());
        newPost.put("postDescription", post.getPostDescription());
        db.collection("post").document(post.getId()).set(newPost, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    result.stopLoading();
                    result.setOnSuccess();

                } else {
                    result.stopLoading();
                    result.setOnError(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void UpdatePostWithNewPic(IUpdatePostCallback result, Post post, PictureFile pic) {
        updatePostCallback = result;
        PostPicWrap payload = new PostPicWrap(post, pic);
        updatePostCallback.startLoading();
        new UpdatePostFunction().execute(payload);


    }


    private class AddPostFunction extends AsyncTask<PostPicWrap, Void, Post> {
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Post doInBackground(PostPicWrap... postPicWraps) {

            PostPicWrap payload = postPicWraps[0];
            OutputStream out = null;
            try
            {
                URL url = new URL(" https://us-central1-cibushub.cloudfunctions.net/Posts ");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);


                JSONObject jsonImage = new JSONObject();
                jsonImage.put("base64", payload.getPostPic().getBase64Image());
                jsonImage.put("name", payload.getPostPic().getName());
                jsonImage.put("size", payload.getPostPic().getSize());

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("postName", payload.getPost().getPostName());
                jsonObject.put("postDescription", payload.getPost().getPostDescription());
                jsonObject.put("image", jsonImage);


                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.writeBytes(jsonObject.toString());
                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(connection.getResponseCode()));
                Log.i("MSG" , connection.getResponseMessage());

                connection.disconnect();

            } catch (Exception e) {
                addPostCallback.setOnError(e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Post post) {
            addPostCallback.stopLoading();
            addPostCallback.setOnSuccess();
            super.onPostExecute(post);
        }


    }

    private class UpdatePostFunction extends AsyncTask<PostPicWrap,Void,Void>{
        @Override
        protected Void doInBackground(PostPicWrap... postPicWraps) {
            PostPicWrap payload = postPicWraps[0];
            OutputStream out = null;
            try
            {
                URL url = new URL(" https://us-central1-cibushub.cloudfunctions.net/Posts ");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                connection.setRequestProperty("Accept","application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);


                JSONObject jsonImage = new JSONObject();
                jsonImage.put("base64", payload.getPostPic().getBase64Image());
                jsonImage.put("name", payload.getPostPic().getName());
                jsonImage.put("size", payload.getPostPic().getSize());

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", payload.getPost().getId());
                jsonObject.put("pictureId", payload.getPost().getPictureId());
                jsonObject.put("postName", payload.getPost().getPostName());
                jsonObject.put("postDescription", payload.getPost().getPostDescription());
                jsonObject.put("image", jsonImage);


                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.writeBytes(jsonObject.toString());
                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(connection.getResponseCode()));
                Log.i("MSG" , connection.getResponseMessage());

                connection.disconnect();

            } catch (Exception e) {
                updatePostCallback.setOnError(e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void nothing) {
            updatePostCallback.stopLoading();
            updatePostCallback.setOnSuccess();
            super.onPostExecute(nothing);
        }

    }
}
