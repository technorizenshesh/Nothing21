package com.nothing21.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class SubCatFilterModel {

    @SerializedName("result")
    @Expose
    private ArrayList<Result> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("sub_cat_name")
        @Expose
        private String subCatName;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("hr_rate")
        @Expose
        private String hrRate;
        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("chk")
        @Expose
        public boolean chk;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getSubCatName() {
            return subCatName;
        }

        public void setSubCatName(String subCatName) {
            this.subCatName = subCatName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getHrRate() {
            return hrRate;
        }

        public void setHrRate(String hrRate) {
            this.hrRate = hrRate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }
    }


}