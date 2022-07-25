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
import com.nothing21.databinding.ItemOrderHistoryBinding;
import com.nothing21.databinding.ItemOrderStatusBinding;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.OrderStatusModel;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderStatusModel.Result.CartDatum> arrayList;
    onItemClickListener listener;

    public HistoryAdapter(Context context, ArrayList<OrderStatusModel.Result.CartDatum>arrayList,onItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderHistoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order_history,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //  Glide.with(context).load(arrayList.get(position).image).error(R.drawable.dummy).into(holder.binding.ivImg);
        Glide.with(context).load("https://nothing21.com/nothing21/uploads/images/"+arrayList.get(position).getImage()).error(R.drawable.dummy).into(holder.binding.ivImg);

        holder.binding.tvProductName.setText(arrayList.get(position).getProductName()+"");
        holder.binding.tvSizeColor.setText(context.getString(R.string.size) + arrayList.get(position).getSize() + " , " +
                context.getString(R.string.color) + " : " + arrayList.get(position).getColor());
        holder.binding.tvProductQnty.setText(context.getString(R.string.quentity) + " " + arrayList.get(position).getQuantity() /* + " X " + "AED " +  arrayList.get(position).price*/);
        holder.binding.tvProductDelivery.setText("Order Status : "+arrayList.get(position).getOrderStatus());
        holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).getPrice())));
        holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.black));
        holder.binding.tvOldPrice.setVisibility(View.GONE);

       if(arrayList.get(position).getOrderStatus().equals("Delivered")){
           holder.binding.layoutReturn.setVisibility(View.VISIBLE);
       }
       else holder.binding.layoutReturn.setVisibility(View.GONE);




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrderHistoryBinding binding;
        public MyViewHolder(@NonNull ItemOrderHistoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.tvReturn.setOnClickListener(v -> listener.onItem(getAdapterPosition()));


        }
    }
}
