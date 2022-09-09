package com.nothing21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nothing21.R;
import com.nothing21.databinding.ItemRateBinding;
import com.nothing21.databinding.ItemSubBinding;
import com.nothing21.listener.onSubCatListener;
import com.nothing21.model.CatSubCatModel;

import java.util.ArrayList;

public class SbAdapter extends RecyclerView.Adapter<SbAdapter.MyViewHolder> {
    Context context;
    ArrayList<CatSubCatModel.Result.SubcategoryInfo> arrayList;
    onSubCatListener subCatListener;

    public SbAdapter(Context context, ArrayList<CatSubCatModel.Result.SubcategoryInfo> arrayList, onSubCatListener subCatListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.subCatListener = subCatListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_sub, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.binding.item.setText(arrayList.get(position).getSubCatName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSubBinding binding;

        public MyViewHolder(@NonNull ItemSubBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.mainView.setOnClickListener(v -> {
                subCatListener.onSubCat(arrayList.get(getAdapterPosition()).getCategoryId(),arrayList.get(getAdapterPosition()).getCategoryName(),
                        arrayList.get(getAdapterPosition()).getId(),arrayList.get(getAdapterPosition()).getSubCatName());
            });
        }
    }
}

