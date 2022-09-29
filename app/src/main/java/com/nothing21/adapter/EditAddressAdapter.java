package com.nothing21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nothing21.R;
import com.nothing21.databinding.ItemAddressBinding;
import com.nothing21.databinding.ItemEditAddressBinding;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.AddressModel;

import java.util.ArrayList;

public class EditAddressAdapter extends RecyclerView.Adapter<EditAddressAdapter.MyViewHolder> {
    Context context;
    ArrayList<AddressModel.Result> arrayList;
    onItemClickListener listener;

    public EditAddressAdapter(Context context, ArrayList<AddressModel.Result> arrayList, onItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEditAddressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_edit_address,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //  holder.binding.tvAddress.setText(arrayList.get(position).getName());

        holder.binding.tvAddress.setText(arrayList.get(position).getFlate_no() + " " +
                arrayList.get(position).getBuilding_name() + " " + arrayList.get(position).getNearestLandmark() +
                /*" " + arrayList.get(position).getArea() +*/ " " + arrayList.get(position).getCity() + " "
                + arrayList.get(position).getZipCode());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemEditAddressBinding binding;
        public MyViewHolder(@NonNull ItemEditAddressBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.ivEdit.setOnClickListener(v -> listener.onItem(getAdapterPosition()));
        }
    }
}

