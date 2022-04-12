package com.nothing21.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateModel {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("avg_rating")
    @Expose
    public String avgRating;
    @SerializedName("rating1")
    @Expose
    public Integer rating1;
    @SerializedName("rating2")
    @Expose
    public Integer rating2;
    @SerializedName("rating3")
    @Expose
    public Integer rating3;
    @SerializedName("rating4")
    @Expose
    public Integer rating4;
    @SerializedName("rating5")
    @Expose
    public Integer rating5;

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("rating")
        @Expose
        public String rating;
        @SerializedName("reviews")
        @Expose
        public String reviews;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("user_name")
        @Expose
        public String userName;



    }

}

