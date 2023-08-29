package com.nothing21;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.adapter.CardAdapter;
import com.nothing21.databinding.ActivityCardListBinding;
import com.nothing21.listener.OnItemPositionListener;
import com.nothing21.model.GetCardModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardLitAct  extends AppCompatActivity implements OnItemPositionListener {
   public String TAG = "CardLitAct";
   ActivityCardListBinding binding;
    ArrayList<GetCardModel.Result> arrayList;
    Nothing21Interface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_card_list);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        binding.reservationToolbar.tvTitle.setText(getString(R.string.payment_info));
        if(NetworkAvailablity.checkNetworkStatus(CardLitAct.this)) getCardList();
        else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.reservationToolbar.ivBack.setOnClickListener(v -> finish());

        binding.actionAdd.setOnClickListener(v ->  startActivity(new Intent(CardLitAct.this,AddCardAct.class)));

    }

    private void getCardList() {
        DataManager.getInstance().showProgressMessage(CardLitAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(CardLitAct.this).result.id);
        Log.e(TAG, "GET CARD REQUEST" + map);
        Call<ResponseBody> payCall = apiInterface.getCardList( map);
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
                        binding.txtCardNotFound.setVisibility(View.GONE);
                        arrayList.addAll(data.result);
                        binding.rvPayinfo.setAdapter(new CardAdapter(CardLitAct.this,CardLitAct.this, (ArrayList<GetCardModel.Result>) data.result));
                    } else if (jsonObject.getString("status").equals("0")) {
                        binding.txtCardNotFound.setVisibility(View.VISIBLE);
                        //  Toast.makeText(PaymentInfoActivity.this, data.message, Toast.LENGTH_SHORT).show();
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




    @Override
    protected void onResume() {
        super.onResume();
        if(NetworkAvailablity.checkNetworkStatus(CardLitAct.this)) getCardList();
        else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPosition(int position) {
        startActivity(new Intent(CardLitAct.this,EditCardAct.class)
               // .putExtra("cardHolder",arrayList.get(position).cardHolderName)
                .putExtra("id",arrayList.get(position).id)
                .putExtra("cardNumber",arrayList.get(position).cardNumber)
                .putExtra("monthvalid",arrayList.get(position).expiryMonth)
                .putExtra("monthyear",arrayList.get(position).expiryDate)
                .putExtra("cxx",arrayList.get(position).cardCvv));
    }
}