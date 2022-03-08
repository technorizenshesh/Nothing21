
        package com.nothing21.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {

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
        @SerializedName("category_name")
        @Expose
        public String categoryName;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("selected_image")
        @Expose
        public String selectedImage;



    }
}


