package com.nothing21.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*public class ColorListModel {

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
        @SerializedName("color_name")
        @Expose
        private String colorName;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        @SerializedName("color_details")
        @Expose
        private ArrayList<colorDetails> colorDetails = null;


        @SerializedName("chk")
        @Expose
        private boolean chk;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
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


        public class colorDetails {

            @SerializedName("color")
            @Expose
            private String color;
            @SerializedName("color_code")
            @Expose
            private String color_code;
            @SerializedName("color_id")
            @Expose
            private String color_id;


            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getColor_code() {
                return color_code;
            }

            public void setColor_code(String color_code) {
                this.color_code = color_code;
            }

            public String getColor_id() {
                return color_id;
            }

            public void setColor_id(String color_id) {
                this.color_id = color_id;
            }
        }


        public ArrayList<Result.colorDetails> getColorDetails() {
            return colorDetails;
        }

        public void setColorDetails(ArrayList<Result.colorDetails> colorDetails) {
            this.colorDetails = colorDetails;
        }
    }

}*/

public class ColorListModel {

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
        @SerializedName("color_name")
        @Expose
        private String colorName;
        @SerializedName("color_link")
        @Expose
        private String colorLink;

        @SerializedName("chk")
        @Expose
        private boolean chk = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public String getColorLink() {
            return colorLink;
        }

        public void setColorLink(String colorLink) {
            this.colorLink = colorLink;
        }


        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }

    }


}


