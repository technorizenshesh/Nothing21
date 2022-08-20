package com.nothing21.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.nothing21.ProductSingalCopyAct;
import com.nothing21.R;
import com.nothing21.ViewProductAdapter1;
import com.nothing21.databinding.ItemScrollProductOneBinding;
import com.nothing21.fragment.ProductNewFragment;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.listener.onIconClickProductListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductNewModel;
import com.nothing21.utils.SessionManager;

import java.util.ArrayList;

public class ScrollProductOneAdapterNew extends RecyclerView.Adapter<ScrollProductOneAdapterNew.MyViewHolder> implements onIconClickProductListener {
    Context context;
    ArrayList<ProductNewModel.Result> arrayList;
    ArrayList<String>imgArrayList;
    onIconClickListener listener;
    ViewProductAdapter1 adapter;
    ItemScrollProductOneBinding bindingMain;
    public
    ScrollProductOneAdapterNew(Context context, ArrayList<ProductNewModel.Result> arrayList,onIconClickListener listener) {
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


        Glide.with(context).load(arrayList.get(position).colorDetails.get(0).image).error(R.drawable.dummy).into(holder.binding.ivImg);

       // holder.binding.viewPager.setAdapter(new MyViewPagerProductAdapter(context,arrayList.get(position).colorDetails));
        holder.binding.rvColor.setAdapter(new ColorProductAdapter(context, arrayList.get(position).colorDetails,position,holder,ScrollProductOneAdapterNew.this,holder.binding.viewPager));

        setItemValue(position,0,0,holder);

      /*  holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).colorDetails.get(0).colorVariation.get(0).price)));
        holder.binding.tvProductName.setText(arrayList.get(position).name);

        if(!arrayList.get(position).discountedPrice.equals("0")) {
            holder.binding.tvOldPrice.setVisibility(View.VISIBLE);
            holder.binding.tvDiscount.setVisibility(View.VISIBLE);
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price) - Double.parseDouble(arrayList.get(position).discountedPrice )));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.color_red));
            holder.binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
            holder.binding.tvOldPrice.setPaintFlags(holder.binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.binding.tvDiscount.setText("-"+arrayList.get(position).discountedPrice + "% Off");

        }
        else {
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.tvOldPrice.setVisibility(View.GONE);
            holder.binding.tvDiscount.setVisibility(View.GONE);

        }*/

        if(arrayList.get(position).favProductStatus.equals("false")) holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_white_heart));
        else holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_red_heart));
        //setListData(position,holder.binding);



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onIcon(int position,int mainPosition,MyViewHolder holder, String type, ViewPager viewPager) {
        if(type.equals("color")) {
           // next(position,viewPager);
            setItemValue(mainPosition,position,0,holder);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemScrollProductOneBinding binding;
        public MyViewHolder(@NonNull ItemScrollProductOneBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            bindingMain = itemView;
            binding.ivCart.setOnClickListener(v -> {
                SessionManager.writeString(context,"avaQuantity",arrayList.get(getAdapterPosition()).colorDetails.get(0).colorVariation.get(0).remainingQuantity+"");
                SessionManager.writeString(context,"colorDetailsId",arrayList.get(getAdapterPosition()).colorDetails.get(0).colorId);

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




    public void filterList(ArrayList<ProductNewModel.Result> filterlist) {
        if(filterlist.size()==0)
            //   ProductAct.tvFound.setVisibility(View.VISIBLE);
            ProductNewFragment.tvFound.setVisibility(View.VISIBLE);

        else
            // ProductAct.tvFound.setVisibility(View.GONE);
            ProductNewFragment.tvFound.setVisibility(View.GONE);
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
       // vp.beginFakeDrag();
        vp.setCurrentItem(ii);
        Log.e("slideCurrentAdapter=="+ ii,bindingMain.viewPager.getCurrentItem()+"");

    }


    public void setItemValue(int mainPosition,int colorPosition,int variationPosition, MyViewHolder holder){
        Log.e("Image value==",arrayList.get(mainPosition).colorDetails.get(colorPosition).image);
        holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).price+"")));
        holder.binding.tvProductName.setText(arrayList.get(mainPosition).name);
        Glide.with(context).load(arrayList.get(mainPosition).colorDetails.get(colorPosition).image).error(R.drawable.dummy).into(holder.binding.ivImg);


        if(!arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceDiscount.equals("0")) {
            holder.binding.tvOldPrice.setVisibility(View.VISIBLE);
            holder.binding.tvDiscount.setVisibility(View.VISIBLE);
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceCalculated)));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.color_red));
            holder.binding.tvOldPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).price)));
            holder.binding.tvOldPrice.setPaintFlags(holder.binding.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.binding.tvDiscount.setText("-"+arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceDiscount + "% Off");

        }
        else {
            holder.binding.tvProductPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(mainPosition).colorDetails.get(colorPosition).colorVariation.get(variationPosition).priceCalculated)));
            holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.black));
            holder.binding.tvOldPrice.setVisibility(View.GONE);
            holder.binding.tvDiscount.setVisibility(View.GONE);

        }

    }

}
