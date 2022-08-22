package com.nothing21;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.adapter.HistoryAdapter;
import com.nothing21.adapter.OrderStatusAdapter;
import com.nothing21.databinding.ActivityOrderStatusBinding;
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

public class ReturnListAct extends AppCompatActivity {
    public String TAG = "ReturnListAct";
    ActivityOrderStatusBinding binding;
    ArrayList<OrderStatusModel.Result.CartDatum> arrayList;
    OrderStatusAdapter adapter;
    Nothing21Interface apiInterface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_status);
        initViews();    }

    private void initViews() {
        binding.tvTitle.setText(getString(R.string.order_return));
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        arrayList = new ArrayList<>();

   //    adapter = new OrderStatusAdapter(ReturnListAct.this, arrayList);
   //    binding.rvCart.setAdapter(adapter);

        if (NetworkAvailablity.checkNetworkStatus(ReturnListAct.this)) getAllReturnOrderApi();
        else
            Toast.makeText(ReturnListAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }


    private void getAllReturnOrderApi() {
        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("user_id", DataManager.getInstance().getUserData(ReturnListAct.this).result.id);
        Log.e("paramHashparamHash", "paramHash = " + paramHash);
        Call<ResponseBody> call = apiInterface.returnHistory(paramHash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    String stringResponse = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(stringResponse);

                        Log.e(TAG, "OrderHistoryAPiCall Response = " + stringResponse);

                        if (jsonObject.getString("status").equals("1")) {
                            //  Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = " + stringResponse);
                            OrderStatusModel modelTransactions = new Gson().fromJson(stringResponse, OrderStatusModel.class);
                            arrayList.clear();
                            binding.tvFound.setVisibility(View.GONE);
                            binding.rvCart.setVisibility(View.VISIBLE);

                            for (int i = 0; i < modelTransactions.getResult().size(); i++) {
                                for (int j = 0; j < modelTransactions.getResult().get(i).getCartData().size(); j++) {
                                    arrayList.add(modelTransactions.getResult().get(i).getCartData().get(j));
                                }
                            }


                            OrderStatusAdapter adapter = new OrderStatusAdapter(ReturnListAct.this, arrayList);
                            binding.rvCart.setAdapter(adapter);

                        } else {

                            binding.rvCart.setVisibility(View.GONE);
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

}
