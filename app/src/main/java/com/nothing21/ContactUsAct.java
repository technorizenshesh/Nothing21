package com.nothing21;

import android.os.Bundle;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nothing21.databinding.ActivityContactUsBinding;

public class ContactUsAct extends AppCompatActivity {
    ActivityContactUsBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact_us);
        initViews();
    }

    private void initViews() {
        binding.webView.loadUrl("https://nothing21.com/nothing21/index.php/webservice/get_contact_us?");
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());

        binding.ivBack.setOnClickListener(v -> finish());
    }

}
