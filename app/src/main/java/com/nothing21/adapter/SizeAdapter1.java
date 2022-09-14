package com.nothing21.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nothing21.R;
import com.nothing21.databinding.ItemSizeColorBinding;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.model.ProductModelCopyNew;

import java.util.ArrayList;

public class SizeAdapter1 extends RecyclerView.Adapter<SizeAdapter1.MyViewHolder> {
    Context context;
    ArrayList<ProductModelCopyNew.Result.ColorDetail.ColorVariation> arrayList;
    onIconClickListener listener;

    public SizeAdapter1(Context context, ArrayList<ProductModelCopyNew.Result.ColorDetail.ColorVariation>arrayList, onIconClickListener listener) {
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

        if(arrayList.get(position).selectColor == false)  holder.binding.tvColor.setTextColor(context.getResources().getColor(R.color.black));
        else if(arrayList.get(position).selectColor == true)  holder.binding.tvColor.setTextColor(context.getResources().getColor(R.color.color_red));

     if(arrayList.get(position).remainingQuantity > 0)  {
         holder.binding.tvColor.setText(arrayList.get(position).size);
     } else{
         holder.binding.tvColor.setTextColor(context.getResources().getColor(R.color.color_grey));
         holder.binding.tvColor.setText(arrayList.get(position).size);
         holder.binding.tvColor.setPaintFlags(holder.binding.tvColor.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);     }

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
              if(arrayList.get(getAdapterPosition()).remainingQuantity>0) {
                  for (int i = 0; i < arrayList.size(); i++) {
                      arrayList.get(i).setSelectColor(false);
                  }
                  arrayList.get(getLayoutPosition()).setSelectColor(true);
                  listener.onIcon(getAdapterPosition(), "size");
                  notifyDataSetChanged();
              }
              else {
                  Toast.makeText(context, context.getString(R.string.size_out_of_stock), Toast.LENGTH_SHORT).show();
              }
            });

        }
    }
}
