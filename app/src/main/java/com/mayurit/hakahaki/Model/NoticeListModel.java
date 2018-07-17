package com.mayurit.hakahaki.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NoticeListModel implements Serializable {


    @SerializedName("post_title")
    @Expose
    private String postTitle;

    @SerializedName("post_url")
    @Expose
    private String post_url;

    @SerializedName("post_date")
    @Expose
    private String postDate;

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}
