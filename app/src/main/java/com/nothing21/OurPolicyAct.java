package com.nothing21;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nothing21.databinding.ActivityPolicyBinding;
import com.nothing21.retrofit.Constant;

public class OurPolicyAct extends AppCompatActivity {
    ActivityPolicyBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_policy);
        initViews();

    }

    private void initViews() {

        binding.ivBack.setOnClickListener(v -> finish());


        binding.tvPrivacyPolicy.setOnClickListener(v ->
                startActivity(new Intent(OurPolicyAct.this, AboutAct.class)
                        .putExtra("url", Constant.PRIVACY_POLICY)
                        .putExtra("title",getString(R.string.privacy_policy))));

        binding.tvTermsCondition.setOnClickListener(v ->
                startActivity(new Intent(OurPolicyAct.this, AboutAct.class)
                        .putExtra("url",Constant.TERMS_CONDITIONS)
                        .putExtra("title",getString(R.string.terms_condition))));


        binding.tvShippingPolicy.setOnClickListener(v ->
                startActivity(new Intent(OurPolicyAct.this, AboutAct.class)
                        .putExtra("url",Constant.SHIPPING_POLICY)
                        .putExtra("title",getString(R.string.shipping_policy))));

        binding.tvReturnPolicy.setOnClickListener(v ->
                startActivity(new Intent(OurPolicyAct.this, AboutAct.class)
                        .putExtra("url",Constant.RETURN_POLICY)
                        .putExtra("title",getString(R.string.return_policy))));
    }
}
