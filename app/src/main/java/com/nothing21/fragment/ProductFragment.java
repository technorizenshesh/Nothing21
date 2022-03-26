package com.nothing21.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nothing21.HomeAct;
import com.nothing21.ProductAct;
import com.nothing21.R;
import com.nothing21.adapter.GirdProductAdapter;
import com.nothing21.adapter.ScrollProductAdapter;
import com.nothing21.adapter.ScrollProductOneAdapter;
import com.nothing21.databinding.ActivityProductBinding;
import com.nothing21.databinding.FragmentProductBinding;
import com.nothing21.listener.InfoListener;
import com.nothing21.listener.onIconClickListener;
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

public class ProductFragment extends Fragment implements onIconClickListener, InfoListener {
    public String TAG = "ProductFragment";
    FragmentProductBinding binding;
    Nothing21Interface apiInterface;
    ArrayList<ProductModel.Result> arrayList;
   // ScrollProductAdapter adapterScroll;
    ScrollProductOneAdapter adapterScroll;
    GirdProductAdapter adapterGrid;
    String viewType = "vertical",catId="";
    public static TextView tvFound ;
    String refreshedToken = "",userId="",sortData = "DESC",filterString="Name";
    boolean chk = false;
    static int y = 0;


    public ProductFragment(String catId) {
        this.catId = catId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        tvFound = binding.tvFound;


        HomeAct.cardTabs.animate().alpha(0.0f);
        HomeAct.cardTabs.setVisibility(View.GONE);
        HomeAct.cardTabIcons.animate().alpha(1.0f);
        HomeAct.cardTabIcons.setVisibility(View.VISIBLE);


        binding.rvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapterScroll = new ScrollProductOneAdapter(getActivity(),arrayList,ProductFragment.this);
        //  binding.rvProducts.scrollToPosition(0);
        // binding.rvProducts.setLayoutFrozen(true);
        binding.rvProducts.setAdapter(adapterScroll);



        adapterGrid =  new GirdProductAdapter(getActivity(),arrayList,ProductFragment.this);

        binding.ivDisplay.setOnClickListener(v -> {
            if(sortData.equals("DESC")) sortData = "ASC";
            else if(sortData.equals("ASC")) sortData = "DESC";
            if(NetworkAvailablity.checkNetworkStatus(getActivity())) GetAllProduct(catId,sortData);
            else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        });


        binding.ivFilter.setOnClickListener(v -> {
             popupMenuDialog();
        });



        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                if(!SessionManager.readString(getActivity(), Constant.USER_INFO,"").equals("")) {
                    userId = DataManager.getInstance().getUserData(getActivity()).result.id;
                }
                else   userId = instanceIdResult.getToken();     //LogInAlert();

                refreshedToken = instanceIdResult.getToken();

              //  if(getIntent()!=null){
                  //  catId = getIntent().getStringExtra("catId");
                    if(NetworkAvailablity.checkNetworkStatus(getActivity())) GetAllProduct(catId,sortData);
                    else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
             //   }

                Log.e("Token===", userId);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        binding.ivDesign.setOnClickListener(v -> {
            if(chk == false){
                viewType = "grid";
                binding.rvProducts.setLayoutManager(new GridLayoutManager(getActivity(),2));
                binding.rvProducts.setAdapter(adapterGrid);
                chk = true;
            }
            else {
                viewType = "vertical";
                binding.rvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                binding.rvProducts.setAdapter(adapterScroll);
                chk = false;
            }


        });



        binding.tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                getFilterSearch(query.toString(),filterString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (binding.rvProducts.SCROLL_STATE_DRAGGING == newState) {
                    //fragProductLl.setVisibility(View.GONE);
                }
                if (binding.rvProducts.SCROLL_STATE_IDLE == newState) {
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







    }

    private void popupMenuDialog() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), binding.ivFilter);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Toast message on menu item clicked
                filterString = String.valueOf(menuItem.getTitle());
                Toast.makeText(getActivity(), "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // Showing the popup menu
        popupMenu.show();
    }


    public void GetAllProduct(String catId,String sortData){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("category_id",catId);
        map.put("user_id",userId);
        map.put("order_by",sortData);
        Call<ProductModel> loginCall = apiInterface.getAllProduct(map);
        loginCall.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    ProductModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        binding.tvFound.setVisibility(View.GONE);
                        arrayList.addAll(data.result);
                        adapterGrid.notifyDataSetChanged();
                        adapterScroll.notifyDataSetChanged();
                    } else if (data.status.equals("0")){
                        // Toast.makeText(ProductAct.this, data.message, Toast.LENGTH_SHORT).show();
                        arrayList.clear();
                        adapterScroll.notifyDataSetChanged();
                        adapterGrid.notifyDataSetChanged();
                        binding.tvFound.setVisibility(View.VISIBLE);

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
                adapterScroll.notifyDataSetChanged();
                binding.tvFound.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onIcon(int position, String type) {
        if(type.equals("Info")){
            if(!arrayList.get(position).description.equals(""))
                new InfoFragmentBottomSheet(arrayList.get(position)).callBack(this::info).show(getChildFragmentManager(),"");
            else   Toast.makeText(getActivity(), getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        }
        else if(type.equals("Cart")){

            if(arrayList.get(position).colorDetails.size()!=0) {
                SessionManager.writeString(getActivity(),"selectImage",arrayList.get(position).imageDetails.get(0).image);
                new CartFragmentBootomSheet(arrayList.get(position)).callBack(this::info).show(getChildFragmentManager(),"");
            }
            else Toast.makeText(getActivity(), getString(R.string.not_available), Toast.LENGTH_SHORT).show();

        }
        else if(type.equals("Color")){
            if(arrayList.get(position).colorDetails.size()!=0){
                SessionManager.writeString(getActivity(),"selectImage",arrayList.get(position).colorDetails.get(0).image);
                new ColorSizeFragmentBottomSheet(arrayList.get(position),arrayList.get(position).colorDetails.get(0).size).callBack(this::info).show(getChildFragmentManager(),"");
            }
            else Toast.makeText(getActivity(), getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        }
        else if(type.equals("Size")){
            if(arrayList.get(position).colorDetails.size()!=0) {
                SessionManager.writeString(getActivity(),"selectImage",arrayList.get(position).colorDetails.get(0).image);
                new SizeFragmentBottomSheet(arrayList.get(position),arrayList.get(position).colorDetails.get(0).color).callBack(this::info).show(getChildFragmentManager(),"");
            }
            else Toast.makeText(getActivity(), getString(R.string.not_available), Toast.LENGTH_SHORT).show();

        }


        else if(type.equals("Fav")){
            if(NetworkAvailablity.checkNetworkStatus(getActivity()))addFavrirr(arrayList.get(position).id+"");
            else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void info(String value,String size) {

    }



    public void addFavrirr(String proId){
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("product_id",proId);
        Call<Map<String,String>> loginCall = apiInterface.addFav(map);
        loginCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.get("status").equals("1")) {

                        if(NetworkAvailablity.checkNetworkStatus(getActivity())) GetAllProduct(catId,sortData);
                        else Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


                    } else if (data.get("status").equals("0")){

                        if(NetworkAvailablity.checkNetworkStatus(getActivity())) GetAllProduct(catId,sortData);
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


    /// user_id=41&product_id=1




    public void getFilterSearch(String query,String filter){
        try {
            query = query.toLowerCase();

            final ArrayList<ProductModel.Result> filteredList = new ArrayList<ProductModel.Result>();

            if(arrayList != null) {
                if(filter.equals("Name")){
                    for (int i = 0; i < arrayList.size(); i++) {
                        String text = arrayList.get(i).name.toLowerCase();
                        //  String brand = arrayList.get(i).brand1.toLowerCase();
                        if (text.contains(query)) {
                            filteredList.add(arrayList.get(i));
                        }

                    }
                }
                else  if(filter.equals("Brand")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        String brand = arrayList.get(i).brand1.toLowerCase();
                        if ( brand.contains(query)) {
                            filteredList.add(arrayList.get(i));
                        }

                    }
                }


                else  if(filter.equals("Price")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        String price = arrayList.get(i).price.toLowerCase();
                        if ( price.contains(query)) {
                            filteredList.add(arrayList.get(i));
                        }

                    }
                }


                else  if(filter.equals("Color")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        for (int j = 0; j < arrayList.get(i).colorDetails.size(); j++) {

                            String color = arrayList.get(i).colorDetails.get(j).color.toLowerCase();
                            if (color.contains(query)) {
                                filteredList.add(arrayList.get(i));
                            }
                        }

                    }
                }

                else  if(filter.equals("Size")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        for (int j = 0; j < arrayList.get(i).colorDetails.size(); j++) {

                            String size = arrayList.get(i).colorDetails.get(j).size.toLowerCase();
                            if (size.contains(query)) {
                                filteredList.add(arrayList.get(i));
                            }
                        }

                    }
                }



                if(viewType.equals("vertical"))   adapterScroll.filterList(filteredList);
                else if(viewType.equals("grid"))   adapterGrid.filterList(filteredList);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
