package com.example.a21__void.afroturf;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a21__void.afroturf.manager.BookmarkManager;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.ReviewsManager;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.afroturf.manager.ServicesManager;
import com.example.a21__void.afroturf.manager.StylistsManager;
import com.example.a21__void.afroturf.manager.UserManager;

public class LandingActivity extends AppCompatActivity {

    private static final int REQ_CODE_LOGIN = 9;
    private static final int REQ_CODE_SIGN_UP = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SalonsManager salonsManager = (SalonsManager) CacheManager.getManager(this, SalonsManager.class);
        ServicesManager servicesManager = (ServicesManager)CacheManager.getManager(this, ServicesManager.class);
        StylistsManager stylistsManager = (StylistsManager)CacheManager.getManager(this, StylistsManager.class);
        ReviewsManager reviewsManager = (ReviewsManager)CacheManager.getManager(this, ReviewsManager.class);
        BookmarkManager bookmarkManager =(BookmarkManager) CacheManager.getManager(this, BookmarkManager.class);


        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE);


        boolean isFirstRun = sharedPreferences.getBoolean(HomeActivity.FLAG_FIRST_RUN, true);
        if(isFirstRun){
            salonsManager.init(this.getApplicationContext());
            stylistsManager.init(this.getApplicationContext());
            servicesManager.init(this.getApplicationContext());
            reviewsManager.init(this.getApplicationContext());
            bookmarkManager.init(this.getApplicationContext());
        }
        salonsManager.beginManagement();
        stylistsManager.beginManagement();
        servicesManager.beginManagement();
        reviewsManager.beginManagement();
        bookmarkManager.beginManagement();

        UserManager userManager = UserManager.getInstance(this.getApplicationContext());
        if(!userManager.isLoggedIn()){
           this.openLoginActivity();
        }else{
            this.openHomeActivity();
        }
    }

    private void openLoginActivity() {
        Intent loginIntent = new Intent(this.getApplicationContext(), LoginActivity.class);
        this.startActivityForResult(loginIntent, REQ_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_LOGIN){
            switch (resultCode){
                case LoginActivity.RESULT_LOGGED_IN:
                    this.openHomeActivity();
                    break;
                case LoginActivity.RESULT_REQUEST_SIGN_UP:
                    this.openSignUpActivity();
                    break;
                case LoginActivity.RESULT_CANCELED:
                    default:
                        this.finish();
            }
        }else if(requestCode == REQ_CODE_SIGN_UP){
            if(resultCode == RESULT_OK)
                this.openHomeActivity();
            else
                this.openLoginActivity();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openSignUpActivity() {
        Intent signUpIntent = new Intent(this.getApplicationContext(), SignUpActivity.class);
        this.startActivityForResult(signUpIntent, REQ_CODE_SIGN_UP);
    }

    private void openHomeActivity() {
        Intent homeIntent = new Intent(this.getApplicationContext(), HomeActivity.class);
        this.startActivity(homeIntent);
        this.finish();
    }
}
