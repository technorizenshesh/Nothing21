package com.nothing21.fragment;

import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nothing21.HomeAct;
import com.nothing21.LoginAct;
import com.nothing21.OrderPlaceAct;
import com.nothing21.ProductAct;
import com.nothing21.ProductSingalAct;
import com.nothing21.adapter.CartAdapter;
import com.nothing21.R;
import com.nothing21.databinding.FragmentCartBinding;
import com.nothing21.listener.InfoListener;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.GetCartModel;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductModelCopy;
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

public class CartFragment extends Fragment implements onIconClickListener, InfoListener {
    public String TAG = "CartFragment";
    FragmentCartBinding binding;
    ArrayList<GetCartModel.Result> arrayList;
    CartAdapter adapter;
    Nothing21Interface apiInterface;
    GetCartModel data11;
    String refreshedToken = "",userId="";
    ArrayList<String> stringArrayList = new ArrayList<>();
    ArrayList<String> stringArrayListCartId = new ArrayList<>();
    double totalAmt =0.0,totalCountAmt=0.0;

    static int y;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);

        arrayList = new ArrayList<>();

        adapter = new CartAdapter(getActivity(), arrayList, CartFragment.this);
        binding.rvCart.setAdapter(adapter);











        binding.rvCart.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (binding.rvCart.SCROLL_STATE_DRAGGING == newState) {
                    //fragProductLl.setVisibility(View.GONE);
                }
                if (binding.rvCart.SCROLL_STATE_IDLE == newState) {
                    // fragProductLl.setVisibility(View.VISIBLE);
                    if (y <= 0) {
                        HomeAct.cardTabs.animate().alpha(1.0f);
                        HomeAct.cardTabs.setVisibility(View.VISIBLE);

                    } else {
                        y = 0;
                        HomeAct.cardTabs.animate().alpha(0.0f);
                        HomeAct.cardTabs.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                y = dy;
            }
        });


         binding.btnCheckout.setOnClickListener(v -> {
             if(!SessionManager.readString(getActivity(), Constant.USER_INFO,"").equals("")) {
                 getActivity().startActivity(new Intent(getActivity(), OrderPlaceAct.class)
                         .putExtra("product_id",AddCommaValues())
                         .putExtra("cart_id",AddCommaValues11())
                         .putExtra("amount",totalAmt+""));


             }else LogInAlert();
         });






    }

    public void getCartLists() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        Log.e(TAG, "get Cart List Request :" + map);

        Call<GetCartModel> loginCall = apiInterface.getCartList(map);
        loginCall.enqueue(new Callback<GetCartModel>() {
            @Override
            public void onResponse(Call<GetCartModel> call, Response<GetCartModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    data11 = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "get Cart List Response :" + responseString);
                    if (data11.status.equals("1")) {
                        arrayList.clear();
                        stringArrayList.clear();
                        stringArrayListCartId.clear();
                        binding.tvFound.setVisibility(View.GONE);
                        binding.layoutHeader.setVisibility(View.VISIBLE);
                        arrayList.addAll(data11.result);
                        for (int i=0;i<arrayList.size();i++){
                            stringArrayList.add(arrayList.get(i).productId);
                            stringArrayListCartId.add(arrayList.get(i).cartId);
                            if(!arrayList.get(i).discount.equals("")){
                                totalCountAmt =   totalCountAmt +  Double.parseDouble(arrayList.get(i).price) - Double.parseDouble(arrayList.get(i).discount );
                            }else {
                                totalCountAmt = totalCountAmt + Double.parseDouble(arrayList.get(i).price);
                            }

                        }
                        Log.e("prolist_size===",stringArrayList.size()+"");
                        adapter.notifyDataSetChanged();
                        totalAmt = Double.parseDouble(data11.totalAmount);
                        binding.tvPrice.setText("AED " + String.format("%.2f",totalAmt ));


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
            public void onFailure(Call<GetCartModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                arrayList.clear();
                adapter.notifyDataSetChanged();
                binding.tvFound.setVisibility(View.VISIBLE);
                binding.layoutHeader.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onIcon(int position, String type) {
        if (type.equals("Delete")) {
            DeleteAlert(arrayList.get(position).cartId);
        }
        else if(type.equals("Edit")) {
            SessionManager.writeString(getActivity(),"selectImage",arrayList.get(position).image);
            if (NetworkAvailablity.checkNetworkStatus(getActivity())) GetProduct(arrayList.get(position).productId,position);
            else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        }
    }

    private void DeleteAlert(String ids) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_delete_this_cart_item));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (NetworkAvailablity.checkNetworkStatus(getActivity()))
                            deleteCartItem(ids);
                        else
                            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
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

    private void deleteCartItem(String ids) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("cart_id", ids);
        Log.e(TAG, "Delete Cart Request :" + map);
        Call<Map<String, String>> loginCall = apiInterface.deleteCart(map);
        loginCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String, String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Delete Cart Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) getCartLists();
                        else
                            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                    } else if (data.get("status").equals("0")) {


                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



    public void GetProduct(String productId,int pos){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("product_id",productId);
        Call<ProductModelCopy> loginCall = apiInterface.getProduct(map);
        loginCall.enqueue(new Callback<ProductModelCopy>() {
            @Override
            public void onResponse(Call<ProductModelCopy> call, Response<ProductModelCopy> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    ProductModelCopy   data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.status.equals("1")) {
                        callEdit(data,pos);
                    } else if (data.status.equals("0")){
                        // Toast.makeText(ProductAct.this, data.message, Toast.LENGTH_SHORT).show();


                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductModelCopy> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    private void callEdit(ProductModelCopy data,int pos) {
        new EditCartBottomSheet(data.result,arrayList.get(pos),data11.totalAmount+"").callBack(this::info).show(getActivity().getSupportFragmentManager(),"");

    }

    @Override
    public void info(String value,String size) {
        if(value.equals("Edit")){
            if (NetworkAvailablity.checkNetworkStatus(getActivity())) getCartLists();
            else
                Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }


    public void LogInAlert(){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(getResources().getString(R.string.please_login));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Login",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        getActivity().startActivity(new Intent(getActivity(), LoginAct.class)
                        .putExtra("check_out","CART")
                        .putExtra("product_id",AddCommaValues())
                        .putExtra("cart_id",AddCommaValues11())
                                .putExtra("amount",totalAmt+""));
                      //  getActivity().finish();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                       // getActivity().startActivity(new Intent(getActivity(), HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        //getActivity().finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public String  AddCommaValues(){
        StringBuilder str = new StringBuilder("");

        // Traversing the ArrayList
        for (String eachstring : stringArrayList) {

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

        Log.e("AddedString===",commaseparatedlist);
        return commaseparatedlist;
    }

    public String  AddCommaValues11(){
        StringBuilder str = new StringBuilder("");

        // Traversing the ArrayList
        for (String eachstring : stringArrayListCartId) {

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

        Log.e("AddedString===",commaseparatedlist);
        return commaseparatedlist;
    }

    @Override
    public void onResume() {
        super.onResume();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                if(!SessionManager.readString(getActivity(), Constant.USER_INFO,"").equals("")) {
                    userId = DataManager.getInstance().getUserData(getActivity()).result.id;
                }
                else   userId = instanceIdResult.getToken();     //LogInAlert();

                refreshedToken = instanceIdResult.getToken();

                if (NetworkAvailablity.checkNetworkStatus(getActivity())) getCartLists();
                else
                    Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

                Log.e("Token===", userId);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
