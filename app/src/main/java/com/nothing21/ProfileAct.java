package com.nothing21;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nothing21.databinding.ActivityProfileBinding;
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

public class ProfileAct extends AppCompatActivity {
    public String TAG = "ProfileAct";
    ActivityProfileBinding binding;
    Nothing21Interface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        initViews();
    }

    private void initViews() {

        binding.btnEdit.setOnClickListener(v -> startActivity(new Intent(ProfileAct.this, EditProfileAct.class)));

        binding.ivBack.setOnClickListener(v -> finish());
    }

    public void getUserProfile(){
        DataManager.getInstance().showProgressMessage(ProfileAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(ProfileAct.this).result.id);
        Log.e(TAG, "get Profile Request :" + map);
        Call<LoginModel> loginCall = apiInterface.userProfile(map);
        loginCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    LoginModel  data11 = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "get Profile Response :" + responseString);
                    if (data11.status.equals("1")) {
                        SessionManager.writeString(ProfileAct.this, Constant.USER_INFO, responseString);
                        binding.etName.setText(DataManager.getInstance().getUserData(ProfileAct.this).result.firstName);
                        binding.etSurName.setText(DataManager.getInstance().getUserData(ProfileAct.this).result.lastName);
                        binding.etEmail.setText(DataManager.getInstance().getUserData(ProfileAct.this).result.email);
                        binding.etEmirate.setText(DataManager.getInstance().getUserData(ProfileAct.this).result.emirate);
                        binding.etAddress.setText(DataManager.getInstance().getUserData(ProfileAct.this).result.address);
                       // binding.etEmail.setText(DataManager.getInstance().getUserData(getActivity()).result.);


                        if(!DataManager.getInstance().getUserData(ProfileAct.this).result.mobile.equals(""))   binding.etMobile.setText(DataManager.getInstance().getUserData(ProfileAct.this).result.mobile);
                        if(!DataManager.getInstance().getUserData(ProfileAct.this).result.countryCode.equals("")) binding.ccp.setCountryForPhoneCode(Integer.parseInt(DataManager.getInstance().getUserData(ProfileAct.this).result.countryCode));
                        binding.ccp.setCountryForPhoneCode(971);

                        if(!data11.result.image.equals("")){
                            Glide.with(ProfileAct.this)
                                    .load(data11.result.image)
                                    .override(150,150)
                                    .error(R.drawable.dummy)
                                    .into(binding.ivProfile);
                        }


                    } else if (data11.status.equals("0")) {
                        Toast.makeText(ProfileAct.this, "", Toast.LENGTH_SHORT).show();


                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(NetworkAvailablity.checkNetworkStatus(ProfileAct.this)) getUserProfile();
        else Toast.makeText(ProfileAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }
}
