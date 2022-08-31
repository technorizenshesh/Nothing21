package com.nothing21;

import static com.nothing21.retrofit.Constant.emailPattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nothing21.databinding.ActivityLoginBinding;
import com.nothing21.model.LoginModel;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Constant;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;
import com.nothing21.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login2Act extends AppCompatActivity {
    public String TAG  = "Login2Act";
    ActivityLoginBinding binding;
    Nothing21Interface apiInterface;
    String without_login_cart = "";
    String refreshedToken = "",product_id="",cart_id="",amount = "";;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        initViews();

    }

    private void initViews() {

      /*  if(getIntent()!=null){
            product_id = getIntent().getStringExtra("product_id");
            cart_id = getIntent().getStringExtra("cart_id");
            amount = getIntent().getStringExtra("amount");
            without_login_cart = getIntent().getStringExtra("check_out");
        }*/



        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                refreshedToken = instanceIdResult.getToken();
                Log.e("Token===", refreshedToken);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.btnSignUp.setOnClickListener(v -> {
            //  startActivity(new Intent(Login2Act.this,RegisterAct.class))

            startActivity(new Intent(Login2Act.this, Register2Act.class));

        });

        binding.btnSignin.setOnClickListener(v ->{
            validation();
        } );

        binding.btnForgotPass.setOnClickListener(v ->{
        } );


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
        else {
            if(NetworkAvailablity.checkNetworkStatus(Login2Act.this)) userLogin();
            else Toast.makeText(Login2Act.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }
    }


    public void userLogin() {
        DataManager.getInstance().showProgressMessage(Login2Act.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("email", binding.etEmail.getText().toString());
        map.put("password",binding.etPasswoprd.getText().toString());
        map.put("lat", "");
        map.put("lon", "");
        map.put("without_login_cart", "");
        map.put("register_id",refreshedToken);

        Log.e(TAG, "Login Request " + map);
        Call<LoginModel> loginCall = apiInterface.userLogin(map);
        loginCall.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    LoginModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Login Response :" + responseString);
                    if (data.status.equals("1")) {
                        Toast.makeText(Login2Act.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        SessionManager.writeString(Login2Act.this, Constant.USER_INFO, responseString);
                            // finish();
                            startActivity(new Intent(Login2Act.this, HomeAct.class)
                                   .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();


                    } else if (data.status.equals("0"))
                        Toast.makeText(Login2Act.this, data.message, Toast.LENGTH_SHORT).show();

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

