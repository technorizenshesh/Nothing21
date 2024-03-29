package com.nothing21.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ProductModel {

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
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("discount")
        @Expose
        public String discount;
        @SerializedName("discounted_price")
        @Expose
        public String discountedPrice;

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
        @SerializedName("fav_product_status")
        @Expose
        public String fav_product_status;
        @SerializedName("image_details")
        @Expose
        public ArrayList<ImageDetail> imageDetails = null;
        @SerializedName("color_details")
        @Expose
        public ArrayList<ColorDetail> colorDetails = null;

        @SerializedName("touch_check")
        @Expose
        public boolean touchCheck = false;


        @SerializedName("brand")
        @Expose
        public String brand1;



        public boolean isTouchCheck() {
            return touchCheck;
        }

        public void setTouchCheck(boolean touchCheck) {
            this.touchCheck = touchCheck;
        }

        public class ColorDetail {

            @SerializedName("color_id")
            @Expose
            public String colorId;
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

            @SerializedName("image")
            @Expose
            public String image;


            @SerializedName("color_code")
            @Expose
            public String colorCode;


            @SerializedName("remaining_quantity")
            @Expose
            public String remainingQuantity;

            @SerializedName("chk_color")
            @Expose
            public boolean chkColor = false;

            public boolean isChkColor() {
                return chkColor;
            }

            public void setChkColor(boolean chkColor) {
                this.chkColor = chkColor;
            }
        }

        public class ImageDetail {

            @SerializedName("image")
            @Expose
            public String image;


        }

    }

}

