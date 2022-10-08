package com.nothing21;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.adapter.MyOrderAdapterTwo;
import com.nothing21.databinding.ActivityContactUsBinding;
import com.nothing21.model.ContactUseModel;
import com.nothing21.model.OrderStatusModel;
import com.nothing21.model.ProductNewModel;
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

public class ContactUsAct extends AppCompatActivity {
    public String TAG = "ContactUsAct";

    ActivityContactUsBinding binding;
    Nothing21Interface apiInterface;
    String email="",mobile="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact_us);
        initViews();
    }

    private void initViews() {
        binding.webView.loadUrl("https://nothing21.com/nothing21/index.php/webservice/get_contact_us?");
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());

        binding.ivBack.setOnClickListener(v -> finish());
        if(NetworkAvailablity.checkNetworkStatus(ContactUsAct.this)) GetAllWishList();
        else Toast.makeText(ContactUsAct.this,getString(R.string.network_failure),Toast.LENGTH_SHORT).show();


        binding.ivEmail.setOnClickListener(v -> {
            setClipboard(ContactUsAct.this,email,"Email copied");
        });

        binding.ivMobile.setOnClickListener(v -> {
            setClipboard(ContactUsAct.this,mobile,"Mobile Number copied");
        });


    }



    public void GetAllWishList(){
        DataManager.getInstance().showProgressMessage(ContactUsAct.this, getString(R.string.please_wait));
        Call<ResponseBody> loginCall = apiInterface.getContact();
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String stringResponse = response.body().string();


                    JSONObject jsonObject = new JSONObject(stringResponse);

                    Log.e(TAG, "get Contact us Response = " + stringResponse);

                    if (jsonObject.getString("status").equals("1")) {
                        //  Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = " + stringResponse);
                        ContactUseModel modelTransactions = new Gson().fromJson(stringResponse, ContactUseModel.class);
                        email = modelTransactions.getResult().get(0).getEmail();
                        mobile = modelTransactions.getResult().get(0).getMobile();
                         binding.tvNumber.setText("Contact Number : " + modelTransactions.getResult().get(0).getMobile());
                        binding.tvEmail.setText("Email : " + modelTransactions.getResult().get(0).getEmail());

                        // serviceAdapter.notifyDataSetChanged();
                    } else {

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

    private void setClipboard(Context context, String text,String msg) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        }
    }

}
