package com.nothing21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nothing21.R;
import com.nothing21.databinding.ItemOtherProductBinding;
import com.nothing21.databinding.ItemProductBinding;
import com.nothing21.fragment.HomeFragment;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.CategoryModel;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductNewModel;

import java.util.ArrayList;

public class OtherProAdapter extends RecyclerView.Adapter<OtherProAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductNewModel.Result> arrayList;
    onItemClickListener listener;

    public OtherProAdapter(Context context, ArrayList<ProductNewModel.Result> arrayList,onItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOtherProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_other_product,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).colorDetails.get(0).image).error(R.drawable.dummy).into(holder.binding.ivImg);
        holder.binding.tvName.setText(arrayList.get(position).name);

        holder.itemView.setOnClickListener(v -> listener.onItem(position) );

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOtherProductBinding binding;
        public MyViewHolder(@NonNull ItemOtherProductBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;



        }
    }



}
