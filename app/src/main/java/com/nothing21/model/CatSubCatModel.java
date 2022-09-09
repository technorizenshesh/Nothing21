package com.nothing21.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatSubCatModel {

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
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("accept_status")
        @Expose
        private String acceptStatus;
        @SerializedName("admin_id")
        @Expose
        private String adminId;
        @SerializedName("admin_type")
        @Expose
        private String adminType;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("category_order_id")
        @Expose
        private String categoryOrderId;
        @SerializedName("subcategory_info")
        @Expose
        private ArrayList<SubcategoryInfo> subcategoryInfo = null;

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

        public String getAcceptStatus() {
            return acceptStatus;
        }

        public void setAcceptStatus(String acceptStatus) {
            this.acceptStatus = acceptStatus;
        }

        public String getAdminId() {
            return adminId;
        }

        public void setAdminId(String adminId) {
            this.adminId = adminId;
        }

        public String getAdminType() {
            return adminType;
        }

        public void setAdminType(String adminType) {
            this.adminType = adminType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCategoryOrderId() {
            return categoryOrderId;
        }

        public void setCategoryOrderId(String categoryOrderId) {
            this.categoryOrderId = categoryOrderId;
        }

        public ArrayList<SubcategoryInfo> getSubcategoryInfo() {
            return subcategoryInfo;
        }

        public void setSubcategoryInfo(ArrayList<SubcategoryInfo> subcategoryInfo) {
            this.subcategoryInfo = subcategoryInfo;
        }


        public class SubcategoryInfo {

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
            @SerializedName("category_name")
            @Expose
            private String categoryName;



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

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }
        }

    }


}



