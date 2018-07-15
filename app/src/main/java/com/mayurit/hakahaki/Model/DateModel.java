package com.mayurit.hakahaki.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DateModel implements Serializable {


    @SerializedName("date")
    @Expose
    private String postDate;

    @SerializedName("time")
    @Expose
    private String postTime;


    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }


    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postDate;
    }


}
