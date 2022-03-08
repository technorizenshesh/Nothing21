package com.nothing21.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nothing21.ProductAct;
import com.nothing21.R;
import com.nothing21.databinding.ItemCartBinding;
import com.nothing21.databinding.ItemProductBinding;
import com.nothing21.fragment.HomeFragment;
import com.nothing21.model.CategoryModel;
import com.nothing21.model.ProductModel;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    Context context;
    ArrayList<CategoryModel.Result>arrayList;

    public ProductAdapter(Context context, ArrayList<CategoryModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_product,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).image).error(R.drawable.dummy).into(holder.binding.ivImg);
        holder.binding.tvName.setText(arrayList.get(position).categoryName);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;
        public MyViewHolder(@NonNull ItemProductBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.layoutMain.setOnClickListener(v -> context.startActivity(new Intent(context, ProductAct.class).putExtra("catId",arrayList.get(getAdapterPosition()).id)));

        }
    }

    public void filterList(ArrayList<CategoryModel.Result> filterlist) {
        if(filterlist.size()==0)
            HomeFragment.tvFound.setVisibility(View.VISIBLE);
        else
            HomeFragment.tvFound.setVisibility(View.GONE);
        arrayList = filterlist;
        notifyDataSetChanged();
    }


}

