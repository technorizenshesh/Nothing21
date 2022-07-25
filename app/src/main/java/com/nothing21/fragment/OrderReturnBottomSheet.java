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
import com.nothing21.R;
import com.nothing21.databinding.FragmentGiveRateBinding;
import com.nothing21.databinding.FragmentOrderReturnBinding;
import com.nothing21.listener.InfoListener;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderReturnBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "OrderReturnBottomSheet";
    FragmentOrderReturnBinding binding;
    BottomSheetDialog dialog;
    private BottomSheetBehavior<View> mBehavior;
    InfoListener listener;
    Nothing21Interface apiInterface;
    String orderId = "";

    public OrderReturnBottomSheet(String orderId) {
        this.orderId = orderId;
    }


    public OrderReturnBottomSheet callBack(InfoListener listener) {
        this.listener = listener;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_order_return, null, false);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initViews();
        return dialog;
    }

    private void initViews() {
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding.ivBack.setOnClickListener(v -> dismiss());

        binding.btnRate.setOnClickListener(v -> {
            if (NetworkAvailablity.checkNetworkStatus(getActivity())) OrderReturn();
            else
                Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        });
    }


    public void OrderReturn() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).result.id);
        map.put("order_id", orderId);
        map.put("comment", binding.etReason.getText().toString());
        map.put("status","Return");
        Log.e(TAG, "OrderReturn Request :" + map);
        Call<ResponseBody> loginCall = apiInterface.returnOrder(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    String stringResponse = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "OrderReturn Response = " + stringResponse);
                        if (jsonObject.getString("status").equals("1")) {
                            listener.info("Done", binding.etReason.getText().toString());  // AddCommaValues()
                            dismiss();
                        } else {
                            //  Toast.makeText(OrderStatusAct.this, jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONException", "JSONException = " + e.getMessage());
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




}
