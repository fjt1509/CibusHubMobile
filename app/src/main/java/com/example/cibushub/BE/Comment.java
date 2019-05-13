package com.example.cibushub.BE;

import java.util.Date;

public class Comment {
    private String Id;
    private String Comment;
    private Date Time;

    public Comment() {
    }

    public Comment(String id, String comment, Date commentDate) {
        Id = id;
        Comment = comment;
        Time = commentDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
