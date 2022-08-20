package com.nothing21.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.nothing21.R;
import com.nothing21.databinding.ItemGirdProductBinding;
import com.nothing21.fragment.ProductNewFragment;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.listener.onIconClickProductGirdListener;
import com.nothing21.listener.onIconClickProductListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductNewModel;

import java.util.ArrayList;

public class GirdProductAdapterNew extends RecyclerView.Adapter<GirdProductAdapterNew.MyViewHolder> implements onIconClickProductGirdListener {
    Context context;
    ArrayList<ProductNewModel.Result> arrayList;
    ArrayList<String>imgArrayList;
    onIconClickListener listener;


    public GirdProductAdapterNew(Context context, ArrayList<ProductNewModel.Result> arrayList, onIconClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
        imgArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGirdProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_gird_product,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load(arrayList.get(position).colorDetails.get(0).image).error(R.drawable.dummy).into(holder.binding.ivImg);

       // holder.binding.viewPager.setAdapter(new MyViewPagerProductGirdAdapter(context,arrayList.get(position).colorDetails,arrayList.get(position).id));
        holder.binding.rvColor.setAdapter(new ColorProductGirdAdapter(context, arrayList.get(position).colorDetails,position,holder,GirdProductAdapterNew.this,holder.binding.viewPager));

        if(arrayList.get(position).favProductStatus.equals("false")) holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_white_heart));
        else holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_red_heart));


        setItemValue(position,0,0,holder);



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onGirdIcon(int position, int mainPosition, MyViewHolder holder, String type, ViewPager viewPager) {
        if(type.equals("color")) {
           // next(position,viewPager);
            setItemValue(mainPosition,position,0,holder);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemGirdProductBinding binding;
        public MyViewHolder(@NonNull ItemGirdProductBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.ivLike.setOnClickListener(v -> {
                listener.onIcon(getAdapterPosition(),"Fav");
            });

        }
    }


    public void filterList(ArrayList<ProductNewModel.Result> filterlist) {
        if(filterlist.size()==0)
            // ProductAct.tvFound.setVisibility(View.VISIBLE);
            ProductNewFragment.tvFound.setVisibility(View.VISIBLE);
        else
            // ProductAct.tvFound.setVisibility(View.GONE);
            ProductNewFragment.tvFound.setVisibility(View.GONE);
        arrayList = filterlist;
        notifyDataSetChanged();
    }





    public void next(int ii, ViewPager vp) {
        vp.setCurrentItem(ii);
      //  vp.beginFakeDrag();

    }

    public void setItemValue(int mainPosition,int colorPosition,int variationPosition, MyViewHolder holder){
        holder.binding.tvName.setText(arrayList.get(mainPosition).name);
        Glide.with(context).load(arrayList.get(mainPosition).colorDetails.get(colorPosition).image).error(R.drawable.dummy).into(holder.binding.ivImg);

        if(!arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceDiscount.equals("0")) {
            holder.binding.tvOldPrice.setVisibility(View.VISIBLE);
            holder.binding.tvDiscount.setVisibility(View.VISIBLE);
            holder.binding.tvPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceCalculated)));
            holder.binding.tvPrice.setTextColor(context.getResources().getColor(R.color.color_red));


            holder.binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).price)));
            holder.binding.tvOldPrice.setPaintFlags(holder.binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.binding.tvDiscount.setText("-"+arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceDiscount + "% Off");

        }
        else {
            holder.binding.tvPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceCalculated)));
            holder.binding.tvPrice.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.tvOldPrice.setVisibility(View.GONE);
            holder.binding.tvDiscount.setVisibility(View.GONE);

        }




    }





}
