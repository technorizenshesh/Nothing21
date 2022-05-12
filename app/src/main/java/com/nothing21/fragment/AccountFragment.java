package com.nothing21.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nothing21.HomeAct;

import com.nothing21.OrderStatusAct;
import com.nothing21.R;
import com.nothing21.adapter.MyOrderAdapter;
import com.nothing21.adapter.MyOrderAdapterTwo;
import com.nothing21.databinding.FragmentAccountBinding;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.FavModel;
import com.nothing21.model.ProductModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Constant;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment implements onIconClickListener {
    public String TAG = "AccountFragment";
    FragmentAccountBinding binding;
    Nothing21Interface apiInterface;
    ArrayList<FavModel.Result> arrayList;
    ArrayList<ProductModel.Result> arrayListTwo;

    MyOrderAdapter adapter;
    MyOrderAdapterTwo adapterTwo;

    String refreshedToken = "",userId="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        if(SessionManager.readString(getActivity(), Constant.USER_INFO,"").equals("")){
            binding.tvName.setText("Hi Guest");
            binding.tvLogout.setVisibility(View.GONE);
        }
        else {
            binding.tvName.setText(DataManager.getInstance().getUserData(getActivity()).result.firstName + " " +
                    DataManager.getInstance().getUserData(getActivity()).result.lastName);
            binding.tvLogout.setVisibility(View.VISIBLE);
        }


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                if(!SessionManager.readString(getActivity(), Constant.USER_INFO,"").equals("")) {
                    userId = DataManager.getInstance().getUserData(getActivity()).result.id;
                }
                else   userId = instanceIdResult.getToken();     //LogInAlert();

                refreshedToken = instanceIdResult.getToken();

                 tabSelect(1);

                Log.e("Token===", userId);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.viewScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int x = scrollY - oldScrollY;
                    if (x > 0) {
                        //scroll up
                       // HomeAct.cardTabs.animate().alpha(1.0f);
                        HomeAct.cardTabs.setVisibility(View.VISIBLE);
                    } else if (x < 0) {
                        //scroll down
                      //  HomeAct.cardTabs.animate().alpha(0.0f);
                        HomeAct.cardTabs.setVisibility(View.VISIBLE);
                    } else {
                        HomeAct.cardTabs.setVisibility(View.VISIBLE);

                    }

                }
            });
        }

        binding.tvLogout.setOnClickListener(v -> {
            LogOutAlert();
        });


       binding.layoutProcessing.setOnClickListener(v -> {
            if(!SessionManager.readString(getActivity(), Constant.USER_INFO,"").equals("")){
              startActivity(new Intent(getActivity(), OrderStatusAct.class));
            }
        });


        arrayList = new ArrayList<>();
        arrayListTwo = new ArrayList<>();



        tabSelect(1);

        binding.tvWishList.setOnClickListener(v -> {
            tabSelect(1);
        });


        binding.tvView.setOnClickListener(v -> {
            tabSelect(2);
        });

    }



    public void LogOutAlert(){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_logout_this_app));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        SessionManager.clear(getActivity(), DataManager.getInstance().getUserData(getActivity()).result.id+"");
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


   /* public void getAllMyOrder(){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Log.e(TAG, "get My Order List Request :" + map);
        Call<MyOrderModel> loginCall = apiInterface.getMyOrders(map);
        loginCall.enqueue(new Callback<MyOrderModel>() {
            @Override
            public void onResponse(Call<MyOrderModel> call, Response<MyOrderModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    MyOrderModel  data11 = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "get My Order List Response :" + responseString);
                    if (data11.status.equals("1")) {
                        arrayList.clear();
                        binding.tvFound.setVisibility(View.GONE);
                        binding.layoutHeader.setVisibility(View.VISIBLE);
                        arrayList.addAll(data11.result);

                        adapter.notifyDataSetChanged();
                    } else if (data11.status.equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvFound.setVisibility(View.VISIBLE);
                        binding.layoutHeader.setVisibility(View.GONE);


                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MyOrderModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                arrayList.clear();
                adapter.notifyDataSetChanged();
                binding.tvFound.setVisibility(View.VISIBLE);
                binding.layoutHeader.setVisibility(View.GONE);

            }
        });
    }*/

    public void tabSelect(int i){
        if(i==1){
            binding.tvWishList.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
            binding.tvView.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            binding.tvWishList.setTextColor(getActivity().getResources().getColor(R.color.white));
            binding.tvView.setTextColor(getActivity().getResources().getColor(R.color.black));
            binding.rvFavList.setVisibility(View.VISIBLE);
            if(NetworkAvailablity.checkNetworkStatus(getActivity())) getAllFav();
            else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
        else if(i==2){
            binding.tvWishList.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            binding.tvView.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
            binding.tvWishList.setTextColor(getActivity().getResources().getColor(R.color.black));
            binding.tvView.setTextColor(getActivity().getResources().getColor(R.color.white));
            binding.rvFavList.setVisibility(View.VISIBLE);
         //   binding.tvFound.setVisibility(View.VISIBLE);
            if(NetworkAvailablity.checkNetworkStatus(getActivity())) GetAllWishList();
            else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        }
    }




    public void getAllFav(){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Log.e(TAG, "get Fav List Request :" + map);
        Call<FavModel> loginCall = apiInterface.getAllFav(map);
        loginCall.enqueue(new Callback<FavModel>() {
            @Override
            public void onResponse(Call<FavModel> call, Response<FavModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    FavModel  data11 = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "get Fav List Response :" + responseString);
                    if (data11.status.equals("1")) {
                        arrayList.clear();
                        binding.tvFound.setVisibility(View.GONE);
                        binding.layoutHeader.setVisibility(View.VISIBLE);
                        binding.rvFavList.setVisibility(View.VISIBLE);
                        arrayList.addAll(data11.result);
                        binding.rvFavList.setAdapter(new MyOrderAdapter(getActivity(), arrayList,AccountFragment.this));
                    } else if (data11.status.equals("0")) {
                        arrayList.clear();
                        binding.tvFound.setVisibility(View.VISIBLE);
                        binding.rvFavList.setVisibility(View.GONE);

                        //  binding.layoutHeader.setVisibility(View.GONE);


                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FavModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                arrayList.clear();
                adapter.notifyDataSetChanged();
                binding.tvFound.setVisibility(View.VISIBLE);
                binding.layoutHeader.setVisibility(View.GONE);

            }
        });
    }


    public void GetAllWishList(){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
      //  map.put("category_id",catId);
        map.put("user_id",userId);
      //  map.put("order_by",sortData);
        Call<ProductModel> loginCall = apiInterface.getRecentView(map);
        loginCall.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    ProductModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayListTwo.clear();
                        binding.tvFound.setVisibility(View.GONE);
                        binding.rvFavList.setVisibility(View.VISIBLE);
                        arrayListTwo.addAll(data.result);
                        binding.rvFavList.setAdapter(new MyOrderAdapterTwo(getActivity(), arrayListTwo));
                    } else if (data.status.equals("0")){
                        // Toast.makeText(ProductAct.this, data.message, Toast.LENGTH_SHORT).show();
                        arrayList.clear();
                        binding.tvFound.setVisibility(View.VISIBLE);
                        binding.rvFavList.setVisibility(View.GONE);

                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                arrayList.clear();
                adapterTwo.notifyDataSetChanged();
                binding.tvFound.setVisibility(View.VISIBLE);
            }
        });
    }


    public void DeleteProWish1(String wishId){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("wish_id",wishId);
        map.put("user_id",userId);
        Call<Map<String,String>> loginCall = apiInterface.deleteWishList(map);
        loginCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        if(NetworkAvailablity.checkNetworkStatus(getActivity())) getAllFav();
                        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    } else if (data.get("status").equals("0")){
                        // Toast.makeText(ProductAct.this, data.message, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });









    }



    public void DeleteProWish(String proId){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("product_id",proId);
        Log.e(TAG,"Remove Fav Req===" + map.toString());
        Call<Map<String,String>> loginCall = apiInterface.addFav(map);
        loginCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Remove fav Response :" + responseString);
                    if (data.get("status").equals("1")) {

                        if(NetworkAvailablity.checkNetworkStatus(getActivity())) getAllFav();
                        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


                    } else if (data.get("status").equals("0")){

                        if(NetworkAvailablity.checkNetworkStatus(getActivity())) getAllFav();
                        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }







    @Override
    public void onIcon(int position, String type) {
     DeleteWishListAlert(arrayList.get(position).id);
    }


    public void DeleteWishListAlert(String idss){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_delete_this_product_from_wish_list));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if(NetworkAvailablity.checkNetworkStatus(getActivity())) DeleteProWish(idss);
                        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



}