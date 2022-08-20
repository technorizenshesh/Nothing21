package com.nothing21.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.nothing21.R;
import com.nothing21.listener.ImageListener;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.model.ProductModelCopyNew;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.util.ArrayList;



public class MyViewPagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    Context context;
    ArrayList<ProductModelCopyNew.Result.ColorDetail> arrayList;
    ImageListener listener;

    public MyViewPagerAdapter( Context context, ArrayList<ProductModelCopyNew.Result.ColorDetail> arrayList,ImageListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener =listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_images, container, false);
        ImageView imageViewZoom = view.findViewById(R.id.ivImg);
        Glide.with(context).load(arrayList.get(position).image).error(R.drawable.dummy).into(imageViewZoom);

        imageViewZoom.setOnClickListener(v -> {listener.onImage(position);});

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
}
