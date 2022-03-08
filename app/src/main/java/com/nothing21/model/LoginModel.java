package com.nothing21.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

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
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("user_name")
        @Expose
        public String userName;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("dob")
        @Expose
        public String dob;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("age")
        @Expose
        public String age;
        @SerializedName("category_id")
        @Expose
        public String categoryId;
        @SerializedName("category_name")
        @Expose
        public String categoryName;
        @SerializedName("sub_cat_id")
        @Expose
        public String subCatId;
        @SerializedName("sub_cat_name")
        @Expose
        public String subCatName;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("ios_register_id")
        @Expose
        public Object iosRegisterId;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("otp_number")
        @Expose
        public String otpNumber;
        @SerializedName("step")
        @Expose
        public String step;
        @SerializedName("country_code")
        @Expose
        public String countryCode;
        @SerializedName("id_proof_image")
        @Expose
        public String idProofImage;
        @SerializedName("online_status")
        @Expose
        public String onlineStatus;
        @SerializedName("about")
        @Expose
        public String about;
        @SerializedName("emirate")
        @Expose
        public String emirate;

    }


}

