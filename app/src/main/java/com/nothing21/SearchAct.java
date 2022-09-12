package com.nothing21;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.nothing21.adapter.AdapterSearch;
import com.nothing21.databinding.ActivitySearchBinding;
import com.nothing21.fragment.SearchByCatSubCatBottomSheet;
import com.nothing21.fragment.SizeFilterBottomSheet;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.listener.onSearchListener;
import com.nothing21.model.SearchModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.NetworkAvailablity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAct extends AppCompatActivity implements onItemClickListener , onSearchListener {
    public String TAG = "SearchAct";
    Nothing21Interface apiInterface;
    private ArrayList<SearchModel.Result> arrayList = new ArrayList<>();
    private AdapterSearch adapter;
    private ActivitySearchBinding binding;
    private String queryString = "",subCatId="",catId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        BindView();
    }

    private void BindView() {
        //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.search.setIconified(false);
        binding.search.setQueryHint(getString(R.string.search_product_name1));
        adapter = new AdapterSearch(this, arrayList, this);
        binding.recyList.setAdapter(adapter);
        binding.swiperRefresh.setOnRefreshListener(this::getProduct);
        binding.search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return false;
            }
        });
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                queryString = s;
                getProduct();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                queryString = s;
                getProduct();
                return false;
            }
        });



        binding.tvCatSubCat.setOnClickListener(v -> callCatSubCat() );


    }

    private void callCatSubCat() {
        new SearchByCatSubCatBottomSheet().callBack(this::onSearch).show(getSupportFragmentManager(), "");
    }



    private void getProduct() {
        binding.swiperRefresh.setRefreshing(true);
        Map<String, String> map = new HashMap<>();
        map.put("name", queryString);
        map.put("subcat_id", subCatId);
        Log.e(TAG, "Search Product Request :" + queryString);
        Call<SearchModel> loginCall = apiInterface.searchProduct(map);
        loginCall.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                binding.swiperRefresh.setRefreshing(false);
                try {
                    SearchModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Search Product Response :" + responseString);
                    if (data.status.equals("1")) {
                        binding.tvNotFound.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();

                    } else if (data.status.equals("0")) {
                        // App.showToast(getActivity(), data.message, Toast.LENGTH_SHORT);
                        binding.tvNotFound.setVisibility(View.VISIBLE);
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                call.cancel();
                binding.swiperRefresh.setRefreshing(false);
            }
        });
    }





    @Override
    public void onItem(int position) {
        /*startActivity(new Intent(SearchAct.this, ShopDetailsAct.class)
                .putExtra("shopId",arrayList.get(position).id+"")
                .putExtra("seller_id",arrayList.get(position).sellerId));*/
       // FragTrans(new ProductFragment(arrayList.get(position).categoryId+""));
       // finish();
        Intent intent = new Intent();
       // intent.putExtra("id", catId/*arrayList.get(position).categoryId+""*/);
      //  intent.putExtra("subCat_id", subCatId+"");
          intent.putExtra("id", arrayList.get(position).productId+"");
          setResult(RESULT_OK, intent);
        finish();
    }


    public void FragTrans(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
    }

    @Override
    public void onSearch(String catId,String catName, String subCatId,String subCatName) {
        this.subCatId = subCatId;
        this.catId = catId;
        binding.tvCatSubCat.setText(subCatName);
        if(NetworkAvailablity.checkNetworkStatus(SearchAct.this)) getProduct();
        else Toast.makeText(SearchAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

    }
}
