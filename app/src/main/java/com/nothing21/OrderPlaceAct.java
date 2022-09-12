package com.nothing21;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.databinding.ActivityPlaceOrderBinding;
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
    String lat = "", lon = "", address = "", product_id = "", cart_id = "", amount = "", deliveryCharge = "20";
    double total = 0.0;

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

            total = Double.parseDouble(amount) + Double.parseDouble(deliveryCharge);
            binding.tvPrice.setText("AED " + String.format("%.2f", Double.parseDouble(amount)));
            binding.tvDeliveryPrice.setText("AED " + String.format("%.2f", Double.parseDouble(deliveryCharge)));
            binding.tvTotal.setText("AED " + String.format("%.2f", total));


        }


        binding.tvCompleteadd.setOnClickListener(v -> {
            Intent i = new Intent(this, AddressAct.class);
            i.putExtra("product_id", product_id);
            i.putExtra("cart_id", cart_id);
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
        });

        binding.btnNext.setOnClickListener(v -> validation());


    }

    private void validation() {
        if (address.equals("")) {
            Toast.makeText(OrderPlaceAct.this, getString(R.string.please_select_delivery_address), Toast.LENGTH_SHORT).show();
        } else if (binding.edArea.getText().toString().equals("")) {
            binding.edArea.setError(getString(R.string.required));
            binding.edArea.setFocusable(true);
        } else if (binding.edBuildingName.getText().toString().equals("")) {
            binding.edBuildingName.setError(getString(R.string.required));
            binding.edBuildingName.setFocusable(true);
        }
        else if (binding.edApartment.getText().toString().equals("")) {
            binding.edApartment.setError(getString(R.string.required));
            binding.edApartment.setFocusable(true);
        }
        else if (binding.edContact.getText().toString().equals("")) {
            binding.edContact.setError(getString(R.string.required));
            binding.edContact.setFocusable(true);
        } else {
            if (NetworkAvailablity.checkNetworkStatus(OrderPlaceAct.this)) placeOrder();
            else
                Toast.makeText(OrderPlaceAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }


    private void placeOrder() {
        DataManager.getInstance().showProgressMessage(OrderPlaceAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(OrderPlaceAct.this).result.id + "");
        map.put("product_id", product_id);
        map.put("address", address);
        map.put("lat", lat);
        map.put("lon", lon);
        map.put("payment_type", "Card");
        map.put("amount", total + "");
        map.put("order_date", DataManager.getCurrent());
        map.put("order_time", DataManager.getCurrentTime());
        map.put("cart_id", cart_id);

        map.put("area", binding.edArea.getText().toString());
        map.put("building_name", binding.edBuildingName.getText().toString());
        map.put("apartment", binding.edApartment.getText().toString());
        map.put("contact_number",binding.ccp.getSelectedCountryCode()+ binding.edContact.getText().toString());

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
                                .putExtra("amount", total + ""));
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                lat = data.getStringExtra("lat");
                lon = data.getStringExtra("lon");
                address = data.getStringExtra("address");
                product_id = data.getStringExtra("product_id");
                cart_id = data.getStringExtra("cart_id");
                binding.tvCompleteadd.setText(address);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}
