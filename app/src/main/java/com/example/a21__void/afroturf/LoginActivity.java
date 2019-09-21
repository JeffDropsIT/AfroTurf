package com.example.a21__void.afroturf;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.a21__void.afroturf.fragments.ProgressFragment;
import com.example.a21__void.afroturf.libIX.activity.AfroActivity;
import com.example.a21__void.afroturf.libIX.fragment.AfroFragment;
import com.example.a21__void.afroturf.libIX.fragment.ProcessErrorFragment;
import com.example.a21__void.afroturf.libIX.ui.RichTextView;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.UserManager;
import com.example.a21__void.afroturf.object.UserAfroObject;
import com.example.a21__void.afroturf.pkgCommon.AfroEditText;
import com.example.a21__void.afroturf.pkgCommon.AfroTextView;
import com.example.a21__void.afroturf.user.UserGeneral;

import java.util.HashMap;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class LoginActivity extends AfroActivity implements View.OnClickListener {
    public static final int RESULT_LOGGED_IN = 19;
    public static final int RESULT_REQUEST_SIGN_UP = 29;
    private static final String TAG_UNKNOWN =  "tag_unknown_fragment"
                            , TAG_PROCESS_ERROR = "tag_process_error_fragment"
                            , TAG_PROGRESS = "tag_progress_fragment";

    private static final int REQ_PROCESS_ERROR_LOGIN = 12;
    private static final int PROCESS_ID_LOGIN = 1;

    private static final String KEY_ERROR_EMAIL = "key_error_email"
                                    , KEY_ERROR_PASSWORD = "key_error_password";

    private HashMap<String, String> errors = new HashMap<>();

    AfroTextView txtLogin, txtSignUp;
    RichTextView txtError;
    AfroEditText edtEmailAddress, edtPassword;
    private MaterialProgressBar progBackgroundWork;

    private Animation animSlideUp, animSlideDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        this.animSlideUp = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.anim_slide_up);
        this.animSlideDown = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.anim_slide_down);
        this.animSlideUp.setDuration(250);
        this.animSlideDown.setDuration(250);
        this.animSlideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(LoginActivity.this.txtError != null)
                    LoginActivity.this.txtError.setVisibility(View.VISIBLE);
                Log.i("anim", "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.animSlideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(LoginActivity.this.txtError != null)
                    LoginActivity.this.txtError.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        this.progBackgroundWork = this.findViewById(R.id.prog_background_work);
        this.txtLogin = findViewById(R.id.txt_login);
        this.txtSignUp = findViewById(R.id.txt_sign_up);
        this.txtError = this.findViewById(R.id.txt_error);
        this.edtEmailAddress = findViewById(R.id.edt_email);
        this.edtPassword = findViewById(R.id.edt_password);
        this.edtEmailAddress.setNextFocusDownId(this.edtPassword.getId());

        txtLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(
                this);

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
        this.hideError();
        this.showProgress("Logging in");
        this.txtError.requestFocus();
        String username = this.edtEmailAddress.getText().trim(),
                password = this.edtPassword.getText().trim();

        boolean usernameValid = this.isUsernameValid(username),
                passwordValid = this.isPasswordValid(password);



        if(usernameValid && passwordValid){
            UserManager.getInstance(this).login(username, password, new CacheManager.ManagerRequestListener<UserGeneral>() {
                @Override
                public void onRespond(UserGeneral result) {
                    LoginActivity.this.hideProgress();
                    Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
                    LoginActivity.this.startActivity(intentHome);
                    LoginActivity.this.finish();
                }

                @Override
                public void onApiError(CacheManager.ApiError apiError) {
                    LoginActivity.this.hideProgress();
                    if(apiError.errorCode < 0){
                        LoginActivity.this.onProcessError(null, PROCESS_ID_LOGIN, R.drawable.ic_no_connection, "No Connection", "Please make sure your wifi or mobile data is turned on.");
                    }else{
                        HashMap<String, String> errors = new HashMap<>();
                        errors.put(KEY_ERROR_EMAIL, "Email Address");
                        errors.put(KEY_ERROR_PASSWORD, "Password");
                        showError(errors);
                        hideProgress();
                    }
                }
            });
        }else{
            HashMap<String, String> errors = new HashMap<>();
            if(!usernameValid)
                errors.put(KEY_ERROR_EMAIL, "Email Address");
            if(!passwordValid)
                errors.put(KEY_ERROR_PASSWORD, "Password");

            showError(errors);
            hideProgress();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() > 0;
    }

    private boolean isUsernameValid(String username) {
        return username != null && username.length() > 0;
    }

    private void showError(HashMap<String, String> errors){
        if(this.txtError != null){
            String compiledErrors = this.compileErrors(errors, false);
            if(compiledErrors.length() > 0){
                txtError.setMessage(compiledErrors + " Invalid");

                if(!this.animSlideDown.hasEnded())
                    this.animSlideDown.cancel();

                Log.i("anim", "onAnimationStart: " + this.animSlideDown.hasEnded());

                this.txtError.startAnimation(this.animSlideUp);


                if(errors.containsKey(KEY_ERROR_EMAIL))
                    edtEmailAddress.setHasError(true);
                if(errors.containsKey(KEY_ERROR_PASSWORD))
                    edtPassword.setHasError(true);
            }
        }
    }

    private void hideError(){
        if(this.txtError != null){
            if(!this.animSlideUp.hasEnded())
                this.animSlideUp.cancel();

            if(this.animSlideDown.hasEnded())
                this.txtError.startAnimation(this.animSlideDown);

            this.edtEmailAddress.setHasError(false);
            this.edtPassword.setHasError(false);
        }
    }

    private String compileErrors(HashMap<String, String> errors, boolean and){
        String compiledErrors = "";
        String ender = and ? " and " : " or ";

        int size = errors.size();
        for(String key : errors.keySet()){
            size--;
            String error = errors.get(key);
            compiledErrors += error + ((size == 1) ? ender : (size == 0) ? "" : ", ");
        }
        return compiledErrors;
    }


    @Override
    public void onProcessError(AfroFragment parent, int processId, int resIcon, String title, String message) {
        ProcessErrorFragment processErrorFragment = ProcessErrorFragment.newInstance(REQ_PROCESS_ERROR_LOGIN, processId, title, message, resIcon);

        this.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(processErrorFragment, TAG_PROCESS_ERROR)
                .commit();
    }

    @Override
    public void onDoBackgroundWork() {
        if(this.progBackgroundWork != null)
            this.progBackgroundWork.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishBackgroundWork() {
        if(this.progBackgroundWork != null)
            this.progBackgroundWork.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress(String message) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

        ProgressFragment progressFragment = ProgressFragment.newInstance(message);
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.rel_secondary_container, progressFragment, TAG_PROGRESS)
                .commitNow();
    }

    @Override
    public void hideProgress() {
        Fragment fragment = this.getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS);
        if(fragment == null)
            return;

        this.getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }

    @Override
    public void onRequestRetry(int requestID, int processID) {
        Fragment fragment = this.getSupportFragmentManager().findFragmentByTag(TAG_PROCESS_ERROR);

        if(fragment != null)
            this.getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();


        if(processID == PROCESS_ID_LOGIN){
            this.login();
        }
    }

    @Override
    public void onRequestCancel(int requestID, int processID) {
        Fragment fragment = this.getSupportFragmentManager().findFragmentByTag(TAG_PROCESS_ERROR);

        if(fragment != null)
            this.getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
    }

    @Override
    protected void onWorkModeChange(int mode) {

    }
}
