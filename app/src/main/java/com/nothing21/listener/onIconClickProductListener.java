package com.nothing21.listener;

import androidx.viewpager.widget.ViewPager;

import com.nothing21.adapter.ScrollProductOneAdapterNew;

public interface onIconClickProductListener {
    void onIcon(int position, int mainPosition, ScrollProductOneAdapterNew.MyViewHolder holder, String type , ViewPager viewPager);

}
