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
import com.nothing21.databinding.ActivityAddressBinding;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.GPSTracker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAct extends AppCompatActivity {
    public String TAG = "UpdateAddressAct";
    ActivityAddressBinding binding;
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    String address="",city="";
    GPSTracker gpsTracker;
    GoogleMap mMap;
    int PERMISSION_ID = 44;
    private Animation myAnim;
    String product_id="",cart_id="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_address);
        initViews();
        bindMap();

    }

    private void initViews() {

        if(getIntent()!=null){
            product_id = getIntent().getStringExtra("product_id");
            cart_id = getIntent().getStringExtra("cart_id");
            Log.e("Product_id",product_id);
            Log.e("cart_id",cart_id);
        }

        initLocation();

       /* if(DataManager.getInstance().getUserData(UpdateAddressAct.this).user.address==null){

        }
        else { if(DataManager.getInstance().getUserData(UpdateAddressAct.this).user.address!=null||!DataManager.getInstance().getUserData(UpdateAddressAct.this).user.address.equals(""))
            binding.tvAddress.setText(DataManager.getInstance().getUserData(UpdateAddressAct.this).user.address);
        }*/



        if (!Places.isInitialized()) {
            Places.initialize(AddressAct.this, getString(R.string.place_api_key));
        }


        binding.btnUpdate.setOnClickListener(v -> {
            if(!address.equals("")) {
               // Intent returnIntent = new Intent().putExtra("lat", latitude + "").putExtra("lon", longitude + "").putExtra("address", address);
             //   setResult(Activity.RESULT_OK, returnIntent);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("lat", latitude + "");
                returnIntent.putExtra("lon", longitude + "");
                returnIntent.putExtra("address", address);
                returnIntent.putExtra("product_id",product_id);
                returnIntent.putExtra("cart_id",cart_id);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

              //  finish();
            }

        });

        binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    //.setCountry("SA")
                    .build(AddressAct.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });
    }


    private void initLocation() {
        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        gpsTracker = new GPSTracker(AddressAct.this);
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                setCurrentLoc();
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        } else {
            requestPermissions();
        }
    }

    private void bindMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMap = map;
                mMap.clear();
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                if (gpsTracker != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 17.0f));
                }
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        latitude = mMap.getCameraPosition().target.latitude;
                        longitude = mMap.getCameraPosition().target.longitude;
                        address =  DataManager.getInstance().getAddress(AddressAct.this, latitude, longitude);
                        binding.tvAddress.setText(DataManager.getInstance().getAddress(AddressAct.this, latitude, longitude));
                        binding.imgMarker.startAnimation(myAnim);
                    }
                });
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
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    city = DataManager.getInstance().getAddress(AddressAct.this,latitude,longitude);
                    //  binding.tvCity.setVisibility(View.VISIBLE);
                    // binding.tvCity.setText(city);
                    binding.tvAddress.setText(place.getAddress());
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17.0f));
                    binding.imgMarker.startAnimation(myAnim);

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


}
