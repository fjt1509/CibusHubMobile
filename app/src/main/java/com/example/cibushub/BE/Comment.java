package com.example.cibushub.BE;

import java.util.Date;

public class Comment {
    private String Id;
    private String Comment;
    private Date Time;

    private String uId = "Anonymous";
    private String userDisplayName = "Anonymous";
    private String userDisplayUrl = "https://4.bp.blogspot.com/-H232JumEqSc/WFKY-6H-zdI/AAAAAAAAAEw/DcQaHyrxHi863t8YK4UWjYTBZ72lI0cNACLcB/s1600/profile%2Bpicture.png";

    public Comment() {
    }

    public Comment(String id, String comment, Date commentDate) {
        Id = id;
        Comment = comment;
        Time = commentDate;
    }

    public Comment(String comment, Date time) {
        Comment = comment;
        Time = time;
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

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserDisplayUrl() {
        return userDisplayUrl;
    }

    public void setUserDisplayUrl(String userDisplayUrl) {
        this.userDisplayUrl = userDisplayUrl;
    }
}
