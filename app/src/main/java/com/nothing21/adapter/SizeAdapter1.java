package com.nothing21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nothing21.R;
import com.nothing21.databinding.ItemSizeColorBinding;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;

import java.util.ArrayList;

public class SizeAdapter1 extends RecyclerView.Adapter<SizeAdapter1.MyViewHolder> {
    Context context;
    ArrayList<ProductModelCopy.Result.ColorDetail> arrayList;
    onIconClickListener listener;

    public SizeAdapter1(Context context, ArrayList<ProductModelCopy.Result.ColorDetail>arrayList, onIconClickListener listener) {
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
        holder.binding.tvColor.setText(arrayList.get(position).size);
        // holder.binding.tvSize.setText(arrayList.get(position).size);

        if(arrayList.get(position).selectColor == false)  holder.binding.tvColor.setTextColor(context.getResources().getColor(R.color.black));
       else if(arrayList.get(position).selectColor == true)  holder.binding.tvColor.setTextColor(context.getResources().getColor(R.color.color_red));

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
                for(int i =0;i<arrayList.size();i++){
                    arrayList.get(i).setSelectColor(false);
                }
                arrayList.get(getLayoutPosition()).setSelectColor(true);
                listener.onIcon(getAdapterPosition(),"size");
                notifyDataSetChanged();
            });

        }
    }
}
