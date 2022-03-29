package com.nothing21.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nothing21.ProductSingalAct;
import com.nothing21.R;
import com.nothing21.databinding.ItemGirdProductBinding;
import com.nothing21.databinding.ItemImagBinding;
import com.nothing21.databinding.ItemProductImagBinding;
import com.nothing21.fragment.ProductFragment;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductModelCopy.Result.ColorDetail> arrayList;


    public ImageAdapter(Context context, ArrayList<ProductModelCopy.Result.ColorDetail> arrayList ) {
        this.context = context;
        this.arrayList = arrayList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImagBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_imag,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).image).error(R.drawable.dummy).into(holder.binding.ivImg);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemImagBinding binding;
        public MyViewHolder(@NonNull ItemImagBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }





}
