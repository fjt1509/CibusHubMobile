package com.example.cibushub;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cibushub.BE.Post;
import com.example.cibushub.Interfaces.IDataImage;
import com.example.cibushub.Interfaces.IMainCallback;
import com.example.cibushub.Model.DataAccessFactory;
import com.example.cibushub.Interfaces.IDataAccess;
import com.example.cibushub.Model.DataImage;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainCallback {

    public static String TAG = "CibusHub";

    private ListView listView;
    private PostAdapter adpater;
    private ProgressBar pBar;

    private IDataAccess mDataAccess;
    private IDataImage dataImage;

    private ArrayList<Post> postHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        setContentView(R.layout.activity_main);

        mDataAccess = DataAccessFactory.getInstance(this);
        dataImage = new DataImage();
        listView = findViewById(R.id.postList);
        pBar = findViewById(R.id.progressBar);
        pBar.setVisibility(View.GONE);
        mDataAccess.ReadAllPosts(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }


    private void setupListView(ArrayList<Post> postList) {

        postHolder = postList;
        adpater = new PostAdapter(this, R.layout.activity_main_cell, postList);
        listView.setAdapter(adpater);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent x = new Intent(MainActivity.this, DetailsActivity.class);
                Post post = postHolder.get(position);
                x.putExtra("post", post);
                startActivity(x);
            }
        });
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
            postPic.setImageResource(0);

            dataImage.setImageFromPostPicId(v.getContext(), postPic, post.getPictureId());

            txtPostName.setText(post.getPostName());
            txtPostDesc.setText(post.getPostDescription());
            txtPostDate.setText(post.getPostTime().subSequence(0,10));

            return v;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAddPost:
                Intent x = new Intent(MainActivity.this, AddPostActivity.class);
                startActivity(x);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        adpater.clear();
        mDataAccess.ReadAllPosts(this);
        super.onRestart();

    }
    @Override
    public void setError(String error) {
        //Handle error
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


    private void checkPermissions() {
        ArrayList<String> permissions = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.CAMERA);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.CALL_PHONE);

        if (permissions.size() > 0)
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1);
    }

}


