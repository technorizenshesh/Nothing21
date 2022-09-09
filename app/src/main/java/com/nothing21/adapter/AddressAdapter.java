package com.nothing21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nothing21.R;
import com.nothing21.databinding.ItemAddressBinding;
import com.nothing21.databinding.ItemCartBinding;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.AddressModel;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {
    Context context;
    ArrayList<AddressModel.Result>arrayList;
    onItemClickListener listener;

    public AddressAdapter(Context context, ArrayList<AddressModel.Result> arrayList, onItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_address,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvAddress.setText(arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemAddressBinding binding;
        public MyViewHolder(@NonNull ItemAddressBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.cardAddress.setOnClickListener(v -> listener.onItem(getAdapterPosition()));
        }
    }
}
