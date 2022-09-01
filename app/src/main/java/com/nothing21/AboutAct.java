package com.nothing21;

import android.os.Bundle;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nothing21.databinding.ActivityAboutBinding;

public class AboutAct extends AppCompatActivity {
    ActivityAboutBinding binding;
    String url = "", title = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        initViews();
    }

    private void initViews() {
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");

        }
        binding.tvTitle.setText(title);
        binding.webView.loadUrl(url);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());

        binding.ivBack.setOnClickListener(v -> finish());
    }

}



