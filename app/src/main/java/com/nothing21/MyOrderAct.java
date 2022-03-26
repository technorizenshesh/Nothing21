/*
package com.nothing21;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.adapter.CartAdapter;
import com.nothing21.adapter.MyOrderAdapter;
import com.nothing21.databinding.ActivityMyOrderBinding;
import com.nothing21.fragment.CartFragment;
import com.nothing21.model.FavModel;
import com.nothing21.model.GetCartModel;
import com.nothing21.model.MyOrderModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderAct extends AppCompatActivity {
    public String TAG = "MyOrderAct";
    ActivityMyOrderBinding binding;
    Nothing21Interface apiInterface;
    ArrayList<FavModel.Result> arrayList;
    MyOrderAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order);
        initViews();

    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new MyOrderAdapter(MyOrderAct.this, arrayList);
        binding.rvMyOrder.setAdapter(adapter);

        if (NetworkAvailablity.checkNetworkStatus(MyOrderAct.this)) getAllMyOrder();
        else
            Toast.makeText(MyOrderAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }


    public void getAllMyOrder() {
        DataManager.getInstance().showProgressMessage(MyOrderAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(MyOrderAct.this).result.id + "");
        Log.e(TAG, "get My Order List Request :" + map);
        Call<MyOrderModel> loginCall = apiInterface.getMyOrders(map);
        loginCall.enqueue(new Callback<MyOrderModel>() {
            @Override
            public void onResponse(Call<MyOrderModel> call, Response<MyOrderModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    MyOrderModel data11 = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "get My Order List Response :" + responseString);
                    if (data11.status.equals("1")) {
                        arrayList.clear();
                        binding.tvFound.setVisibility(View.GONE);
                        binding.layoutHeader.setVisibility(View.VISIBLE);
                        arrayList.addAll(data11.result);

                        adapter.notifyDataSetChanged();
                    } else if (data11.status.equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvFound.setVisibility(View.VISIBLE);
                        binding.layoutHeader.setVisibility(View.GONE);


                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MyOrderModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                arrayList.clear();
                adapter.notifyDataSetChanged();
                binding.tvFound.setVisibility(View.VISIBLE);
                binding.layoutHeader.setVisibility(View.GONE);

            }
        });
    }
}
*/
