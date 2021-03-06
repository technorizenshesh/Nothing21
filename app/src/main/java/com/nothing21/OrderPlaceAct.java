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

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPlaceAct extends AppCompatActivity {
    public String TAG = "OrderPlaceAct";
    int LAUNCH_SECOND_ACTIVITY = 1;
    ActivityPlaceOrderBinding binding;
    Nothing21Interface apiInterface;
    String lat="",lon="",address="",product_id="",cart_id="",amount = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_place_order);
        initViews();

    }

    private void initViews() {

        if(getIntent()!=null){
            product_id = getIntent().getStringExtra("product_id");
            cart_id = getIntent().getStringExtra("cart_id");
            amount = getIntent().getStringExtra("amount");
            Log.e("Product_id",product_id);
            Log.e("cart_id",cart_id);
        }


        binding.tvCompleteadd.setOnClickListener(v -> {
            Intent i = new Intent(this, AddressAct.class);
            i.putExtra("product_id",product_id);
            i.putExtra("cart_id",cart_id);
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
        });

        binding.btnNext.setOnClickListener(v -> {
            if(!address.equals("")) {
                if(NetworkAvailablity.checkNetworkStatus(OrderPlaceAct.this)) placeOrder();
                else Toast.makeText(OrderPlaceAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(OrderPlaceAct.this, getString(R.string.please_select_delivery_address), Toast.LENGTH_SHORT).show();
        });

    }

    private void placeOrder() {
        DataManager.getInstance().showProgressMessage(OrderPlaceAct.this, getString(R.string.please_wait));
            Map<String, String> map = new HashMap<>();
            map.put("user_id", DataManager.getInstance().getUserData(OrderPlaceAct.this).result.id+"");
            map.put("product_id",product_id);
            map.put("address",address);
            map.put("lat", lat);
            map.put("lon",lon);
            map.put("payment_type", "Cash");
            map.put("amount",amount);
            map.put("order_date",DataManager.getCurrent());
            map.put("order_time",DataManager.getCurrentTime());
            map.put("cart_id",cart_id);
            Log.e(TAG, "order Place Request " + map);
            Call<Map<String,String>> loginCall = apiInterface.orderBooking(map);
            loginCall.enqueue(new Callback<Map<String,String>>() {
                @Override
                public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                    DataManager.getInstance().hideProgressMessage();

                    try {
                        Map<String,String> data = response.body();
                        String responseString = new Gson().toJson(response.body());
                        Log.e(TAG, "order Place Response :" + responseString);
                        if (data.get("status").equals("1")) {
                            startActivity(new Intent(OrderPlaceAct.this, PaymentAct.class)
                                    .putExtra("request_id",data.get("request_id"))
                                    .putExtra("amount",amount+""));

                        } else if (data.get("status").equals("0"))
                            Toast.makeText(OrderPlaceAct.this, data.get("message"), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Map<String,String>> call, Throwable t) {
                    call.cancel();
                    DataManager.getInstance().hideProgressMessage();
                }
            });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                 lat =data.getStringExtra("lat");
                 lon =data.getStringExtra("lon");
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
