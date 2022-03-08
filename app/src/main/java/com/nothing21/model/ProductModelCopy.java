package com.nothing21.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


;import java.util.List;

public class ProductModelCopy {

    @SerializedName("result")
    @Expose
    public Result result;
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
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("discount")
        @Expose
        public String discount;
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
        @SerializedName("color")
        @Expose
        public String color;
        @SerializedName("size")
        @Expose
        public String size;
        @SerializedName("category_name")
        @Expose
        public String categoryName;
        @SerializedName("image_details")
        @Expose
        public List<ImageDetail> imageDetails = null;
        @SerializedName("fav_product")
        @Expose
        public String favProduct;
        @SerializedName("color_details")
        @Expose
        public List<ColorDetail> colorDetails = null;

        @SerializedName("fav_product_status")
        @Expose
        public String fav_product_status;

        public class ColorDetail {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("product_id")
            @Expose
            public String productId;
            @SerializedName("color")
            @Expose
            public String color;
            @SerializedName("size")
            @Expose
            public String size;
            @SerializedName("date_time")
            @Expose
            public String dateTime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

        }

        public class ImageDetail {

            @SerializedName("image")
            @Expose
            public String image;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

        }


    }

}


