package com.nothing21.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.nothing21.R;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.model.ProductNewModel;

import java.util.ArrayList;

public class MyViewPagerProductAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    Context context;
    ArrayList<ProductNewModel.Result.ColorDetail> arrayList;

    public MyViewPagerProductAdapter( Context context, ArrayList<ProductNewModel.Result.ColorDetail> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_images, container, false);
        ImageView ivImg = view.findViewById(R.id.ivImg);
        Glide.with(context).load(arrayList.get(position).image).error(R.drawable.dummy).into(ivImg);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = (View) object;
        container.removeView(v);
    }

    @Override
    public boolean isViewFromObject(View v, Object object) {
        return v == object;
    }


    @Override
    public int getItemPosition(Object object) {
        //return super.getItemPosition(object);
        return PagerAdapter.POSITION_NONE;
    }

}
