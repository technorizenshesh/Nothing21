package com.nothing21;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.nothing21.adapter.AddressAdapter;
import com.nothing21.adapter.OrderStatusAdapter;
import com.nothing21.databinding.ActivityAddressBinding;
import com.nothing21.listener.onItemClickListener;
import com.nothing21.model.AddressModel;
import com.nothing21.model.OrderStatusModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.GPSTracker;
import com.nothing21.utils.NetworkAvailablity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAct extends AppCompatActivity  implements onItemClickListener {
    public String TAG = "UpdateAddressAct";
    ActivityAddressBinding binding;
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    String address="",city="",select="0",area="",landMark="",buildingName="",flatNo="",zipCode="",addressId="";
    GPSTracker gpsTracker;
    GoogleMap mMap;
    int PERMISSION_ID = 44;
    private Animation myAnim;
    String product_id="",cart_id="";
    Nothing21Interface apiInterface;
    AddressAdapter adapter;
    ArrayList<AddressModel.Result>arrayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_address);
        initViews();
       // bindMap();

    }

    private void initViews() {

        if(getIntent()!=null){
            product_id = getIntent().getStringExtra("product_id");
            cart_id = getIntent().getStringExtra("cart_id");
            addressId = getIntent().getStringExtra("address_id");

            Log.e("Product_id",product_id);
            Log.e("cart_id",cart_id);
        }

        arrayList = new ArrayList<>();

        adapter = new AddressAdapter(AddressAct.this,arrayList,AddressAct.this);
        binding.rvAddress.setAdapter(adapter);
        binding.ccp.setCountryForPhoneCode(971);

        //initLocation();

       /* if(DataManager.getInstance().getUserData(UpdateAddressAct.this).user.address==null){

        }
        else { if(DataManager.getInstance().getUserData(UpdateAddressAct.this).user.address!=null||!DataManager.getInstance().getUserData(UpdateAddressAct.this).user.address.equals(""))
            binding.tvAddress.setText(DataManager.getInstance().getUserData(UpdateAddressAct.this).user.address);
        }*/
        if (NetworkAvailablity.checkNetworkStatus(AddressAct.this)) getAddressssss();
        else
            Toast.makeText(AddressAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();



        if (!Places.isInitialized()) {
            Places.initialize(AddressAct.this, getString(R.string.place_api_key));
        }


        binding.btnUpdate.setOnClickListener(v -> {
           /* if(!address.equals("")) {
               // Intent returnIntent = new Intent().putExtra("lat", latitude + "").putExtra("lon", longitude + "").putExtra("address", address);
             //   setResult(Activity.RESULT_OK, returnIntent);
             if(select.equals("0")) {
                 if (NetworkAvailablity.checkNetworkStatus(AddressAct.this)) addAddresssssss();
                 else
                     Toast.makeText(AddressAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
             }
             else {
                 Intent returnIntent = new Intent();
                 returnIntent.putExtra("address_id", addressId);
                 returnIntent.putExtra("product_id",product_id);
                 returnIntent.putExtra("cart_id",cart_id);
                 setResult(Activity.RESULT_OK,returnIntent);
                 finish();
             }
              //  finish();
            }*/

            validation();

        });

      /*  binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    //.setCountry("SA")
                    .build(AddressAct.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });*/
    }


    private void validation() {

        if (binding.edCity.getText().toString().equals("")) {
            binding.edCity.setError(getString(R.string.required));
            binding.edCity.setFocusable(true);
        }
        else if (binding.edArea.getText().toString().equals("")) {
            binding.edArea.setError(getString(R.string.required));
            binding.edArea.setFocusable(true);
        }
        else if (binding.edLandmark.getText().toString().equals("")) {
            binding.edLandmark.setError(getString(R.string.required));
            binding.edLandmark.setFocusable(true);
        }
        else if (binding.edBuildingName.getText().toString().equals("")) {
            binding.edBuildingName.setError(getString(R.string.required));
            binding.edBuildingName.setFocusable(true);
        }
        else if (binding.edApartment.getText().toString().equals("")) {
            binding.edApartment.setError(getString(R.string.required));
            binding.edApartment.setFocusable(true);
        }
        else if (binding.edZipcode.getText().toString().equals("")) {
            binding.edZipcode.setError(getString(R.string.required));
            binding.edZipcode.setFocusable(true);
        }
        else if (binding.edContact.getText().toString().equals("")) {
            binding.edContact.setError(getString(R.string.required));
            binding.edContact.setFocusable(true);
        } else {
            if (NetworkAvailablity.checkNetworkStatus(AddressAct.this)) addAddresssssss();
            else
                Toast.makeText(AddressAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }



    private void addAddresssssss() {
        DataManager.getInstance().showProgressMessage(AddressAct.this, getString(R.string.please_wait));
        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("city", binding.edCity.getText().toString());
        paramHash.put("area", binding.edArea.getText().toString());
        paramHash.put("landmark", binding.edLandmark.getText().toString());
        paramHash.put("building", binding.edBuildingName.getText().toString());
        paramHash.put("flat", binding.edApartment.getText().toString());
        paramHash.put("zip_code", binding.edZipcode.getText().toString());
        paramHash.put("country_code", binding.ccp.getSelectedCountryCode()+"");
        paramHash.put("mobile", binding.edContact.getText().toString());
        paramHash.put("user_id", DataManager.getInstance().getUserData(AddressAct.this).result.id);
        Log.e("add address===", "paramHash = " + paramHash);
        Call<ResponseBody> call = apiInterface.addAddress(paramHash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    String stringResponse = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "add address Response = " + stringResponse);
                        if (jsonObject.getString("status").equals("1")) {
                            JSONObject object = jsonObject.getJSONObject("result");
                            //  Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = " + stringResponse);
                            addressId = object.getString("id");
                            Intent returnIntent = new Intent();
                          //  returnIntent.putExtra("lat", latitude + "");
                            returnIntent.putExtra("address_id", addressId);
                            returnIntent.putExtra("product_id",product_id);
                            returnIntent.putExtra("cart_id",cart_id);
                            returnIntent.putExtra("city",binding.edCity.getText().toString());
                            returnIntent.putExtra("area",binding.edArea.getText().toString());
                            returnIntent.putExtra("landmark",binding.edLandmark.getText().toString());
                            returnIntent.putExtra("building",binding.edBuildingName.getText().toString());
                            returnIntent.putExtra("flat",binding.edApartment.getText().toString());
                            returnIntent.putExtra("mobile",binding.edContact.getText().toString());
                            returnIntent.putExtra("zipCode",binding.edZipcode.getText().toString());
                            returnIntent.putExtra("chkAddress","1");


                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        } else {



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
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }

        });
    }


    private void getAddressssss() {
        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("user_id", DataManager.getInstance().getUserData(AddressAct.this).result.id);
        Log.e("get address===", "paramHash = " + paramHash);
        Call<ResponseBody> call = apiInterface.getAddress(paramHash);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    String stringResponse = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "get address Response = " + stringResponse);
                        if (jsonObject.getString("status").equals("1")) {
                            //  Log.e("sendMoneyAPiCall", "sendMoneyAPiCall = " + stringResponse);
                          AddressModel model = new Gson().fromJson(stringResponse,AddressModel.class);
                          arrayList.clear();
                          arrayList.addAll(model.getResult());
                          adapter.notifyDataSetChanged();
                          binding.tvSave.setVisibility(View.VISIBLE);

                        } else {
                            binding.tvSave.setVisibility(View.GONE);
                            arrayList.clear();
                            adapter.notifyDataSetChanged();
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
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }

        });
    }







    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ADDRESS) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                try {
                    Log.e("addressStreet====", place.getAddress());
                    address = place.getAddress();
                    select = "0";
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    city = DataManager.getInstance().getAddress(AddressAct.this,latitude,longitude);
                    //  binding.tvCity.setVisibility(View.VISIBLE);
                    // binding.tvCity.setText(city);
                    binding.tvAddress.setText(place.getAddress());
                 //   latitude = place.getLatLng().latitude;
                 //   longitude = place.getLatLng().longitude;
                  //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17.0f));
                 //   binding.imgMarker.startAnimation(myAnim);

                } catch (Exception e) {
                    e.printStackTrace();
                    //setMarker(latLng);
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            }

        }

    }





    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setCurrentLoc();
            }
        }
    }


    private void setCurrentLoc() {
        if (gpsTracker != null) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
          address =  DataManager.getInstance().getAddress(AddressAct.this, latitude, longitude);
            binding.tvAddress.setText(address);
        }
    }


    @Override
    public void onItem(int position) {
        select = "1";
        city = arrayList.get(position).getCity();
        area = arrayList.get(position).getArea();
        landMark = arrayList.get(position).getNearestLandmark();
        buildingName = arrayList.get(position).getBuilding_name();
        flatNo = arrayList.get(position).getFlate_no();
        zipCode = arrayList.get(position).getZipCode();
     //   binding.edCity.setText(city);
     //   binding.edCity.setText(area);
     //   binding.edCity.setText(landMark);
      //  binding.edCity.setText(buildingName);
     //   binding.edCity.setText(flatNo);
     //   binding.edCity.setText(zipCode);

        addressId = arrayList.get(position).getId();
        Intent returnIntent = new Intent();
        //  returnIntent.putExtra("lat", latitude + "");
        returnIntent.putExtra("address_id", addressId);
        returnIntent.putExtra("product_id",product_id);
        returnIntent.putExtra("cart_id",cart_id);
        returnIntent.putExtra("city",arrayList.get(position).getCity());
        returnIntent.putExtra("area",arrayList.get(position).getArea());
        returnIntent.putExtra("landmark",arrayList.get(position).getNearestLandmark());
        returnIntent.putExtra("building",arrayList.get(position).getBuilding_name());
        returnIntent.putExtra("flat",arrayList.get(position).getFlate_no());
        returnIntent.putExtra("mobile",arrayList.get(position).getMobile());
        returnIntent.putExtra("zipCode",arrayList.get(position).getZipCode());
        returnIntent.putExtra("chkAddress","1");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();



    }
}
