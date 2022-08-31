package com.nothing21.retrofit;







import com.nothing21.model.CategoryListModel;
import com.nothing21.model.CategoryModel;
import com.nothing21.model.ColorListModel;
import com.nothing21.model.FavModel;
import com.nothing21.model.GetCartModel;
import com.nothing21.model.LoginModel;
import com.nothing21.model.MyOrderModel;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.model.ProductModelCopyNew;
import com.nothing21.model.ProductNewModel;
import com.nothing21.model.RateModel;
import com.nothing21.model.SearchModel;
import com.nothing21.model.SizeListModel;
import com.nothing21.model.SubCatFilterModel;
import com.nothing21.model.SubCatModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Nothing21Interface {


    @FormUrlEncoded
    @POST("signup")
    Call<Map<String,String>> signupUser(@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> userLogin (@FieldMap Map<String, String> params);






    @GET("get_category")
    Call<CategoryModel> getAllCategory();


    @FormUrlEncoded
    @POST("get_sub_category")
    Call<SubCatModel> getAllSubCategory (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_product_by_category")
    Call<ProductModel> getAllProduct (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_product_by_category_new")
    Call<ProductNewModel> getAllProductNew (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_product_details")
    Call<ProductModelCopyNew> getProduct (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_product_details")
    Call<ProductModelCopyNew> getProductNew (@FieldMap Map<String, String> params);




    @FormUrlEncoded
    @POST("favorite")
    Call<ProductModel> addFavProduct (@FieldMap Map<String, String> params);

  //  https://www.adspot.ae/nothing21/webservice/favorite?user_id=1&product_id=2


    @FormUrlEncoded
    @POST("add_to_cart")
    Call<Map<String,String>> addToCart (@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("update_to_cart")
    Call<Map<String,String>> updateCart (@FieldMap Map<String, String> params);




    @Multipart
    @POST("add_to_cart")
    Call<Map<String,String>> addToCart11(
            @Part("user_id") RequestBody user_id,
            @Part("product_id") RequestBody product_id,
            @Part("quantity") RequestBody quantity,
            @Part("color") RequestBody color,
            @Part("size") RequestBody size,
            @Part MultipartBody.Part file);


  //  https://www.adspot.ae/nothing21/webservice/add_to_cart?user_id=1&product_id=2&quantity=2

    @FormUrlEncoded
    @POST("get_cart")
    Call<GetCartModel> getCartList (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("delete_cart")
    Call<Map<String,String>> deleteCart (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("logout")
    Call<Map<String,String>> logout (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("place_order")
    Call<Map<String,String>> orderBooking (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("payment")
    Call<Map<String,String>> payment (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("favorite")
    Call<Map<String,String>> addFav (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_order_history")
    Call<MyOrderModel> getMyOrders (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_category_search")
    Call<SearchModel> searchProduct (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_product_by_product_id")
    Call<ProductNewModel> getOtherProduct (@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_fav_product")
    Call<FavModel> getAllFav (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_review")
    Call<RateModel> getAllRatesss (@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_recent_product_by_category")
    Call<ProductNewModel> getRecentView (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("delete_wish_list")
    Call<Map<String,String>> deleteWishList (@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_order_history")
    Call<ResponseBody> getOrderStatus(@FieldMap Map<String, String> params);


    @GET("get_color_list")
    Call<ColorListModel> getColors();


    @GET("get_size_list")
    Call<SizeListModel> getSize();

    @GET("get_category")
    Call<CategoryListModel> getAllCategory11();

    @GET("get_category_subcategory")
    Call<SubCatFilterModel> getAllSubCategory();


    @FormUrlEncoded
    @POST("filter_product")
    Call<ProductModel> getApplyFilter (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("filter_product")
    Call<ProductNewModel> getApplyFilterNew (@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("filter_product_subcategory")
    Call<ProductNewModel> getApplyFilterBySubCat (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_profile")
    Call<LoginModel> userProfile (@FieldMap Map<String, String> params);


    @Multipart
    @POST("update_profile")
    Call<LoginModel> profileUpdate(
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("country_code") RequestBody country_code,
            @Part("emirate") RequestBody dob,
            @Part("address") RequestBody address,
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part file);



    @FormUrlEncoded
    @POST("add_rating_review")
    Call<ResponseBody> rateReview(@FieldMap Map<String, String> params);




    @FormUrlEncoded
    @POST("get_order_delivered")
    Call<ResponseBody> orderHistory(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("place_order_return")
    Call<ResponseBody> returnOrder(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("support")
    Call<ResponseBody> supportApi(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_order_return")
    Call<ResponseBody> returnHistory(@FieldMap Map<String, String> params);

    @GET("contact_us")
    Call<ResponseBody> getContact();


}



