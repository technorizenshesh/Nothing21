package com.nothing21.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nothing21.R;
import com.nothing21.databinding.ItemColorBinding;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.model.ProductNewModel;
import com.nothing21.utils.SessionManager;

import java.util.ArrayList;

public class ColorCartAdapter extends RecyclerView.Adapter<ColorCartAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductNewModel.Result.ColorDetail> arrayList;
    onIconClickListener listener;

    public ColorCartAdapter(Context context, ArrayList<ProductNewModel.Result.ColorDetail>arrayList, onIconClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemColorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_color, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        if(arrayList.get(position).isChkColor()== false){
            holder.binding.view2.setVisibility(View.GONE);
            holder.binding.view22.setSolidColor(arrayList.get(position).colorCode);
            SessionManager.writeString(context,"selectImage",arrayList.get(position).image);



        }else {
            holder.binding.view2.setVisibility(View.VISIBLE);
            holder.binding.view2.setStrokeWidth(1);
            holder.binding.view2.setStrokeColor(arrayList.get(position).colorCode);
            holder.binding.view22.setSolidColor(arrayList.get(position).colorCode);




            // SessionManager.writeString(ProductSingalCopyAct.this,"selectImage",data.result.colorDetails.get(0).image);
            //  SessionManager.writeString(ProductSingalCopyAct.this,"selectColor",data.result.colorDetails.get(0).color);





        }
    }





    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemColorBinding binding;

        public MyViewHolder(@NonNull ItemColorBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.layoutMain.setOnClickListener(v -> {
                for(int i =0;i<arrayList.size();i++){
                    arrayList.get(i).setChkColor(false);
                }
                arrayList.get(getAdapterPosition()).setChkColor(true);
                listener.onIcon(getAdapterPosition(),"color");
                Log.e("cureenttt",arrayList.get(getAdapterPosition()).isChkColor()+"");
                notifyDataSetChanged();
            });

        }
    }
}