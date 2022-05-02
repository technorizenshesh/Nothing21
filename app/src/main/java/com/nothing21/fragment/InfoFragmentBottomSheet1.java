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
import com.nothing21.databinding.FragmentInfoBinding;
import com.nothing21.listener.InfoListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.utils.SessionManager;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class InfoFragmentBottomSheet1 extends BottomSheetDialogFragment {
    public String TAG = "InfoFragmentBottomSheet";
    FragmentInfoBinding binding;
    BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;
    InfoListener listener;
    ProductModelCopy.Result productData;

    public InfoFragmentBottomSheet1(ProductModelCopy.Result productData) {
        this.productData = productData;
    }


    public InfoFragmentBottomSheet1 callBack(InfoListener listener) {
        this.listener = listener;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_info, null, false);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initViews();
        return  dialog;
    }

    private void initViews() {
        // binding.BlurImageView.setImageBitmap(BitmapFactory.decodeFile(productData.image1));

        Glide.with(getActivity()).load(SessionManager.readString(getActivity(),"selectImage",""))
                .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                .into(binding.BlurImageView);

        binding.BlurImageView.setBlur(10);

        binding.tvDes.setText(productData.description);

        binding.ivInfo.setOnClickListener(v -> dialog.dismiss());
    }
}
