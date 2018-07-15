package com.mayurit.hakahaki.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoModel implements Serializable {

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("post_excerpt")
    @Expose
    private String postExcerpt;
    @SerializedName("post_content")
    @Expose
    private String post_content;

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    @SerializedName("youtube_id")
    @Expose
    private String youtube_id;
    @SerializedName("image_id")
    @Expose
    private String imageId;

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

    public String getPostExcerpt() {
        return postExcerpt;
    }

    public void setPostExcerpt(String postExcerpt) {
        this.postExcerpt = postExcerpt;
    }

    public String getPostContent() {
        return post_content;
    }

    public void setPostContent(String post_content) {
        this.post_content = post_content;
    }

    public String getImageId() {
        if(imageId== null){
            imageId="false";
        }
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

}