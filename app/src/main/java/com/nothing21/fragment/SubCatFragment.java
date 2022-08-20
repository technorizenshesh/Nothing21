package com.nothing21.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nothing21.HomeAct;
import com.nothing21.R;
import com.nothing21.adapter.ProductAdapter;
import com.nothing21.adapter.SubCatAdapter;
import com.nothing21.databinding.FragmentSubCatBinding;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.CategoryModel;
import com.nothing21.model.SubCatModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCatFragment extends Fragment implements onItemClickListener {
    public static String TAG = "SubCatFragment";
    Nothing21Interface apiInterface;
    FragmentSubCatBinding binding;
    ArrayList<SubCatModel.Result> arrayList;
    SubCatAdapter adapter;
    String catId = "";
    static int y = 0;
    public static TextView tvFound;




    public SubCatFragment(String catId) {
        this.catId = catId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_cat, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        initViews();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                Log.i(TAG, "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i(TAG, "onKey Back listener is working!!!");
                    startActivity(new Intent(getActivity(), HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();
                    return true;
                }
                return false;
            }


        });

    }

    private void initViews() {
        arrayList = new ArrayList<>();
        tvFound = binding.tvFound;

        arrayList = new ArrayList<>();
        adapter = new SubCatAdapter(getActivity(),arrayList,SubCatFragment.this);
        binding.rvSubCat.setAdapter(adapter);

        HomeAct.cardTabs.animate().alpha(0.0f);
        HomeAct.cardTabs.setVisibility(View.GONE);
        HomeAct.cardTabIcons.animate().alpha(1.0f);
        HomeAct.cardTabIcons.setVisibility(View.VISIBLE);



        binding.rvSubCat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (binding.rvSubCat.SCROLL_STATE_DRAGGING == newState) {
                    //fragProductLl.setVisibility(View.GONE);
                }
                if (binding.rvSubCat.SCROLL_STATE_IDLE == newState) {
                    // fragProductLl.setVisibility(View.VISIBLE);
                   /* if (y <= 0) {
                        HomeAct.cardTabs.animate().alpha(1.0f);
                        HomeAct.cardTabs.setVisibility(View.VISIBLE);
                        HomeAct.cardTabIcons.animate().alpha(0.0f);
                        HomeAct.cardTabIcons.setVisibility(View.GONE);

                    } else {
                        y = 0;
                        HomeAct.cardTabs.animate().alpha(0.0f);
                        HomeAct.cardTabs.setVisibility(View.GONE);
                        HomeAct.cardTabIcons.animate().alpha(1.0f);
                        HomeAct.cardTabIcons.setVisibility(View.VISIBLE);
                    }*/

                    HomeAct.cardTabs.animate().alpha(0.0f);
                    HomeAct.cardTabs.setVisibility(View.GONE);
                    HomeAct.cardTabIcons.animate().alpha(1.0f);
                    HomeAct.cardTabIcons.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                y = dy;
            }
        });


        HomeAct.cardTabIcons.setOnClickListener(v -> {
            if (y <= 0) {
                HomeAct.cardTabs.animate().alpha(1.0f);
                HomeAct.cardTabs.setVisibility(View.VISIBLE);
                HomeAct.cardTabIcons.animate().alpha(0.0f);
                HomeAct.cardTabIcons.setVisibility(View.GONE);

            } else {
                y = 0;
                HomeAct.cardTabs.animate().alpha(0.0f);
                HomeAct.cardTabs.setVisibility(View.GONE);
                HomeAct.cardTabIcons.animate().alpha(1.0f);
                HomeAct.cardTabIcons.setVisibility(View.VISIBLE);
            }
        });


        if (NetworkAvailablity.checkNetworkStatus(getActivity())) GetSubCate(catId);
        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();



    }




    public void GetSubCate(String catId){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("category_id",catId);
        Log.e(TAG, "sub Category Request :" + map);

        Call<SubCatModel> loginCall = apiInterface.getAllSubCategory(map);
        loginCall.enqueue(new Callback<SubCatModel>() {
            @Override
            public void onResponse(Call<SubCatModel> call, Response<SubCatModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    SubCatModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "sub Category Response :" + responseString);
                    if (data.getStatus().equals("1")) {
                        binding.tvFound.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(data.getResult());
                        adapter.notifyDataSetChanged();




                    } else if (data.getStatus().equals("0")){
                        binding.tvFound.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), data.getStatus(), Toast.LENGTH_SHORT).show();
                        arrayList.clear();
                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SubCatModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public void FragTrans(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
    }


    @Override
    public void onItem(int position) {
        //   FragTrans(new ProductFragment(arrayList.get(position).id+""));
        //  FragTrans(new ProductFragment(catId,arrayList.get(position).getId()+""));
        FragTrans(new ProductNewFragment(catId,arrayList.get(position).getId()+""));

    }
}
