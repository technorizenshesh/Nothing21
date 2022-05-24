package com.nothing21.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.nothing21.ProductSingalCopyAct;
import com.nothing21.R;
import com.nothing21.model.ProductModel;

import java.util.ArrayList;

public class MyViewPagerProductGirdAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    Context context;
    ArrayList<ProductModel.Result.ColorDetail> arrayList;
    String iddd;

    public MyViewPagerProductGirdAdapter( Context context, ArrayList<ProductModel.Result.ColorDetail> arrayList,String iddd) {
        this.context = context;
        this.arrayList = arrayList;
        this.iddd = iddd;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_images, container, false);
        ImageView ivImg = view.findViewById(R.id.ivImg);
        RelativeLayout layoutMain = view.findViewById(R.id.layoutMain);
        Glide.with(context).load(arrayList.get(position).image).error(R.drawable.dummy).into(ivImg);

        layoutMain.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ProductSingalCopyAct.class).putExtra("id",iddd));

        });

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
