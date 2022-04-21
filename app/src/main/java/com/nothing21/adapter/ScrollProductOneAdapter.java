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

import com.bumptech.glide.Glide;
import com.nothing21.ProductSingalAct;
import com.nothing21.R;
import com.nothing21.ViewProductAdapter1;
import com.nothing21.databinding.ItemScrollProductBinding;
import com.nothing21.databinding.ItemScrollProductOneBinding;
import com.nothing21.fragment.ProductFragment;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.ProductModel;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.SessionManager;

import java.util.ArrayList;

public class ScrollProductOneAdapter extends RecyclerView.Adapter<ScrollProductOneAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductModel.Result> arrayList;
    ArrayList<String>imgArrayList;
    onIconClickListener listener;
    ViewProductAdapter1 adapter;
    LinearLayoutManager layoutManager;

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




        holder.binding.rvProductItm.setAdapter(new ViewProductAdapter1(context,arrayList.get(position).colorDetails,arrayList.get(position).id,arrayList.get(position).isTouchCheck()));
        Glide.with(context).load(arrayList.get(position).colorDetails.get(0).image).error(R.drawable.dummy).into(holder.binding.ivImg);
        DataManager.getInstance().hideProgressMessage();


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









       if(arrayList.get(position).colorDetails!=null){
            if(arrayList.get(position).colorDetails.size()==1){
                holder.binding.layoutOne.setVisibility(View.VISIBLE);
                holder.binding.layoutTwo.setVisibility(View.GONE);
                holder.binding.layoutThree.setVisibility(View.GONE);
                holder.binding.layoutFour.setVisibility(View.GONE);

                if(arrayList.get(position).colorDetails.get(0).chkColor== false){
                    holder.binding.view1.setVisibility(View.GONE);
                    holder.binding.view11.setSolidColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.ivImg.setVisibility(View.GONE);
                    holder.binding.rvProductItm.setVisibility(View.VISIBLE);

                }else {
                    holder.binding.view1.setVisibility(View.VISIBLE);
                    holder.binding.view1.setStrokeWidth(1);
                    holder.binding.view1.setStrokeColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.view11.setSolidColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.ivImg.setVisibility(View.VISIBLE);
                    holder.binding.rvProductItm.setVisibility(View.GONE);
                    SessionManager.writeString(context,"selectImage",arrayList.get(position).colorDetails.get(0).image);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(0).image).error(R.drawable.dummy).into(holder.binding.ivImg);

                }
            }

            if(arrayList.get(position).colorDetails.size()==2){
                holder.binding.layoutOne.setVisibility(View.VISIBLE);
                holder.binding.layoutTwo.setVisibility(View.VISIBLE);
                holder.binding.layoutThree.setVisibility(View.GONE);
                holder.binding.layoutFour.setVisibility(View.GONE);

                if(arrayList.get(position).colorDetails.get(0).chkColor == false){
                    holder.binding.view1.setVisibility(View.GONE);
                    holder.binding.view11.setSolidColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.ivImg.setVisibility(View.GONE);
                    holder.binding.rvProductItm.setVisibility(View.VISIBLE);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(0).image).error(R.drawable.dummy).into(holder.binding.ivImg);

                }else {
                    holder.binding.view1.setVisibility(View.VISIBLE);
                    holder.binding.view1.setStrokeWidth(1);
                    holder.binding.view1.setStrokeColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.view1.setSolidColor("#FFFFFF");
                    holder.binding.view11.setSolidColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.ivImg.setVisibility(View.VISIBLE);
                    holder.binding.rvProductItm.setVisibility(View.GONE);
                    SessionManager.writeString(context,"selectImage",arrayList.get(position).colorDetails.get(0).image);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(0).image).error(R.drawable.dummy).into(holder.binding.ivImg);
                }


                if(arrayList.get(position).colorDetails.get(1).chkColor == false){
                    holder.binding.view2.setVisibility(View.GONE);
                    holder.binding.view22.setSolidColor(arrayList.get(position).colorDetails.get(1).colorCode);
                    holder.binding.ivImg.setVisibility(View.GONE);
                    holder.binding.rvProductItm.setVisibility(View.VISIBLE);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(1).image).error(R.drawable.dummy).into(holder.binding.ivImg);


                }else {
                    holder.binding.view2.setVisibility(View.VISIBLE);
                    holder.binding.view2.setStrokeWidth(1);
                    holder.binding.view2.setStrokeColor(arrayList.get(position).colorDetails.get(1).colorCode);
                    holder.binding.view2.setSolidColor("#FFFFFF");
                    holder.binding.view22.setSolidColor(arrayList.get(position).colorDetails.get(1).colorCode);
                    holder.binding.ivImg.setVisibility(View.VISIBLE);
                    holder.binding.rvProductItm.setVisibility(View.GONE);
                    SessionManager.writeString(context,"selectImage",arrayList.get(position).colorDetails.get(1).image);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(1).image).error(R.drawable.dummy).into(holder.binding.ivImg);
                }


            }


            if(arrayList.get(position).colorDetails.size()==3){
                holder.binding.layoutOne.setVisibility(View.VISIBLE);
                holder.binding.layoutTwo.setVisibility(View.VISIBLE);
                holder.binding.layoutThree.setVisibility(View.VISIBLE);
                holder.binding.layoutFour.setVisibility(View.GONE);

                if(arrayList.get(position).colorDetails.get(0).chkColor== false){
                    holder.binding.view1.setVisibility(View.GONE);
                    holder.binding.view11.setSolidColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.ivImg.setVisibility(View.GONE);
                    holder.binding.rvProductItm.setVisibility(View.VISIBLE);

                }else {
                    holder.binding.view1.setVisibility(View.VISIBLE);
                    holder.binding.view1.setStrokeWidth(1);
                    holder.binding.view1.setStrokeColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.view1.setSolidColor("#FFFFFF");
                    holder.binding.view11.setSolidColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.ivImg.setVisibility(View.VISIBLE);
                    holder.binding.rvProductItm.setVisibility(View.GONE);
                    SessionManager.writeString(context,"selectImage",arrayList.get(position).colorDetails.get(0).image);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(0).image).error(R.drawable.dummy).into(holder.binding.ivImg);

                }


                if(arrayList.get(position).colorDetails.get(1).chkColor== false){
                    holder.binding.view2.setVisibility(View.GONE);
                    holder.binding.view22.setSolidColor(arrayList.get(position).colorDetails.get(1).colorCode);
                    holder.binding.ivImg.setVisibility(View.GONE);
                    holder.binding.rvProductItm.setVisibility(View.VISIBLE);

                }else {
                    holder.binding.view2.setVisibility(View.VISIBLE);
                    holder.binding.view2.setStrokeWidth(1);
                    holder.binding.view2.setStrokeColor(arrayList.get(position).colorDetails.get(1).colorCode);
                    holder.binding.view2.setSolidColor("#FFFFFF");
                    holder.binding.view22.setSolidColor(arrayList.get(position).colorDetails.get(1).colorCode);
                    holder.binding.ivImg.setVisibility(View.VISIBLE);
                    holder.binding.rvProductItm.setVisibility(View.GONE);
                    SessionManager.writeString(context,"selectImage",arrayList.get(position).colorDetails.get(1).image);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(1).image).error(R.drawable.dummy).into(holder.binding.ivImg);

                }

                if(arrayList.get(position).colorDetails.get(2).chkColor== false){
                    holder.binding.view3.setVisibility(View.GONE);
                    holder.binding.view33.setSolidColor(arrayList.get(position).colorDetails.get(2).colorCode);
                    holder.binding.ivImg.setVisibility(View.GONE);
                    holder.binding.rvProductItm.setVisibility(View.VISIBLE);
                }else {
                    holder.binding.view3.setVisibility(View.VISIBLE);
                    holder.binding.view3.setStrokeWidth(1);
                    holder.binding.view3.setStrokeColor(arrayList.get(position).colorDetails.get(2).colorCode);
                    holder.binding.view3.setSolidColor("#FFFFFF");
                    holder.binding.view33.setSolidColor(arrayList.get(position).colorDetails.get(2).colorCode);
                    holder.binding.ivImg.setVisibility(View.VISIBLE);
                    holder.binding.rvProductItm.setVisibility(View.GONE);
                    SessionManager.writeString(context,"selectImage",arrayList.get(position).colorDetails.get(2).image);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(2).image).error(R.drawable.dummy).into(holder.binding.ivImg);

                }





            }

            if(arrayList.get(position).colorDetails.size()==4){
                holder.binding.layoutOne.setVisibility(View.VISIBLE);
                holder.binding.layoutTwo.setVisibility(View.VISIBLE);
                holder.binding.layoutThree.setVisibility(View.VISIBLE);
                holder.binding.layoutFour.setVisibility(View.VISIBLE);

                if(arrayList.get(position).colorDetails.get(0).chkColor== false){
                    holder.binding.view1.setVisibility(View.GONE);
                    holder.binding.view11.setSolidColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.ivImg.setVisibility(View.GONE);
                    holder.binding.rvProductItm.setVisibility(View.VISIBLE);

                }else {
                    holder.binding.view1.setVisibility(View.VISIBLE);
                    holder.binding.view1.setStrokeWidth(1);
                    holder.binding.view1.setStrokeColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.view1.setSolidColor("#FFFFFF");
                    holder.binding.view11.setSolidColor(arrayList.get(position).colorDetails.get(0).colorCode);
                    holder.binding.ivImg.setVisibility(View.VISIBLE);
                    holder.binding.rvProductItm.setVisibility(View.GONE);
                    SessionManager.writeString(context,"selectImage",arrayList.get(position).colorDetails.get(0).image);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(0).image).error(R.drawable.dummy).into(holder.binding.ivImg);

                }


                if(arrayList.get(position).colorDetails.get(1).chkColor== false){
                    holder.binding.view2.setVisibility(View.GONE);
                    holder.binding.view22.setSolidColor(arrayList.get(position).colorDetails.get(1).colorCode);
                    holder.binding.ivImg.setVisibility(View.GONE);
                    holder.binding.rvProductItm.setVisibility(View.VISIBLE);

                }else {
                    holder.binding.view2.setVisibility(View.VISIBLE);
                    holder.binding.view2.setStrokeWidth(1);
                    holder.binding.view2.setStrokeColor(arrayList.get(position).colorDetails.get(1).colorCode);
                    holder.binding.view2.setSolidColor("#FFFFFF");
                    holder.binding.view22.setSolidColor(arrayList.get(position).colorDetails.get(1).colorCode);
                    holder.binding.ivImg.setVisibility(View.VISIBLE);
                    holder.binding.rvProductItm.setVisibility(View.GONE);
                    SessionManager.writeString(context,"selectImage",arrayList.get(position).colorDetails.get(1).image);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(1).image).error(R.drawable.dummy).into(holder.binding.ivImg);

                }

                if(arrayList.get(position).colorDetails.get(2).chkColor== false){
                    holder.binding.view3.setVisibility(View.GONE);
                    holder.binding.view33.setSolidColor(arrayList.get(position).colorDetails.get(2).colorCode);
                    holder.binding.ivImg.setVisibility(View.GONE);
                    holder.binding.rvProductItm.setVisibility(View.VISIBLE);

                }else {
                    holder.binding.view3.setVisibility(View.VISIBLE);
                    holder.binding.view3.setStrokeWidth(1);
                    holder.binding.view3.setStrokeColor(arrayList.get(position).colorDetails.get(2).colorCode);
                    holder.binding.view3.setSolidColor("#FFFFFF");
                    holder.binding.view33.setSolidColor(arrayList.get(position).colorDetails.get(2).colorCode);
                    holder.binding.ivImg.setVisibility(View.VISIBLE);
                    holder.binding.rvProductItm.setVisibility(View.GONE);
                    SessionManager.writeString(context,"selectImage",arrayList.get(position).colorDetails.get(2).image);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(2).image).error(R.drawable.dummy).into(holder.binding.ivImg);

                }


                if(arrayList.get(position).colorDetails.get(3).chkColor== false){
                    holder.binding.view4.setVisibility(View.GONE);
                    holder.binding.view44.setSolidColor(arrayList.get(position).colorDetails.get(3).colorCode);
                    holder.binding.ivImg.setVisibility(View.GONE);
                    holder.binding.rvProductItm.setVisibility(View.VISIBLE);

                }else {
                    holder.binding.view4.setVisibility(View.VISIBLE);
                    holder.binding.view4.setStrokeWidth(1);
                    holder.binding.view4.setStrokeColor(arrayList.get(position).colorDetails.get(3).colorCode);
                    holder.binding.view4.setSolidColor("#FFFFFF");
                    holder.binding.view44.setSolidColor(arrayList.get(position).colorDetails.get(3).colorCode);
                    holder.binding.ivImg.setVisibility(View.VISIBLE);
                    holder.binding.rvProductItm.setVisibility(View.GONE);
                    SessionManager.writeString(context,"selectImage",arrayList.get(position).colorDetails.get(3).image);
                    Glide.with(context).load(arrayList.get(position).colorDetails.get(3).image).error(R.drawable.dummy).into(holder.binding.ivImg);

                }



            }




        }




        holder.binding.layoutOne.setOnClickListener(v -> {
            // DataManager.getInstance().showProgressMessage((Activity) context,"");
            for(int i =0; i<arrayList.get(position).colorDetails.size();i++){
                arrayList.get(position).colorDetails.get(i).setChkColor(false);
            }
            arrayList.get(position).colorDetails.get(0).setChkColor(true);
            notifyItemChanged(position);
        });


        holder.binding.layoutTwo.setOnClickListener(v -> {
            // DataManager.getInstance().showProgressMessage((Activity) context,"");
            for(int i =0; i<arrayList.get(position).colorDetails.size();i++){
                arrayList.get(position).colorDetails.get(i).setChkColor(false);
            }
            arrayList.get(position).colorDetails.get(1).setChkColor(true);

            notifyItemChanged(position);
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemScrollProductOneBinding binding;
        public MyViewHolder(@NonNull ItemScrollProductOneBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.ivCart.setOnClickListener(v -> {
                SessionManager.writeString(context,"selectImage",arrayList.get(getAdapterPosition()).colorDetails.get(0).image);
                listener.onIcon(getAdapterPosition(),"Cart");
            });

            binding.ivLike.setOnClickListener(v -> {
                listener.onIcon(getAdapterPosition(),"Fav");
            });

            binding.layoutSecond.setOnClickListener(v -> { context.startActivity(new Intent(context, ProductSingalAct.class).putExtra("id",arrayList.get(getAdapterPosition()).id));

            });


           binding.ivImg.setOnClickListener(v -> {
               if(binding.rvProductItm.getVisibility() == View.GONE){
                   binding.rvProductItm.setVisibility(View.VISIBLE);
                   binding.ivImg.setVisibility(View.GONE);
               }

           });





            binding.layoutThree.setOnClickListener(v -> {
                DataManager.getInstance().showProgressMessage((Activity) context,"");
                for(int i =0; i<arrayList.get(getAdapterPosition()).colorDetails.size();i++){
                    arrayList.get(getAdapterPosition()).colorDetails.get(i).setChkColor(false);
                }
                arrayList.get(getAdapterPosition()).colorDetails.get(2).setChkColor(true);

                notifyItemChanged(getAdapterPosition());
            });


            binding.layoutFour.setOnClickListener(v -> {
                DataManager.getInstance().showProgressMessage((Activity) context,"");
                for(int i =0; i<arrayList.get(getAdapterPosition()).colorDetails.size();i++){
                    arrayList.get(getAdapterPosition()).colorDetails.get(i).setChkColor(false);
                }
                arrayList.get(getAdapterPosition()).colorDetails.get(3).setChkColor(true);
                notifyItemChanged(getAdapterPosition());
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

}
