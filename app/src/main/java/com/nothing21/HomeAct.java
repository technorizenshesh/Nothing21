package com.nothing21;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.nothing21.databinding.ActivityHomeBinding;
import com.nothing21.fragment.AccountFragment;
import com.nothing21.fragment.CartFragment;
import com.nothing21.fragment.HomeFragment;

public class HomeAct extends AppCompatActivity {
    ActivityHomeBinding binding;
    public static CardView cardTabs,cardTabIcons;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        initViews();
    }

    private void initViews() {
        cardTabs = findViewById(R.id.cardTabs);
        cardTabIcons = findViewById(R.id.cardTabIcons);

        binding.rlHome.setOnClickListener(v -> tab(1));

        binding.rlCart.setOnClickListener(v -> tab(2));

        binding.rlAccount.setOnClickListener(v -> tab(3));

        tab(1);

    }


    public void tab(int i){
        if (i == 1) {
            binding.ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_active));
            binding.ivCart.setImageDrawable(getResources().getDrawable(R.drawable.cart_inactive));
            binding.ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.account_inactive));
            FragTrans(new HomeFragment());
        } else if (i == 2) {
            binding.ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_inactive));
            binding.ivCart.setImageDrawable(getResources().getDrawable(R.drawable.cart_active));
            binding.ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.account_inactive));
            FragTrans(new CartFragment());

        } else if (i == 3) {
            binding.ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_inactive));
            binding.ivCart.setImageDrawable(getResources().getDrawable(R.drawable.cart_inactive));
            binding.ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.account_active));
            FragTrans(new AccountFragment());
        }
    }

    public  void FragTrans(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        ExitAlert();
    }

    public void ExitAlert(){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(HomeAct.this);
        builder1.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_exit_this_app));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
