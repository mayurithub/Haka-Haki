package com.mayurit.hakahaki.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProjectModel implements Serializable{

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("like_count")
    @Expose
    private Object likeCount;
    @SerializedName("post_excerpt")
    @Expose
    private String post_excerpt;
    @SerializedName("post_content")
    @Expose
    private String post_content;

    public String getPost_excerpt() {
        return post_excerpt;
    }

    public void setPost_excerpt(String post_excerpt) {
        this.post_excerpt = post_excerpt;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Object getLikeCount() {
        if(likeCount==null){
            likeCount="";
        }
        return likeCount;
    }

    public void setLikeCount(Object likeCount) {
        this.likeCount = likeCount;
    }

}