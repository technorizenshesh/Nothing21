package com.nothing21.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryListModel {
    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
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

        @SerializedName("chk")
        @Expose
        public boolean chk;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSelectedImage() {
            return selectedImage;
        }

        public void setSelectedImage(String selectedImage) {
            this.selectedImage = selectedImage;
        }

        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }
    }
}
