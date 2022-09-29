package com.nothing21;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nothing21.databinding.ActivityAddAddress1Binding;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUserAddress extends AppCompatActivity {
    public String TAG = "AddUserAddress";
    ActivityAddAddress1Binding binding;
    Nothing21Interface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_address1);
        initViews();
    }

    private void initViews() {
        binding.ccp.setCountryForPhoneCode(971);

        binding.btnAdd.setOnClickListener(v -> {
              validation();
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
            if (NetworkAvailablity.checkNetworkStatus(AddUserAddress.this))

                addAddresssssss();
            else
                Toast.makeText(AddUserAddress.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }


    private void addAddresssssss() {
        DataManager.getInstance().showProgressMessage(AddUserAddress.this, getString(R.string.please_wait));
        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("city", binding.edCity.getText().toString());
        paramHash.put("area", binding.edArea.getText().toString());
        paramHash.put("landmark", binding.edLandmark.getText().toString());
        paramHash.put("building", binding.edBuildingName.getText().toString());
        paramHash.put("flat", binding.edApartment.getText().toString());
        paramHash.put("zip_code", binding.edZipcode.getText().toString());
        paramHash.put("country_code", binding.ccp.getSelectedCountryCode()+"");
        paramHash.put("mobile", binding.edCity.getText().toString());
        paramHash.put("user_id", DataManager.getInstance().getUserData(AddUserAddress.this).result.id);
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
                        //    addressId = object.getString("id");
                            Toast.makeText(AddUserAddress.this, getString(R.string.add_address_successfully), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {

                            Toast.makeText(AddUserAddress.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


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
