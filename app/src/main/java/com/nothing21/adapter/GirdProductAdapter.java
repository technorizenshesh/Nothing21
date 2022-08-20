/*
package com.nothing21.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.nothing21.ProductAct;
import com.nothing21.ProductSingalAct;
import com.nothing21.ProductSingalCopyAct;
import com.nothing21.R;
import com.nothing21.databinding.ItemGirdProductBinding;
import com.nothing21.databinding.ItemScrollProductBinding;
import com.nothing21.databinding.ItemScrollProductOneBinding;
import com.nothing21.fragment.ProductFragment;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.listener.onIconClickProductListener;
import com.nothing21.model.ProductModel;
import com.nothing21.utils.DataManager;

import java.util.ArrayList;

public class GirdProductAdapter extends RecyclerView.Adapter<GirdProductAdapter.MyViewHolder> implements onIconClickProductListener {
    Context context;
    ArrayList<ProductModel.Result> arrayList;
    ArrayList<String> imgArrayList;
    onIconClickListener listener;


    public GirdProductAdapter(Context context, ArrayList<ProductModel.Result> arrayList, onIconClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
        imgArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGirdProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_gird_product, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).name);
        if (!arrayList.get(position).discountedPrice.equals("0")) {
            holder.binding.tvOldPrice.setVisibility(View.VISIBLE);
            holder.binding.tvDiscount.setVisibility(View.VISIBLE);
            holder.binding.tvPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price) - Double.parseDouble(arrayList.get(position).discountedPrice)));
            holder.binding.tvPrice.setTextColor(context.getResources().getColor(R.color.color_red));
            holder.binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
            holder.binding.tvOldPrice.setPaintFlags(holder.binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.binding.tvDiscount.setText("-" + arrayList.get(position).discountedPrice + "% Off");

        } else {
            holder.binding.tvPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
            holder.binding.tvPrice.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.tvOldPrice.setVisibility(View.GONE);
            holder.binding.tvDiscount.setVisibility(View.GONE);

        }


        holder.binding.viewPager.setAdapter(new MyViewPagerProductGirdAdapter(context, arrayList.get(position).colorDetails, arrayList.get(position).id));
        holder.binding.rvColor.setAdapter(new ColorProductAdapter(context, arrayList.get(position).colorDetails, GirdProductAdapter.this, holder.binding.viewPager));

        if (arrayList.get(position).fav_product_status.equals("false"))
            holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_white_heart));
        else holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_red_heart));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemGirdProductBinding binding;

        public MyViewHolder(@NonNull ItemGirdProductBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.ivLike.setOnClickListener(v -> {
                listener.onIcon(getAdapterPosition(), "Fav");
            });

        }
    }


    public void filterList(ArrayList<ProductModel.Result> filterlist) {
        if (filterlist.size() == 0)
            // ProductAct.tvFound.setVisibility(View.VISIBLE);
            ProductFragment.tvFound.setVisibility(View.VISIBLE);
        else
            // ProductAct.tvFound.setVisibility(View.GONE);
            ProductFragment.tvFound.setVisibility(View.GONE);
        arrayList = filterlist;
        notifyDataSetChanged();
    }


    @Override
    public void onIcon(int position, String type, ViewPager viewPager) {
        if (type.equals("color")) {
            next(position, viewPager);
        }
    }


    public void next(int ii, ViewPager vp) {
        vp.setCurrentItem(ii);

    }


}
*/
