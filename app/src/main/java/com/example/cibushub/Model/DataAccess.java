package com.example.cibushub.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.cibushub.BE.Comment;
import com.example.cibushub.BE.PictureFile;
import com.example.cibushub.BE.Post;
import com.example.cibushub.Interfaces.IAddPostCallback;
import com.example.cibushub.Interfaces.ICommentCallBack;
import com.example.cibushub.Interfaces.IDataAccess;
import com.example.cibushub.Interfaces.IDetailsCallback;
import com.example.cibushub.Interfaces.IMainCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firestore.v1.DocumentTransform;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.cibushub.MainActivity.TAG;

class DataAccess implements IDataAccess {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFunctions functions = FirebaseFunctions.getInstance();

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

        Map<String, Object> comment = new HashMap<>();
        comment.put("comment", commentMessage);
        comment.put("time", date);

        db.collection("post").document(id).collection("comments").add(comment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
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
    }


}
