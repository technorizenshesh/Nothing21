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
import com.nothing21.databinding.ItemRateBinding;
import com.nothing21.model.RateModel;

import java.util.ArrayList;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.MyViewHolder> {
    Context context;
    ArrayList<RateModel.Result> arrayList;

    public RateAdapter(Context context, ArrayList<RateModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRateBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_rate, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvUserName.setText(arrayList.get(position).userName + "");
        holder.binding.viewRate.setText(arrayList.get(position).dateTime);
        holder.binding.tvComment.setText(arrayList.get(position).reviews);
        holder.binding.RatingBar.setRating(Float.parseFloat(arrayList.get(position).rating));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemRateBinding binding;

        public MyViewHolder(@NonNull ItemRateBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;


        }
    }
}

