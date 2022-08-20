/*
package com.nothing21.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nothing21.R;
import com.nothing21.databinding.ItemColorFilterBinding;
import com.nothing21.databinding.ItemShadeBinding;
import com.nothing21.model.ColorListModel;

import java.util.ArrayList;

public class ShadeColorAdapter extends RecyclerView.Adapter<ShadeColorAdapter.MyViewHolder> {
    Context context;
    ArrayList<ColorListModel.Result.colorDetails> arrayList;


    public ShadeColorAdapter(Context context, ArrayList<ColorListModel.Result.colorDetails> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShadeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_shade, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      holder.binding.viewShade.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor_code()));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemShadeBinding binding;
        public MyViewHolder(@NonNull ItemShadeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
*/
