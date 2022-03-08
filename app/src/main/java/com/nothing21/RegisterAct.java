package com.nothing21;

import static com.nothing21.retrofit.Constant.emailPattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.nothing21.databinding.ActivityRegisterBinding;
import com.nothing21.model.LoginModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Constant;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

public class RegisterAct extends AppCompatActivity {
    public String TAG  = "RegisterAct";
    ActivityRegisterBinding binding;
    Nothing21Interface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        binding.btnSave.setOnClickListener(v -> {
         validation();
        });
    }


    public void validation(){
        if (binding.etEmail.getText().toString().equals("")){
            binding.etEmail.setError(getString(R.string.required));
            binding.etEmail.setFocusable(true);
        }
        else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        }
        else if (binding.etPasswoprd.getText().toString().equals("")){
            binding.etPasswoprd.setError(getString(R.string.required));
            binding.etPasswoprd.setFocusable(true);
        }
        else if (binding.etRepeatPasswoprd.getText().toString().equals("")){
            binding.etRepeatPasswoprd.setError(getString(R.string.required));
            binding.etRepeatPasswoprd.setFocusable(true);
        }

        else if (!binding.etRepeatPasswoprd.getText().toString().equals(binding.etPasswoprd.getText().toString())){
            binding.etRepeatPasswoprd.setError(getString(R.string.required));
            binding.etRepeatPasswoprd.setFocusable(true);
        }
        else if (!binding.etRepeatPasswoprd.getText().toString().equals(binding.etPasswoprd.getText().toString())){
            binding.etRepeatPasswoprd.setError(getString(R.string.required));
            binding.etRepeatPasswoprd.setFocusable(true);
        }

        else if (binding.etName.getText().toString().equals("")){
            binding.etName.setError(getString(R.string.required));
            binding.etName.setFocusable(true);
        }

        else if (binding.etSurname.getText().toString().equals("")){
            binding.etSurname.setError(getString(R.string.required));
            binding.etSurname.setFocusable(true);
        }

        else if (binding.etEmirate.getText().toString().equals("")){
            binding.etEmirate.setError(getString(R.string.required));
            binding.etEmirate.setFocusable(true);
        }

        else if (binding.etAddress.getText().toString().equals("")){
            binding.etAddress.setError(getString(R.string.required));
            binding.etAddress.setFocusable(true);
        }

        else if (binding.etRegion.getText().toString().equals("")){
            binding.etRegion.setError(getString(R.string.required));
            binding.etRegion.setFocusable(true);
        }

        else if (binding.etNumber.getText().toString().equals("")){
            binding.etNumber.setError(getString(R.string.required));
            binding.etNumber.setFocusable(true);
        }
        else {
            if(NetworkAvailablity.checkNetworkStatus(RegisterAct.this)) userRegister();
            else Toast.makeText(RegisterAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }



    public void userRegister() {
        DataManager.getInstance().showProgressMessage(RegisterAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("first_name",binding.etName.getText().toString());
        map.put("last_name",binding.etSurname.getText().toString());
        map.put("email", binding.etEmail.getText().toString());
        map.put("password ",binding.etPasswoprd.getText().toString());
        map.put("mobile",binding.etNumber.getText().toString());
        map.put("emirate",binding.etEmirate.getText().toString());
        map.put("address",binding.etAddress.getText().toString());
        map.put("lat", "");
        map.put("lon", "");
        map.put("register_id","13244343");
        Log.e(TAG, "Signup Request " + map);

        Call<Map<String,String>> loginCall = apiInterface.signupUser(map);
        loginCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Signup Response :" + responseString);
                    if (data.get("status").equals("1")) {
                        Toast.makeText(RegisterAct.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterAct.this, LoginAct.class).putExtra("home","1").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else if (data.get("status").equals("0"))
                        Toast.makeText(RegisterAct.this, data.get("message"), Toast.LENGTH_SHORT).show();

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

}
