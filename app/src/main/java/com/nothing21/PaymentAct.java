package com.nothing21;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.adapter.ShowCardAdapter;
import com.nothing21.databinding.ActivityPaymentBinding;
import com.nothing21.listener.OnItemPositionListener;
import com.nothing21.model.GetCardModel;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentAct extends AppCompatActivity implements OnItemPositionListener {
    public String TAG = "PaymentAct";
    ActivityPaymentBinding binding;
    Nothing21Interface apiInterface;
    String amount = "", requestId = "",category_order_id="",couponId="", cardNumber = "", expiryMonth = "", expiryDate = "", cvvv = "",promo_code="";
    ArrayList<GetCardModel.Result> arrayList;
    boolean selectCheck = false;


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

        arrayList = new ArrayList<>();

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
                                    addCard(amount,requestId,token.getId());

                              //  PayProvider1(amount, requestId, token.getId());
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


        if (NetworkAvailablity.checkNetworkStatus(PaymentAct.this)) getCardList();
        else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.payment.setOnClickListener(v -> {

            if(selectCheck== true)   cardPayment(); //PayConfrm();
            else Toast.makeText(this, getString(R.string.please_select_card), Toast.LENGTH_SHORT).show();

        });


    }

    private void cardPayment() {

        Card.Builder card = new Card.Builder(cardNumber,
                Integer.valueOf(expiryMonth),
                Integer.valueOf(expiryDate),
                cvvv);

        if (!card.build().validateCard()) {
            Toast.makeText(PaymentAct.this, getString(R.string.card_not_valid), Toast.LENGTH_SHORT).show();
            return;
        }
        Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_TEST_KEY);
        // Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_LIVE_KEY);

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


    private void getCardList() {
        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(PaymentAct.this).result.id);
        Log.e(TAG, "GET CARD REQUEST" + map);
        Call<ResponseBody> payCall = apiInterface.getCardList(map);
        payCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "GET CARD RESPONSE" + stringResponse);
                    if (jsonObject.getString("status").equals("1")) {
                        arrayList.clear();
                        GetCardModel data = new Gson().fromJson(stringResponse,GetCardModel.class);
                        arrayList.addAll(data.result);
                        if (arrayList.size() != 0) {
                            binding.layoutRv.setVisibility(View.VISIBLE);
                            binding.ViewScroll.setVisibility(View.GONE);
                           // binding.tvPayment.setTe(getString(R.string.pay));
                            //  binding.payment.setText("€" + String.format("%.2f", Double.parseDouble(amount1)) + " "+getString(R.string.pay));
                            binding.rvCard.setAdapter(new ShowCardAdapter(PaymentAct.this, PaymentAct.this, arrayList));
                        } else {
                            Log.e("dhhdhdhdhd", "jjdfjfdjdjdj====");
                            binding.layoutRv.setVisibility(View.GONE);
                            binding.ViewScroll.setVisibility(View.VISIBLE);
                        }

                    } else if (jsonObject.getString("status").equals("0")) {
                        binding.layoutRv.setVisibility(View.GONE);
                        binding.ViewScroll.setVisibility(View.VISIBLE);
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


    public void addCard(String amount, String requestId, String token) {
        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        //  map.put("card_holder_name", binding.cardForm.getCardholderName());
        map.put("card_number", binding.cardForm.getCardNumber());
        map.put("expiry_date",  binding.cardForm.getExpirationYear());
        map.put("expiry_month",  binding.cardForm.getExpirationMonth());
        map.put("card_cvv", binding.cardForm.getCvv()+"");
        map.put("user_id", DataManager.getInstance().getUserData(PaymentAct.this).result.id);
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
                       // finish();
                        if (NetworkAvailablity.checkNetworkStatus(PaymentAct.this))
                            PayProvider1(amount, requestId, token);
                        else
                        Toast.makeText(PaymentAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equals("0")) {
                        Toast.makeText(PaymentAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
    public void onPosition(int position) {
        cardNumber = arrayList.get(position).cardNumber;
        expiryMonth = arrayList.get(position).expiryMonth;
        expiryDate = arrayList.get(position).expiryDate;
        cvvv = arrayList.get(position).cardCvv;
        selectCheck = true;
    }
}
