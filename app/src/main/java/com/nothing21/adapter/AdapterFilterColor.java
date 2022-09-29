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
import com.nothing21.listener.onPositionClickListener;
import com.nothing21.model.ColorListModel;

import java.util.ArrayList;

public class AdapterFilterColor extends RecyclerView.Adapter<AdapterFilterColor.MyViewHolder> {
    Context context;
    ArrayList<ColorListModel.Result> arrayList;
    onPositionClickListener listener;

    public AdapterFilterColor(Context context, ArrayList<ColorListModel.Result> arrayList,onPositionClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemColorFilterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_color_filter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // holder.binding.tvColorName.setVisibility(View.GONE);
        holder.binding.tvColorName.setText(arrayList.get(position).getColorName());
        holder.binding.viewShade.setBackgroundColor(Color.parseColor(arrayList.get(position).getColorLink()));

      //  holder.binding.rvColorShade.setAdapter(new ShadeColorAdapter(context,arrayList.get(position).getColorDetails()));
        if(arrayList.get(position).isChk()==true){
            holder.binding.LlMain.setBackground(context.getDrawable(R.drawable.rounded_select_yellow_bg));
        } else  holder.binding.LlMain.setBackground(context.getDrawable(R.drawable.rounded_unselect_white_bg));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemColorFilterBinding binding;

        public MyViewHolder(@NonNull ItemColorFilterBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.layoutClick.setOnClickListener(v -> {
/*
                if(arrayList.get(getAdapterPosition()).isChk()==false){
                    arrayList.get(getAdapterPosition()).setChk(true);
                    listener.onPosition(getAdapterPosition(),"true");
                    notifyItemChanged(getAdapterPosition());
                }
*/
/*
                else if(arrayList.get(getAdapterPosition()).isChk()==true){
                    arrayList.get(getAdapterPosition()).setChk(false);
                    listener.onPosition(getAdapterPosition(),"false");
                    notifyItemChanged(getAdapterPosition());
                }
*/

                for (int i =0;i<arrayList.size();i++){
                    arrayList.get(i).setChk(false);

                }
                arrayList.get(getAdapterPosition()).setChk(true);
                listener.onPosition(getAdapterPosition(),"true");
               notifyDataSetChanged();

            });



        }
    }
}
