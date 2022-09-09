package com.nothing21.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nothing21.HomeAct;
import com.nothing21.ProductSingalCopyAct;
import com.nothing21.R;
import com.nothing21.SearchAct;
import com.nothing21.adapter.CartAdapter;
import com.nothing21.adapter.ProductAdapter;
import com.nothing21.databinding.FragmentHomeBinding;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.CategoryModel;
import com.nothing21.model.ProductModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment  implements onItemClickListener {
    public static String TAG = "HomeFragment";
    FragmentHomeBinding binding;
    static int y;
    Nothing21Interface apiInterface;
    ArrayList<CategoryModel.Result>arrayList;
    ProductAdapter adapter;
   public static TextView tvFound;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        tvFound = binding.tvFound;
        arrayList = new ArrayList<>();
        adapter = new ProductAdapter(getActivity(),arrayList,HomeFragment.this);
        binding.rvProduct.setAdapter(adapter);

        binding.rvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged( RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(binding.rvProduct.SCROLL_STATE_DRAGGING==newState){
                    //fragProductLl.setVisibility(View.GONE);
                }
                if(binding.rvProduct.SCROLL_STATE_IDLE==newState){
                    // fragProductLl.setVisibility(View.VISIBLE);
                    if(y<=0){
                        HomeAct.cardTabs.animate().alpha(1.0f);
                        HomeAct.cardTabs.setVisibility(View.VISIBLE);

                    }
                    else{
                        y=0;
                        HomeAct.cardTabs.animate().alpha(0.0f);
                        HomeAct.cardTabs.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScrolled( RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                y = dy;
            }
        });

       /* binding.tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                getFilterSearch(query.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


        binding.tvSearch.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), SearchAct.class),1);
        });


        if (NetworkAvailablity.checkNetworkStatus(getActivity())) GetAllCate();
       else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        
    }


    public void GetAllCate(){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Call<CategoryModel> loginCall = apiInterface.getAllCategory();
        loginCall.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    CategoryModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Category Response :" + responseString);
                    if (data.status.equals("1")) {
                        binding.tvFound.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(data.result);
                         adapter.notifyDataSetChanged();




                    } else if (data.status.equals("0")){
                        binding.tvFound.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                        arrayList.clear();
                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public void getFilterSearch(String query){
        try {
            query = query.toLowerCase();

            final ArrayList<CategoryModel.Result> filteredList = new ArrayList<CategoryModel.Result>();

            if(arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    String text = arrayList.get(i).categoryName.toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(arrayList.get(i));
                    }

                }
                adapter.filterList(filteredList);


            }

        }catch (Exception e){
            e.printStackTrace();
        }

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

        FragTrans(new SubCatFragment(arrayList.get(position).id+""));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                //FragTrans(new ProductFragment(data.getStringExtra("id")));    ;
              //  FragTrans(new ProductNewFragment(data.getStringExtra("id"),data.getStringExtra("subCat_id")));    ;
             startActivity(new Intent(getActivity(), ProductSingalCopyAct.class)
             .putExtra("id",data.getStringExtra("id")));
            }
        }



    }
}
