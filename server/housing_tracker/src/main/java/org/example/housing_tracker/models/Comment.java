package org.example.housing_tracker.models;

import java.util.Objects;

public class Comment {

    private int commentId;
    private String commentText;
    private int appUserId;
    private int listingId;

    public Comment() {
    }

    public Comment(int commentId, String commentText, int appUserId, int listingId) {
        this.commentId = commentId;
        this.commentText = commentText;
        this.appUserId = appUserId;
        this.listingId = listingId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment that = (Comment) o;
        return commentId == that.commentId && commentText.equals(that.commentText) && listingId == that.listingId && appUserId == that.appUserId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, commentText, listingId, appUserId);
    }
}
