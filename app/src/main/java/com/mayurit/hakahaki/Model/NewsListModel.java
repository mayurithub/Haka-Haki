package com.mayurit.hakahaki.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewsListModel implements Serializable {

    @SerializedName("ID")
    @Expose
    private String iD;

    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("post_excerpt")
    @Expose
    private String postExcerpt;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("like_count")
    @Expose
    private Object likeCount;
    @SerializedName("post_content")
    @Expose
    private String postContent;
    @SerializedName("post_url")
    @Expose
    private String post_url;
    @SerializedName("guest_author")
    @Expose
    private String guest_author;

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

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostExcerpt() {
        return postExcerpt;
    }

    public void setPostExcerpt(String postExcerpt) {
        this.postExcerpt = postExcerpt;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public String getGuest_author() {
        if(guest_author==null){
            guest_author="";
        }
        return guest_author;
    }

    public void setGuest_author(String guest_author) {
        this.guest_author = guest_author;
    }

    public String getImageId() {
        if(imageId==null){
            imageId="";
        }
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Object getLikeCount() {
        if(likeCount==null){
            likeCount="0";
        }
        return likeCount;
    }

    public void setLikeCount(Object likeCount) {
        this.likeCount = likeCount;
    }

}
