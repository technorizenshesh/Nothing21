package com.nothing21.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.nothing21.R;
import com.nothing21.adapter.RateAdapter;
import com.nothing21.databinding.FragmentInfoBinding;
import com.nothing21.databinding.FragmentRateBinding;
import com.nothing21.listener.InfoListener;
import com.nothing21.model.ProductModelCopy;
import com.nothing21.model.RateModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateBottomsheet extends BottomSheetDialogFragment {
    public String TAG = "RateBottomsheet";
    FragmentRateBinding binding;
    BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;
    InfoListener listener;
    ProductModelCopy.Result productData;
    Nothing21Interface apiInterface;
    RateAdapter adapter;
    ArrayList<RateModel.Result>arrayList;

    public RateBottomsheet(ProductModelCopy.Result productData) {
        this.productData = productData;
    }


    public RateBottomsheet callBack(InfoListener listener) {
        this.listener = listener;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_rate, null, false);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initViews();
        return  dialog;
    }

    private void initViews() {
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        arrayList = new ArrayList<>();


        Glide.with(getActivity()).load(SessionManager.readString(getActivity(),"selectImage",""))
                .apply(RequestOptions.bitmapTransform( new BlurTransformation(25, 3)))
                .into(binding.BlurImageView);

        binding.BlurImageView.setBlur(10);

        adapter = new RateAdapter(getActivity(),arrayList);
        binding.rvRating.setAdapter(adapter);

        if(NetworkAvailablity.checkNetworkStatus(getActivity())) getAllRate();
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();



    }


    private void getAllRate() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("product_id","1");

        Log.e(TAG,"Get AllRate Request : "+map.toString());
        Call<RateModel> loginCall = apiInterface.getAllRatesss(map);
        loginCall.enqueue(new Callback<RateModel>() {
            @Override
            public void onResponse(Call<RateModel> call, Response<RateModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    RateModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Get Dashboard Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                        binding.tvAvgRate.setText(data.avgRating+"");
                        binding.RatingBar.setRating(Float.parseFloat(data.avgRating));
                        setChar(String.valueOf(data.rating5),String.valueOf(data.rating4),
                                String.valueOf(data.rating3),String.valueOf(data.rating2)
                                ,String.valueOf(data.rating1));

                    } else if (data.status.equals("0")) {
                        dialog.dismiss();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RateModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void setChar(String d5,String d4,String d3,String d2,String d1) {
        BarData data = new BarData(getDataSet(d5,d4,d3,d2,d1));
        binding.chart.setData(data);
        binding.chart.animateXY(2000, 2000);
        binding.chart.invalidate();
        binding.chart.getAxisRight().setDrawGridLines(false);
        binding.chart.getAxisLeft().setDrawGridLines(false);
        binding.chart.getXAxis().setEnabled(false);
        binding.chart.getXAxis().setDrawAxisLine(false);
        binding.chart.getDescription().setEnabled(false);
        binding.chart.getAxisLeft().setDrawLabels(false);
        binding.chart.getAxisRight().setDrawLabels(false);
        //chart.getXAxis().setDrawLabels(false);

        binding.chart.getLegend().setEnabled(false);


        //remove left border
        binding.chart.getAxisLeft().setDrawAxisLine(false);

        //remove right border
        binding.chart.getAxisRight().setDrawAxisLine(false);
    }


    private BarDataSet getDataSet(String d1, String d2, String d3,String d4,String d5) {

        ArrayList<BarEntry> entries = new ArrayList();
        entries.add(new BarEntry(0f, Float.parseFloat(d5)));
        entries.add(new BarEntry(2f, Float.parseFloat(d4)));

        entries.add(new BarEntry(4f, Float.parseFloat(d3)));
        entries.add(new BarEntry(6f, Float.parseFloat(d2)));
        entries.add(new BarEntry(8f, Float.parseFloat(d1)));
        //   entries.add(new BarEntry(12f, 3));
        //    entries.add(new BarEntry(18f, 4));
        //     entries.add(new BarEntry(9f, 5));

        BarDataSet dataset = new BarDataSet(entries,"hi");
        dataset.setColors(new int[]{R.color.color_red , R.color.color_red,R.color.color_red,R.color.color_red , R.color.color_red} , getActivity());

        return dataset;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> labels = new ArrayList();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        return labels;
    }




}
