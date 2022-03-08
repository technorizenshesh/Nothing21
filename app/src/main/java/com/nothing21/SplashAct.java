package com.nothing21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nothing21.databinding.ActivitySplashBinding;
import com.nothing21.retrofit.Constant;
import com.nothing21.utils.DataManager;
import com.nothing21.utils.SessionManager;

public class SplashAct extends AppCompatActivity {
   ActivitySplashBinding binding;
    public static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        processNextActivity();
    }


    private void processNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DataManager.getInstance().getUserData(getApplicationContext()) != null&&
                        DataManager.getInstance().getUserData(getApplicationContext()).result != null &&
                        DataManager.getInstance().getUserData(getApplicationContext()).result.id!=null) {
                } else {
                    SessionManager.writeString(SplashAct.this, Constant.USER_INFO,"");
                }
                startActivity(new Intent(SplashAct.this, HomeAct.class));
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}