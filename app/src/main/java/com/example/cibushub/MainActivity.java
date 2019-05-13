package com.example.cibushub;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cibushub.BE.Post;
import com.example.cibushub.Interfaces.IResult;
import com.example.cibushub.Model.DataAccessFactory;
import com.example.cibushub.Interfaces.IDataAccess;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IResult {

    public static String TAG = "CibusHub";

    private ListView listView;
    private PostAdapter adpater;
    private ProgressBar pBar;

    private IDataAccess mDataAccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataAccess = DataAccessFactory.getInstance(this);
        listView = findViewById(R.id.postList);
        pBar = findViewById(R.id.progressBar);
        pBar.setVisibility(View.GONE);
        mDataAccess.ReadAllPosts(this);

    }


    private void setupListView(ArrayList<Post> postList) {

        adpater = new PostAdapter(this, R.layout.activity_main_cell, postList);
        listView.setAdapter(adpater);
    }



    private class PostAdapter extends ArrayAdapter<Post> {

        List<Post> postList  = new ArrayList<>();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        public PostAdapter(Context context, int resource, List<Post> posts) {
            super(context, resource, posts);
            this.postList.addAll(posts);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {

            final Post post = postList.get(position);

            if (v == null) {
                LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.activity_main_cell, null);
                Log.d("LIST", "Position: " + position + " View created");
            }
            else {
                Log.d("LIST", "Position: " + position + " View Reused");
            }


            TextView txtPostName = v.findViewById(R.id.txtPostName);
            TextView txtPostDesc = v.findViewById(R.id.txtPostDesc);
            TextView txtPostDate = v.findViewById(R.id.txtPostDate);
            final ImageView postPic = v.findViewById(R.id.imgThumnail);

           // Log.d(TAG, post.getPostImage().toString());
           // GlideApp.with(v.getContext()).load(post.getPostImage()).into(postPic);

            setImage(v.getContext(), postPic, post);


            txtPostName.setText(post.getPostName());
            txtPostDesc.setText(post.getPostDescription());
            txtPostDate.setText(post.getPostTime().subSequence(0,10));

            return v;
        }

        private void setImage(final Context context, final ImageView postPic, Post post) {
            storage.getReference("post-pictures/" + post.getPictureId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    GlideApp.with(context).load(uri).into(postPic);
                }
            });
        }
    }

    @Override
    public void setResult(Post post) {
    }

    @Override
    public void setResult(ArrayList<Post> postList) {
        setupListView(postList);
    }

    @Override
    public void startLoad() {
        pBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoad() {
        pBar.setVisibility(View.GONE);
    }

}


