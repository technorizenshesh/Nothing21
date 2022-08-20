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
    onIconClickListener listener;

    public MyOrderAdapter(Context context, ArrayList<FavModel.Result>arrayList,onIconClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_my_order,parent,false);
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
        holder.binding.layoutDelete.setVisibility(View.VISIBLE);

        if(!arrayList.get(position).discount.equals("")) {
            holder.binding.tvOldPrice.setVisibility(View.VISIBLE);
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price) - Double.parseDouble(arrayList.get(position).discount )));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.color_red));
            holder.binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
            holder.binding.tvOldPrice.setPaintFlags(holder.binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
        else {
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.tvOldPrice.setVisibility(View.GONE);

        }

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

            binding.layoutDelete.setOnClickListener(v -> listener.onIcon(getAdapterPosition(),"delete"));

            binding.layoutMain.setOnClickListener(v -> {
             /*   context.startActivity(new Intent(context, ProductSingalAct.class)
                .putExtra("id",arrayList.get(getAdapterPosition()).id));*/
            });


        }
    }
}
