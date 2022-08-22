package com.nothing21.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class OrderStatusModel {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

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
        private String id;
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("cart_id")
        @Expose
        private String cartId;
        @SerializedName("order_date")
        @Expose
        private String orderDate;
        @SerializedName("order_time")
        @Expose
        private String orderTime;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lon")
        @Expose
        private String lon;
        @SerializedName("fav_product")
        @Expose
        private String favProduct;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("cart_data")
        @Expose
        private List<CartDatum> cartData = null;
        @SerializedName("products_data")
        @Expose
        private List<ProductsDatum> productsData = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getFavProduct() {
            return favProduct;
        }

        public void setFavProduct(String favProduct) {
            this.favProduct = favProduct;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<CartDatum> getCartData() {
            return cartData;
        }

        public void setCartData(List<CartDatum> cartData) {
            this.cartData = cartData;
        }

        public List<ProductsDatum> getProductsData() {
            return productsData;
        }

        public void setProductsData(List<ProductsDatum> productsData) {
            this.productsData = productsData;
        }


        public class CartDatum {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("product_id")
            @Expose
            private String productId;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("price")
            @Expose
            private String price;
            @SerializedName("color")
            @Expose
            private String color;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("size")
            @Expose
            private String size;
            @SerializedName("quantity")
            @Expose
            private String quantity;
            @SerializedName("date_time")
            @Expose
            private String dateTime;

            @SerializedName("product_name")
            @Expose
            private String productName;

            @SerializedName("order_status")
            @Expose
            private String orderStatus;

            @SerializedName("order_id")
            @Expose
            private String orderIdss="";



            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }


            public String getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(String orderStatus) {
                this.orderStatus = orderStatus;
            }

            public String getOrderIdss() {
                return orderIdss;
            }

            public void setOrderIdss(String orderIdss) {
                this.orderIdss = orderIdss;
            }
        }







        public class ProductsDatum {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("product_name")
            @Expose
            private String productName;

            @SerializedName("price")
            @Expose
            private String price;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

        }


    }

}

