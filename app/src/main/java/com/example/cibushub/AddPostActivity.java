package com.example.cibushub;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cibushub.BE.PictureFile;
import com.example.cibushub.BE.Post;
import com.example.cibushub.Helpers.PostPicUtils;
import com.example.cibushub.Interfaces.IAddPostCallback;
import com.example.cibushub.Interfaces.IDataAccess;
import com.example.cibushub.DAL.DataAccessFactory;

import java.io.File;
import java.util.Date;

public class AddPostActivity extends AppCompatActivity implements IAddPostCallback {
    private final static String LOGTAG = "Camtag";
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private final static int REQUEST_GET_SINGLE_FILE = 101;
    private PostPicUtils postPicUtils;
    File mFile;
    ImageView imageTaken;
    EditText inputPostName;
    EditText inputPostDesc;
    IDataAccess dataAccess;
    FloatingActionButton btnTakePhoto;
    FloatingActionButton btnAddPost;
    ProgressBar progressAdd;
    TextView txtAddLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        imageTaken = findViewById(R.id.imageTaken);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        dataAccess = DataAccessFactory.getInstance(this);
        postPicUtils = new PostPicUtils();

        progressAdd = findViewById(R.id.progressAdd);
        progressAdd.setVisibility(View.GONE);
        txtAddLoading = findViewById(R.id.txtAddLoading);
        txtAddLoading.setVisibility(View.INVISIBLE);

        inputPostName = findViewById(R.id.inputPostNameAdd);
        inputPostDesc = findViewById(R.id.inputPostDescAdd);

        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        btnAddPost = findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
    }


    private void addPost() {

        if(!inputPostName.getText().toString().trim().isEmpty() && !inputPostDesc.getText().toString().isEmpty())
        {
            if(mFile.exists())
            {
                String base64 = postPicUtils.base64Encode(mFile);
                PictureFile postPic = new PictureFile(base64, mFile.length(), mFile.getName());

                String postName = inputPostName.getText().toString();
                String postDesc = inputPostDesc.getText().toString();
                Date postDate = new Date();
                Post post = new Post(postName, postDesc, postDate);

                dataAccess.AddPost(this, post, postPic);
            } else {
                Toast.makeText(this, "Please take a photo to post", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Please enter a post name and post description", Toast.LENGTH_LONG).show();
        }

    }

    private void takePhoto()
    {

        mFile = postPicUtils.getOutputMediaFile();
        if (mFile == null)
        {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));

        Log.d(LOGTAG, "file uri = " + Uri.fromFile(mFile).toString());

        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(LOGTAG, "camera app will be started");
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        else
            Log.d(LOGTAG, "camera app could NOT be started");

    }




    void log(String s) {
        Log.d(LOGTAG, s);
    }


    private void showPictureTaken(File f) {
        imageTaken.setImageURI(Uri.fromFile(f));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showPictureTaken(mFile);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show();
                return;

            } else
                Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show();
        }
    }





    @Override
    public void setOnSuccess() {
        finish();
    }

    @Override
    public void setOnError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void startLoading() {
        btnTakePhoto.hide();
        inputPostName.setVisibility(View.GONE);
        inputPostName.setHint("");
        inputPostDesc.setVisibility(View.GONE);
        inputPostName.setHint("");
        imageTaken.setVisibility(View.GONE);
        btnAddPost.hide();

        txtAddLoading.setVisibility(View.VISIBLE);
        progressAdd.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        btnTakePhoto.show();
        inputPostName.setVisibility(View.VISIBLE);
        inputPostDesc.setVisibility(View.VISIBLE);
        imageTaken.setVisibility(View.VISIBLE);
        btnAddPost.show();

        progressAdd.setVisibility(View.GONE);
        txtAddLoading.setVisibility(View.GONE);

    }
}
