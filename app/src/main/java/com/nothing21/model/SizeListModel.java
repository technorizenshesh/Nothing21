package com.nothing21.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SizeListModel {

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
        @SerializedName("size_name")
        @Expose
        private String sizeName;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        @SerializedName("chk")
        @Expose
        private boolean chk;



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSizeName() {
            return sizeName;
        }

        public void setSizeName(String sizeName) {
            this.sizeName = sizeName;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }
    }


}