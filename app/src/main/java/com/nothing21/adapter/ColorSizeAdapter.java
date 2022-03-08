package com.nothing21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nothing21.R;
import com.nothing21.databinding.ItemSizeColorBinding;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.ProductModel;

import java.util.ArrayList;

public class ColorSizeAdapter extends RecyclerView.Adapter<ColorSizeAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductModel.Result.ColorDetail>arrayList;
    onItemClickListener listener;

    public ColorSizeAdapter(Context context, ArrayList<ProductModel.Result.ColorDetail>arrayList, onItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSizeColorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_size_color, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvColor.setText(arrayList.get(position).color);
       // holder.binding.tvSize.setText(arrayList.get(position).size);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSizeColorBinding binding;

        public MyViewHolder(@NonNull ItemSizeColorBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.layMain.setOnClickListener(v -> {
                listener.onItem(getAdapterPosition());
            });

        }
    }
}

