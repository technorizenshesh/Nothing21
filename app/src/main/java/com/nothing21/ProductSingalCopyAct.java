package com.nothing21;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nothing21.adapter.ColorAdapter;
import com.nothing21.adapter.ImageAdapter;
import com.nothing21.adapter.MyViewPagerAdapter;
import com.nothing21.adapter.OtherProAdapter;
import com.nothing21.adapter.SizeAdapter1;
import com.nothing21.databinding.ActivityProductSingalBinding;
import com.nothing21.databinding.ActivityProductSingalCopyBinding;
import com.nothing21.databinding.DialogFullscreenImageBinding;
import com.nothing21.fragment.CartFragmentBootomSheet1;
import com.nothing21.fragment.ColorSizeFragmentBottomSheet1;
import com.nothing21.fragment.InfoFragmentBottomSheet1;
import com.nothing21.fragment.RateBottomsheet;
import com.nothing21.listener.ImageListener;
import com.nothing21.listener.InfoListener;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.model.ProductModelCopyNew;
import com.nothing21.model.ProductNewModel;
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

public class ProductSingalCopyAct  extends AppCompatActivity implements InfoListener, onItemClickListener, onIconClickListener, ImageListener {
    public String TAG = "ProductSingalCopyAct";
    ActivityProductSingalCopyBinding binding;
    DialogFullscreenImageBinding dialogBinding;
    Nothing21Interface apiInterface;
    ArrayList<ProductModelCopyNew.Result.ColorDetail> imgArrayList;
    ArrayList<ProductNewModel.Result> arrayList;

    // https://nothing21.com/product?product_id=11
   // ProductModelCopy data;
    ProductModelCopyNew data;

    OtherProAdapter adapter;
    String product_id="";
    String refreshedToken = "",userId="",imgg="";
    ImageAdapter adapter11;
    boolean chk = false;
    ArrayList<ProductModelCopyNew.Result.ColorDetail>colorArrayList;
    ArrayList<ProductModelCopyNew.Result.ColorDetail.ColorVariation>sizeArrayList;
    SizeAdapter1 sizeAdapter;
    ColorAdapter colorAdapter;
    MyViewPagerAdapter myViewPagerAdapter;
    int colorPosition=0,variationPosition=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_product_singal_copy);
        initViews();
    }

    private void initViews() {
        imgArrayList = new ArrayList<>();
        colorArrayList = new ArrayList<>();
        sizeArrayList = new ArrayList<>();

        arrayList = new ArrayList<>();

        adapter = new OtherProAdapter(ProductSingalCopyAct.this,arrayList,ProductSingalCopyAct.this);
        binding.rvProduct.setAdapter(adapter);

        myViewPagerAdapter = new MyViewPagerAdapter(ProductSingalCopyAct.this,imgArrayList,ProductSingalCopyAct.this);
        binding.viewPager.setAdapter(myViewPagerAdapter);

        sizeAdapter = new SizeAdapter1(ProductSingalCopyAct.this, sizeArrayList,ProductSingalCopyAct.this);
        binding.rvSize.setAdapter(sizeAdapter);

        colorAdapter = new ColorAdapter(ProductSingalCopyAct.this, colorArrayList,ProductSingalCopyAct.this);
        binding.rvColor.setAdapter(colorAdapter);



        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                if(!SessionManager.readString(ProductSingalCopyAct.this, Constant.USER_INFO,"").equals("")) {
                    userId = DataManager.getInstance().getUserData(ProductSingalCopyAct.this).result.id;
                }
                else   userId = instanceIdResult.getToken();     //LogInAlert();

                refreshedToken = instanceIdResult.getToken();


                if(getIntent()!=null) {
                    if(getIntent().getData().getScheme().equals("https")){
                        Intent appLinkIntent = getIntent();
                        Uri appLinkData = appLinkIntent.getData();
                        product_id = appLinkData.getQueryParameter("product_id");
                        Log.e("This is DeepLink==", String.valueOf(appLinkData));

                    }
                    else {
                        product_id = getIntent().getStringExtra("id");
                        Log.e("Not DeepLink==",product_id);
                    }
                    if(NetworkAvailablity.checkNetworkStatus(ProductSingalCopyAct.this))  GetProduct(product_id);
                    else Toast.makeText(ProductSingalCopyAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();                }

                Log.e("Token===", userId);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
                product_id = getIntent().getStringExtra("id");
                Log.e("Not DeepLink==",product_id);
                if(NetworkAvailablity.checkNetworkStatus(ProductSingalCopyAct.this))  GetProduct(product_id);
                else Toast.makeText(ProductSingalCopyAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }
        });


        binding.ivInfo.setOnClickListener(v -> {
            if(!data.result.description.equals("")) {
                if(imgg.equals("")) imgg = colorArrayList.get(0).image;
                new InfoFragmentBottomSheet1(data.result, imgg).callBack(this::info).show(getSupportFragmentManager(), "");
            }
            else   Toast.makeText(ProductSingalCopyAct.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        });

        binding.layoutRatting.setOnClickListener(v -> {
            if(!data.result.description.equals("")) {
                if (imgg.equals("")) imgg = colorArrayList.get(0).image;
                new RateBottomsheet(data.result, imgg).callBack(this::info).show(getSupportFragmentManager(), "");
            }
            else   Toast.makeText(ProductSingalCopyAct.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        });



        binding.ivCart.setOnClickListener(v -> {
            if(data.result.colorDetails.size()!=0) {
               if(imgg.equals("")) imgg = colorArrayList.get(colorPosition).image;
                SessionManager.writeString(ProductSingalCopyAct.this,"avaQuantity",colorArrayList.get(colorPosition).colorVariation.get(variationPosition).remainingQuantity+"");
                SessionManager.writeString(ProductSingalCopyAct.this,"colorDetailsId",colorArrayList.get(colorPosition).colorId);
                new CartFragmentBootomSheet1(data.result,imgg).callBack(this::info).show(getSupportFragmentManager(),"");
            }
            else Toast.makeText(ProductSingalCopyAct.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        });

       /* binding.ivColor.setOnClickListener(v -> {
            if(data.result.colorDetails.size()!=0) {
                // SessionManager.writeString(ProductSingalCopyAct.this,"selectImage",data.result.colorDetails.get(0).image);
                new ColorSizeFragmentBottomSheet1(data.result,data.result.colorDetails.get(0).colorVariation.get(0).size).callBack(this::info).show(getSupportFragmentManager(),"");
            }
            else Toast.makeText(ProductSingalCopyAct.this, getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        });*/


        binding.ivLike.setOnClickListener(v -> {
            if(NetworkAvailablity.checkNetworkStatus(ProductSingalCopyAct.this))addFavrirr(product_id);
            else Toast.makeText(ProductSingalCopyAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        });


        binding.ivShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://nothing21.com/product?product_id=" + product_id);
            startActivity(Intent.createChooser(shareIntent, "Share link using"));
        });

    }



    /*public void GetProduct(String productId){
        DataManager.getInstance().showProgressMessage(ProductSingalCopyAct.this, getString(R.string.please_wait));
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
                        colorArrayList.clear();
                        colorArrayList.addAll(data.result.colorDetails);
                        imgArrayList.addAll(data.result.colorDetails);

                        if(data.result.fav_product_status.equals("false")) binding.ivLike.setImageDrawable(ProductSingalCopyAct.this.getDrawable(R.drawable.ic_white_heart));
                        else binding.ivLike.setImageDrawable(ProductSingalCopyAct.this.getDrawable(R.drawable.ic_red_heart));


                        //   binding.tvPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price)));
                        binding.tvProName.setText(data.result.name);
                        if(!data.result.discountedPrice.equals("0")) {
                            binding.tvOldPrice.setVisibility(View.VISIBLE);
                            //  binding.tvDiscount.setVisibility(View.VISIBLE);
                            binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price) - Double.parseDouble(data.result.discountedPrice )));
                            binding.tvProductPrice.setTextColor(getResources().getColor(R.color.color_red));
                            binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price)));
                            binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            //  binding.tvDiscount.setText("-"+data.result.discountedPrice + "% Off");

                        }
                        else {
                            binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price)));
                            binding.tvProductPrice.setTextColor(getResources().getColor(R.color.black));
                            binding.tvOldPrice.setVisibility(View.GONE);
                            //  binding.tvDiscount.setVisibility(View.GONE);

                        }

                        binding.viewPager.setAdapter(new MyViewPagerAdapter(ProductSingalCopyAct.this,imgArrayList,ProductSingalCopyAct.this));
                        binding.rvSize.setAdapter(new SizeAdapter1(ProductSingalCopyAct.this, colorArrayList,ProductSingalCopyAct.this));
                        binding.rvColor.setAdapter(new ColorAdapter(ProductSingalCopyAct.this, colorArrayList,ProductSingalCopyAct.this));
                        SessionManager.writeString(ProductSingalCopyAct.this,"selectImage",data.result.colorDetails.get(0).image);
                        imgg = data.result.colorDetails.get(0).image;


                       *//* if(!data.result.discount.equals("")) {
                            binding.tvProductName.setVisibility(View.VISIBLE);
                            binding.tvOffer.setText(data.result.discount + "% Off");
                        }
                        else binding.tvProductName.setVisibility(View.GONE);*//*


                        if(NetworkAvailablity.checkNetworkStatus(ProductSingalCopyAct.this))    getProduct(product_id);
                        else Toast.makeText(ProductSingalCopyAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

                    } else if (data.status.equals("0")){
                        imgArrayList.clear();
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
    }*/



    public void GetProduct(String productId){
        DataManager.getInstance().showProgressMessage(ProductSingalCopyAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("product_id",productId);
        map.put("user_id",userId);
        Call<ProductModelCopyNew> loginCall = apiInterface.getProductNew(map);
        loginCall.enqueue(new Callback<ProductModelCopyNew>() {
            @Override
            public void onResponse(Call<ProductModelCopyNew> call, Response<ProductModelCopyNew> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.status.equals("1")) {
                        imgArrayList.clear();
                        colorArrayList.clear();
                        sizeArrayList.clear();

                        colorArrayList.addAll(data.result.colorDetails);
                        imgArrayList.addAll(data.result.colorDetails);
                        sizeArrayList.addAll(data.result.colorDetails.get(0).colorVariation);
                        myViewPagerAdapter.notifyDataSetChanged();
                        colorAdapter.notifyDataSetChanged();
                        sizeAdapter.notifyDataSetChanged();

                        if(data.result.favProductStatus.equals("false")) binding.ivLike.setImageDrawable(ProductSingalCopyAct.this.getDrawable(R.drawable.ic_white_heart));
                        else binding.ivLike.setImageDrawable(ProductSingalCopyAct.this.getDrawable(R.drawable.ic_red_heart));
                        binding.RatingBar.setRating(Float.parseFloat(data.result.ratingByAdmin));
                        binding.viewRate.setText(" ("+data.result.ratingByAdmin + ")");

                        //   binding.tvPrice.setText("AED" + String.format("%.2f", Double.parseDouble(data.result.price)));
                        binding.tvProName.setText(data.result.name);
                        setItemValue(sizeArrayList);


                       // binding.viewPager.setAdapter(new MyViewPagerAdapter(ProductSingalCopyAct.this,imgArrayList,ProductSingalCopyAct.this));
                       // binding.rvSize.setAdapter(new SizeAdapter1(ProductSingalCopyAct.this, colorArrayList,ProductSingalCopyAct.this));
                       // binding.rvColor.setAdapter(new ColorAdapter(ProductSingalCopyAct.this, colorArrayList,ProductSingalCopyAct.this));
                        SessionManager.writeString(ProductSingalCopyAct.this,"selectImage",data.result.colorDetails.get(0).image);
                        imgg = data.result.colorDetails.get(0).image;


                       /* if(!data.result.discount.equals("")) {
                            binding.tvProductName.setVisibility(View.VISIBLE);
                            binding.tvOffer.setText(data.result.discount + "% Off");
                        }
                        else binding.tvProductName.setVisibility(View.GONE);*/


                        if(NetworkAvailablity.checkNetworkStatus(ProductSingalCopyAct.this))    getProduct(product_id);
                        else Toast.makeText(ProductSingalCopyAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

                    } else if (data.status.equals("0")){
                        imgArrayList.clear();
                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductModelCopyNew> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    private void setItemValue(ArrayList<ProductModelCopyNew.Result.ColorDetail.ColorVariation> sizeArrayList) {

        if(!sizeArrayList.get(variationPosition).priceDiscount.equals("0")) {
            binding.tvOldPrice.setVisibility(View.VISIBLE);
            //  binding.tvDiscount.setVisibility(View.VISIBLE);
            binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(sizeArrayList.get(variationPosition).priceCalculated)));
            binding.tvProductPrice.setTextColor(getResources().getColor(R.color.color_red));
            binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(sizeArrayList.get(variationPosition).price)));
            binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //  binding.tvDiscount.setText("-"+data.result.discountedPrice + "% Off");

        }
        else {
            binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(sizeArrayList.get(variationPosition).price)));
            binding.tvProductPrice.setTextColor(getResources().getColor(R.color.black));
            binding.tvOldPrice.setVisibility(View.GONE);
            //  binding.tvDiscount.setVisibility(View.GONE);

        }
    }


    @Override
    public void info(String value,String size) {
        //    if(value.equals("color"))
        //  Glide.with(ProductSingalCopyAct.this).load(SessionManager.readString(ProductSingalCopyAct.this,"selectImage","")).error(R.drawable.dummy).into(binding.ivProduct);
        //    if(value.equals("size"))
        //       Glide.with(ProductSingalCopyAct.this).load(SessionManager.readString(ProductSingalCopyAct.this,"selectImage","")).error(R.drawable.dummy).into(binding.ivProduct);
        //    SessionManager.writeString(ProductSingalCopyAct.this,"selectSize",colorArrayList.get(position).size);

    }



    public void addFavrirr(String proId){
        DataManager.getInstance().showProgressMessage(ProductSingalCopyAct.this, getString(R.string.please_wait));
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

                        if(NetworkAvailablity.checkNetworkStatus(ProductSingalCopyAct.this)) GetProduct(proId);
                        else Toast.makeText(ProductSingalCopyAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


                    } else if (data.get("status").equals("0")){

                        if(NetworkAvailablity.checkNetworkStatus(ProductSingalCopyAct.this)) GetProduct(proId);
                        else Toast.makeText(ProductSingalCopyAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
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
        DataManager.getInstance().showProgressMessage(ProductSingalCopyAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("product_id",proId);
        Call<ProductNewModel> loginCall = apiInterface.getOtherProduct(map);
        loginCall.enqueue(new Callback<ProductNewModel>() {
            @Override
            public void onResponse(Call<ProductNewModel> call, Response<ProductNewModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    ProductNewModel data = response.body();
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
            public void onFailure(Call<ProductNewModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }


    @Override
    public void onItem(int position) {
        product_id = arrayList.get(position).id;
        if(NetworkAvailablity.checkNetworkStatus(ProductSingalCopyAct.this))  GetProduct(product_id);
        else Toast.makeText(ProductSingalCopyAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }






    @Override
    public void onIcon(int position, String type) {
        if(type.equals("size")) {
            SessionManager.writeString(ProductSingalCopyAct.this, "selectSize", sizeArrayList.get(position).size);
            SessionManager.writeString(ProductSingalCopyAct.this,"avaQuantity",sizeArrayList.get(position).remainingQuantity+"");
            variationPosition = position;
            setItemValue(sizeArrayList);
        }
        else if(type.equals("color")) {
            next(position);
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
      //  startActivity(new Intent(ProductSingalCopyAct.this,HomeAct.class)
               // .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    public void next(int ii) {
        Log.e("slideCurrent==" + ii,binding.viewPager.getCurrentItem()+"");

        imgg = colorArrayList.get(ii).image;
        sizeArrayList.clear();
        sizeArrayList.addAll(colorArrayList.get(ii).colorVariation);
        sizeAdapter.notifyDataSetChanged();

        SessionManager.writeString(ProductSingalCopyAct.this,"selectImage",colorArrayList.get(ii).image);
        SessionManager.writeString(ProductSingalCopyAct.this,"colorDetailsId",colorArrayList.get(ii).colorId);
        setItemValue(sizeArrayList);
        binding.viewPager.setCurrentItem(ii);
        colorPosition = ii;
            Log.e("slideCurrentImage=="+ ii,SessionManager.readString(ProductSingalCopyAct.this,"selectImage",""));
        Log.e("slideCurrentImage=="+ ii,imgg);

    }

    private void openFullScreenImageDialog(Context context, String image) {

        Dialog dialogFullscreen    = new Dialog(context, WindowManager.LayoutParams.MATCH_PARENT);
        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_fullscreen_image, null, false);
        dialogFullscreen.setContentView(dialogBinding.getRoot());

        Glide.with(context).load(image).into(dialogBinding.imageViewMain);


        dialogFullscreen.show();

    }

    @Override
    public void onImage(int position) {
        openFullScreenImageDialog(ProductSingalCopyAct.this,imgArrayList.get(position).image);
    }
}

