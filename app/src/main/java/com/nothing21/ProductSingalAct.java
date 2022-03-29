package com.nothing21;

import android.app.Activity;
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
import com.nothing21.listener.onItemClickListener;
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

public class ProductSingalAct extends AppCompatActivity implements InfoListener, onItemClickListener {
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

        adapter = new OtherProAdapter(ProductSingalAct.this,arrayList,ProductSingalAct.this);
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
                    if(NetworkAvailablity.checkNetworkStatus(ProductSingalAct.this))  GetProduct(product_id);
                    else Toast.makeText(ProductSingalAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();                }

                Log.e("Token===", userId);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
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


        binding.ivImg.setOnClickListener(v -> {
            if(binding.rvItems.getVisibility() == View.GONE){
                binding.rvItems.setVisibility(View.VISIBLE);
                binding.ivImg.setVisibility(View.GONE);

            }
        });


        binding.layoutOne.setOnClickListener(v -> {
            for(int i =0; i<data.result.colorDetails.size();i++){
               data.result.colorDetails.get(i).setChkColor(false);
            }
            data.result.colorDetails.get(0).setChkColor(true);
            setColorData(data);
        });


        binding.layoutTwo.setOnClickListener(v -> {
            for(int i =0; i< data.result.colorDetails.size();i++){
                data.result.colorDetails.get(i).setChkColor(false);
            }
            data.result.colorDetails.get(1).setChkColor(true);
            setColorData(data);
        });


        binding.layoutThree.setOnClickListener(v -> {
            for(int i =0; i< data.result.colorDetails.size();i++){
                data.result.colorDetails.get(i).setChkColor(false);
            }
            data.result.colorDetails.get(2).setChkColor(true);
            setColorData(data);
        });


        binding.layoutFour.setOnClickListener(v -> {
            for(int i =0; i< data.result.colorDetails.size();i++){
                data.result.colorDetails.get(i).setChkColor(false);
            }
            data.result.colorDetails.get(3).setChkColor(true);
            setColorData(data);
        });



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
                        setColorData(data);

                        if(data.result.fav_product_status.equals("false")) binding.ivLike.setImageDrawable(ProductSingalAct.this.getDrawable(R.drawable.ic_white_heart));
                        else binding.ivLike.setImageDrawable(ProductSingalAct.this.getDrawable(R.drawable.ic_red_heart));


                     //   binding.tvPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price)));
                        binding.tvProductName.setText(data.result.brand1);
                        if(!data.result.discount.equals("")) {
                            binding.tvOldPrice.setVisibility(View.VISIBLE);
                            //  binding.tvDiscount.setVisibility(View.VISIBLE);
                            binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price) - Double.parseDouble(data.result.discount )));
                            binding.tvProductPrice.setTextColor(getResources().getColor(R.color.color_red));
                            binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price)));
                            binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            //  binding.tvDiscount.setText("-"+data.result.discount + "% Off");

                        }
                        else {
                            binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price)));
                            binding.tvProductPrice.setTextColor(getResources().getColor(R.color.black));
                            binding.tvOldPrice.setVisibility(View.GONE);
                            //  binding.tvDiscount.setVisibility(View.GONE);

                        }

                        Glide.with(ProductSingalAct.this).load(data.result.colorDetails.get(0).image).error(R.drawable.dummy).into(binding.ivImg);

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

    private void setColorData(ProductModelCopy data) {
        if(data.result.colorDetails!=null){
            if(data.result.colorDetails.size()==1){
                binding.layoutOne.setVisibility(View.VISIBLE);
                binding.layoutTwo.setVisibility(View.GONE);
                binding.layoutThree.setVisibility(View.GONE);
                binding.layoutFour.setVisibility(View.GONE);

                if(data.result.colorDetails.get(0).chkColor== false){
                    binding.view1.setVisibility(View.GONE);
                    binding.view11.setSolidColor(data.result.colorDetails.get(0).colorCode);
                    binding.ivImg.setVisibility(View.VISIBLE);
                    binding.rvItems.setVisibility(View.GONE);

                }else {
                    binding.view1.setVisibility(View.VISIBLE);
                    binding.view1.setStrokeWidth(1);
                    binding.view1.setStrokeColor(data.result.colorDetails.get(0).colorCode);
                    binding.view11.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(0).colorCode);
                    binding.ivImg.setVisibility(View.VISIBLE);
                    binding.rvItems.setVisibility(View.GONE);
                    Glide.with(ProductSingalAct.this).load(data.result.colorDetails.get(0).image).error(R.drawable.dummy).into(binding.ivImg);

                }
            }

            if(data.result.colorDetails.size()==2){
                binding.layoutOne.setVisibility(View.VISIBLE);
                binding.layoutTwo.setVisibility(View.VISIBLE);
                binding.layoutThree.setVisibility(View.GONE);
                binding.layoutFour.setVisibility(View.GONE);

                if(ProductSingalAct.this.data.result.colorDetails.get(0).chkColor == false){
                    binding.view1.setVisibility(View.GONE);
                    binding.view11.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(0).colorCode);
                    binding.ivImg.setVisibility(View.VISIBLE);
                    binding.rvItems.setVisibility(View.GONE);

                }else {
                    binding.view1.setVisibility(View.VISIBLE);
                    binding.view1.setStrokeWidth(1);
                    binding.view1.setStrokeColor(ProductSingalAct.this.data.result.colorDetails.get(0).colorCode);
                    binding.view1.setSolidColor("#FFFFFF");
                    binding.view11.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(0).colorCode);
                    binding.ivImg.setVisibility(View.VISIBLE);
                    binding.rvItems.setVisibility(View.GONE);
                    Glide.with(ProductSingalAct.this).load(ProductSingalAct.this.data.result.colorDetails.get(0).image).error(R.drawable.dummy).into(binding.ivImg);
                }


                if(ProductSingalAct.this.data.result.colorDetails.get(1).chkColor == false){
                    binding.view2.setVisibility(View.GONE);
                    binding.view22.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(1).colorCode);
                    binding.ivImg.setVisibility(View.VISIBLE);
                    binding.rvItems.setVisibility(View.GONE);

                }else {
                    binding.view2.setVisibility(View.VISIBLE);
                    binding.view2.setStrokeWidth(1);
                    binding.view2.setStrokeColor(ProductSingalAct.this.data.result.colorDetails.get(1).colorCode);
                    binding.view2.setSolidColor("#FFFFFF");
                    binding.view22.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(1).colorCode);
                    binding.ivImg.setVisibility(View.VISIBLE);
                    binding.rvItems.setVisibility(View.GONE);
                    Glide.with(ProductSingalAct.this).load(ProductSingalAct.this.data.result.colorDetails.get(1).image).error(R.drawable.dummy).into(binding.ivImg);
                }


            }


            if(ProductSingalAct.this.data.result.colorDetails.size()==3){
                binding.layoutOne.setVisibility(View.VISIBLE);
                binding.layoutTwo.setVisibility(View.VISIBLE);
                binding.layoutThree.setVisibility(View.VISIBLE);
                binding.layoutFour.setVisibility(View.GONE);

                if(ProductSingalAct.this.data.result.colorDetails.get(0).chkColor== false){
                    binding.view1.setVisibility(View.GONE);
                    binding.view11.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(0).colorCode);

                }else {
                    binding.view1.setVisibility(View.VISIBLE);
                    binding.view1.setStrokeWidth(1);
                    binding.view1.setStrokeColor(ProductSingalAct.this.data.result.colorDetails.get(0).colorCode);
                    binding.view1.setSolidColor("#FFFFFF");
                    binding.view11.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(0).colorCode);
                    Glide.with(ProductSingalAct.this).load(ProductSingalAct.this.data.result.colorDetails.get(0).image).error(R.drawable.dummy).into(binding.ivImg);

                }


                if(ProductSingalAct.this.data.result.colorDetails.get(1).chkColor== false){
                    binding.view2.setVisibility(View.GONE);
                    binding.view22.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(1).colorCode);

                }else {
                    binding.view2.setVisibility(View.VISIBLE);
                    binding.view2.setStrokeWidth(1);
                    binding.view2.setStrokeColor(ProductSingalAct.this.data.result.colorDetails.get(1).colorCode);
                    binding.view2.setSolidColor("#FFFFFF");
                    binding.view22.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(1).colorCode);
                    Glide.with(ProductSingalAct.this).load(ProductSingalAct.this.data.result.colorDetails.get(1).image).error(R.drawable.dummy).into(binding.ivImg);

                }

                if(ProductSingalAct.this.data.result.colorDetails.get(2).chkColor== false){
                    binding.view3.setVisibility(View.GONE);
                    binding.view33.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(2).colorCode);

                }else {
                    binding.view3.setVisibility(View.VISIBLE);
                    binding.view3.setStrokeWidth(1);
                    binding.view3.setStrokeColor(ProductSingalAct.this.data.result.colorDetails.get(2).colorCode);
                    binding.view3.setSolidColor("#FFFFFF");
                    binding.view33.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(2).colorCode);
                    Glide.with(ProductSingalAct.this).load(ProductSingalAct.this.data.result.colorDetails.get(2).image).error(R.drawable.dummy).into(binding.ivImg);

                }





            }

            if(ProductSingalAct.this.data.result.colorDetails.size()==4){
                binding.layoutOne.setVisibility(View.VISIBLE);
                binding.layoutTwo.setVisibility(View.VISIBLE);
                binding.layoutThree.setVisibility(View.VISIBLE);
                binding.layoutFour.setVisibility(View.VISIBLE);

                if(ProductSingalAct.this.data.result.colorDetails.get(0).chkColor== false){
                    binding.view1.setVisibility(View.GONE);
                    binding.view11.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(0).colorCode);

                }else {
                    binding.view1.setVisibility(View.VISIBLE);
                    binding.view1.setStrokeWidth(1);
                    binding.view1.setStrokeColor(ProductSingalAct.this.data.result.colorDetails.get(0).colorCode);
                    binding.view1.setSolidColor("#FFFFFF");
                    binding.view11.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(0).colorCode);
                    Glide.with(ProductSingalAct.this).load(ProductSingalAct.this.data.result.colorDetails.get(0).image).error(R.drawable.dummy).into(binding.ivImg);

                }


                if(ProductSingalAct.this.data.result.colorDetails.get(1).chkColor== false){
                    binding.view2.setVisibility(View.GONE);
                    binding.view22.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(1).colorCode);

                }else {
                    binding.view2.setVisibility(View.VISIBLE);
                    binding.view2.setStrokeWidth(1);
                    binding.view2.setStrokeColor(ProductSingalAct.this.data.result.colorDetails.get(1).colorCode);
                    binding.view2.setSolidColor("#FFFFFF");
                    binding.view22.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(1).colorCode);
                    Glide.with(ProductSingalAct.this).load(ProductSingalAct.this.data.result.colorDetails.get(1).image).error(R.drawable.dummy).into(binding.ivImg);

                }

                if(ProductSingalAct.this.data.result.colorDetails.get(2).chkColor== false){
                    binding.view3.setVisibility(View.GONE);
                    binding.view33.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(2).colorCode);

                }else {
                    binding.view3.setVisibility(View.VISIBLE);
                    binding.view3.setStrokeWidth(1);
                    binding.view3.setStrokeColor(ProductSingalAct.this.data.result.colorDetails.get(2).colorCode);
                    binding.view3.setSolidColor("#FFFFFF");
                    binding.view33.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(2).colorCode);
                    Glide.with(ProductSingalAct.this).load(ProductSingalAct.this.data.result.colorDetails.get(2).image).error(R.drawable.dummy).into(binding.ivImg);

                }


                if(ProductSingalAct.this.data.result.colorDetails.get(3).chkColor== false){
                    binding.view4.setVisibility(View.GONE);
                    binding.view44.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(3).colorCode);

                }else {
                    binding.view4.setVisibility(View.VISIBLE);
                    binding.view4.setStrokeWidth(1);
                    binding.view4.setStrokeColor(ProductSingalAct.this.data.result.colorDetails.get(3).colorCode);
                    binding.view4.setSolidColor("#FFFFFF");
                    binding.view44.setSolidColor(ProductSingalAct.this.data.result.colorDetails.get(3).colorCode);
                    Glide.with(ProductSingalAct.this).load(ProductSingalAct.this.data.result.colorDetails.get(3).image).error(R.drawable.dummy).into(binding.ivImg);

                }



            }




        }

    }


    @Override
    public void info(String value,String size) {
    //    if(value.equals("color"))
      //  Glide.with(ProductSingalAct.this).load(SessionManager.readString(ProductSingalAct.this,"selectImage","")).error(R.drawable.dummy).into(binding.ivProduct);
    //    if(value.equals("size"))
     //       Glide.with(ProductSingalAct.this).load(SessionManager.readString(ProductSingalAct.this,"selectImage","")).error(R.drawable.dummy).into(binding.ivProduct);

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


    @Override
    public void onItem(int position) {
        product_id = arrayList.get(position).id;
      if(NetworkAvailablity.checkNetworkStatus(ProductSingalAct.this))  GetProduct(product_id);
      else Toast.makeText(ProductSingalAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }
}
