package com.nothing21;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.nothing21.databinding.ActivityHomeBinding;
import com.nothing21.fragment.AccountFragment;
import com.nothing21.fragment.CartFragment;
import com.nothing21.fragment.HomeFragment;
import com.nothing21.utils.SessionManager;

public class HomeAct extends AppCompatActivity {
    ActivityHomeBinding binding;
    public static CardView cardTabs,cardTabIcons;
    Fragment fragment;
   public static String fragment_tag="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);

        initViews();

        instantiateFragments(savedInstanceState);
    }

    private void initViews() {
        cardTabs = findViewById(R.id.cardTabs);
        cardTabIcons = findViewById(R.id.cardTabIcons);

        binding.rlHome.setOnClickListener(v -> tab(1));

        binding.rlCart.setOnClickListener(v -> tab(2));

        binding.rlAccount.setOnClickListener(v -> tab(3));



        SessionManager.writeString(HomeAct.this,"selectImage","");
        SessionManager.writeString(HomeAct.this,"selectColor","");

    }


    public void tab(int i){
        if (i == 1) {
            fragment_tag = "home";
            binding.ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_active));
            binding.ivCart.setImageDrawable(getResources().getDrawable(R.drawable.ic_logo));
            binding.ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.account_inactive));
            FragTrans(new HomeFragment());
        } else if (i == 2) {
            fragment_tag = "cart";
            binding.ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_inactive));
            binding.ivCart.setImageDrawable(getResources().getDrawable(R.drawable.ic_logo));
            binding.ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.account_inactive));
            FragTrans(new CartFragment());

        } else if (i == 3) {
            fragment_tag = "profile";
            binding.ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_inactive));
            binding.ivCart.setImageDrawable(getResources().getDrawable(R.drawable.ic_logo));
            binding.ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.account_active));
            FragTrans(new AccountFragment());
        }
    }

    public  void FragTrans(Fragment fragment) {
        this.fragment = fragment;
        FragmentManager  manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction(); //getSupportFragmentManager().beginTransaction()
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("fragment");
            transaction.commit();

    }


    @Override
    public void onBackPressed() {
        ExitAlert();
    }


 /*   @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            ExitAlert();
        }
    }*/

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



    private void instantiateFragments(Bundle inState) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (inState != null) {
            fragment = (Fragment) manager.getFragment(inState, fragment_tag);
          //  if(manager.findFragmentByTag(fragment.getTag()).equals("home")) tab(1);
        //    if(fragment.getTag().equals("cart")) tab(2);
        //    if(fragment.getTag().equals("profile")) tab(3);
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("fragment");
            transaction.commit();

        } else {
            tab(1);
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        FragmentManager manager = getSupportFragmentManager();
        manager.putFragment(outState, fragment_tag, fragment);
        super.onSaveInstanceState(outState);

    }


    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        instantiateFragments(inState);
    }

}
