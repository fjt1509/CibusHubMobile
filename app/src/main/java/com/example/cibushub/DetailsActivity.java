package com.example.cibushub;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cibushub.BE.Post;
import com.example.cibushub.Interfaces.IDataAccess;
import com.example.cibushub.Interfaces.IDataImage;
import com.example.cibushub.Interfaces.IDetailsCallback;
import com.example.cibushub.Model.DataAccessFactory;
import com.example.cibushub.Model.DataImage;

public class DetailsActivity extends AppCompatActivity implements IDetailsCallback {

    TextView txtPostName;
    TextView txtPostDesc;
    ImageView postImage;
    FloatingActionButton deleteBtn;

    private ProgressBar deleteProgress;

    private Post post;
    private IDataImage dataImage;
    private IDataAccess dataAccess;




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        txtPostName = findViewById(R.id.txtDetailPostName);
        txtPostDesc = findViewById(R.id.txtDetailPostDesc);
        postImage = findViewById(R.id.postImage);
        deleteProgress = findViewById(R.id.deleteProgress);
        deleteProgress.setVisibility(View.GONE);

        dataAccess = DataAccessFactory.getInstance(this);
        dataImage = new DataImage();

        post = (Post) getIntent().getSerializableExtra("post");

        txtPostName.setText(post.getPostName());
        txtPostDesc.setText(post.getPostDescription());

        dataImage.setImageFromPostPicId(this, postImage, post.getPictureId());

        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
            }
        });

        findViewById(R.id.btnComments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(DetailsActivity.this, CommentActivity.class);
                x.putExtra("postId", post.getId());
                startActivity(x);
            }
        });

    }

    private void deletePost() {
        dataAccess.DeletePost(this, post.getId());
    }




    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void startLoad() {
        deleteProgress.setVisibility(View.VISIBLE);
        deleteBtn.hide();
    }

    @Override
    public void stopLoad() {
        deleteProgress.setVisibility(View.GONE);
    }

    @Override
    public void setError(String error) {
        deleteBtn.show();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
