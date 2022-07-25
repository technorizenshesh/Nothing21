package com.nothing21;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nothing21.databinding.ActivityEditProfileBinding;
import com.nothing21.databinding.ActivitySupportBinding;
import com.nothing21.model.LoginModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Constant;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportAct extends AppCompatActivity {
   // =1&=cancel&user_id=1
   public String TAG = "SupportAct";
    ActivitySupportBinding binding;
    Nothing21Interface apiInterface;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_support);
        initViews();

    }

    private void initViews() {

        binding.ivBack.setOnClickListener(v -> finish());


        binding.btnSend.setOnClickListener(v -> {
            validation();
        });
    }



    private void validation() {
        if (binding.etSubject.getText().toString().equals("")) {
            binding.etSubject.setError(getString(R.string.required));
            binding.etSubject.setFocusable(true);
        }
        else if (binding.etDescription.getText().toString().equals("")) {
            binding.etDescription.setError(getString(R.string.required));
            binding.etDescription.setFocusable(true);
        }

        else {
            if(NetworkAvailablity.checkNetworkStatus(SupportAct.this)) sentToSupport();
            else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }


    }


    public void sentToSupport(){
        DataManager.getInstance().showProgressMessage(SupportAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("subject", binding.etSubject.getText().toString());
        map.put("description", binding.etDescription.getText().toString());
        map.put("user_id", DataManager.getInstance().getUserData(SupportAct.this).result.id);
        Log.e(TAG, "SendSupport Request :" + map);
        Call<ResponseBody> loginCall = apiInterface.supportApi(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    String stringResponse = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "SendSupport Response = " + stringResponse);
                        if (jsonObject.getString("status").equals("1")) {
                            Toast.makeText(SupportAct.this, getString(R.string.succssfully_send), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            //  Toast.makeText(OrderStatusAct.this, jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
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
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}
