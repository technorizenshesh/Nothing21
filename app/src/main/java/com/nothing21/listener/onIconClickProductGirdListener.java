package com.nothing21.listener;

import androidx.viewpager.widget.ViewPager;

import com.nothing21.adapter.GirdProductAdapterNew;

public interface onIconClickProductGirdListener {
    void onGirdIcon(int position, int mainPosition, GirdProductAdapterNew.MyViewHolder holder, String type , ViewPager viewPager);

}
