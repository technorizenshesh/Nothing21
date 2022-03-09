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
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.GetCartModel;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    ArrayList<GetCartModel.Result>arrayList;
    onIconClickListener listener;

    public CartAdapter(Context context, ArrayList<GetCartModel.Result>arrayList,onIconClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_cart,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       //  Glide.with(context).load(arrayList.get(position).image).error(R.drawable.dummy).into(holder.binding.ivImg);
        Glide.with(context).load(arrayList.get(position).image1).error(R.drawable.dummy).into(holder.binding.ivImg);

         holder.binding.tvProductName.setText(arrayList.get(position).name+"");
         holder.binding.tvSizeColor.setText(context.getString(R.string.size) + arrayList.get(position).size + " , " +
                 context.getString(R.string.color) + " : " + arrayList.get(position).color);



        holder.binding.tvProductQnty.setText(context.getString(R.string.quentity) + " " + arrayList.get(position).quantity + " X " + "AED " +  arrayList.get(position).price);
   //     holder.binding.tvProductSize.setText(context.getString(R.string.size) + " " + arrayList.get(position).size);
         holder.binding.tvProductPrice.setText("AED "+ String.format("%.2f",Double.parseDouble(arrayList.get(position).price) * Integer.parseInt(arrayList.get(position).quantity)));
         holder.binding.tvProductDelivery.setText(context.getString(R.string.delivery_two_day));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        public MyViewHolder(@NonNull ItemCartBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.ivDelete.setOnClickListener(v -> listener.onIcon(getAdapterPosition(),"Delete"));

            binding.ivEdit.setOnClickListener(v -> listener.onIcon(getAdapterPosition(),"Edit"));


        }
    }
}
