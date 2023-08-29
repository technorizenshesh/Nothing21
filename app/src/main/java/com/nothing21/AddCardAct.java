package com.nothing21;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.braintreepayments.cardform.view.CardForm;
import com.google.gson.Gson;
import com.nothing21.databinding.ActvityAddCardBinding;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCardAct extends AppCompatActivity {
    public String TAG = "AddCardAct";
    ActvityAddCardBinding binding;
    Nothing21Interface apiInterface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.actvity_add_card);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        initViews();
    }

    private void initViews() {
        binding.cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel(getString(R.string.purchase))
                .setup(this);

        binding.reservationToolbar.tvTitle.setText(getString(R.string.add_card_));

        binding.reservationToolbar.ivBack.setOnClickListener(v -> finish());

        binding.btnAdd.setOnClickListener(v -> {
            if(NetworkAvailablity.checkNetworkStatus(AddCardAct.this)) addCard(); else Toast.makeText(AddCardAct.this,getString(R.string.network_failure),Toast.LENGTH_LONG).show();
        });
    }


    public void addCard() {
        DataManager.getInstance().showProgressMessage(AddCardAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
      //  map.put("card_holder_name", binding.cardForm.getCardholderName());
        map.put("card_number", binding.cardForm.getCardNumber());
        map.put("expiry_date",  binding.cardForm.getExpirationYear());
        map.put("expiry_month",  binding.cardForm.getExpirationMonth());
        map.put("card_cvv", binding.cardForm.getCvv()+"");
        map.put("user_id", DataManager.getInstance().getUserData(AddCardAct.this).result.id);
        Log.e(TAG, "Add Bank Card Request :" + map);
        Call<ResponseBody> callNearCar = apiInterface.addCardss(map);
        callNearCar.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Add Bank Card Response :" + stringResponse);
                    if (jsonObject.getString("status").equals("1")) {
                        finish();
                    } else if (jsonObject.getString("status").equals("0")) {
                        Toast.makeText(AddCardAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
