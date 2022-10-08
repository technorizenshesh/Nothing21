package com.nothing21;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.databinding.ActivityPaymentBinding;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Constant;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentAct extends AppCompatActivity {
    public String TAG = "PaymentAct";
    ActivityPaymentBinding binding;
    Nothing21Interface apiInterface;
    String amount = "", requestId = "",category_order_id="",couponId="", cardNumber = "", expiryMonth = "", expiryDate = "", cvvv = "",promo_code="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment);
        initViews();
    }



    private void initViews() {


        if (getIntent() != null) {
            requestId = getIntent().getStringExtra("request_id");
            amount = getIntent().getStringExtra("amount");
            promo_code = getIntent().getStringExtra("promo_code");
            category_order_id = getIntent().getStringExtra("category_order_id");
            couponId = getIntent().getStringExtra("couponId");

            // DecimalFormat df = new DecimalFormat("0.00");
            // binding.btMakePayment.setText("€" + String.format("%.2f", Double.parseDouble(amount1)) + " " + getString(R.string.pay));
            // binding.payment.setText("€" + String.format("%.2f", Double.parseDouble(amount1)) + " " +   getString(R.string.pay));

            binding.reservationToolbar.tvTitle.setText(getString(R.string.pay));

        }


        binding.btnPay.setOnClickListener(v -> {

            if (binding.cardForm.isValid()){
                Card.Builder card = new Card.Builder(binding.cardForm.getCardNumber(),
                        Integer.valueOf(binding.cardForm.getExpirationMonth()),
                        Integer.valueOf(binding.cardForm.getExpirationYear()),
                        binding.cardForm.getCvv());

                if (!card.build().validateCard()) {
                    Toast.makeText(PaymentAct.this, getString(R.string.card_not_valid), Toast.LENGTH_SHORT).show();
                    return;
                }
                Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_TEST_KEY);
                //  Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_LIVE_KEY);

                DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
                stripe.createCardToken(
                        card.build(), new ApiResultCallback<Token>() {
                            @Override
                            public void onSuccess(Token token) {
                                DataManager.getInstance().hideProgressMessage();
                                Log.e("Stripe Token===", token.getId());
                                // Toast.makeText(mContext, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                                // charge(token);
                                if (NetworkAvailablity.checkNetworkStatus(PaymentAct.this))
                                    PayProvider1(amount, requestId, token.getId());
                                else
                                    Toast.makeText(PaymentAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                               /* if(!DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId.equals(""))
                                addCard(DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId,token.getId());
                                 else
                                     SaveCard(token.getId());*/
                            }

                            @Override
                            public void onError(@NotNull Exception e) {
                                DataManager.getInstance().hideProgressMessage();
                                e.printStackTrace();
                                Toast.makeText(PaymentAct.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                // paymentConfirmDialog();
            }

            else Toast.makeText(this, getText(R.string.please_complete_form), Toast.LENGTH_LONG).show();

        });

        binding.reservationToolbar.ivBack.setOnClickListener(v -> onBackPressed());


        cardInit();
    }


    private void cardInit() {
        binding.cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                // .mobileNumberExplanation("SMS is required on this number")
                .setup(PaymentAct.this);
        binding.cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }




    private void PayProvider1(String amount, String requestId, String token) {
        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(PaymentAct.this).result.id);
        map.put("order_id", requestId);
        map.put("payment_method", "Stripe");
        map.put("total_amount", amount);
        map.put("token", token);
        map.put("currency", "EUR");
        map.put("promo_code",promo_code);
        map.put("category_order_id", category_order_id);
        map.put("promo_code_id",couponId);
        Log.e("MapMap", "PAYMENT REQUEST" + map);
        Call<Map<String,String>> payCall = apiInterface.payment(map);
        payCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "PAYMENT RESPONSE" + dataResponse);
                    if (data.get("status").equals("1")) {
                        //String dataResponse = new Gson().toJson(response.body());
                        // Log.e("MapMap", "PAYMENT RESPONSE" + dataResponse);

                        Toast.makeText(PaymentAct.this, getString(R.string.order_placed_successfully), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentAct.this, HomeAct.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(PaymentAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }


}
