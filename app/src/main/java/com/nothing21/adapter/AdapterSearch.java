package com.nothing21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nothing21.R;
import com.nothing21.databinding.ItemSearchBinding;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.SearchModel;

import java.util.ArrayList;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.MyViewHolder> {
    private onItemClickListener listener;
    Context context;
    ArrayList<SearchModel.Result> arrayList;

    public AdapterSearch(Context context, ArrayList<SearchModel.Result> arrayList, onItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_search,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     //   Glide.with(context).load("https://www.adspot.ae/nothing21/uploads/images/"+arrayList.get(position).image1).error(R.drawable.dummy).into(holder.binding.ivImg);
        Glide.with(context).load(arrayList.get(position).image).error(R.drawable.dummy).into(holder.binding.ivImg);

        holder.binding.tvProductName.setText(arrayList.get(position).name);
        holder.binding.tvBrand.setText(arrayList.get(position).brand);
        holder.binding.tvPrise.setText("AFD" +arrayList.get(position).price);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSearchBinding binding;
        public MyViewHolder(@NonNull ItemSearchBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.llMain.setOnClickListener(v -> listener.onItem(getAdapterPosition()));
        }
    }
}

