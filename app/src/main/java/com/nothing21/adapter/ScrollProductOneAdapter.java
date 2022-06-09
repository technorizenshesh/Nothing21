package com.nothing21.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.nothing21.ProductSingalAct;
import com.nothing21.ProductSingalCopyAct;
import com.nothing21.R;
import com.nothing21.ViewProductAdapter1;
import com.nothing21.databinding.ItemScrollProductBinding;
import com.nothing21.databinding.ItemScrollProductOneBinding;
import com.nothing21.fragment.ProductFragment;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.listener.onIconClickProductListener;
import com.nothing21.model.ProductModel;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.SessionManager;

import java.util.ArrayList;

public class ScrollProductOneAdapter extends RecyclerView.Adapter<ScrollProductOneAdapter.MyViewHolder> implements onIconClickProductListener {
    Context context;
    ArrayList<ProductModel.Result> arrayList;
    ArrayList<String>imgArrayList;
    onIconClickListener listener;
    ViewProductAdapter1 adapter;
    ItemScrollProductOneBinding bindingMain;
    public
    ScrollProductOneAdapter(Context context, ArrayList<ProductModel.Result> arrayList,onIconClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        imgArrayList = new ArrayList<>();
        this.listener  = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScrollProductOneBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_scroll_product_one,parent,false);
       // setChkColor();
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
        holder.binding.tvProductName.setText(arrayList.get(position).name);

        holder.binding.viewPager.setAdapter(new MyViewPagerProductAdapter(context,arrayList.get(position).colorDetails));
        holder.binding.rvColor.setAdapter(new ColorProductAdapter(context, arrayList.get(position).colorDetails,ScrollProductOneAdapter.this,holder.binding.viewPager));


        if(!arrayList.get(position).discount.equals("")) {
            holder.binding.tvOldPrice.setVisibility(View.VISIBLE);
            holder.binding.tvDiscount.setVisibility(View.VISIBLE);
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price) - Double.parseDouble(arrayList.get(position).discount )));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.color_red));
            holder.binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
            holder.binding.tvOldPrice.setPaintFlags(holder.binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.binding.tvDiscount.setText("-"+arrayList.get(position).discount + "% Off");

        }
        else {
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.tvOldPrice.setVisibility(View.GONE);
            holder.binding.tvDiscount.setVisibility(View.GONE);

        }

        if(arrayList.get(position).fav_product_status.equals("false")) holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_white_heart));
        else holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_red_heart));
        //setListData(position,holder.binding);



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onIcon(int position, String type, ViewPager viewPager) {
        if(type.equals("color")) {
            next(position,viewPager);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemScrollProductOneBinding binding;
        public MyViewHolder(@NonNull ItemScrollProductOneBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            bindingMain = itemView;
            binding.ivCart.setOnClickListener(v -> {
                arrayList.get(getAdapterPosition()).colorDetails.get(0).setChkColor(true);
                listener.onIcon(getAdapterPosition(),"Cart");
            });

            binding.ivLike.setOnClickListener(v -> {
                listener.onIcon(getAdapterPosition(),"Fav");
            });

            binding.layoutSecond.setOnClickListener(v -> {
                SessionManager.writeString(context,"selectImage",arrayList.get(getAdapterPosition()).colorDetails.get(0).image);

                context.startActivity(new Intent(context, ProductSingalCopyAct.class).putExtra("id",arrayList.get(getAdapterPosition()).id));

            });



        }
    }




    public void filterList(ArrayList<ProductModel.Result> filterlist) {
        if(filterlist.size()==0)
            //   ProductAct.tvFound.setVisibility(View.VISIBLE);
            ProductFragment.tvFound.setVisibility(View.VISIBLE);

        else
            // ProductAct.tvFound.setVisibility(View.GONE);
            ProductFragment.tvFound.setVisibility(View.GONE);
        arrayList = filterlist;
        notifyDataSetChanged();
    }


    public void setChkColor (){
        Log.e("challaaa",arrayList.size()+"=======");
        for (int i =0 ; i< arrayList.size();i++){
            Log.e("challaaa",i+"=======");
            arrayList.get(i).colorDetails.get(0).setChkColor(true);
            Log.e("Ist Position==", arrayList.get(i).colorDetails.get(0).chkColor+"");
        }
    }


    public void next(int ii, ViewPager vp) {
        vp.setCurrentItem(ii);
        Log.e("slideCurrentAdapter=="+ ii,bindingMain.viewPager.getCurrentItem()+"");

    }

}
