package com.nothing21.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nothing21.LoginAct;
import com.nothing21.ProductSingalAct;
import com.nothing21.R;
import com.nothing21.adapter.SizeAdapter;
import com.nothing21.adapter.SizeAdapter1;
import com.nothing21.databinding.FragmentCartSheetBinding;
import com.nothing21.listener.InfoListener;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Constant;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragmentBootomSheet1 extends BottomSheetDialogFragment implements InfoListener , onIconClickListener {

    public String TAG = "CartFragmentBootomSheet";
    FragmentCartSheetBinding binding;
    BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;
    InfoListener listener;
    ProductModelCopy.Result productData;
    int count =1;
    double price =0.00,priceTol=0.00;
    Nothing21Interface apiInterface;
    String refreshedToken = "",userId="";
    boolean check = false , chkColor = false,chkSize=false;
    ArrayList<ProductModelCopy.Result.ColorDetail> colorArrayList;


    public CartFragmentBootomSheet1(ProductModelCopy.Result productData) {
        this.productData = productData;
    }


    public CartFragmentBootomSheet1 callBack(InfoListener listener) {
        this.listener = listener;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_cart_sheet, null, false);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initViews();
        return  dialog;

    }

    private void initViews() {
        colorArrayList = new ArrayList<>();
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                if(!SessionManager.readString(getActivity(), Constant.USER_INFO,"").equals("")) {
                    userId = DataManager.getInstance().getUserData(getActivity()).result.id+"";
                }
                else   userId = instanceIdResult.getToken();     //LogInAlert();

                refreshedToken = instanceIdResult.getToken();
                Log.e("Token===", userId);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        price = Double.parseDouble(productData.price);
        if(productData.colorDetails.size()!=0) {
            binding.tvColor.setText(productData.colorDetails.get(0).color);
            binding.tvSize.setText(productData.colorDetails.get(0).size);
        }
        priceCal(count);
        binding.BlurImageView.setBlur(2);
        //productData.imageDetails.get(0).image
        Log.e("Image Cart =====",SessionManager.readString(getActivity(),"selectImage",""));

        Glide.with(getActivity()).load(SessionManager.readString(getActivity(),"selectImage",""))
                .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                .into(binding.BlurImageView);

        colorArrayList.clear();
        colorArrayList.addAll(productData.colorDetails);
        binding.rvSize.setAdapter(new SizeAdapter1(getActivity(), colorArrayList,CartFragmentBootomSheet1.this));


        binding.BlurImageView.setBlur(10);

        setColorData(productData);

        binding.tvMinus.setOnClickListener(v -> {
          //  if(!SessionManager.readString(getActivity(), Constant.USER_INFO,"").equals("")) {
                if (count > 1) {
                    count = count - 1;
                    check = true;
                    priceCal(count);
              //  }
            }
          //  else LogInAlert();
        });

        binding.tvPlus.setOnClickListener(v -> {
           // if(!SessionManager.readString(getActivity(), Constant.USER_INFO,"").equals("")) {
                count = count + 1;
                check = true;
                priceCal(count);
         //   }

         //   else LogInAlert();

        });

        binding.ivCart.setOnClickListener(v -> dialog.dismiss());


        binding.ivColor1.setOnClickListener(v -> {
            new ColorSizeFragmentBottomSheet1(productData,binding.tvSize.getText().toString()).callBack(this::info).show(getActivity().getSupportFragmentManager(),"");
        });


        binding.ivSize.setOnClickListener(v -> {
            new SizeFragmentBottomSheet1(productData,binding.tvColor.getText().toString()).callBack(this::info).show(getActivity().getSupportFragmentManager(),"");
        });


        binding.tvAddCart.setOnClickListener(v -> {
            if(chkColor==false) Toast.makeText(getActivity(), getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
            if(chkSize==false) Toast.makeText(getActivity(), getString(R.string.please_select_size), Toast.LENGTH_SHORT).show();

            else {
                if (NetworkAvailablity.checkNetworkStatus(getActivity())) AddProductToCart11(userId);
                else
                    Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }
        });


        binding.layoutOne.setOnClickListener(v -> {
            for(int i =0; i<productData.colorDetails.size();i++){
                productData.colorDetails.get(i).setChkColor(false);
            }
            productData.colorDetails.get(0).setChkColor(true);
            setColorData(productData);
        });


        binding.layoutTwo.setOnClickListener(v -> {
            for(int i =0; i< productData.colorDetails.size();i++){
                productData.colorDetails.get(i).setChkColor(false);
            }
            productData.colorDetails.get(1).setChkColor(true);
            setColorData(productData);
        });


        binding.layoutThree.setOnClickListener(v -> {
            for(int i =0; i< productData.colorDetails.size();i++){
                productData.colorDetails.get(i).setChkColor(false);
            }
            productData.colorDetails.get(2).setChkColor(true);
            setColorData(productData);
        });


        binding.layoutFour.setOnClickListener(v -> {
            for(int i =0; i< productData.colorDetails.size();i++){
                productData.colorDetails.get(i).setChkColor(false);
            }
            productData.colorDetails.get(3).setChkColor(true);
            setColorData(productData);
        });



    }

    private void priceCal(int i){
        Log.e("price=====",i+"");
        priceTol = price * i ;
        binding.tvCart1.setText(count+"");
      //  binding.tvPri.setText("AED" + String.format("%.2f", priceTol));
      //  binding.tvPri.setText("AED" + String.format("%.2f", price));


        if(!productData.discount.equals("")) {
            binding.tvOldPrice.setVisibility(View.VISIBLE);
            //  binding.tvDiscount.setVisibility(View.VISIBLE);
            binding.tvPri.setText("AED" + String.format("%.2f", Double.parseDouble(productData.price) - Double.parseDouble(productData.discount )));
            binding.tvPri.setTextColor(getResources().getColor(R.color.color_red));
            binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(productData.price)));
            binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //  binding.tvDiscount.setText("-"+data.result.discount + "% Off");

        }
        else {
            binding.tvPri.setText("AED" + String.format("%.2f", Double.parseDouble(productData.price)));
            binding.tvPri.setTextColor(getResources().getColor(R.color.black));
            binding.tvOldPrice.setVisibility(View.GONE);
            //  binding.tvDiscount.setVisibility(View.GONE);

        }





       /* if(check == true) {
            if (NetworkAvailablity.checkNetworkStatus(getActivity())) AddProductToCart(userId);
            else
                Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }*/
    }

    public void AddProductToCart11(String userIdss){
        String imgName [] = SessionManager.readString(getActivity(),"selectImage","").split("/");
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userIdss);
        map.put("product_id",productData.id);
        map.put("quantity",count+"");
        map.put("color",SessionManager.readString(getActivity(),"selectColor",""));
        map.put("size",SessionManager.readString(getActivity(),"selectSize",""));
        map.put("image", imgName[6]);
        map.put("price",priceTol+"");
        Log.e(TAG, "Add to Cart Request :" + map);
        Call<Map<String,String>> loginCall = apiInterface.addToCart(map);
        loginCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Add to Cart Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        Toast.makeText(getActivity(), data.get("message"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (data.get("status").equals("0")){
                        Toast.makeText(getActivity(), data.get("message"), Toast.LENGTH_SHORT).show();
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


    /*public void AddProductToCart(String userIddd){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        MultipartBody.Part filePart;

        if (!SessionManager.readString(getActivity(),"selectImage","").equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(SessionManager.readString(getActivity(),"selectImage","")));
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody uId = RequestBody.create(MediaType.parse("text/plain"), userIddd);
        RequestBody ProId = RequestBody.create(MediaType.parse("text/plain"), productData.id);
        RequestBody counttt = RequestBody.create(MediaType.parse("text/plain"), count+"");
        RequestBody colorrr = RequestBody.create(MediaType.parse("text/plain"), binding.tvColor.getText().toString());
        RequestBody sizeee = RequestBody.create(MediaType.parse("text/plain"), binding.tvSize.getText().toString());


        Call<Map<String,String>> editNameCall = apiInterface.addToCart(uId,ProId,counttt,colorrr,sizeee, filePart);
        editNameCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Add to Cart Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        Toast.makeText(getActivity(), getString(R.string.item_added), Toast.LENGTH_SHORT).show();

                    } else if (data.get("status").equals("0")){
                        Toast.makeText(getActivity(), data.get("message"), Toast.LENGTH_SHORT).show();
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




    }*/


    public void LogInAlert(){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(getResources().getString(R.string.please_login));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Login",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        getActivity().startActivity(new Intent(getActivity(), LoginAct.class));
                        getActivity().finish();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void info(String value,String size) {
        if(!value.equals("")) {
            binding.tvColor.setText(value);
            binding.tvSize.setText(size);
        }
        else binding.tvColor.setText(productData.colorDetails.get(0).color);

        if(!size.equals(""))  {
            binding.tvColor.setText(value);
            binding.tvSize.setText(size);
        }
        else binding.tvSize.setText(productData.colorDetails.get(0).size);
    }

    private void setColorData(ProductModelCopy.Result data) {
        if(data.colorDetails!=null){
            if(data.colorDetails.size()==1){
                binding.layoutOne.setVisibility(View.VISIBLE);
                binding.layoutTwo.setVisibility(View.GONE);
                binding.layoutThree.setVisibility(View.GONE);
                binding.layoutFour.setVisibility(View.GONE);

                if(data.colorDetails.get(0).chkColor== false){
                    binding.view1.setVisibility(View.GONE);
                    binding.view11.setSolidColor(data.colorDetails.get(0).colorCode);
                  //  SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(0).color);
                    chkColor = false;

                }else {
                    binding.view1.setVisibility(View.VISIBLE);
                    binding.view1.setStrokeWidth(1);
                    binding.view1.setStrokeColor(data.colorDetails.get(0).colorCode);
                    binding.view11.setSolidColor(data.colorDetails.get(0).colorCode);
                    Glide.with(getActivity()).load(data.colorDetails.get(0).image)
                            .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                            .into(binding.BlurImageView);
                    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(0).color);
                    SessionManager.writeString(getActivity(),"selectImage",data.colorDetails.get(0).image);

                    binding.BlurImageView.setBlur(10);
                    chkColor = true;

                }
            }

            if(data.colorDetails.size()==2){
                binding.layoutOne.setVisibility(View.VISIBLE);
                binding.layoutTwo.setVisibility(View.VISIBLE);
                binding.layoutThree.setVisibility(View.GONE);
                binding.layoutFour.setVisibility(View.GONE);

                if(data.colorDetails.get(0).chkColor == false){
                    binding.view1.setVisibility(View.GONE);
                    binding.view11.setSolidColor(data.colorDetails.get(0).colorCode);
                 //   SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(0).color);
                    chkColor = false;

                }else {
                    binding.view1.setVisibility(View.VISIBLE);
                    binding.view1.setStrokeWidth(1);
                    binding.view1.setStrokeColor(data.colorDetails.get(0).colorCode);
                    binding.view1.setSolidColor("#FFFFFF");
                    binding.view11.setSolidColor(data.colorDetails.get(0).colorCode);
                    Glide.with(getActivity()).load(data.colorDetails.get(0).image)
                            .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                            .into(binding.BlurImageView);
                    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(0).color);
                    SessionManager.writeString(getActivity(),"selectImage",data.colorDetails.get(0).image);
                    chkColor = true;

                }


                if(data.colorDetails.get(1).chkColor == false){
                    binding.view2.setVisibility(View.GONE);
                    binding.view22.setSolidColor(data.colorDetails.get(1).colorCode);
                  //  SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(1).color);
                    chkColor = false;

                }else {
                    binding.view2.setVisibility(View.VISIBLE);
                    binding.view2.setStrokeWidth(1);
                    binding.view2.setStrokeColor(data.colorDetails.get(1).colorCode);
                    binding.view2.setSolidColor("#FFFFFF");
                    binding.view22.setSolidColor(data.colorDetails.get(1).colorCode);
                    Glide.with(getActivity()).load(data.colorDetails.get(1).image)
                            .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                            .into(binding.BlurImageView);
                    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(1).color);
                    SessionManager.writeString(getActivity(),"selectImage",data.colorDetails.get(1).image);
                    chkColor = true;

                }


            }


            if(data.colorDetails.size()==3){
                binding.layoutOne.setVisibility(View.VISIBLE);
                binding.layoutTwo.setVisibility(View.VISIBLE);
                binding.layoutThree.setVisibility(View.VISIBLE);
                binding.layoutFour.setVisibility(View.GONE);

                if(data.colorDetails.get(0).chkColor== false){
                    binding.view1.setVisibility(View.GONE);
                    binding.view11.setSolidColor(data.colorDetails.get(0).colorCode);
                  //  SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(0).color);
                    chkColor = false;
                }else {
                    binding.view1.setVisibility(View.VISIBLE);
                    binding.view1.setStrokeWidth(1);
                    binding.view1.setStrokeColor(data.colorDetails.get(0).colorCode);
                    binding.view1.setSolidColor("#FFFFFF");
                    binding.view11.setSolidColor(data.colorDetails.get(0).colorCode);
                    Glide.with(getActivity()).load(data.colorDetails.get(0).image)
                            .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                            .into(binding.BlurImageView);
                    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(0).color);
                    SessionManager.writeString(getActivity(),"selectImage",data.colorDetails.get(0).image);
                    chkColor = true;

                }


                if(data.colorDetails.get(1).chkColor== false){
                    binding.view2.setVisibility(View.GONE);
                    binding.view22.setSolidColor(data.colorDetails.get(1).colorCode);
                 //   SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(1).color);
                    chkColor = false;
                }else {
                    binding.view2.setVisibility(View.VISIBLE);
                    binding.view2.setStrokeWidth(1);
                    binding.view2.setStrokeColor(data.colorDetails.get(1).colorCode);
                    binding.view2.setSolidColor("#FFFFFF");
                    binding.view22.setSolidColor(data.colorDetails.get(1).colorCode);
                    Glide.with(getActivity()).load(data.colorDetails.get(1).image)
                            .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                            .into(binding.BlurImageView);
                    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(1).color);
                    SessionManager.writeString(getActivity(),"selectImage",data.colorDetails.get(1).image);
                    chkColor = true;

                }

                if(data.colorDetails.get(2).chkColor== false){
                    binding.view3.setVisibility(View.GONE);
                    binding.view33.setSolidColor(data.colorDetails.get(2).colorCode);
                //    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(2).color);
                    chkColor = false;
                }else {
                    binding.view3.setVisibility(View.VISIBLE);
                    binding.view3.setStrokeWidth(1);
                    binding.view3.setStrokeColor(data.colorDetails.get(2).colorCode);
                    binding.view3.setSolidColor("#FFFFFF");
                    binding.view33.setSolidColor(data.colorDetails.get(2).colorCode);
                    Glide.with(getActivity()).load(data.colorDetails.get(2).image)
                            .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                            .into(binding.BlurImageView);
                    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(2).color);
                    SessionManager.writeString(getActivity(),"selectImage",data.colorDetails.get(2).image);
                    chkColor = true;

                }





            }

            if(data.colorDetails.size()==4){
                binding.layoutOne.setVisibility(View.VISIBLE);
                binding.layoutTwo.setVisibility(View.VISIBLE);
                binding.layoutThree.setVisibility(View.VISIBLE);
                binding.layoutFour.setVisibility(View.VISIBLE);

                if(data.colorDetails.get(0).chkColor== false){
                    binding.view1.setVisibility(View.GONE);
                    binding.view11.setSolidColor(data.colorDetails.get(0).colorCode);
               //     SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(0).color);
                    chkColor = false;
                }else {
                    binding.view1.setVisibility(View.VISIBLE);
                    binding.view1.setStrokeWidth(1);
                    binding.view1.setStrokeColor(data.colorDetails.get(0).colorCode);
                    binding.view1.setSolidColor("#FFFFFF");
                    binding.view11.setSolidColor(data.colorDetails.get(0).colorCode);

                    Glide.with(getActivity()).load(data.colorDetails.get(0).image)
                            .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                            .into(binding.BlurImageView);
                    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(0).color);
                    SessionManager.writeString(getActivity(),"selectImage",data.colorDetails.get(0).image);
                    chkColor = true;
                }


                if(data.colorDetails.get(1).chkColor== false){
                    binding.view2.setVisibility(View.GONE);
                    binding.view22.setSolidColor(data.colorDetails.get(1).colorCode);
              //      SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(1).color);
                    chkColor = false;
                }else {
                    binding.view2.setVisibility(View.VISIBLE);
                    binding.view2.setStrokeWidth(1);
                    binding.view2.setStrokeColor(data.colorDetails.get(1).colorCode);
                    binding.view2.setSolidColor("#FFFFFF");
                    binding.view22.setSolidColor(data.colorDetails.get(1).colorCode);
                    Glide.with(getActivity()).load(data.colorDetails.get(1).image)
                            .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                            .into(binding.BlurImageView);
                    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(1).color);
                    SessionManager.writeString(getActivity(),"selectImage",data.colorDetails.get(1).image);
                    chkColor = true;

                }

                if(data.colorDetails.get(2).chkColor== false){
                    binding.view3.setVisibility(View.GONE);
                    binding.view33.setSolidColor(data.colorDetails.get(2).colorCode);
             //       SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(2).color);
                    chkColor = false;
                }else {
                    binding.view3.setVisibility(View.VISIBLE);
                    binding.view3.setStrokeWidth(1);
                    binding.view3.setStrokeColor(data.colorDetails.get(2).colorCode);
                    binding.view3.setSolidColor("#FFFFFF");
                    binding.view33.setSolidColor(data.colorDetails.get(2).colorCode);
                    Glide.with(getActivity()).load(data.colorDetails.get(2).image)
                            .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                            .into(binding.BlurImageView);
                    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(2).color);
                    SessionManager.writeString(getActivity(),"selectImage",data.colorDetails.get(2).image);
                    chkColor = true;

                }


                if(data.colorDetails.get(3).chkColor== false){
                    binding.view4.setVisibility(View.GONE);
                    binding.view44.setSolidColor(data.colorDetails.get(3).colorCode);
             //       SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(3).color);
                    chkColor = false;
                }else {
                    binding.view4.setVisibility(View.VISIBLE);
                    binding.view4.setStrokeWidth(1);
                    binding.view4.setStrokeColor(data.colorDetails.get(3).colorCode);
                    binding.view4.setSolidColor("#FFFFFF");
                    binding.view44.setSolidColor(data.colorDetails.get(3).colorCode);
                    Glide.with(getActivity()).load(data.colorDetails.get(3).image)
                            .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                            .into(binding.BlurImageView);
                    SessionManager.writeString(getActivity(),"selectColor",data.colorDetails.get(3).color);
                    SessionManager.writeString(getActivity(),"selectImage",data.colorDetails.get(3).image);
                    chkColor = true;
                }



            }




        }

    }


    @Override
    public void onIcon(int position, String type) {
        if(type.equals("size"))
           // SessionManager.writeString(getActivity(),"selectImage",colorArrayList.get(position).image);
        Log.e("selected Image===",colorArrayList.get(position).image);
            SessionManager.writeString(getActivity(),"selectSize",colorArrayList.get(position).size);
           chkSize = true;

    }



}