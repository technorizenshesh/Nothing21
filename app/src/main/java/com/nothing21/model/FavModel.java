package com.nothing21.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class FavModel {

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
        @SerializedName("accept_status")
        @Expose
        public String acceptStatus;
        @SerializedName("quantity_in_stock")
        @Expose
        public String quantityInStock;
        @SerializedName("start_date")
        @Expose
        public String startDate;
        @SerializedName("end_date")
        @Expose
        public String endDate;
        @SerializedName("admin_id")
        @Expose
        public String adminId;
        @SerializedName("admin_type")
        @Expose
        public String adminType;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("product_type")
        @Expose
        public String productType;
        @SerializedName("product_size")
        @Expose
        public String productSize;
        @SerializedName("product_calculated_price")
        @Expose
        public String productCalculatedPrice;
        @SerializedName("sub_category_id")
        @Expose
        public String subCategoryId;
        @SerializedName("product_order_id")
        @Expose
        public String productOrderId;
        @SerializedName("rating_by_admin")
        @Expose
        public String ratingByAdmin;


        @SerializedName("color_details")
        @Expose
        public ArrayList<ColorDetail> colorDetails = null;

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
            @SerializedName("date_time")
            @Expose
            public String dateTime;
            @SerializedName("image")
            @Expose
            public String image;
            @SerializedName("color_code")
            @Expose
            public String colorCode;
            @SerializedName("color_variation")
            @Expose
            public ArrayList<ColorVariation> colorVariation = null;




            public class ColorVariation {

                @SerializedName("cart_quantity")
                @Expose
                public Integer cartQuantity;
                @SerializedName("remaining_quantity")
                @Expose
                public Integer remainingQuantity;
                @SerializedName("size")
                @Expose
                public String size;
                @SerializedName("quantity")
                @Expose
                public String quantity;
                @SerializedName("price")
                @Expose
                public String price;
                @SerializedName("price_discount")
                @Expose
                public String priceDiscount;
                @SerializedName("price_calculated")
                @Expose
                public String priceCalculated;




            }


        }



    }

}


