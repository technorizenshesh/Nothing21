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
import com.nothing21.ProductSingalCopyAct;
import com.nothing21.R;
import com.nothing21.databinding.ItemMyOrderBinding;
import com.nothing21.databinding.ItemMyOrderTwoBinding;
import com.nothing21.model.FavModel;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductNewModel;

import java.util.ArrayList;

public class MyOrderAdapterTwo extends RecyclerView.Adapter<MyOrderAdapterTwo.MyViewHolder> {
    Context context;
    ArrayList<ProductNewModel.Result> arrayList;

    public MyOrderAdapterTwo(Context context, ArrayList<ProductNewModel.Result>arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyOrderTwoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_my_order_two,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // "https://www.adspot.ae/nothing21//uploads/images/" +
        Glide.with(context).load( arrayList.get(position).colorDetails.get(0).image).error(R.drawable.dummy).into(holder.binding.ivImg);
        //  holder.binding.tvProductQnty.setText(context.getString(R.string.quentity) + " " + arrayList.get(position).quantity);
        // holder.binding.tvProductSize.setText(context.getString(R.string.size) + " " + arrayList.get(position).size);
        holder.binding.tvProductName.setText(arrayList.get(position).name);
        // holder.binding.tvTotal.setText("AED "+arrayList.get(position).price);
        // holder.binding.tvStatus.setText(arrayList.get(position).description);
        // holder.binding.tvAddress.setText("Delivery address  : "+arrayList.get(position).address);
        holder.binding.tvStatus.setVisibility(View.GONE);


        if(!arrayList.get(position).colorDetails.get(0).colorVariation.get(0).equals("")) {
            holder.binding.tvOldPrice.setVisibility(View.VISIBLE);
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f",Double.parseDouble(arrayList.get(position).colorDetails.get(0).colorVariation.get(0).priceCalculated)));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.color_red));
            holder.binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).colorDetails.get(0).colorVariation.get(0).price)));
            holder.binding.tvOldPrice.setPaintFlags(holder.binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
        else {
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).colorDetails.get(0).colorVariation.get(0).price)));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.tvOldPrice.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemMyOrderTwoBinding binding;
        public MyViewHolder(@NonNull ItemMyOrderTwoBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.layoutMain.setOnClickListener(v -> {
                context.startActivity(new Intent(context, ProductSingalCopyAct.class)
                        .putExtra("id",arrayList.get(getAdapterPosition()).id));
            });


        }
    }
}