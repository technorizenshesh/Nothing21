package com.nothing21;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.databinding.ActivityEditCardBinding;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.stripe.android.model.Card;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCardAct extends AppCompatActivity {
    ActivityEditCardBinding binding;
    String cardNumber="",monthvalid="",monthyear="",cxx="",id="",cardHolder="";
    Card.Builder card;
    Nothing21Interface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_card);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        initViews();
    }

    private void initViews() {
        if(getIntent()!=null){
          //  cardHolder = getIntent().getStringExtra("cardHolder");
            cardNumber = getIntent().getStringExtra("cardNumber");
            monthvalid = getIntent().getStringExtra("monthvalid");
            monthyear = getIntent().getStringExtra("monthyear");
            id = getIntent().getStringExtra("id");
            cxx = getIntent().getStringExtra("cxx");

        }
        cardInit();
        binding.btnUpdate.setOnClickListener(v -> validation());

        binding.cardForm.getCardEditText().setText(cardNumber);
        binding.cardForm.getCvvEditText().setText(cxx);
        binding.cardForm.getExpirationDateEditText().setText(monthvalid  + monthyear);

        binding.header1.tvTitle.setText(getString(R.string.edit_card));

        binding.header1.ivBack.setOnClickListener(v -> finish());
    }

    private void cardInit() {
        binding.cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                // .mobileNumberExplanation("SMS is required on this number")
                .setup(EditCardAct.this);

        binding.cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

    }


    private void validation() {
        Card.Builder card = new Card.Builder(binding.cardForm.getCardNumber(),
                Integer.valueOf(binding.cardForm.getExpirationMonth()),
                Integer.valueOf(binding.cardForm.getExpirationYear()),
                binding.cardForm.getCvv());

        if (!card.build().validateCard()) {
            Toast.makeText(EditCardAct.this, getString(R.string.card_not_valid), Toast.LENGTH_SHORT).show();

        }
        else {
            if(NetworkAvailablity.checkNetworkStatus(EditCardAct.this)) {
                updateCard(binding.cardForm.getCardNumber(), binding.cardForm.getExpirationMonth().toString(),
                        binding.cardForm.getExpirationYear().toString()
                        , binding.cardForm.getCvv());
            }
            else {
                Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void updateCard(String cardNumber, String expiryMonth,String expiryYear, String cvv) {
        DataManager.getInstance().showProgressMessage(EditCardAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
      //  map.put("card_holder_name",cardHolder);
        map.put("card_number",cardNumber);
        map.put("expiry_date",expiryYear);
        map.put("expiry_month",expiryMonth);
        map.put("card_cvv",cvv);
        map.put("user_id",DataManager.getInstance().getUserData(EditCardAct.this).result.id);
        Log.e("MapMap", "EDIT CARD REQUEST" + map);
        Call<ResponseBody> payCall = apiInterface.editCard( map);
        payCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e("MapMap", "EDIT CARD RESPONSE" + stringResponse);
                    if (jsonObject.getString("status").equals("1")) {
                        finish();
                    } else if (jsonObject.getString("status").equals("0")) {
                        Toast.makeText(EditCardAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
