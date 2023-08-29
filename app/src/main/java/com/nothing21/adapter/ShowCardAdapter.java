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
import com.nothing21.databinding.ItemCard2Binding;
import com.nothing21.listener.OnItemPositionListener;
import com.nothing21.model.GetCardModel;

import java.util.ArrayList;

public class ShowCardAdapter extends RecyclerView.Adapter<ShowCardAdapter.MyViewHolder> {
    Context context;
    OnItemPositionListener listener;
    ArrayList<GetCardModel.Result> arrayList;

    public ShowCardAdapter(Context context, OnItemPositionListener listener, ArrayList<GetCardModel.Result> arrayList) {
        this.context = context;
        this.listener = listener;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCard2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_card2,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String input = arrayList.get(position).cardNumber;     //input string
        String lastFourDigits = "";     //substring containing last 4 characters
        if (input.length() > 4) {
            lastFourDigits = input.substring(input.length() - 4);
        } else {
            lastFourDigits = input;
        }



        holder.binding.tvCardNumber.setText("XXXXXXXXXXXX" + lastFourDigits);
        holder.binding.tvExpDate.setText(context.getString(R.string.valid_upto) + " " + arrayList.get(position).expiryMonth + "/" +
                arrayList.get(position).expiryDate);


        if(arrayList.get(position).isChk()) {
            holder.binding.chkRadio.setChecked(true);
        } else {
            holder.binding.chkRadio.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<arrayList.size();i++) {
                    arrayList.get(i).setChk(false);
                }

                for(int i=0;i<arrayList.size();i++) {
                    arrayList.get(i).setChk(false);
                    Log.e("shgdfgsdf","position = " + position + "   " + arrayList.get(i).isChk());
                }

                arrayList.get(position).setChk(true);

                Log.e("dssdggsdg","position = " + position + "   " + arrayList.get(position).isChk());

                notifyDataSetChanged();
                listener.onPosition(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCard2Binding binding;
        public MyViewHolder(@NonNull ItemCard2Binding itemView) {
            super(itemView.getRoot());
            binding = itemView;


        }
    }
}
