package com.nothing21.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nothing21.ProductAct;
import com.nothing21.ProductSingalAct;
import com.nothing21.R;
import com.nothing21.ViewProductAdapter1;
import com.nothing21.databinding.ItemGirdProductBinding;
import com.nothing21.databinding.ItemScrollProductBinding;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.ProductModel;

import java.util.ArrayList;

public class GirdProductAdapter extends RecyclerView.Adapter<GirdProductAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductModel.Result> arrayList;
    ArrayList<String>imgArrayList;
    onIconClickListener listener;

    public GirdProductAdapter(Context context, ArrayList<ProductModel.Result> arrayList, onIconClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
        imgArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGirdProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_gird_product,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvPrice.setText("AED" + String.format("%.2f", Double.parseDouble(arrayList.get(position).price)));
        holder.binding.tvProductName.setText(arrayList.get(position).brand1);
        if(!arrayList.get(position).discount.equals("")) {
            holder.binding.tvProductName.setVisibility(View.VISIBLE);
            holder.binding.tvOffer.setText(arrayList.get(position).discount + "% Off");
        }
        else holder.binding.tvProductName.setVisibility(View.GONE);

        holder.binding.rvProductItm.setAdapter(new ViewProductAdapter2(context,arrayList.get(position).imageDetails,arrayList.get(position).id));

        if(arrayList.get(position).fav_product_status.equals("false")) holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_white_heart));
        else holder.binding.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_red_heart));


        // setListData(position,holder.binding);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemGirdProductBinding binding;
        public MyViewHolder(@NonNull ItemGirdProductBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.layoutMain.setOnClickListener(v -> context.startActivity(new Intent(context, ProductSingalAct.class)));


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


        }
    }

  /*  public void setListData(int position , ItemGirdProductBinding binding){
        //   imgArrayList.clear();
        if(!arrayList.get(position).image1.equals("")) imgArrayList.add(arrayList.get(position).image1);
        else if(!arrayList.get(position).image2.equals("")) imgArrayList.add(arrayList.get(position).image2);
        else if(!arrayList.get(position).image3.equals("")) imgArrayList.add(arrayList.get(position).image3);
        else if(!arrayList.get(position).image4.equals("")) imgArrayList.add(arrayList.get(position).image4);

        binding.rvProductItm.setAdapter(new ViewProductAdapter1(context,imgArrayList,arrayList.get(position).id));


    }*/
  public void filterList(ArrayList<ProductModel.Result> filterlist) {
      if(filterlist.size()==0)
          ProductAct.tvFound.setVisibility(View.VISIBLE);
      else
          ProductAct.tvFound.setVisibility(View.GONE);
      arrayList = filterlist;
      notifyDataSetChanged();
  }


}
