package com.nothing21;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nothing21.adapter.ImageAdapter;
import com.nothing21.adapter.OtherProAdapter;
import com.nothing21.databinding.ActivityProductBinding;
import com.nothing21.databinding.ActivityProductSingalBinding;
import com.nothing21.fragment.CartFragmentBootomSheet;
import com.nothing21.fragment.CartFragmentBootomSheet1;
import com.nothing21.fragment.ColorSizeFragmentBottomSheet;
import com.nothing21.fragment.ColorSizeFragmentBottomSheet1;
import com.nothing21.fragment.InfoFragmentBottomSheet;
import com.nothing21.fragment.InfoFragmentBottomSheet1;
import com.nothing21.fragment.SizeFragmentBottomSheet1;
import com.nothing21.listener.InfoListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Constant;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductSingalAct extends AppCompatActivity implements InfoListener {
    public String TAG = "ProductSingalAct";
    ActivityProductSingalBinding binding;
    Nothing21Interface apiInterface;
    ArrayList<ProductModelCopy.Result.ColorDetail> imgArrayList;
    ArrayList<ProductModel.Result> arrayList;

    ProductModelCopy data;
    OtherProAdapter adapter;
    String product_id="";
    String refreshedToken = "",userId="";
    ImageAdapter adapter11;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_product_singal);
        initViews();
    }

    private void initViews() {
        imgArrayList = new ArrayList<>();
        arrayList = new ArrayList<>();

        adapter = new OtherProAdapter(ProductSingalAct.this,arrayList);
        binding.rvProduct.setAdapter(adapter);



        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                if(!SessionManager.readString(ProductSingalAct.this, Constant.USER_INFO,"").equals("")) {
                    userId = DataManager.getInstance().getUserData(ProductSingalAct.this).result.id;
                }
                else   userId = instanceIdResult.getToken();     //LogInAlert();

                refreshedToken = instanceIdResult.getToken();


                if(getIntent()!=null) {
                    product_id = getIntent().getStringExtra("id");
                    GetProduct(product_id);
                }

                Log.e("Token===", userId);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });





       binding.icOne.setOnClickListener(v -> {
           Glide.with(ProductSingalAct.this).load(imgArrayList.get(0).image).error(R.drawable.dummy).into(binding.ivProduct);

       });

        binding.icTwo.setOnClickListener(v -> {
            Glide.with(ProductSingalAct.this).load(imgArrayList.get(1).image).error(R.drawable.dummy).into(binding.ivProduct);

        });

        binding.icThree.setOnClickListener(v -> {
            Glide.with(ProductSingalAct.this).load(imgArrayList.get(2).image).error(R.drawable.dummy).into(binding.ivProduct);

        });

        binding.icFour.setOnClickListener(v -> {
            Glide.with(ProductSingalAct.this).load(imgArrayList.get(3).image).error(R.drawable.dummy).into(binding.ivProduct);

        });

        binding.ivInfo.setOnClickListener(v -> {
            if(!data.result.description.equals(""))
                new InfoFragmentBottomSheet1(data.result).callBack(this::info).show(getSupportFragmentManager(),"");
            else   Toast.makeText(ProductSingalAct.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        });

        binding.ivCart.setOnClickListener(v -> {
            if(data.result.colorDetails.size()!=0) {
                SessionManager.writeString(ProductSingalAct.this,"selectImage",data.result.colorDetails.get(0).image);

                new CartFragmentBootomSheet1(data.result).callBack(this::info).show(getSupportFragmentManager(),"");
            }
            else Toast.makeText(ProductSingalAct.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        });

        binding.ivColor.setOnClickListener(v -> {
            if(data.result.colorDetails.size()!=0) {
                SessionManager.writeString(ProductSingalAct.this,"selectImage",data.result.colorDetails.get(0).image);
                new ColorSizeFragmentBottomSheet1(data.result,data.result.colorDetails.get(0).size).callBack(this::info).show(getSupportFragmentManager(),"");
            }
            else Toast.makeText(ProductSingalAct.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        });

        binding.ivIn.setOnClickListener(v -> {
            if(data.result.colorDetails.size()!=0) {
                SessionManager.writeString(ProductSingalAct.this,"selectImage",data.result.colorDetails.get(0).image);
                new SizeFragmentBottomSheet1(data.result,data.result.colorDetails.get(0).color).callBack(this::info).show(getSupportFragmentManager(),"");
            }
            else Toast.makeText(ProductSingalAct.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        });



        binding.ivLike.setOnClickListener(v -> {
            if(NetworkAvailablity.checkNetworkStatus(ProductSingalAct.this))addFavrirr(product_id);
                else Toast.makeText(ProductSingalAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        });



        adapter11 = new ImageAdapter(ProductSingalAct.this,imgArrayList);
        binding.rvItems.setAdapter(adapter11);


    }



    public void GetProduct(String productId){
        DataManager.getInstance().showProgressMessage(ProductSingalAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("product_id",productId);
        map.put("user_id",userId);
        Call<ProductModelCopy> loginCall = apiInterface.getProduct(map);
        loginCall.enqueue(new Callback<ProductModelCopy>() {
            @Override
            public void onResponse(Call<ProductModelCopy> call, Response<ProductModelCopy> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.status.equals("1")) {
                        imgArrayList.clear();
                        imgArrayList.addAll(data.result.colorDetails);
                       /* Glide.with(ProductSingalAct.this).load(data.result.imageDetails.get(0).image).error(R.drawable.dummy).into(binding.ivProduct);
                        Glide.with(ProductSingalAct.this).load(data.result.imageDetails.get(0).image).error(R.drawable.dummy).into(binding.icOne);
                        Glide.with(ProductSingalAct.this).load(data.result.imageDetails.get(1).image).error(R.drawable.dummy).into(binding.icTwo);
                        Glide.with(ProductSingalAct.this).load(data.result.imageDetails.get(2).image).error(R.drawable.dummy).into(binding.icThree);
                        Glide.with(ProductSingalAct.this).load(data.result.imageDetails.get(3).image).error(R.drawable.dummy).into(binding.icFour);*/

                        adapter11.notifyDataSetChanged();


                        if(data.result.fav_product_status.equals("false")) binding.ivLike.setImageDrawable(ProductSingalAct.this.getDrawable(R.drawable.ic_white_heart));
                        else binding.ivLike.setImageDrawable(ProductSingalAct.this.getDrawable(R.drawable.ic_red_heart));


                     //   binding.tvPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price)));
                        binding.tvProductName.setText(data.result.brand1);
                        if(!data.result.discount.equals("")) {
                            binding.tvOldPrice.setVisibility(View.VISIBLE);
                            //  holder.binding.tvDiscount.setVisibility(View.VISIBLE);
                            binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price) - Double.parseDouble(data.result.discount )));
                            binding.tvProductPrice.setTextColor(getResources().getColor(R.color.color_red));
                            binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price)));
                            binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            //  holder.binding.tvDiscount.setText("-"+arrayList.get(position).discount + "% Off");

                        }
                        else {
                            binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price)));
                            binding.tvProductPrice.setTextColor(getResources().getColor(R.color.black));
                            binding.tvOldPrice.setVisibility(View.GONE);
                            //  holder.binding.tvDiscount.setVisibility(View.GONE);

                        }


                       /* if(!data.result.discount.equals("")) {
                            binding.tvProductName.setVisibility(View.VISIBLE);
                            binding.tvOffer.setText(data.result.discount + "% Off");
                        }
                        else binding.tvProductName.setVisibility(View.GONE);*/


                    if(NetworkAvailablity.checkNetworkStatus(ProductSingalAct.this))    getProduct(product_id);
                     else Toast.makeText(ProductSingalAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

                    } else if (data.status.equals("0")){
                        // Toast.makeText(ProductAct.this, data.message, Toast.LENGTH_SHORT).show();
                        imgArrayList.clear();
                        adapter11.notifyDataSetChanged();
                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductModelCopy> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }


    @Override
    public void info(String value,String size) {
        if(value.equals("color"))
        Glide.with(ProductSingalAct.this).load(SessionManager.readString(ProductSingalAct.this,"selectImage","")).error(R.drawable.dummy).into(binding.ivProduct);
        if(value.equals("size"))
            Glide.with(ProductSingalAct.this).load(SessionManager.readString(ProductSingalAct.this,"selectImage","")).error(R.drawable.dummy).into(binding.ivProduct);

    }



    public void addFavrirr(String proId){
        DataManager.getInstance().showProgressMessage(ProductSingalAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("product_id",proId);
        Call<Map<String,String>> loginCall = apiInterface.addFav(map);
        loginCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.get("status").equals("1")) {

                        if(NetworkAvailablity.checkNetworkStatus(ProductSingalAct.this)) GetProduct(proId);
                        else Toast.makeText(ProductSingalAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


                    } else if (data.get("status").equals("0")){

                        if(NetworkAvailablity.checkNetworkStatus(ProductSingalAct.this)) GetProduct(proId);
                        else Toast.makeText(ProductSingalAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    }

                    // serviceAdapter.notifyDataSetChanged();

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


    public void getProduct(String proId){
        DataManager.getInstance().showProgressMessage(ProductSingalAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("product_id",proId);
        Call<ProductModel> loginCall = apiInterface.getOtherProduct(map);
        loginCall.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    ProductModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.status.equals("1")) {
                       arrayList.clear();
                       binding.viqq.setVisibility(View.VISIBLE);
                       arrayList.addAll(data.result);
                       adapter.notifyDataSetChanged();


                    } else if (data.status.equals("0")){
                        binding.viqq.setVisibility(View.GONE);
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }


}
