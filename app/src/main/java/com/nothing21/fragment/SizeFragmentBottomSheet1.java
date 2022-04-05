package com.nothing21.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nothing21.R;
import com.nothing21.adapter.SizeAdapter;
import com.nothing21.adapter.SizeAdapter1;
import com.nothing21.databinding.FragmentColorSizeSheetBinding;
import com.nothing21.databinding.FragmentSizeSheetBinding;
import com.nothing21.listener.InfoListener;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.utils.SessionManager;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SizeFragmentBottomSheet1 extends BottomSheetDialogFragment implements onItemClickListener {

    public String TAG = "SizeFragmentBottomSheet1";
    FragmentSizeSheetBinding binding;
    BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;
    InfoListener listener;
    ProductModelCopy.Result productData;
    ArrayList<ProductModelCopy.Result.ColorDetail> arrayList;
    String color ="";


    public SizeFragmentBottomSheet1(ProductModelCopy.Result productData,String color) {
        this.productData = productData;
        this.color = color;
    }


    public SizeFragmentBottomSheet1 callBack(InfoListener listener) {
        this.listener = listener;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_size_sheet, null, false);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initViews();
        return dialog;
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        arrayList.clear();
        arrayList.addAll(productData.colorDetails);
        Glide.with(getActivity()).load(productData.imageDetails.get(0).image)
                .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                .into(binding.BlurImageView);

        binding.BlurImageView.setBlur(10);
     //   binding.rvColorSize.setAdapter(new SizeAdapter1(getActivity(), arrayList,SizeFragmentBottomSheet1.this));

        binding.ivColor.setOnClickListener(v -> dialog.dismiss());

        binding.ivIn.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onItem(int position) {
        listener.info(color,arrayList.get(position).size);
        SessionManager.writeString(getActivity(),"selectImage",arrayList.get(position).image);
        dialog.dismiss();
    }
}