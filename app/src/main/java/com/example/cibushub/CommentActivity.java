package com.example.cibushub;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cibushub.BE.Comment;
import com.example.cibushub.Interfaces.ICommentCallBack;
import com.example.cibushub.Interfaces.IDataAccess;
import com.example.cibushub.Model.DataAccessFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity implements ICommentCallBack {


    String postId;

    EditText inputComment;
    IDataAccess dataAccess;
    ProgressBar commentListProgress;
    ProgressBar commentAddProgress;
    FloatingActionButton btnAddComment;

    private CommentAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postId = (String) getIntent().getSerializableExtra("postId");

        inputComment = findViewById(R.id.inputComment);
        btnAddComment = findViewById(R.id.btnAddComment);
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        commentListProgress = findViewById(R.id.commentListProgess);
        commentListProgress.setVisibility(View.GONE);
        commentAddProgress = findViewById(R.id.commentAddProgress);
        commentAddProgress.setVisibility(View.INVISIBLE);

        listView = findViewById(R.id.commentList);


        dataAccess = DataAccessFactory.getInstance(this);
        dataAccess.GetAllComments(this, postId);




    }

    private void addComment() {
        if(!inputComment.getText().toString().isEmpty()) {
            dataAccess.AddComment(this, postId, inputComment.getText().toString());
        }
    }

    private void setupListView(ArrayList<Comment> commentList) {
        adapter = new CommentAdapter(this, R.layout.activity_comment_cell, commentList);
        listView.setAdapter(adapter);
        Log.d(MainActivity.TAG, "WERE HERE!!!!!!!!");
        Log.d(MainActivity.TAG, commentList.toString());
    }

    private class CommentAdapter extends ArrayAdapter {

        ArrayList<Comment> comments = new ArrayList<>();

        public CommentAdapter(Context context, int resource, List<Comment> commentList) {
            super(context, resource, commentList);
            this.comments.addAll(commentList);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {

            final Comment comment = comments.get(position);

            if (v == null) {
                LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.activity_comment_cell, null);
                Log.d("LIST", "Position: " + position + " View created");
            }
            else {
                Log.d("LIST", "Position: " + position + " View Reused");
            }

            TextView txtCommentDate = v.findViewById(R.id.txtCommentDate);
            TextView txtComment = v.findViewById(R.id.txtComment);


            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(comment.getTime());

            txtCommentDate.setText(date);
            txtComment.setText(comment.getComment());

            return v;

        }



        }






    @Override
    public void setResult(ArrayList<Comment> commentList) {
        setupListView(commentList);
    }

    @Override
    public void startReadLoad() {
        commentListProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopReadLoad() {
        commentListProgress.setVisibility(View.GONE);
    }

    @Override
    public void startAddLoad() {
        inputComment.getText().clear();
        inputComment.clearFocus();
        btnAddComment.hide();
        commentAddProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopAddLoad() {
        btnAddComment.show();
        commentAddProgress.setVisibility(View.GONE);
    }


    @Override
    public void setOnSuccess() {
        dataAccess.GetAllComments(this, postId);
    }

    @Override
    public void setError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }


}
