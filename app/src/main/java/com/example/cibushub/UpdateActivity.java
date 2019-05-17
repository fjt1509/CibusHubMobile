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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cibushub.BE.PictureFile;
import com.example.cibushub.BE.Post;
import com.example.cibushub.Helpers.PostPicUtils;
import com.example.cibushub.Interfaces.IDataAccess;
import com.example.cibushub.Interfaces.IDataImage;
import com.example.cibushub.Interfaces.IUpdatePostCallback;
import com.example.cibushub.Model.DataAccessFactory;
import com.example.cibushub.Model.DataImage;

import java.io.File;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity implements IUpdatePostCallback {
    private final static String LOGTAG = "Camtag";
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    Post post;
    IDataAccess dataAccess;
    IDataImage dataImage;

    EditText inputPostName;
    EditText inputPostDesc;
    Button btnNewPic;
    ImageView postPic;
    FloatingActionButton btnUpdatePost;

    ProgressBar updateProgress;
    TextView updateInfo;

    File mFile;
    Boolean newPic;
    FloatingActionButton btnGoBackUpdate;

    private PostPicUtils postPicUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        newPic = false;
        post = (Post) getIntent().getSerializableExtra("post");
        dataAccess = DataAccessFactory.getInstance(this);
        dataImage = new DataImage();
        postPicUtils = new PostPicUtils();

        inputPostName = findViewById(R.id.inputNameUpdate);
        inputPostDesc = findViewById(R.id.inputDescUpdate);

        inputPostName.setText(post.getPostName());
        inputPostDesc.setText(post.getPostDescription());

        btnNewPic = findViewById(R.id.btnNewPic);
        btnNewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        btnGoBackUpdate = findViewById(R.id.btnGoBackUpdate);
        btnGoBackUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noNewPic();
            }
        });
        btnGoBackUpdate.hide();
        
        btnUpdatePost = findViewById(R.id.btnUpdatePost);
        btnUpdatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhoto();
            }
        });

        postPic = findViewById(R.id.imageTakenUpdate);


        dataImage.setImageFromPostPicId(this, postPic, post.getPictureId());

        updateProgress = findViewById(R.id.progressUpdate);
        updateProgress.setVisibility(View.GONE);

        updateInfo = findViewById(R.id.txtUpdateLoading);
        updateInfo.setVisibility(View.GONE);


        Log.d(LOGTAG, post.toString());



    }

    private void updatePhoto() {

        if(!inputPostName.getText().toString().trim().isEmpty() && !inputPostDesc.getText().toString().isEmpty())
        {
            String postName = inputPostName.getText().toString();
            String postDesc = inputPostDesc.getText().toString();
            Date postDate = new Date();
            post.setPostName(postName);
            post.setPostDescription(postDesc);

            Log.d(LOGTAG, newPic.toString());
            if(newPic) {
                if(mFile.exists())
                {
                    String base64 = postPicUtils.base64Encode(mFile);
                    PictureFile postPic = new PictureFile(base64, mFile.length(), mFile.getName());
                    dataAccess.UpdatePostWithNewPic(this, post, postPic);

                }
            }

            else {
                dataAccess.UpdatePostWithoutPic(this, post);
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

    private void showPictureTaken(File f) {
        postPic.setImageURI(Uri.fromFile(f));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showPictureTaken(mFile);
                isNewPic();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show();
                return;

            } else
                Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show();
        }
    }

    private void isNewPic() {
        newPic = true;
        btnGoBackUpdate.show();
    }

    private void noNewPic() {
        newPic = false;
        btnGoBackUpdate.hide();
        dataImage.setImageFromPostPicId(this, postPic, post.getPictureId());
    }


    @Override
    public void setOnSuccess() {
        Intent intent = new Intent();
        intent.putExtra("updatedPost", post);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setOnError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }


    @Override
    public void startLoading() {
        inputPostName.setVisibility(View.GONE);
        inputPostDesc.setVisibility(View.GONE);
        postPic.setVisibility(View.GONE);
        btnUpdatePost.hide();
        btnGoBackUpdate.hide();
        btnNewPic.setVisibility(View.GONE);

        updateProgress.setVisibility(View.VISIBLE);
        updateInfo.setVisibility(View.VISIBLE);

    }

    @Override
    public void stopLoading() {

        inputPostName.setVisibility(View.VISIBLE);
        inputPostDesc.setVisibility(View.VISIBLE);
        postPic.setVisibility(View.VISIBLE);
        btnUpdatePost.show();
        btnGoBackUpdate.show();
        btnNewPic.setVisibility(View.VISIBLE);

        updateProgress.setVisibility(View.GONE);
        updateInfo.setVisibility(View.GONE);

    }
}
