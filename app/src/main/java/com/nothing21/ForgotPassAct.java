package com.nothing21;

import static com.nothing21.retrofit.Constant.emailPattern;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nothing21.databinding.ActivityForgotPassBinding;
import com.nothing21.retrofit.ApiClient;
import com.nothing21.retrofit.Nothing21Interface;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.NetworkAvailablity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassAct extends AppCompatActivity {
    public String TAG = "ForgotPassAct";
    ActivityForgotPassBinding binding;
    Nothing21Interface apiInterface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_pass);
        initViews();
    }

    private void initViews() {
        binding.btnSend.setOnClickListener(v ->{
            validation();
        } );
    }


    private void validation() {
        if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.required));
            binding.etEmail.setFocusable(true);
        }
        else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        }
        else {
            if(NetworkAvailablity.checkNetworkStatus(ForgotPassAct.this)) sentEmail();
            else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }


    }


    public void sentEmail(){
        DataManager.getInstance().showProgressMessage(ForgotPassAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("email", binding.etEmail.getText().toString());
        Log.e(TAG, "SendEmail Request :" + map);
        Call<ResponseBody> loginCall = apiInterface.forgotPassApi(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                        Log.e(TAG, "SendEmail Response = " + stringResponse);
                        if (jsonObject.getString("status").equals("1")) {
                            Toast.makeText(ForgotPassAct.this, getString(R.string.please_check_your_email), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgotPassAct.this, getString(R.string.email_not_register), Toast.LENGTH_SHORT).show();
                        }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                Log.e("JSONException", "JSONException = " + e.getMessage());

            }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}
