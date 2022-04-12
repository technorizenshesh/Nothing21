package com.nothing21.retrofit;







import com.nothing21.model.CategoryModel;
import com.nothing21.model.FavModel;
import com.nothing21.model.GetCartModel;
import com.nothing21.model.LoginModel;
import com.nothing21.model.MyOrderModel;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.model.RateModel;
import com.nothing21.model.SearchModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    @POST("get_product_by_category")
    Call<ProductModel> getAllProduct (@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_product_details")
    Call<ProductModelCopy> getProduct (@FieldMap Map<String, String> params);




    @FormUrlEncoded
    @POST("favorite")
    Call<ProductModel> addFavProduct (@FieldMap Map<String, String> params);

  //  https://www.adspot.ae/nothing21/webservice/favorite?user_id=1&product_id=2


    @FormUrlEncoded
    @POST("add_to_cart")
    Call<Map<String,String>> addToCart (@FieldMap Map<String, String> params);





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
    Call<ProductModel> getOtherProduct (@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_fav_product")
    Call<FavModel> getAllFav (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_review")
    Call<RateModel> getAllRatesss (@FieldMap Map<String, String> params);




}



