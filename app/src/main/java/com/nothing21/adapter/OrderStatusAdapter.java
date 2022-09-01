package com.nothing21.adapter;

import android.content.Context;
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
import com.nothing21.databinding.ItemOrderStatusBinding;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.GetCartModel;
import com.nothing21.model.OrderStatusModel;

import java.util.ArrayList;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderStatusModel.Result.CartDatum> arrayList;

    public OrderStatusAdapter(Context context, ArrayList<OrderStatusModel.Result.CartDatum>arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderStatusBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order_status,parent,false);
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
        //     holder.binding.tvProductSize.setText(context.getString(R.string.size) + " " + arrayList.get(position).size);
      //  holder.binding.tvProductPrice.setText("AED "+ String.format("%.2f",Double.parseDouble(arrayList.get(position).price) * Integer.parseInt(arrayList.get(position).quantity)));
      //  holder.binding.tvProductDelivery.setText(context.getString(R.string.delivery_two_day));
       if(arrayList.get(position).getOrderStatus().equals("Pending")){
           holder.binding.tvProductDelivery.setVisibility(View.GONE);

       }else {
           holder.binding.tvProductDelivery.setVisibility(View.VISIBLE);
           holder.binding.tvProductDelivery.setText("Order Status : "+arrayList.get(position).getOrderStatus());
       }


       /* if(!arrayList.get(position).discount.equals("")) {
            holder.binding.tvOldPrice.setVisibility(View.VISIBLE);
            //  holder.binding.tvDiscount.setVisibility(View.VISIBLE);
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price) - Double.parseDouble(arrayList.get(position).discount )));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.color_red));
            holder.binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
            holder.binding.tvOldPrice.setPaintFlags(holder.binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //  holder.binding.tvDiscount.setText("-"+arrayList.get(position).discount + "% Off");

        }
        else {*/
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).getPrice())));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.tvOldPrice.setVisibility(View.GONE);
            //   holder.binding.tvDiscount.setVisibility(View.GONE);

       // }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrderStatusBinding binding;
        public MyViewHolder(@NonNull ItemOrderStatusBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;




        }
    }
}
