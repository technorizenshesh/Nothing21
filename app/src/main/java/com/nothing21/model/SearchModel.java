package com.nothing21.model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SearchModel {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("category_id")
        @Expose
        public String categoryId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
       /* @SerializedName("price")
        @Expose
        public String price;*/
    /*    @SerializedName("discount")
        @Expose
        public String discount;*/
        @SerializedName("image1")
        @Expose
        public String image1;
        @SerializedName("image2")
        @Expose
        public String image2;
        @SerializedName("image3")
        @Expose
        public String image3;
        @SerializedName("image4")
        @Expose
        public String image4;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("category_name")
        @Expose
        public String categoryName;
        @SerializedName("image")
        @Expose
        public String image;

        @SerializedName("price_calculated")
        @Expose
        public String priceCalculated;

        @SerializedName("product_id")
        @Expose
        public String productId;








    }
}
