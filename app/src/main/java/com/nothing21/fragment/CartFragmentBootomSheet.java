package com.nothing21.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
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
import com.nothing21.R;
import com.nothing21.adapter.ColorCartAdapter;
import com.nothing21.adapter.SizeAdapter;
import com.nothing21.adapter.SizeAdapter1;
import com.nothing21.databinding.FragmentCartSheetBinding;
import com.nothing21.listener.InfoListener;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.ProductModel;
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

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragmentBootomSheet extends BottomSheetDialogFragment implements InfoListener, onIconClickListener {

    public String TAG = "CartFragmentBootomSheet";
    FragmentCartSheetBinding binding;
    BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;
    InfoListener listener;
    ProductNewModel.Result productData;
    int count = 1, pos,colorPosition=0,variationPosition=0;
    double price = 0.00, priceTol = 0.00;
    Nothing21Interface apiInterface;
    String refreshedToken = "", userId = "",avaQnty="";
    boolean check = false, chkColor = true, chkSize = false;
    ArrayList<ProductNewModel.Result.ColorDetail> colorArrayList;
    ArrayList<ProductNewModel.Result.ColorDetail.ColorVariation> sizeArrayList;


    SizeAdapter sizeAdapter;
    ColorCartAdapter colorCartAdapter;

    public CartFragmentBootomSheet(ProductNewModel.Result productData) {
        this.productData = productData;
    }


    public CartFragmentBootomSheet callBack(InfoListener listener) {
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
        return dialog;
    }

    private void initViews() {
        colorArrayList = new ArrayList<>();
        sizeArrayList = new ArrayList<>();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                if (!SessionManager.readString(getActivity(), Constant.USER_INFO, "").equals("")) {
                    userId = DataManager.getInstance().getUserData(getActivity()).result.id + "";
                } else userId = instanceIdResult.getToken();     //LogInAlert();

                refreshedToken = instanceIdResult.getToken();
                Log.e("Token===", userId);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        priceCal(count,colorPosition,variationPosition);

        for (int i = 0; i < productData.colorDetails.size(); i++) {
            if (productData.colorDetails.get(i).isChkColor() == true) {
                SessionManager.writeString(getActivity(), "selectImage", productData.colorDetails.get(i).image);
                binding.BlurImageView.setBlur(10);
                Glide.with(getActivity()).load(productData.colorDetails.get(i).image)
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                        .into(binding.BlurImageView);
            }
        }



        colorArrayList.clear();
        colorArrayList.addAll(productData.colorDetails);
        sizeArrayList.addAll(productData.colorDetails.get(0).colorVariation);
        avaQnty = colorArrayList.get(0).colorVariation.get(0).remainingQuantity+"";


        sizeAdapter = new SizeAdapter(getActivity(), sizeArrayList,CartFragmentBootomSheet.this);
        binding.rvSize.setAdapter(sizeAdapter);

        colorCartAdapter = new ColorCartAdapter(getActivity(), colorArrayList, CartFragmentBootomSheet.this);
        binding.rvColor.setAdapter(colorCartAdapter);

        binding.tvMinus.setOnClickListener(v -> {
            if (count > 1) {
                count = count - 1;
                check = true;
                priceCal(count,colorPosition,variationPosition);
            }
        });

        binding.tvPlus.setOnClickListener(v -> {
         //   if (chkColor == false)
          //      Toast.makeText(getActivity(), getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
          //  else {
                    count = count + 1;
                    check = true;
               /// if (Integer.parseInt(productData.colorDetails.get(pos).remainingQuantity) > count) {
            priceCal(count,colorPosition,variationPosition);
            //    }
             //   else{
             //       Toast.makeText(getActivity(), getString(R.string.product__out_of_stock), Toast.LENGTH_SHORT).show();
            //        count = 1;

            //    }
         //   }


        });

        binding.ivCart.setOnClickListener(v -> dialog.dismiss());

        binding.tvAddCart.setOnClickListener(v -> {
            if (chkColor == false)
                Toast.makeText(getActivity(), getString(R.string.please_select_color), Toast.LENGTH_SHORT).show();
            else if (chkSize == false)
                Toast.makeText(getActivity(), getString(R.string.please_select_size), Toast.LENGTH_SHORT).show();

            else {

                if (Integer.parseInt(avaQnty) >= count) {

                    if (NetworkAvailablity.checkNetworkStatus(getActivity()))
                        AddProductToCart11(userId);
                    else
                        Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getActivity(), getString(R.string.product__out_of_stock), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < colorArrayList.size(); i++) {
                colorArrayList.get(i).setChkColor(false);
                    }
                    for (int i = 0; i < sizeArrayList.size(); i++) {
                        sizeArrayList.get(i).setChkColor(false);
                    }
                    sizeAdapter.notifyDataSetChanged();
                    colorCartAdapter.notifyDataSetChanged();

                    chkColor = false;
                    chkSize = false;
                    SessionManager.writeString(getActivity(), "selectImage", "");
                    SessionManager.writeString(getActivity(), "selectColor", "");
                    SessionManager.writeString(getActivity(), "colorDetailsId", "");
                    SessionManager.writeString(getActivity(),"avaQuantity","");
                    SessionManager.writeString(getActivity(), "selectSize", "");
                    SessionManager.writeString(getActivity(), "selectVariationId", "");
                }

            }
        });


    }

    private void priceCal(int i,int colorPosition,int variationPosition) {
        //  binding.tvPri.setText("AED" + String.format("%.2f", priceTol));
        binding.tvPri.setText("AED" + String.format("%.2f", price));
        if(!productData.colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceDiscount.equals("0")) {
            binding.tvOldPrice.setVisibility(View.VISIBLE);
            //  binding.tvDiscount.setVisibility(View.VISIBLE);
            binding.tvPri.setText("AED" + String.format("%.2f", Double.parseDouble(productData.colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceCalculated)));
            binding.tvPri.setTextColor(getResources().getColor(R.color.color_red));
            binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(productData.colorDetails.get(colorPosition).colorVariation.get(variationPosition).price)));
            binding.tvOldPrice.setPaintFlags(binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //  binding.tvDiscount.setText("-"+data.result.discount + "% Off");
            price = Double.parseDouble(productData.colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceCalculated);

        }
        else {
            binding.tvPri.setText("AED" + String.format("%.2f", Double.parseDouble(productData.colorDetails.get(colorPosition).colorVariation.get(variationPosition).price)));
            binding.tvPri.setTextColor(getResources().getColor(R.color.black));
            binding.tvOldPrice.setVisibility(View.GONE);
            //  binding.tvDiscount.setVisibility(View.GONE);
            price = Double.parseDouble(productData.colorDetails.get(colorPosition).colorVariation.get(variationPosition).price);
        }
        Log.e("price=====", i + "");
        priceTol = price;
        binding.tvCart1.setText(count + "");


    }

    public void AddProductToCart11(String userIddd) {
        String imgName[] = null;
        for (int i = 0; i < colorArrayList.size(); i++) {
            if (colorArrayList.get(i).isChkColor() == true)
                imgName = colorArrayList.get(i).image.split("/");
        }
        // String imgName [] = SessionManager.readString(getActivity(),"selectImage","").split("/");
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userIddd);
        map.put("product_id", productData.id);
        map.put("quantity", count + "");
        map.put("color", SessionManager.readString(getActivity(), "selectColor", ""));
        map.put("size", SessionManager.readString(getActivity(), "selectSize", ""));
        map.put("color_id", SessionManager.readString(getActivity(), "colorDetailsId", ""));
        map.put("image", imgName[6]);
        map.put("price", priceTol + "");
        map.put("variation_id", SessionManager.readString(getActivity(), "selectVariationId", ""));

        Log.e(TAG, "Add to Cart Request :" + map);

        Call<Map<String, String>> loginCall = apiInterface.addToCart(map);
        loginCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String, String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Add to Cart Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        Toast.makeText(getActivity(), getString(R.string.item_added), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(getActivity(), data.get("message"), Toast.LENGTH_SHORT).show();
                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    public void LogInAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
    public void info(String value, String size) {
       /* if (!value.equals("")) {
            binding.tvColor.setText(value);
            binding.tvSize.setText(size);
        } else binding.tvColor.setText(productData.colorDetails.get(0).color);

        if (!size.equals("")) {
            binding.tvColor.setText(value);
            binding.tvSize.setText(size);
        } else binding.tvSize.setText(productData.colorDetails.get(0).size);*/
    }


    @Override
    public void onIcon(int position, String type) {
        /*if (type.equals("size")) {
            SessionManager.writeString(getActivity(), "selectSize", colorArrayList.get(position).size);
            chkSize = true;
           // avaQnty = colorArrayList.get(position).remainingQuantity+"";

        } else if (type.equals("color")) {
            for (int i = 0; i < colorArrayList.size(); i++) {
                colorArrayList.get(i).setChkColor(false);
            }
            colorArrayList.get(position).setChkColor(true);

            SessionManager.writeString(getActivity(), "avaQuantity", colorArrayList.get(position).remainingQuantity+"");
            SessionManager.writeString(getActivity(), "selectImage", colorArrayList.get(position).image);
            SessionManager.writeString(getActivity(), "selectColor", colorArrayList.get(position).color);
            SessionManager.writeString(getActivity(), "colorDetailsId", colorArrayList.get(position).colorId);

            binding.BlurImageView.setBlur(10);
            Glide.with(getActivity()).load(SessionManager.readString(getActivity(), "selectImage", ""))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                    .into(binding.BlurImageView);
            chkColor = true;
            pos = position;
            avaQnty = colorArrayList.get(position).remainingQuantity+"";
           // count =1;
           // priceCal(count);
        }*/

        if(type.equals("size")) {
            avaQnty = sizeArrayList.get(position).remainingQuantity+"";
            SessionManager.writeString(getActivity(),"avaQuantity",sizeArrayList.get(position).remainingQuantity+"");
            SessionManager.writeString(getActivity(), "selectSize", sizeArrayList.get(position).size+"");
            SessionManager.writeString(getActivity(), "selectVariationId", sizeArrayList.get(position).variationId+"");
            chkSize = true;
            variationPosition = position;
            priceCal(count,colorPosition,variationPosition);

        }
        else if(type.equals("color"))
        {
            for (int i = 0; i < colorArrayList.size(); i++) {
                colorArrayList.get(i).setChkColor(false);
            }
            colorPosition = position;
            colorArrayList.get(position).setChkColor(true);
            sizeArrayList.clear();
            sizeArrayList.addAll(colorArrayList.get(position).colorVariation);
            sizeAdapter.notifyDataSetChanged();

            SessionManager.writeString(getActivity(), "selectImage", colorArrayList.get(position).image);
            SessionManager.writeString(getActivity(), "selectColor", colorArrayList.get(position).color);
            SessionManager.writeString(getActivity(), "colorDetailsId", colorArrayList.get(position).colorId);

            binding.BlurImageView.setBlur(10);
            Glide.with(getActivity()).load(SessionManager.readString(getActivity(), "selectImage", ""))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                    .into(binding.BlurImageView);
            chkColor = true;
            pos = position;
            priceCal(count,colorPosition,variationPosition);


        }


    }


}
