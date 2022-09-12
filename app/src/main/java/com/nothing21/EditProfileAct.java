package com.nothing21;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nothing21.databinding.ActivityEditProfileBinding;
import com.nothing21.databinding.ActivityProfileBinding;
import com.nothing21.model.LoginModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Constant;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileAct extends AppCompatActivity {
    public String TAG = "EditProfileAct";
    ActivityEditProfileBinding binding;
    Nothing21Interface apiInterface;
    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);
        initViews();

    }

    private void initViews() {

        if(NetworkAvailablity.checkNetworkStatus(EditProfileAct.this)) getUserProfile();
        else Toast.makeText(EditProfileAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.ivBack.setOnClickListener(v -> finish());

        binding.ivProfile.setOnClickListener(v -> {
            if(checkPermisssionForReadStorage())
                showImageSelection();
        });

        binding.btnUpdate11.setOnClickListener(v -> {
          validation();
        });


    }

    private void validation() {
        if (binding.etName.getText().toString().equals("")) {
            binding.etName.setError(getString(R.string.required));
            binding.etName.setFocusable(true);
        }
       else if (binding.etSurName.getText().toString().equals("")) {
            binding.etSurName.setError(getString(R.string.required));
            binding.etSurName.setFocusable(true);
        }

        else if (binding.etMobile.getText().toString().equals("")) {
            binding.etMobile.setError(getString(R.string.required));
            binding.etMobile.setFocusable(true);
        }

        else if (binding.etEmirate.getText().toString().equals("")) {
            binding.etEmirate.setError(getString(R.string.required));
            binding.etEmirate.setFocusable(true);
        }

        else if (binding.etAddress.getText().toString().equals("")) {
            binding.etAddress.setError(getString(R.string.required));
            binding.etAddress.setFocusable(true);
        }


        else {
            if(NetworkAvailablity.checkNetworkStatus(EditProfileAct.this)) updateProfile();
            else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }


    }


    public void getUserProfile(){
        DataManager.getInstance().showProgressMessage(EditProfileAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(EditProfileAct.this).result.id);
        Log.e(TAG, "get Profile Request :" + map);
        Call<LoginModel> loginCall = apiInterface.userProfile(map);
        loginCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    LoginModel  data11 = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "get Profile Response :" + responseString);
                    if (data11.status.equals("1")) {
                        SessionManager.writeString(EditProfileAct.this, Constant.USER_INFO, responseString);
                        binding.etName.setText(DataManager.getInstance().getUserData(EditProfileAct.this).result.firstName);
                        binding.etSurName.setText(DataManager.getInstance().getUserData(EditProfileAct.this).result.lastName);
                        binding.etEmail.setText(DataManager.getInstance().getUserData(EditProfileAct.this).result.email);
                        binding.etEmirate.setText(DataManager.getInstance().getUserData(EditProfileAct.this).result.emirate);
                        binding.etAddress.setText(DataManager.getInstance().getUserData(EditProfileAct.this).result.address);
                        // binding.etEmail.setText(DataManager.getInstance().getUserData(getActivity()).result.);


                        if(!DataManager.getInstance().getUserData(EditProfileAct.this).result.mobile.equals(""))   binding.etMobile.setText(DataManager.getInstance().getUserData(EditProfileAct.this).result.mobile);
                        if(!DataManager.getInstance().getUserData(EditProfileAct.this).result.countryCode.equals("")) binding.ccp.setCountryForPhoneCode(Integer.parseInt(DataManager.getInstance().getUserData(EditProfileAct.this).result.countryCode));

                        if(!data11.result.image.equals("")){
                            Glide.with(EditProfileAct.this)
                                    .load(data11.result.image)
                                    .override(150,150)
                                    .error(R.drawable.dummy)
                                    .into(binding.ivProfile);
                        }


                    } else if (data11.status.equals("0")) {
                        Toast.makeText(EditProfileAct.this, "", Toast.LENGTH_SHORT).show();


                    }

                    // serviceAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(EditProfileAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

    }

    private void openCamera () {

          /*  File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/VeryCycle/Images/");

            if (!dirtostoreFile.exists()) {
                dirtostoreFile.mkdirs();
            }

            String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

            File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/VeryCycle/Images/" + "IMG_" + timestr + ".jpg");

            str_image_path = tostoreFile.getPath();

            uriSavedImage = FileProvider.getUriForFile(Register.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    tostoreFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

            startActivityForResult(intent, REQUEST_CAMERA);*/



        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(EditProfileAct.this,
                        "com.nothing21.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" /*+ timeStamp + "_"*/;
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        str_image_path = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(EditProfileAct.this, data.getData());
                Glide.with(EditProfileAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfile);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(EditProfileAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfile);
            }

        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(EditProfileAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(EditProfileAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(EditProfileAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(EditProfileAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(EditProfileAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(EditProfileAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(EditProfileAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(EditProfileAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }


    private void updateProfile() {
        DataManager.getInstance().showProgressMessage(EditProfileAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody f_name = RequestBody.create(MediaType.parse("text/plain"), binding.etName.getText().toString());
        RequestBody l_name = RequestBody.create(MediaType.parse("text/plain"), binding.etSurName.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), binding.etEmail.getText().toString());
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), binding.etMobile.getText().toString());
        RequestBody country_code = RequestBody.create(MediaType.parse("text/plain"), binding.ccp.getSelectedCountryCode());
        RequestBody emirate = RequestBody.create(MediaType.parse("text/plain"),binding.etEmirate.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), binding.etAddress.getText().toString());
       // RequestBody GEN = RequestBody.create(MediaType.parse("text/plain"),DataManager.getInstance().getUserData(EditProfileAct.this).result.gender );
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(EditProfileAct.this).result.id);



        Call<LoginModel> signupCall = apiInterface.profileUpdate(f_name,l_name, email, mobile, country_code,emirate,address,user_id, filePart);


        signupCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    LoginModel data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SessionManager.writeString(EditProfileAct.this, Constant.USER_INFO, dataResponse);
                        finish();
                    } else if (data.status.equals("0")) {
                        // App.showToast(EditProfileActivity.this, data.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }


}
