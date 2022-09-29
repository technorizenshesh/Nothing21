package com.nothing21;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nothing21.databinding.ActivitySupportBinding;
import com.nothing21.databinding.ActivitySupportTwoBinding;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;

public class SupportAct2 extends AppCompatActivity {
    // =1&=cancel&user_id=1
    public String TAG = "SupportAct";
    ActivitySupportTwoBinding binding;
    Nothing21Interface apiInterface;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_support_two);
        initViews();

    }
  //  https://nothing21.com/nothing21/contact-us?web_user_id=77

    private void initViews() {
        // Log.e("url===","https://nothing21.com/nothing21/contact-us?web_user_id="+DataManager.getInstance().getUserData(SupportAct.this).result.id);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
              //  super.onPageFinished(view, url);
              //  binding.webView.loadUrl("https://nothing21.com/nothing21/contact-us?web_user_id="+ DataManager.getInstance().getUserData(SupportAct2.this).result.id);

            }
        });
        binding.webView.loadUrl("https://nothing21.com/nothing21/contact-us?web_user_id="+ DataManager.getInstance().getUserData(SupportAct2.this).result.id);


        binding.ivBack.setOnClickListener(v -> finish());


        binding.btnSubmit.setOnClickListener(v -> {
            finish();
        });
    }







}

