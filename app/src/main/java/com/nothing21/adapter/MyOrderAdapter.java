package com.nothing21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nothing21.R;
import com.nothing21.databinding.ItemCartBinding;
import com.nothing21.databinding.ItemMyOrderBinding;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.FavModel;
import com.nothing21.model.GetCartModel;
import com.nothing21.model.MyOrderModel;

import java.util.ArrayList;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    Context context;
    ArrayList<FavModel.Result> arrayList;

    public MyOrderAdapter(Context context, ArrayList<FavModel.Result>arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_my_order,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load("https://www.adspot.ae/nothing21//uploads/images/" + arrayList.get(position).image1).error(R.drawable.dummy).into(holder.binding.ivImg);
      //  holder.binding.tvProductQnty.setText(context.getString(R.string.quentity) + " " + arrayList.get(position).quantity);
       // holder.binding.tvProductSize.setText(context.getString(R.string.size) + " " + arrayList.get(position).size);
        holder.binding.tvProductName.setText(arrayList.get(position).name);
        holder.binding.tvTotal.setText("AED "+arrayList.get(position).price);
        holder.binding.tvStatus.setText(arrayList.get(position).description);
        holder.binding.tvAddress.setText("Delivery address  : "+arrayList.get(position).address);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemMyOrderBinding binding;
        public MyViewHolder(@NonNull ItemMyOrderBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;




        }
    }
}
