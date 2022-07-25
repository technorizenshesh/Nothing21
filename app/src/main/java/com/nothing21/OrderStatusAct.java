package com.nothing21;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.adapter.CartAdapter;
import com.nothing21.adapter.OrderStatusAdapter;
import com.nothing21.databinding.ActivityOrderStatusBinding;
import com.nothing21.databinding.FragmentCartBinding;
import com.nothing21.fragment.CartFragment;
import com.nothing21.fragment.GiveReviewBottomSheet;
import com.nothing21.fragment.RateBottomsheet;
import com.nothing21.listener.InfoListener;
import com.nothing21.model.GetCartModel;
import com.nothing21.model.OrderStatusModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusAct extends AppCompatActivity implements InfoListener {
    public String TAG = "OrderStatusAct";
    ActivityOrderStatusBinding binding;
    ArrayList<OrderStatusModel.Result.CartDatum> arrayList;
    OrderStatusAdapter adapter;
    Nothing21Interface apiInterface;


    BroadcastReceiver OrderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                if(intent.getStringExtra("status").equals("Delivered")){
                    callRate(intent.getStringExtra("productId"));
                }
                else {
                    if(NetworkAvailablity.checkNetworkStatus(OrderStatusAct.this)) getAllTransactionsApi();
                    else Toast.makeText(OrderStatusAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    private void callRate(String productId) {
        new GiveReviewBottomSheet(productId).callBack(this::info).show(getSupportFragmentManager(), "");
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_status);
        initViews();
    }

    private void initViews() {
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        arrayList = new ArrayList<>();

        adapter = new OrderStatusAdapter(OrderStatusAct.this, arrayList);
        binding.rvCart.setAdapter(adapter);

        if(NetworkAvailablity.checkNetworkStatus(OrderStatusAct.this)) getAllTransactionsApi();
        else Toast.makeText(OrderStatusAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }


    private void getAllTransactionsApi() {

        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("user_id", DataManager.getInstance().getUserData(OrderStatusAct.this).result.id);
        Log.e("paramHashparamHash", "paramHash = " + paramHash);
        Call<ResponseBody> call = apiInterface.getOrderStatus(paramHash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 DataManager.getInstance().hideProgressMessage();
                try {

                    String stringResponse = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(stringResponse);

                        Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = " + stringResponse);

                        if (jsonObject.getString("status").equals("1")) {
                          //  Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = " + stringResponse);
                            OrderStatusModel modelTransactions = new Gson().fromJson(stringResponse, OrderStatusModel.class);
                             arrayList.clear();
                             binding.tvFound.setVisibility(View.GONE);
                             for (int i = 0;i< modelTransactions.getResult().size();i++){
                                 for (int j = 0;j< modelTransactions.getResult().get(i).getCartData().size();j++) {
                                     arrayList.add(modelTransactions.getResult().get(i).getCartData().get(j));
                                 }
                             }

                             adapter = new OrderStatusAdapter(OrderStatusAct.this,arrayList);
                            binding.rvCart.setAdapter(adapter);

                        } else {
                           // Toast.makeText(OrderStatusAct.this, jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            binding.tvFound.setVisibility(View.VISIBLE);

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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(OrderReceiver, new IntentFilter("Order_Status_Action"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(OrderReceiver);

    }

    @Override
    public void info(String value, String size) {
        if(NetworkAvailablity.checkNetworkStatus(OrderStatusAct.this)) getAllTransactionsApi();
        else Toast.makeText(OrderStatusAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }
}
