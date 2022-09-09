package com.nothing21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nothing21.R;
import com.nothing21.databinding.ItemColorFilterBinding;
import com.nothing21.databinding.ItemSpinnerBinding;
import com.nothing21.listener.onCatListener;
import com.nothing21.listener.onSubCatListener;
import com.nothing21.model.CatSubCatModel;

import java.util.ArrayList;

public class CatSpinnerAdapter extends RecyclerView.Adapter<CatSpinnerAdapter.MyViewHolder> implements onSubCatListener {
    Context context;
    ArrayList<CatSubCatModel.Result>arrayList;
    onCatListener catListener;

    public CatSpinnerAdapter(Context context, ArrayList<CatSubCatModel.Result> arrayList,onCatListener catListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.catListener = catListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSpinnerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_spinner, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         holder.binding.item.setText(arrayList.get(position).getCategoryName());
         holder.binding.rvSubCat.setAdapter(new SbAdapter(context,arrayList.get(position).getSubcategoryInfo(),CatSpinnerAdapter.this));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onSubCat(String catId,String catName, String subCatId,String subCatName) {
        catListener.onCat(catId,catName,subCatId,subCatName);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSpinnerBinding binding;
        public MyViewHolder(@NonNull ItemSpinnerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
