package com.nothing21;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nothing21.databinding.ActivityEditProfileBinding;
import com.nothing21.databinding.ActivitySupportBinding;
import com.nothing21.model.LoginModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Constant;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportAct extends AppCompatActivity {
   // =1&=cancel&user_id=1
   public String TAG = "SupportAct";
    ActivitySupportBinding binding;
    Nothing21Interface apiInterface;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_support);
        initViews();

    }

    private void initViews() {
        if(getIntent()!=null) binding.tvTitle.setText(getIntent().getStringExtra("title"));
       // Log.e("url===","https://nothing21.com/nothing21/contact-us?web_user_id="+DataManager.getInstance().getUserData(SupportAct.this).result.id);
      //  binding.webView.loadUrl("https://nothing21.com/nothing21/contact-us?web_user_id="+DataManager.getInstance().getUserData(SupportAct.this).result.id);
        binding.webView.loadUrl(Constant.ORDER_ISSUES);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());

        binding.ivBack.setOnClickListener(v -> finish());


      /*  binding.btnSend.setOnClickListener(v -> {
            validation();
        });*/
    }




}
