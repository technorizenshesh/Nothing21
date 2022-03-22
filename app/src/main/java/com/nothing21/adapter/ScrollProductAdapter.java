package com.nothing21.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nothing21.ProductAct;
import com.nothing21.ProductSingalAct;
import com.nothing21.R;
import com.nothing21.ViewProductAdapter1;
import com.nothing21.databinding.ItemProductBinding;
import com.nothing21.databinding.ItemScrollProductBinding;
import com.nothing21.fragment.ProductFragment;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.ProductModel;

import java.util.ArrayList;

public class ScrollProductAdapter extends RecyclerView.Adapter<ScrollProductAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductModel.Result> arrayList;
    ArrayList<String>imgArrayList;
    onIconClickListener listener;
    

    public
    ScrollProductAdapter(Context context, ArrayList<ProductModel.Result> arrayList,onIconClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        imgArrayList = new ArrayList<>();
        this.listener  = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScrollProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_scroll_product,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // Glide.with(context).load(arrayList.get(position).image1).error(R.drawable.dummy).into(holder.binding.ivImg);
        holder.binding.tvPriceNew.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
        holder.binding.tvProductName.setText(arrayList.get(position).brand1);
        if(!arrayList.get(position).discount.equals("")) {
          //  holder.binding.tvProductName.setVisibility(View.VISIBLE);
            holder.binding.tvDiscountNew.setText("-"+arrayList.get(position).discount + "% Off");
          //  holder.binding.tvPrice.setTextColor(context.getResources().getColor(R.color.color_red));
        }
        // else    holder.binding.tvPrice.setTextColor(context.getResources().getColor(R.color.white));           //holder.binding.tvProductName.setVisibility(View.GONE);

        holder.binding.rvProductItm.setAdapter(new ViewProductAdapter1(context,arrayList.get(position).imageDetails,arrayList.get(position).id,arrayList.get(position).isTouchCheck()));


        if(arrayList.get(position).fav_product_status.equals("false")) holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_white_heart));
         else holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_red_heart));
        //setListData(position,holder.binding);

        if(arrayList.get(position).isTouchCheck()){
           // holder.binding.layoutHeader.setVisibility(View.VISIBLE);
          //  holder.binding.layoutCenter.setVisibility(View.VISIBLE);
         //   holder.binding.layoutBottom.setVisibility(View.VISIBLE);
            holder.binding.layoutMain.setVisibility(View.GONE);

        }
        else {
            //  holder.binding.layoutHeader.setVisibility(View.GONE);
            //   holder.binding.layoutCenter.setVisibility(View.GONE);
            //   holder.binding.layoutBottom.setVisibility(View.GONE);
            holder.binding.layoutMain.setVisibility(View.VISIBLE);
        }

        if(arrayList.get(position).colorDetails!=null){
            if(arrayList.get(position).colorDetails.size()==1){
                holder.binding.view1.setVisibility(View.VISIBLE);
                holder.binding.view2.setVisibility(View.GONE);
                holder.binding.view3.setVisibility(View.GONE);
                holder.binding.view4.setVisibility(View.GONE);
               // holder.binding.view1.setBackgroundColor(Color.parseColor(arrayList.get(position).colorDetails.get(0).color));
                GradientDrawable shape = new GradientDrawable();
                shape.setCornerRadius(30);
                shape.setColor(Color.parseColor(arrayList.get(position).colorDetails.get(0).color));
                holder.binding.view1.setBackground(shape);

            }

            if(arrayList.get(position).colorDetails.size()==2){
                holder.binding.view1.setVisibility(View.VISIBLE);
                holder.binding.view2.setVisibility(View.VISIBLE);
                holder.binding.view3.setVisibility(View.GONE);
                holder.binding.view4.setVisibility(View.GONE);
              //  holder.binding.view2.setBackgroundColor(Color.parseColor(arrayList.get(position).colorDetails.get(1).color));

                GradientDrawable shape1 = new GradientDrawable();
                shape1.setCornerRadius(30);
                shape1.setColor(Color.parseColor(arrayList.get(position).colorDetails.get(0).color));
                holder.binding.view1.setBackground(shape1);


                GradientDrawable shape2 = new GradientDrawable();
                shape2.setCornerRadius(30);
                shape2.setColor(Color.parseColor(arrayList.get(position).colorDetails.get(1).color));
                holder.binding.view2.setBackground(shape2);




            }


            if(arrayList.get(position).colorDetails.size()==3){
                holder.binding.view1.setVisibility(View.VISIBLE);
                holder.binding.view2.setVisibility(View.VISIBLE);
                holder.binding.view3.setVisibility(View.VISIBLE);
                holder.binding.view4.setVisibility(View.GONE);
              //  holder.binding.view3.setBackgroundColor(Color.parseColor(arrayList.get(position).colorDetails.get(2).color));
                GradientDrawable shape1 = new GradientDrawable();
                shape1.setCornerRadius(30);
                shape1.setColor(Color.parseColor(arrayList.get(position).colorDetails.get(0).color));
                holder.binding.view1.setBackground(shape1);


                GradientDrawable shape2 = new GradientDrawable();
                shape2.setCornerRadius(30);
                shape2.setColor(Color.parseColor(arrayList.get(position).colorDetails.get(1).color));
                holder.binding.view2.setBackground(shape2);

                GradientDrawable shape3 = new GradientDrawable();
                shape3.setCornerRadius(30);
                shape3.setColor(Color.parseColor(arrayList.get(position).colorDetails.get(2).color));
                holder.binding.view3.setBackground(shape3);


            }

            if(arrayList.get(position).colorDetails.size()==4){
                holder.binding.view1.setVisibility(View.VISIBLE);
                holder.binding.view2.setVisibility(View.VISIBLE);
                holder.binding.view3.setVisibility(View.VISIBLE);
                holder.binding.view4.setVisibility(View.VISIBLE);
            //    holder.binding.view4.setBackgroundColor(Color.parseColor(arrayList.get(position).colorDetails.get(3).color));

                GradientDrawable shape1 = new GradientDrawable();
                shape1.setCornerRadius(30);
                shape1.setColor(Color.parseColor(arrayList.get(position).colorDetails.get(0).color));
                holder.binding.view1.setBackground(shape1);


                GradientDrawable shape2 = new GradientDrawable();
                shape2.setCornerRadius(30);
                shape2.setColor(Color.parseColor(arrayList.get(position).colorDetails.get(1).color));
                holder.binding.view2.setBackground(shape2);

                GradientDrawable shape3 = new GradientDrawable();
                shape3.setCornerRadius(30);
                shape3.setColor(Color.parseColor(arrayList.get(position).colorDetails.get(2).color));
                holder.binding.view3.setBackground(shape3);


                GradientDrawable shape4 = new GradientDrawable();
                shape4.setCornerRadius(30);
                shape4.setColor(Color.parseColor(arrayList.get(position).colorDetails.get(3).color));
                holder.binding.view4.setBackground(shape4);


            }




        }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemScrollProductBinding binding;
        public MyViewHolder(@NonNull ItemScrollProductBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;






            binding.ivInfo.setOnClickListener(v -> {
                listener.onIcon(getAdapterPosition(),"Info");
            });

            binding.ivCart.setOnClickListener(v -> {
                listener.onIcon(getAdapterPosition(),"Cart");
            });


            binding.ivIn.setOnClickListener(v -> {
                listener.onIcon(getAdapterPosition(),"Size");
            });

            binding.ivColor.setOnClickListener(v -> {
                listener.onIcon(getAdapterPosition(),"Color");
            });


            binding.ivLike.setOnClickListener(v -> {
                listener.onIcon(getAdapterPosition(),"Fav");
            });


            binding.layoutMain.setOnClickListener(v -> {

             for (int i = 0;i<arrayList.size();i++){
                   arrayList.get(i).setTouchCheck(false);
               }
                arrayList.get(getAdapterPosition()).setTouchCheck(true);
               notifyDataSetChanged();
            });





        }
    }


   /* public void setListData(int position , ItemScrollProductBinding binding){
     //   imgArrayList.clear();
        if(!arrayList.get(position).image1.equals("")) imgArrayList.add(arrayList.get(position).image1);
          else if(!arrayList.get(position).image2.equals("")) imgArrayList.add(arrayList.get(position).image2);
         else if(!arrayList.get(position).image3.equals("")) imgArrayList.add(arrayList.get(position).image3);
          else if(!arrayList.get(position).image4.equals("")) imgArrayList.add(arrayList.get(position).image4);

        binding.rvProductItm.setAdapter(new ViewProductAdapter1(context,imgArrayList,arrayList.get(position).id));


    }*/

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


}

