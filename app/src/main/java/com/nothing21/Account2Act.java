package com.nothing21;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nothing21.databinding.ActivityAccountTwoBinding;
import com.nothing21.retrofit.Constant;

public class Account2Act extends AppCompatActivity {
    ActivityAccountTwoBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_account_two);
        initViews();
    }

    private void initViews() {

        binding.ivBack.setOnClickListener(v -> finish());






        binding.tvOrderIssue.setOnClickListener(v ->
                startActivity(new Intent(Account2Act.this, SupportAct.class)
                        .putExtra("title",getString(R.string.order_issues))));

        binding.tvDelivery.setOnClickListener(v ->
                startActivity(new Intent(Account2Act.this, SupportAct.class)
                        .putExtra("title",getString(R.string.delivery))));


        binding.tvReturnRefund.setOnClickListener(v ->
                startActivity(new Intent(Account2Act.this, SupportAct.class)
                        .putExtra("title",getString(R.string.return_refund))));

        binding.tvPaymentPromos.setOnClickListener(v ->
                startActivity(new Intent(Account2Act.this, SupportAct.class)
                        .putExtra("title",getString(R.string.payment_promos))));

        binding.tvAddProduct.setOnClickListener(v ->
                startActivity(new Intent(Account2Act.this, SupportAct.class)
                        .putExtra("title",getString(R.string.add_product))));

        binding.tvStockAccount.setOnClickListener(v ->
                startActivity(new Intent(Account2Act.this, SupportAct.class)
                        .putExtra("title",getString(R.string.stock_account))));

        binding.tvHelp.setOnClickListener(v ->
                startActivity(new Intent(Account2Act.this, SupportAct2.class)));
    }

}
