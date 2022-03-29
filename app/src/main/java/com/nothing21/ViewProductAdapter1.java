package com.nothing21;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nothing21.adapter.ViewProductAdapter;
import com.nothing21.databinding.ItemProductImagBinding;
import com.nothing21.databinding.ItemProductOneBinding;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.ProductModel;
import com.nothing21.retrofit.Constant;
import com.nothing21.utils.SessionManager;

import java.util.ArrayList;

public class ViewProductAdapter1 extends RecyclerView.Adapter<ViewProductAdapter1.MyViewHolder> {
    Context context;
    ArrayList<ProductModel.Result.ColorDetail> imgArrayList;
    ViewProductAdapter adapter;
    String idss;
    boolean chk;


    public ViewProductAdapter1(Context context, ArrayList<ProductModel.Result.ColorDetail> arrayList, String idss,boolean chk) {
        this.context = context;
        this.imgArrayList = arrayList;
        this.idss = idss;
        this.chk =chk;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductOneBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_product_one, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(imgArrayList.get(position).image).error(R.drawable.dummy).into(holder.binding.ivImg);


    }

    @Override
    public int getItemCount() {
        Log.e("size=====", imgArrayList.size()+"");
        return imgArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemProductOneBinding binding;

        public MyViewHolder(@NonNull ItemProductOneBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.layoutMain.setOnClickListener(v ->{

          //  if(!SessionManager.readString(context, Constant.USER_INFO,"").equals("")){

             //  if(chk== true) {
             //      context.startActivity(new Intent(context, ProductSingalAct.class).putExtra("id",idss));
             //  }
         //   }
         //   else LogInAlert();

            // binding.layoutMain.setOnClickListener(v -> context.startActivity(new Intent(context, ProductSingalAct.class)));

             //   if(chk== true)  context.startActivity(new Intent(context, ProductSingalAct.class).putExtra("id",idss));



            });
        }
    }

    public void LogInAlert(){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(context.getResources().getString(R.string.please_login));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Login",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        context.startActivity(new Intent(context, LoginAct.class));
                        ((Activity)context).finish();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}