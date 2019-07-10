package com.example.a21__void.afroturf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.example.a21__void.afroturf.fragments.AccountTypeFragment;
import com.example.a21__void.afroturf.fragments.CreateSalonFragment;
import com.example.a21__void.afroturf.fragments.CreateUserFragment;
import com.example.a21__void.afroturf.fragments.ProgressFragment;
import com.example.a21__void.afroturf.fragments.SalonsFragment;
import com.example.a21__void.afroturf.fragments.StylistsFragment;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.afroturf.manager.UserManager;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.object.UserAfroObject;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class SignUpActivity extends AfroActivity implements View.OnClickListener, CreateUserFragment.EventListener {

    private static final String TAG_CREATE_USER = "create_user"
            , TAG_ACCOUNT_TYPE = "account_type"
            , TAG_SELECT_SALON = "select_salon"
            , TAG_CREATE_SALON = "create_salon";
    private static final String TAG_PROGRESS = "tag_progress";
    private static final String TAG_ERROR = "tag_error";

    private int currentStep = 0;

    private CreateUserFragment createUserFragment;
    private AccountTypeFragment accountTypeFragment;
    private SalonsFragment salonsFragment;
    private CreateSalonFragment createSalonFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        this.createUserFragment = new CreateUserFragment();
        this.accountTypeFragment = new AccountTypeFragment();
        this.salonsFragment = new SalonsFragment();
        this.createSalonFragment = new CreateSalonFragment();

        this.salonsFragment.setMode(SalonsFragment.MODE_SELECT);
        this.createUserFragment.setEventListener(this);

        this.findViewById(R.id.txt_cancel).setOnClickListener(this);
        this.findViewById(R.id.txt_next).setOnClickListener(this);

        this.currentStep = 0;
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rel_main_container, createUserFragment, TAG_CREATE_USER)
                .commit();
    }

    private void addFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_left, R.anim.anim_slide_left_offscreen, android.R.anim.fade_in, R.anim.anim_slide_right)
                .add(R.id.rel_main_container, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_next:
                this.nextFragment();
                break;
            case R.id.txt_cancel:
                this.finish();
                break;
                default:

        }
    }

    private void nextFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        int stackSize = fragmentManager.getBackStackEntryCount();
        String currentTag;
        if(stackSize == 0)
            currentTag = TAG_CREATE_USER;
        else
            currentTag = this.getSupportFragmentManager().getBackStackEntryAt(stackSize - 1).getName();
        switch (currentTag){
            case TAG_CREATE_USER:
                this.createUserFragment.shouldProceed(new Callback<Boolean>() {
                    @Override
                    public void onRespond(Boolean proceed) {
                        if(proceed)
                            addFragment(accountTypeFragment, TAG_ACCOUNT_TYPE);
                    }
                });
                break;
            case TAG_ACCOUNT_TYPE:
                int accountType = this.accountTypeFragment.getAccountType();
                if(accountType >= 0){
                    switch (accountType){
                        case AccountTypeFragment.TYPE_CUSTOMER:
                            SignUpActivity.this.setResult(RESULT_OK);
                            SignUpActivity.this.finish();
                            break;
                        case AccountTypeFragment.TYPE_STYLIST:
                            this.salonsFragment.setMode(SalonsFragment.MODE_SELECT);
                            SignUpActivity.this.addFragment(salonsFragment, TAG_SELECT_SALON);
                            break;
                        case AccountTypeFragment.TYPE_SALON_MANAGER:
                            SignUpActivity.this.addFragment(createSalonFragment, TAG_CREATE_SALON);
                            break;
                    }
                }else
                    Toast.makeText(this, "Please Select Account Type", Toast.LENGTH_SHORT).show();
                break;
            case TAG_SELECT_SALON:
                SalonAfroObject selectedSalon = this.salonsFragment.getSelectedSalon();
                if(selectedSalon != null){
                    this.registerUserWithSalon(selectedSalon);
                }else
                    Toast.makeText(this, "Please Select Salon", Toast.LENGTH_SHORT).show();
                break;
            case TAG_CREATE_SALON:
                this.createSalonFragment.shouldProceed(new Callback<Boolean>() {
                    @Override
                    public void onRespond(Boolean proceed) {
                        if(proceed){
                            SignUpActivity.this.setResult(RESULT_OK);
                            SignUpActivity.this.finish();
                        }
                    }
                });
                break;
        }
    }

    private void registerUserWithSalon (final SalonAfroObject salon) {
        this.showIndeterminateProgress("Making request");
        UserManager.getInstance(this.getApplicationContext()).registerWithSalon(salon, new CacheManager.ManagerRequestListener<UserAfroObject>() {
            @Override
            public void onRespond(UserAfroObject result) {
                hideIndeterminateProgress();
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("Success")
                        .setCancelable(false)
                        .setMessage("A request to be a stylist has been made. we will notify you once the salon manager has accepted your request.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SignUpActivity.this.setResult(RESULT_OK);
                                SignUpActivity.this.finish();
                            }
                        });
                builder.show();
            }

            @Override
            public void onApiError(CacheManager.ApiError apiError) {
                hideIndeterminateProgress();
                showNetworkError(new ErrorFragment.OnFragmentInteractionListener() {
                    @Override
                    public void onRequestRetry() {
                        registerUserWithSalon(salon);
                    }

                    @Override
                    public void onRequestExit() {
                        SignUpActivity.this.finish();
                    }
                });
            }
        });
    }


    @Override
    public void showIndeterminateProgress() {
        this.showIndeterminateProgress("Loading");
    }

    private void showIndeterminateProgress(String msg) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

        ProgressFragment progressFragment = ProgressFragment.newInstance(msg);
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.rel_secondary_container, progressFragment, TAG_PROGRESS)
                .commit();
        this.getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void hideIndeterminateProgress() {
        Fragment fragment = this.getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS);
        if(fragment == null)
            return;

        this.getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();

    }

    @Override
    protected int getErrorContainerId() {
        return R.id.rel_secondary_container;
    }



    private void showNetworkError(ErrorFragment.OnFragmentInteractionListener callback){
        ErrorFragment errorFragment = ErrorFragment.newInstance(R.drawable.ic_no_connection, "No Connection", "dsfe");
        errorFragment.setmListener(callback);

        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.rel_secondary_container, errorFragment, TAG_ERROR)
                .commit();
    }


    @Override
    public void onCreateUser(CreateUserFragment creator, UserAfroObject user) {
        switch(user.getType()){
            case UserAfroObject.TYPE_USER:
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case UserAfroObject.TYPE_STYLIST:
                //addFragment(this.salonsFragment);
            case UserAfroObject.TYPE_MANAGER:
        }
    }
}
