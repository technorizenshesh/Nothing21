package com.nothing21;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.databinding.ActivityEditAddress1Binding;
import com.nothing21.model.AddressModel;
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

public class EditAddessAct extends AppCompatActivity {
    public String TAG = "EditAddessAct";
    ActivityEditAddress1Binding binding;
    Nothing21Interface apiInterface;
    String addressId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_address1);
        initViews();
    }

    private void initViews() {
        if (getIntent() != null) {
            addressId = getIntent().getStringExtra("address_id");
            binding.edCity.setText(getIntent().getStringExtra("city"));
            // binding.edArea.setText(getIntent().getStringExtra("area"));
            binding.edArea.setText(getIntent().getStringExtra("city"));
            binding.edLandmark.setText(getIntent().getStringExtra("landmark"));
            binding.edBuildingName.setText(getIntent().getStringExtra("building"));
            binding.edApartment.setText(getIntent().getStringExtra("flat"));
            binding.edContact.setText(getIntent().getStringExtra("mobile"));
            binding.edZipcode.setText(getIntent().getStringExtra("zipCode"));
            binding.ccp.setCountryForPhoneCode(971);
        }

        binding.btnUpdate.setOnClickListener(v -> {
            validation();
        });

        binding.ivBack.setOnClickListener(v -> {
finish();        });

    }


    private void validation() {
        if (binding.edCity.getText().toString().equals("")) {
            binding.edCity.setError(getString(R.string.required));
            binding.edCity.setFocusable(true);
        } else if (binding.edArea.getText().toString().equals("")) {
            binding.edArea.setError(getString(R.string.required));
            binding.edArea.setFocusable(true);
        } else if (binding.edLandmark.getText().toString().equals("")) {
            binding.edLandmark.setError(getString(R.string.required));
            binding.edLandmark.setFocusable(true);
        } else if (binding.edBuildingName.getText().toString().equals("")) {
            binding.edBuildingName.setError(getString(R.string.required));
            binding.edBuildingName.setFocusable(true);
        } else if (binding.edApartment.getText().toString().equals("")) {
            binding.edApartment.setError(getString(R.string.required));
            binding.edApartment.setFocusable(true);
        } else if (binding.edZipcode.getText().toString().equals("")) {
            binding.edZipcode.setError(getString(R.string.required));
            binding.edZipcode.setFocusable(true);
        } else if (binding.edContact.getText().toString().equals("")) {
            binding.edContact.setError(getString(R.string.required));
            binding.edContact.setFocusable(true);
        } else {
            if (NetworkAvailablity.checkNetworkStatus(EditAddessAct.this))

                updateAddresssssss();
            else
                Toast.makeText(EditAddessAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAddresssssss() {
        DataManager.getInstance().showProgressMessage(EditAddessAct.this,getString(R.string.please_wait));
        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("user_id", DataManager.getInstance().getUserData(EditAddessAct.this).result.id);
        paramHash.put("city", binding.edCity.getText().toString());
        paramHash.put("area", binding.edArea.getText().toString());
        paramHash.put("landmark", binding.edLandmark.getText().toString());
        paramHash.put("building", binding.edBuildingName.getText().toString());
        paramHash.put("flat", binding.edApartment.getText().toString());
        paramHash.put("zip_code", binding.edZipcode.getText().toString());
        paramHash.put("country_code", binding.ccp.getSelectedCountryCode() + "");
        paramHash.put("mobile", binding.edContact.getText().toString());
        paramHash.put("address_id", addressId);


        Log.e("update address===", "paramHash = " + paramHash);
        Call<ResponseBody> call = apiInterface.updateAddress(paramHash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    String stringResponse = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "update address Response = " + stringResponse);
                        if (jsonObject.getString("status").equals("1")) {
                            finish();

                        } else {
                            // binding.tvSave.setVisibility(View.GONE);
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
