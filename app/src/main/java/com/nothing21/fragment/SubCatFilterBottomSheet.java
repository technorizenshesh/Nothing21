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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.nothing21.R;
import com.nothing21.adapter.AdapterFilterCategoty;
import com.nothing21.adapter.AdapterFilterSubCat;
import com.nothing21.databinding.FragmentCategoryFilterBinding;
import com.nothing21.listener.FilterListener;
import com.nothing21.listener.onPositionClickListener;
import com.nothing21.model.CategoryListModel;
import com.nothing21.model.SubCatFilterModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCatFilterBottomSheet  extends BottomSheetDialogFragment implements onPositionClickListener {
    public String TAG = "SubCatFilterBottomSheet";
    BottomSheetDialog dialog;
    FragmentCategoryFilterBinding binding;
    Nothing21Interface apiInterface;
    private BottomSheetBehavior<View> mBehavior;
    public FilterListener listener;
    AdapterFilterSubCat adapter;
    ArrayList<SubCatFilterModel.Result> arrayList;
    SubCatFilterModel.Result colorListModel;
    double total = 0.00;
    ArrayList<String> addItemList;
    String categoryFilter ="";

    public SubCatFilterBottomSheet() {
    }


    public SubCatFilterBottomSheet callBack(FilterListener listener) {
        this.listener = listener;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_category_filter, null, false);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        initBinding();
        return dialog;
    }

    private void initBinding() {
        arrayList = new ArrayList<>();
        addItemList = new ArrayList<>();

        binding.tvTitle.setText(getString(R.string.sub_categoty));

        adapter = new AdapterFilterSubCat(getActivity(), arrayList, SubCatFilterBottomSheet.this);
        binding.rvCategory.setAdapter(adapter);

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) getAllSubServices();
        else
            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.btnApply.setOnClickListener(v -> {
            //   if (addItemList.size() == 0)
            if (categoryFilter.equals(""))    Toast.makeText(getActivity(), getString(R.string.please_select_sub_category), Toast.LENGTH_SHORT).show();
            else {
                listener.onFilter("sub category",categoryFilter ); // AddCommaValues()
                dismiss();
            }
        });

        binding.ivBack.setOnClickListener(v -> dismiss());


    }


    private void getAllSubServices() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<SubCatFilterModel> chatCount = apiInterface.getAllSubCategory();
        chatCount.enqueue(new Callback<SubCatFilterModel>() {
            @Override
            public void onResponse(Call<SubCatFilterModel> call, Response<SubCatFilterModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SubCatFilterModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e(TAG, "Get All Sub -Category RESPONSE" + dataResponse);
                    if (data.getStatus().equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.getResult());
                        adapter.notifyDataSetChanged();

                    } else if (data.getStatus().equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), data.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<SubCatFilterModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    @Override
    public void onPosition(int position, String status) {
        colorListModel = arrayList.get(position);
       /* if (status.equals("true")) {
            addItemList.add(arrayList.get(position).getId());
        } else if (status.equals("false")) {
            if (addItemList.size() > 0) addItemList.remove(position);

        }*/

        categoryFilter = arrayList.get(position).getId();

        String val = AddCommaValues();


    }

    public String AddCommaValues() {
        StringBuilder str = new StringBuilder("");

        // Traversing the ArrayList
        for (String eachstring : addItemList) {

            // Each element in ArrayList is appended
            // followed by comma
            str.append(eachstring).append(",");
        }

        // StringBuffer to String conversion
        String commaseparatedlist = str.toString();

        // By following condition you can remove the last
        // comma
        if (commaseparatedlist.length() > 0)
            commaseparatedlist
                    = commaseparatedlist.substring(
                    0, commaseparatedlist.length() - 1);

        Log.e("AddedString===", commaseparatedlist);
        return commaseparatedlist;
    }
}
