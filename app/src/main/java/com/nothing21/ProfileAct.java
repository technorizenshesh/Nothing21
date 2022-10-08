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
import com.nothing21.adapter.AddressAdapter;
import com.nothing21.adapter.AddressAdapter1;
import com.nothing21.databinding.ActivityProfileBinding;
import com.nothing21.listener.onItemClickListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileAct extends AppCompatActivity implements onItemClickListener {
    public String TAG = "ProfileAct";
    ActivityProfileBinding binding;
    Nothing21Interface apiInterface;
    AddressAdapter1 adapter;
    ArrayList<AddressModel.Result>arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        initViews();
    }

    private void initViews() {

        binding.btnEdit.setOnClickListener(v -> startActivity(new Intent(ProfileAct.this, EditProfileAct.class)));

        binding.tvAnotherAddress.setOnClickListener(v -> startActivity(new Intent(ProfileAct.this, AddUserAddress.class)));


        binding.ivBack.setOnClickListener(v -> finish());

        arrayList = new ArrayList<>();

        adapter = new AddressAdapter1(ProfileAct.this,arrayList,ProfileAct.this);
        binding.rvAddress.setAdapter(adapter);
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
                        if(!DataManager.getInstance().getUserData(ProfileAct.this).result.countryCode.equals("")) binding.edCountryCode.setText("+"+DataManager.getInstance().getUserData(ProfileAct.this).result.countryCode);//binding.ccp.setCountryForPhoneCode(Integer.parseInt(DataManager.getInstance().getUserData(ProfileAct.this).result.countryCode));
                       // binding.ccp.setCountryForPhoneCode(971);

                        if(!data11.result.image.equals("")){
                            Glide.with(ProfileAct.this)
                                    .load(data11.result.image)
                                    .override(150,150)
                                    .error(R.drawable.dummy)
                                    .into(binding.ivProfile);
                        }


                        getAddressssss();


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



    private void getAddressssss() {
        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("user_id", DataManager.getInstance().getUserData(ProfileAct.this).result.id);
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
                            //  Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = " + stringResponse);
                            AddressModel model = new Gson().fromJson(stringResponse,AddressModel.class);
                            arrayList.clear();
                            arrayList.addAll(model.getResult());
                            adapter.notifyDataSetChanged();
                            //binding.tvSave.setVisibility(View.VISIBLE);

                        } else {
                           // binding.tvSave.setVisibility(View.GONE);
                            arrayList.clear();
                            adapter.notifyDataSetChanged();
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
    public void onItem(int position) {

    }
}
