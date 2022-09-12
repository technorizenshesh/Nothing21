package com.nothing21;

import static com.nothing21.retrofit.Constant.emailPattern;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nothing21.databinding.ActivityChangePassBinding;
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

public class ChangePassAct extends AppCompatActivity {
    public String TAG = "ChangePassAct";
    ActivityChangePassBinding binding;
    Nothing21Interface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(Nothing21Interface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_pass);
        initViews();
    }

    private void initViews() {

        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnSubmit.setOnClickListener(v -> validation());
    }

    private void validation() {
        if (binding.etNewPassword.getText().toString().equals("")) {
            binding.etNewPassword.setError(getString(R.string.required));
            binding.etNewPassword.setFocusable(true);
        }

       else if (binding.etConfirmPassword.getText().toString().equals("")) {
            binding.etConfirmPassword.setError(getString(R.string.required));
            binding.etConfirmPassword.setFocusable(true);
        }

        else if (!binding.etConfirmPassword.getText().toString().equals(binding.etNewPassword.getText().toString())) {
            binding.etConfirmPassword.setError(getString(R.string.pass_not_matched));
            binding.etConfirmPassword.setFocusable(true);
        }
        else {
            if(NetworkAvailablity.checkNetworkStatus(ChangePassAct.this)) changePass();
            else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
        }


    }

    public void changePass(){
        DataManager.getInstance().showProgressMessage(ChangePassAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",DataManager.getInstance().getUserData(ChangePassAct.this).result.id+"");
        map.put("password", binding.etNewPassword.getText().toString());
        Log.e(TAG, "Change Pass Request :" + map);
        Call<ResponseBody> loginCall = apiInterface.passwordChangeApi(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    Log.e(TAG, "Change Pass Response = " + stringResponse);
                    if (jsonObject.getString("status").equals("1")) {
                        Toast.makeText(ChangePassAct.this, getString(R.string.password_updated_successfully), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangePassAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
