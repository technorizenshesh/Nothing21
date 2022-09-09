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
import com.nothing21.ReturnListAct;
import com.nothing21.adapter.AdapterFilterSize;
import com.nothing21.adapter.CatSpinnerAdapter;
import com.nothing21.adapter.OrderStatusAdapter;
import com.nothing21.databinding.FragmentCatSubCatBinding;
import com.nothing21.databinding.FragmentSizeFilterBinding;
import com.nothing21.listener.FilterListener;
import com.nothing21.listener.onCatListener;
import com.nothing21.listener.onPositionClickListener;
import com.nothing21.listener.onSearchListener;
import com.nothing21.model.CatSubCatModel;
import com.nothing21.model.OrderStatusModel;
import com.nothing21.model.SizeListModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchByCatSubCatBottomSheet extends BottomSheetDialogFragment implements onCatListener {
    public String TAG = "SearchByCatSubCatBottomSheet";
    BottomSheetDialog dialog;
    FragmentCatSubCatBinding binding;
    Nothing21Interface apiInterface;
    private BottomSheetBehavior<View> mBehavior;
    public onSearchListener listener;
    CatSpinnerAdapter adapter;
    ArrayList<CatSubCatModel.Result> arrayList;

    public SearchByCatSubCatBottomSheet() {
    }


    public SearchByCatSubCatBottomSheet callBack(onSearchListener listener) {
        this.listener = listener;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_cat_sub_cat, null, false);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        initBinding();
        return dialog;
    }

    private void initBinding() {
        arrayList = new ArrayList<>();

        adapter = new CatSpinnerAdapter(getActivity(), arrayList, SearchByCatSubCatBottomSheet.this);
        binding.rvSize.setAdapter(adapter);

        if (NetworkAvailablity.checkNetworkStatus(getActivity())) getAllCatSubCat();
        else
            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


        binding.ivBack.setOnClickListener(v -> dismiss());


    }


    private void getAllCatSubCat() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<ResponseBody> chatCount = apiInterface.getCatSubCatApi();
        chatCount.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "getCatSubCat Response = " + stringResponse);
                    if (jsonObject.getString("status").equals("1")) {
                        CatSubCatModel catSubCatModel = new Gson().fromJson(stringResponse, CatSubCatModel.class);
                        arrayList.clear();
                        arrayList.addAll(catSubCatModel.getResult());
                        adapter.notifyDataSetChanged();
                    } else {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    @Override
    public void onCat(String catId,String catName, String subCatId,String subCatName) {
        listener.onSearch(catId,catName,subCatId,subCatName);  // AddCommaValues()
        dismiss();
    }
}

