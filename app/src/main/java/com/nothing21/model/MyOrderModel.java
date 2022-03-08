package com.nothing21.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyOrderModel {

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
        @SerializedName("order_id")
        @Expose
        public String orderId;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("payment_type")
        @Expose
        public String paymentType;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("cart_id")
        @Expose
        public String cartId;
        @SerializedName("order_date")
        @Expose
        public String orderDate;
        @SerializedName("order_time")
        @Expose
        public String orderTime;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("products_data")
        @Expose
        public List<ProductsDatum> productsData = null;

        public class ProductsDatum {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("product_name")
            @Expose
            public String productName;
            @SerializedName("price")
            @Expose
            public String price;



        }

    }


}



