package com.nothing21.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.nothing21.ProductSingalAct;
import com.nothing21.R;
import com.nothing21.model.ProductModel;

import java.util.ArrayList;

public class ViewProductAdapter extends PagerAdapter {
    Context context;
    ArrayList<String>imgArrayList;
    LayoutInflater mLayoutInflater;

    public ViewProductAdapter(Context context,  ArrayList<String>imgArrayList) {
        this.context = context;
        this.imgArrayList = imgArrayList;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_product_imag, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.ivImg);
        Glide.with(context).load(imgArrayList.get(position)).error(R.drawable.dummy).into(imageView);


        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return imgArrayList.size();
    }




    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);


    }



}
