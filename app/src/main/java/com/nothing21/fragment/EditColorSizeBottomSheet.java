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
import com.nothing21.adapter.EditColorSizeAdapter;
import com.nothing21.databinding.FragmentColorSizeSheetBinding;
import com.nothing21.listener.InfoListener;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.ProductModelCopy;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class EditColorSizeBottomSheet extends BottomSheetDialogFragment implements onItemClickListener {

    public String TAG = "EditColorSizeBottomSheet";
    FragmentColorSizeSheetBinding binding;
    BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;
    InfoListener listener;
    ProductModelCopy.Result productData;
    ArrayList<ProductModelCopy.Result.ColorDetail> arrayList;
    String size="";

    public EditColorSizeBottomSheet(ProductModelCopy.Result productData,String size) {
        this.productData = productData;
        this.size = size;
    }


    public EditColorSizeBottomSheet callBack(InfoListener listener) {
        this.listener = listener;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_color_size_sheet, null, false);
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
        binding.rvColorSize.setAdapter(new EditColorSizeAdapter(getActivity(), arrayList,EditColorSizeBottomSheet.this));

        binding.ivColor.setOnClickListener(v -> dialog.dismiss());

        binding.ivIn.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onItem(int position) {
        listener.info(arrayList.get(position).color,size);
        dialog.dismiss();
    }
}