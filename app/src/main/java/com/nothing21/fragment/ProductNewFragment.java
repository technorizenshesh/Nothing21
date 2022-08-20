package com.nothing21.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nothing21.HomeAct;
import com.nothing21.R;
import com.nothing21.adapter.GirdProductAdapterNew;
import com.nothing21.adapter.ScrollProductOneAdapterNew;
import com.nothing21.databinding.FragmentProductBinding;
import com.nothing21.listener.FilterListener;
import com.nothing21.listener.InfoListener;
import com.nothing21.listener.onIconClickListener;
import com.nothing21.model.ProductModel;
import com.nothing21.model.ProductNewModel;
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

public class ProductNewFragment extends Fragment implements onIconClickListener, InfoListener, FilterListener {
    public String TAG = "ProductFragment";
    FragmentProductBinding binding;
    Nothing21Interface apiInterface;
    ArrayList<ProductNewModel.Result> arrayList;
    // ScrollProductAdapter adapterScroll;
    ScrollProductOneAdapterNew adapterScroll;
    GirdProductAdapterNew adapterGrid;
    String viewType = "vertical", catId = "",subCatId="";
    public static TextView tvFound;
    String refreshedToken = "", userId = "", sortData = "DESC", filterString = "Name";
    boolean chk = false;
    static int y = 0;


    public ProductNewFragment(String catId,String subCatId) {
        this.catId = catId;
        this.subCatId = subCatId;

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

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                Log.i(TAG, "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i(TAG, "onKey Back listener is working!!!");
                    // startActivity(new Intent(getActivity(), HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    // getActivity().finish();
                    // getActivity().onBackPressed();
                    FragTrans(new SubCatFragment(catId+""));

                    return true;
                }
                return false;
            }


        });

    }

    private void initViews() {
        arrayList = new ArrayList<>();
        tvFound = binding.tvFound;


        HomeAct.cardTabs.animate().alpha(0.0f);
        HomeAct.cardTabs.setVisibility(View.GONE);
        HomeAct.cardTabIcons.animate().alpha(1.0f);
        HomeAct.cardTabIcons.setVisibility(View.VISIBLE);

        SessionManager.writeString(getActivity(),"selectImage","");

        binding.rvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapterScroll = new ScrollProductOneAdapterNew(getActivity(), arrayList, ProductNewFragment.this);
        //  binding.rvProducts.scrollToPosition(0);
        // binding.rvProducts.setLayoutFrozen(true);
        binding.rvProducts.setAdapter(adapterScroll);


        adapterGrid = new GirdProductAdapterNew(getActivity(), arrayList, ProductNewFragment.this);

        binding.ivDisplay.setOnClickListener(v -> {
            if (sortData.equals("DESC")) sortData = "ASC";
            else if (sortData.equals("ASC")) sortData = "DESC";
            if (NetworkAvailablity.checkNetworkStatus(getActivity()))
                GetAllProduct(catId,subCatId, sortData,"",0);
            else
                Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        });


        binding.ivFilter.setOnClickListener(v -> {
            popupMenuDialog();
        });




        binding.ivDesign.setOnClickListener(v -> {
            if (chk == false) {
                viewType = "grid";
                binding.rvProducts.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                binding.rvProducts.setAdapter(adapterGrid);
                chk = true;
            } else {
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
               // getFilterSearch(query.toString(), filterString);
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
            public boolean onMenuItemClick(MenuItem item) {
                // Toast message on menu item clicked
                filterString = String.valueOf(item.getTitle());
                //  Toast.makeText(getActivity(), "You Clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();


                switch (item.getItemId()) {
                    case R.id.menuPrice:
                        if (!item.isChecked()) {
                            item.setChecked(true);
                           // callCategory();
                            //  Toast.makeText(getActivity(), "Price", Toast.LENGTH_SHORT).show();
                        } else {
                            item.setChecked(false);
                        }
                        break;
                    case R.id.menuColor:
                        if (!item.isChecked()) {
                            item.setChecked(true);
                            //    Toast.makeText(getActivity(), "Color", Toast.LENGTH_SHORT).show();
                          //  callColor();
                        } else {
                            item.setChecked(false);
                        }
                        break;
                    case R.id.menuSize:
                        if (!item.isChecked()) {
                            item.setChecked(true);
                            //   Toast.makeText(getActivity(), "Size", Toast.LENGTH_SHORT).show();
                         //   callSize();
                        } else {

                            item.setChecked(false);
                        }
                        break;
                }
                return true;
            }

        });


        // Showing the popup menu
        popupMenu.show();
    }

    private void callColor() {
        new ColorFilterBottomSheet().callBack(this::onFilter).show(getChildFragmentManager(), "");
    }

    private void callSize() {
        new SizeFilterBottomSheet().callBack(this::onFilter).show(getChildFragmentManager(), "");
    }

    private void callCategory(){
        new CategoryFilterBottomSheet().callBack(this::onFilter).show(getChildFragmentManager(), "");
    }


    public void GetAllProduct(String catId, String subCatId ,String sortData,String ChkFav,int pos) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("category_id", catId);
        map.put("sub_category_id", subCatId);
        map.put("user_id", userId);
        map.put("order_by", sortData);
        Log.e("product c===",map.toString());
        Call<ProductNewModel> loginCall = apiInterface.getAllProductNew(map);
        loginCall.enqueue(new Callback<ProductNewModel>() {
            @Override
            public void onResponse(Call<ProductNewModel> call, Response<ProductNewModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    ProductNewModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        binding.tvFound.setVisibility(View.GONE);
                        arrayList.addAll(data.result);
                        if(ChkFav.equals("fav")){
                            adapterGrid.notifyItemChanged(pos);
                            adapterScroll.notifyItemChanged(pos);

                        }
                        else {
                            adapterGrid.notifyDataSetChanged();
                            adapterScroll.notifyDataSetChanged();
                        }



                    } else if (data.status.equals("0")) {
                        // Toast.makeText(ProductAct.this, data.message, Toast.LENGTH_SHORT).show();
                        arrayList.clear();
                        if(ChkFav.equals("fav")){
                            adapterGrid.notifyItemChanged(pos);
                            adapterScroll.notifyItemChanged(pos);

                        }
                        else {
                            adapterGrid.notifyDataSetChanged();
                            adapterScroll.notifyDataSetChanged();
                        }
                        binding.tvFound.setVisibility(View.VISIBLE);

                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductNewModel> call, Throwable t) {
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
      /*  if (type.equals("Info")) {
            if (!arrayList.get(position).description.equals(""))
                new InfoFragmentBottomSheet(arrayList.get(position)).callBack(this::info).show(getChildFragmentManager(), "");
            else
                Toast.makeText(getActivity(), getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        } else */ if (type.equals("Cart")) {

            if (arrayList.get(position).colorDetails.size() != 0) {
                new CartFragmentBootomSheet(arrayList.get(position)).callBack(this::info).show(getChildFragmentManager(), "");
            } else
                Toast.makeText(getActivity(), getString(R.string.not_available), Toast.LENGTH_SHORT).show();

        } /*else if (type.equals("Color")) {
            if (arrayList.get(position).colorDetails.size() != 0) {
                new ColorSizeFragmentBottomSheet(arrayList.get(position), arrayList.get(position).colorDetails.get(0).size).callBack(this::info).show(getChildFragmentManager(), "");
            } else
                Toast.makeText(getActivity(), getString(R.string.not_available), Toast.LENGTH_SHORT).show();
        } else if (type.equals("Size")) {
            if (arrayList.get(position).colorDetails.size() != 0) {
                new SizeFragmentBottomSheet(arrayList.get(position), arrayList.get(position).colorDetails.get(0).color).callBack(this::info).show(getChildFragmentManager(), "");
            } else
                Toast.makeText(getActivity(), getString(R.string.not_available), Toast.LENGTH_SHORT).show();

        }*/ else if (type.equals("Fav")) {
            if (NetworkAvailablity.checkNetworkStatus(getActivity()))
                addFavrirr(arrayList.get(position).id + "",position);
            else
                Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void info(String value, String size) {

    }


    public void addFavrirr(String proId,int pos) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("product_id", proId);
        Log.e(TAG, "Add Fav Req===" + map.toString());
        Call<Map<String, String>> loginCall = apiInterface.addFav(map);
        loginCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String, String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Product List Response :" + responseString);
                    if (data.get("status").equals("1")) {

                        if (NetworkAvailablity.checkNetworkStatus(getActivity()))
                            GetAllProduct(catId,subCatId, sortData,"fav",pos);
                        else
                            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();


                    } else if (data.get("status").equals("0")) {

                        if (NetworkAvailablity.checkNetworkStatus(getActivity()))
                            GetAllProduct(catId,subCatId, sortData,"fav",pos);
                        else
                            Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
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


    /// user_id=41&product_id=1


/*
    public void getFilterSearch(String query, String filter) {
        try {
            query = query.toLowerCase();

            final ArrayList<ProductModel.Result> filteredList = new ArrayList<ProductModel.Result>();

            if (arrayList != null) {
                if (filter.equals("Name")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        String text = arrayList.get(i).name.toLowerCase();
                        //  String brand = arrayList.get(i).brand1.toLowerCase();
                        if (text.contains(query)) {
                            filteredList.add(arrayList.get(i));
                        }

                    }
                } else if (filter.equals("Brand")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        String brand = arrayList.get(i).brand1.toLowerCase();
                        if (brand.contains(query)) {
                            filteredList.add(arrayList.get(i));
                        }

                    }
                } else if (filter.equals("Price")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        String price = arrayList.get(i).price.toLowerCase();
                        if (price.contains(query)) {
                            filteredList.add(arrayList.get(i));
                        }

                    }
                } else if (filter.equals("Color")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        for (int j = 0; j < arrayList.get(i).colorDetails.size(); j++) {

                            String color = arrayList.get(i).colorDetails.get(j).color.toLowerCase();
                            if (color.contains(query)) {
                                filteredList.add(arrayList.get(i));
                            }
                        }

                    }
                } else if (filter.equals("Size")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        for (int j = 0; j < arrayList.get(i).colorDetails.size(); j++) {

                            String size = arrayList.get(i).colorDetails.get(j).size.toLowerCase();
                            if (size.contains(query)) {
                                filteredList.add(arrayList.get(i));
                            }
                        }

                    }
                }


                if (viewType.equals("vertical")) {
                    adapterScroll.filterList(filteredList);
                } else if (viewType.equals("grid")) adapterGrid.filterList(filteredList);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/


    @Override
    public void onFilter(String type, String value) {
        if(NetworkAvailablity.checkNetworkStatus(getActivity())) ApplyFilter(type,value);

    }


    public void ApplyFilter(String type, String value) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        if (type.equals("color")) {
            map.put("color", value);
            //  map.put("category_id", value);
        }
        else if (type.equals("size")) {
            map.put("size", value);
            //  map.put("category_id", value);
        }
        else if (type.equals("category")) map.put("category_id", value);
        Log.e(TAG, "Apply Filter Request :" + type +"  " + map);

        Call<ProductNewModel> loginCall = apiInterface.getApplyFilterNew(map);
        loginCall.enqueue(new Callback<ProductNewModel>() {
            @Override
            public void onResponse(Call<ProductNewModel> call, Response<ProductNewModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    ProductNewModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Apply Filter List Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        binding.tvFound.setVisibility(View.GONE);
                        arrayList.addAll(data.result);
                        adapterGrid.notifyDataSetChanged();
                        adapterScroll.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
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
            public void onFailure(Call<ProductNewModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                if (!SessionManager.readString(getActivity(), Constant.USER_INFO, "").equals("")) {
                    userId = DataManager.getInstance().getUserData(getActivity()).result.id;
                } else userId = instanceIdResult.getToken();     //LogInAlert();

                refreshedToken = instanceIdResult.getToken();

                //  if(getIntent()!=null){
                //  catId = getIntent().getStringExtra("catId");
                if (NetworkAvailablity.checkNetworkStatus(getActivity()))
                    GetAllProduct(catId,subCatId, sortData,"",0);
                else
                    Toast.makeText(getActivity(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                //   }

                Log.e("Token===", userId);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
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
}