package com.example.a21__void.afroturf;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.UserManager;
import com.example.a21__void.afroturf.object.UserAfroObject;

import java.util.HashMap;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class LoginActivity extends AfroActivity implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {
    public static final int RESULT_LOGGED_IN = 19;
    public static final int RESULT_REQUEST_SIGN_UP = 29;
    private static final String TAG_ERROR_FRAGMENT = "error_fragment";

    private HashMap<String, String> errors = new HashMap<>();

    TextView txtLogin, txtSignUp, txtError;
    EditText edtEmailAddress, edtPassword;
    LinearLayout linEmailAddress, linPassword;
    private MaterialProgressBar progBackgroundWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        this.progBackgroundWork = this.findViewById(R.id.prog_background_work);
        this.txtLogin = findViewById(R.id.txt_login);
        this.txtSignUp = findViewById(R.id.txt_sign_up);
        this.txtError = this.findViewById(R.id.txt_error);
        this.edtEmailAddress = findViewById(R.id.edt_email_address);
        this.edtPassword = findViewById(R.id.edt_password);
        this.linEmailAddress = findViewById(R.id.lin_email_address);
        this.linPassword = findViewById(R.id.lin_password);

        txtLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(
                this);

        this.edtEmailAddress.addTextChangedListener(this);
        this.edtPassword.addTextChangedListener(this);
        this.edtPassword.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_login:
                this.login();
                break;
            case R.id.txt_sign_up:
                this.setResult(RESULT_REQUEST_SIGN_UP);
                this.finish();
                break;
        }
    }

    private void login() {
        this.showProgress();
        this.hideError();
        this.txtError.requestFocus();
        String username = this.edtEmailAddress.getText().toString().trim(),
                password = this.edtPassword.getText().toString().trim();

        boolean usernameValid = this.isUsernameValid(username),
                passwordValid = this.isPasswordValid(password);



        if(usernameValid && passwordValid){
            UserManager.getInstance(this).login(username, password, new CacheManager.ManagerRequestListener<UserAfroObject>() {
                @Override
                public void onRespond(UserAfroObject result) {
                    LoginActivity.this.hideProgress();
                    Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
                    LoginActivity.this.startActivity(intentHome);
                    LoginActivity.this.finish();
                }

                @Override
                public void onApiError(CacheManager.ApiError apiError) {
                    LoginActivity.this.hideProgress();
                    if(apiError.errorCode < 0){
                        LoginActivity.this.showNetworkError("No Connection", "Please make sure your wifi or mobile data is turned on.", new ErrorFragment.OnFragmentInteractionListener(){

                            @Override
                            public void onRequestRetry() {
                                LoginActivity.this.hideNetworkError();
                                LoginActivity.this.showProgress();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        LoginActivity.this.login();
                                    }
                                }, 1500);
                            }

                            @Override
                            public void onRequestExit() {
                                LoginActivity.this.finish();
                            }
                        });
                    }else{
                        showError();
                        hideProgress();
                    }
                }
            });
        }else{
            showError();
            hideProgress();
        }
    }

    private void showNetworkError(String title, String msg, ErrorFragment.OnFragmentInteractionListener callback){
        ErrorFragment errorFragment =  ErrorFragment.newInstance(R.drawable.ic_no_connection, title, msg);
        errorFragment.setmListener(callback);

        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.rel_error, errorFragment, TAG_ERROR_FRAGMENT)
                .commit();
    }

    public void hideNetworkError(){
        Fragment errorFragment  = this.getSupportFragmentManager().findFragmentByTag(TAG_ERROR_FRAGMENT);

        if(errorFragment != null)
            this.getSupportFragmentManager().beginTransaction()
            .remove(errorFragment)
            .commit();
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() > 0;
    }

    private boolean isUsernameValid(String username) {
        return username != null && username.length() > 0;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(this.linEmailAddress.isSelected()){
            hideError();
        }
    }

    private void showError(){
        if(this.txtError != null){
            txtError.setVisibility(View.VISIBLE);
            this.linEmailAddress.setSelected(true);
            this.linPassword.setSelected(true);
        }
    }

    private void hideError(){
        if(this.txtError != null){
            this.txtError.setVisibility(View.INVISIBLE);
            this.linEmailAddress.setSelected(false);
            this.linPassword.setSelected(false);
        }
    }

    private void showProgress(){
        if(this.progBackgroundWork != null)
            this.progBackgroundWork.setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        if(this.progBackgroundWork != null)
            this.progBackgroundWork.setVisibility(View.GONE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE){
            Interpol.hideKeyBoard(this.getBaseContext(), edtPassword);
            this.login();
            return true;
        }
        return false;
    }

    @Override
    public void showIndeterminateProgress() {

    }

    @Override
    public void hideIndeterminateProgress() {

    }
}
