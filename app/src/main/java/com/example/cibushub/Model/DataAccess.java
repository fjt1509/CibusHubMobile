package com.example.cibushub.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.example.cibushub.BE.Post;
import com.example.cibushub.Interfaces.IDataAccess;
import com.example.cibushub.Interfaces.IResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;

import static com.example.cibushub.MainActivity.TAG;

class DataAccess implements IDataAccess {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    public DataAccess(Context c)
    {

    }

    @Override
    public void ReadAllPosts(final IResult result) {



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
                    Log.w(TAG, "onError: ", task.getException());
                }
            }

        });
    }




}
