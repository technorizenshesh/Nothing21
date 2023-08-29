package com.nothing21;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.databinding.ActivityPlaceOrderBinding;
import com.nothing21.model.AddressModel;
import com.nothing21.model.LoginModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Constant;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPlaceAct extends AppCompatActivity {
    public String TAG = "OrderPlaceAct";
    int LAUNCH_SECOND_ACTIVITY = 1;
    ActivityPlaceOrderBinding binding;
    Nothing21Interface apiInterface;
    String lat = "", lon = "",percent="0",couponId="", address = "", product_id = "", cart_id = "", amount = "", deliveryCharge = "20",addressId="";
    double total = 0.0,percentAmount=0.0;
    String chkAddress="0";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_order);
        initViews();

    }

    private void initViews() {

        if (getIntent() != null) {
            product_id = getIntent().getStringExtra("product_id");
            cart_id = getIntent().getStringExtra("cart_id");
            amount = getIntent().getStringExtra("amount");
            Log.e("Product_id", product_id);
            Log.e("cart_id", cart_id);


         //   binding.ccp.setCountryForPhoneCode(971);

            if(NetworkAvailablity.checkNetworkStatus(OrderPlaceAct.this)) getAddressssss();
            else Toast.makeText(OrderPlaceAct.this,getString(R.string.network_failure),Toast.LENGTH_SHORT).show();

            if(NetworkAvailablity.checkNetworkStatus(OrderPlaceAct.this)) deliveryChargeApi();
            else Toast.makeText(OrderPlaceAct.this,getString(R.string.network_failure),Toast.LENGTH_SHORT).show();
            
            
        }


        binding.btnChangeAdd.setOnClickListener(v -> {
            Intent i = new Intent(this, AddressAct.class);
            i.putExtra("product_id", product_id);
            i.putExtra("cart_id", cart_id);
            i.putExtra("address_id",addressId);
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
        });

        binding.btnNext.setOnClickListener(v -> {
                    if(chkAddress.equals("0")) validation();
                     else {
                        if (NetworkAvailablity.checkNetworkStatus(OrderPlaceAct.this))

                            placeOrder();
                        else
                            Toast.makeText(OrderPlaceAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    }

                });


        binding.btnApply.setOnClickListener(v -> {
            if(binding.btnApply.getText().toString().equals("Apply")){
                if(binding.edPromoCode.getText().toString().equals("")) {
                    Toast.makeText(OrderPlaceAct.this, getString(R.string.please_enter_promo_code), Toast.LENGTH_SHORT).show();

                }
                else {
                    if (NetworkAvailablity.checkNetworkStatus(OrderPlaceAct.this))
                        ApplyCode(binding.edPromoCode.getText().toString());
                    else
                        Toast.makeText(OrderPlaceAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                }

            }
            else {
                total = total + percentAmount;
                binding.tvTotal.setText("AED " + String.format("%.2f", total));
                binding.edPromoCode.setText("");
                binding.edPromoCode.setFocusable(true);
               // binding.edPromoCode.setClickable(true);
                binding.layDiscount.setVisibility(View.GONE);
                binding.btnApply.setText(getString(R.string.apply));
                percent = "0";
                percentAmount =0.0;
            }
        });


    }

    private void deliveryChargeApi() {
           // DataManager.getInstance().showProgressMessage(OrderPlaceAct.this, getString(R.string.please_wait));
            Map<String, String> map = new HashMap<>();
            map.put("amount",amount);
            Log.e(TAG, "get DeliveryCharge Request " + map);
            Call<ResponseBody> loginCall = apiInterface.getDelivery(map);
            loginCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    DataManager.getInstance().hideProgressMessage();
                    try {
                        String stringResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "get DeliveryCharge Response :" + stringResponse);
                        deliveryCharge = jsonObject.getString("delivery_carges");
                        total = Double.parseDouble(amount) + Double.parseDouble(deliveryCharge);
                        binding.tvPrice.setText("AED " + String.format("%.2f", Double.parseDouble(amount)));
                        binding.tvDeliveryPrice.setText("AED " + String.format("%.2f", Double.parseDouble(deliveryCharge)));
                        binding.tvTotal.setText("AED " + String.format("%.2f", total));


                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Log.e("JSONException", "JSONException = " + e.getMessage());

                    }



                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    DataManager.getInstance().hideProgressMessage();
                }
            });



    }

    private void validation() {
        if (binding.edCity.getText().toString().equals("")) {
            binding.edCity.setError(getString(R.string.required));
            binding.edCity.setFocusable(true);
        }
         else if (binding.edArea.getText().toString().equals("")) {
            binding.edArea.setError(getString(R.string.required));
            binding.edArea.setFocusable(true);
        }
          else if (binding.edLandmark.getText().toString().equals("")) {
              binding.edLandmark.setError(getString(R.string.required));
              binding.edLandmark.setFocusable(true);
          }
          else if (binding.edBuildingName.getText().toString().equals("")) {
            binding.edBuildingName.setError(getString(R.string.required));
            binding.edBuildingName.setFocusable(true);
        }
        else if (binding.edApartment.getText().toString().equals("")) {
                  binding.edApartment.setError(getString(R.string.required));
                  binding.edApartment.setFocusable(true);
        }
          else if (binding.edZipcode.getText().toString().equals("")) {
              binding.edZipcode.setError(getString(R.string.required));
              binding.edZipcode.setFocusable(true);
          }
          else if (binding.edContact.getText().toString().equals("")) {
            binding.edContact.setError(getString(R.string.required));
            binding.edContact.setFocusable(true);
        } else {
            if (NetworkAvailablity.checkNetworkStatus(OrderPlaceAct.this))

                addAddresssssss();
            else
                Toast.makeText(OrderPlaceAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }


   private void placeOrder() {
        DataManager.getInstance().showProgressMessage(OrderPlaceAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(OrderPlaceAct.this).result.id + "");
        map.put("product_id", product_id);
        map.put("payment_type", "Card");
        map.put("amount", total + "");
        map.put("order_date", DataManager.getCurrent());
        map.put("order_time", DataManager.getCurrentTime());
        map.put("cart_id", cart_id);
        map.put("address_id", addressId);
        map.put("promo_code", binding.edPromoCode.getText().toString());
        map.put("category_order_id", percent);
        map.put("promo_code_id",couponId);


       // map.put("address", address);
      //  map.put("lat", lat);
       // map.put("lon", lon);


      //  map.put("area", binding.edArea.getText().toString());
     //   map.put("building_name", binding.edBuildingName.getText().toString());
     //   map.put("apartment", binding.edApartment.getText().toString());
    //    map.put("contact_number",binding.ccp.getSelectedCountryCode()+ binding.edContact.getText().toString());

        Log.e(TAG, "order Place Request " + map);
        Call<ResponseBody> loginCall = apiInterface.orderBooking(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

               /* try {
                    Map<String, String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "order Place Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        startActivity(new Intent(OrderPlaceAct.this, PaymentAct.class)
                                .putExtra("request_id", data.get("request_id"))
                                .putExtra("amount", total + ""));

                    } else if (data.get("status").equals("0"))
                        Toast.makeText(OrderPlaceAct.this, data.get("message"), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "order Place Response :" + stringResponse);
                    if (jsonObject.getString("status").equals("1")) {
                        startActivity(new Intent(OrderPlaceAct.this, PaymentAct.class)
                                .putExtra("request_id", jsonObject.getString("request_id"))
                                .putExtra("amount", total + "")
                                .putExtra("promo_code",binding.edPromoCode.getText().toString())
                                .putExtra("category_order_id",percent)
                                .putExtra("couponId",couponId));
                    } else  if (jsonObject.getString("status").equals("0")){
                        Toast.makeText(OrderPlaceAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    Log.e("JSONException", "JSONException = " + e.getMessage());

                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });


    }

    private void ApplyCode(String code) {
        DataManager.getInstance().showProgressMessage(OrderPlaceAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(OrderPlaceAct.this).result.id + "");
        map.put("promo_code",code);
        Log.e(TAG, "Apply PromoCode Request " + map);
        Call<ResponseBody> loginCall = apiInterface.couponCode(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Apply PromoCode Response :" + stringResponse);
                    if (jsonObject.getString("status").equals("1")) {
                         JSONObject result = jsonObject.getJSONObject("result");
                           percent = result.getString("category_order_id");
                           couponId = result.getString("id");
                           percentAmount = (total * Integer.parseInt(percent))/100;
                           total = total - percentAmount;
                           binding.layDiscount.setVisibility(View.VISIBLE);
                           binding.tvDiscountAmt.setText("AED " + String.format("%.2f",percentAmount));
                           binding.tvTotal.setText("AED " + String.format("%.2f", total));
                        //  binding.edPromoCode.setFocusable(false);
                          binding.btnApply.setText(getString(R.string.remove));
                          Toast.makeText(OrderPlaceAct.this,getString(R.string.promo_code_applied_successfully), Toast.LENGTH_SHORT).show();

                    } else  if (jsonObject.getString("status").equals("0")){
                        binding.layDiscount.setVisibility(View.GONE);
                        Toast.makeText(OrderPlaceAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    Log.e("JSONException", "JSONException = " + e.getMessage());

                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
              //  lat = data.getStringExtra("lat");
              //  lon = data.getStringExtra("lon");
                addressId = data.getStringExtra("address_id");
                chkAddress = data.getStringExtra("chkAddress");
                product_id = data.getStringExtra("product_id");
                cart_id = data.getStringExtra("cart_id");
                binding.tvCompleteadd.setText(address);
                binding.edCity.setText(data.getStringExtra("city"));
               // binding.edArea.setText(data.getStringExtra("area"));
                binding.edArea.setText(data.getStringExtra("city"));
                binding.edLandmark.setText(data.getStringExtra("landmark"));
                binding.edBuildingName.setText(data.getStringExtra("building"));
                binding.edApartment.setText(data.getStringExtra("flat"));
                binding.edContact.setText(data.getStringExtra("mobile"));
                binding.edZipcode.setText(data.getStringExtra("zipCode"));
                binding.edCountryCode.setText("+971");

                binding.ccp.setCountryForPhoneCode(971);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }


    private void addAddresssssss() {
        DataManager.getInstance().showProgressMessage(OrderPlaceAct.this, getString(R.string.please_wait));
        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("city", binding.edCity.getText().toString());
        paramHash.put("area", binding.edArea.getText().toString());
        paramHash.put("landmark", binding.edLandmark.getText().toString());
        paramHash.put("building", binding.edBuildingName.getText().toString());
        paramHash.put("flat", binding.edApartment.getText().toString());
        paramHash.put("zip_code", binding.edZipcode.getText().toString());
       // paramHash.put("country_code", binding.ccp.getSelectedCountryCode()+"");
        paramHash.put("country_code",binding.edCountryCode.getText().toString() );
        paramHash.put("mobile", binding.edCity.getText().toString());
        paramHash.put("user_id", DataManager.getInstance().getUserData(OrderPlaceAct.this).result.id);
        Log.e("add address===", "paramHash = " + paramHash);
        Call<ResponseBody> call = apiInterface.addAddress(paramHash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    String stringResponse = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "add address Response = " + stringResponse);
                        if (jsonObject.getString("status").equals("1")) {
                            JSONObject object = jsonObject.getJSONObject("result");
                            //  Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = " + stringResponse);
                            addressId = object.getString("id");
                            if (NetworkAvailablity.checkNetworkStatus(OrderPlaceAct.this))

                                placeOrder();
                            else
                                Toast.makeText(OrderPlaceAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(OrderPlaceAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONException", "JSONException = " + e.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }

        });
    }




    private void getAddressssss() {
        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("user_id", DataManager.getInstance().getUserData(OrderPlaceAct.this).result.id);
        Log.e("get address===", "paramHash = " + paramHash);
        Call<ResponseBody> call = apiInterface.getAddress(paramHash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    String stringResponse = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "get address Response = " + stringResponse);
                        if (jsonObject.getString("status").equals("1")) {
                            chkAddress = "1";
                            //  Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = " + stringResponse);
                            AddressModel model = new Gson().fromJson(stringResponse,AddressModel.class);
                              addressId = model.getResult().get(0).getId();
                              binding.edCity.setText(model.getResult().get(0).getCity());
                               binding.edArea.setText(model.getResult().get(0).getArea());
                               binding.edLandmark.setText(model.getResult().get(0).getNearestLandmark());
                              binding.edBuildingName.setText(model.getResult().get(0).getBuilding_name());
                               binding.edApartment.setText(model.getResult().get(0).getFlate_no());
                               binding.edContact.setText(model.getResult().get(0).getMobile());
                            binding.edZipcode.setText(model.getResult().get(0).getZipCode());
                           // binding.edCountryCode.setText("+971");

                          //  binding.ccp.setCountryForPhoneCode(971);

                        } else {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONException", "JSONException = " + e.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }

        });
    }

}
