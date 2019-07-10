package com.example.a21__void.afroturf.fragments;


import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a21__void.afroturf.Callback;
import com.example.a21__void.afroturf.ErrorFragment;
import com.example.a21__void.afroturf.Interpol;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.libIX.ui.RichTextView;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.UserManager;
import com.example.a21__void.afroturf.object.UserAfroObject;
import com.example.a21__void.afroturf.pkgCommon.AfroDropDown;
import com.example.a21__void.afroturf.pkgCommon.AfroEditText;

import java.util.HashMap;
import java.util.Map;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateUserFragment extends SequenceFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final String ERROR_NAME = "error_name"
            , ERROR_EMAIL = "error_email"
            , ERROR_PASSWORD = "error_password"
            , ERROR_CONFIRM_PASSWORD = "error_confirm_password";
    private static final String TAG_ERROR_FRAGMENT = "error_fragment_create_user";

    private RichTextView txtError;
    private AfroEditText aedtName,  aedtEmailAddress,  aedtPassword,  aedtConfirmPassword;
    private AfroDropDown adrpType;
    private EventListener eventListener;
    //private MaterialProgressBar progBackgroundWork;

    public CreateUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout relParent = (RelativeLayout)inflater.inflate(R.layout.fragment_create_user, container, false);

        this.txtError = relParent.findViewById(R.id.txt_error);

        this.aedtName = relParent.findViewById(R.id.aedt_name);
        this.aedtEmailAddress = relParent.findViewById(R.id.aedt_email_address);
        this.aedtPassword = relParent.findViewById(R.id.aedt_password);
        this.aedtConfirmPassword = relParent.findViewById(R.id.aedt_confirm_password);

        this.adrpType = relParent.findViewById(R.id.adrp_type);
        this.adrpType.setDataset(new String[]{"Customer", "Stylist", "Manager"});

        this.showError(null);
        relParent.findViewById(R.id.txt_create).setOnClickListener(this);

        return relParent;
    }

    @Override
    public void shouldProceed(Callback<Boolean> callback) {
        if(this.getView() == null){
            callback.onRespond(true);
            return;
        }


    }

    private void createUser(String name, String emailAddress, String password, String confirmPassword) {
        Map<String, String> errors = this.handleErrors(name, emailAddress, password, confirmPassword);

        if(errors.isEmpty()){
            UserManager userManager = UserManager.getInstance(this.getContext());
            UserAfroObject currentUser = userManager.getCurrentUser();
            if(currentUser != null){

            }else{
                clearErrors();
                userManager.signUpUser(name, emailAddress, password, new CacheManager.ManagerRequestListener<UserAfroObject>() {
                    @Override
                    public void onRespond(UserAfroObject result) {
                        hideIndeterminateProgress();
                        if(CreateUserFragment.this.eventListener != null)
                            CreateUserFragment.this.eventListener.onCreateUser(CreateUserFragment.this, result);
                    }

                    @Override
                    public void onApiError(CacheManager.ApiError apiError) {
                        if(apiError.errorCode == 409){
                            hideIndeterminateProgress();
                            showError("Email already exists");
                            aedtEmailAddress.setSelected(true);
                        }else{
                            hideIndeterminateProgress();
                            CreateUserFragment.this.showNetworkError("No Connection", "Please make sure your wifi or mobile data is turned on.", new ErrorFragment.OnFragmentInteractionListener(){
                                @Override
                                public void onRequestRetry() {
                                    createAccount();
                                }

                                @Override
                                public void onRequestExit() {
                                    Activity activity = CreateUserFragment.this.getActivity();
                                    if(activity != null)
                                        activity.finish();
                                    else
                                        Toast.makeText(CreateUserFragment.this.getContext(), "Unexpected Error", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });
            }
        }else{
            hideIndeterminateProgress();
            showError(compileErrors(errors));
        }
    }

    private void showNetworkError(String title, String msg, ErrorFragment.OnFragmentInteractionListener callback) {
        ErrorFragment errorFragment = ErrorFragment.newInstance(R.drawable.ic_no_connection, title, msg);
        errorFragment.setmListener(callback);

        this.setTargetFragment(null, 9);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.rel_error, errorFragment, TAG_ERROR_FRAGMENT)
                .commit();
    }

    private String compileErrors(Map<String, String> errors) {
       String compiledErrors = "invalid: ";
       if(!errors.isEmpty()){
           String[] errorsArray = errors.values().toArray(new String[errors.size()]);
           if(errorsArray.length > 1){
               for(int pos = 0; pos < errorsArray.length - 1; pos++)
                   if(pos != errorsArray.length -2)
                       compiledErrors += errorsArray[pos] + ", ";
                    else
                        compiledErrors += errorsArray[pos];

               compiledErrors += " and " + errorsArray[errorsArray.length -1];
               return  compiledErrors;
           }else{
               return compiledErrors += errorsArray[0];
           }
       }else
           return "";
    }

    private void clearErrors() {
        this.aedtName.setSelected(false);
        this.aedtEmailAddress.setSelected(false);
        this.aedtPassword.setSelected(false);
        this.aedtConfirmPassword.setSelected(false);
        this.txtError.setVisibility(View.GONE);
    }

    private Map<String, String> handleErrors(String name, String emailAddress, String password, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();
        if(name.length() == 0){
            errors.put(ERROR_NAME, "name");
            this.aedtName.setSelected(true);
        }

        if(!this.validEmailAddress(emailAddress)){
            errors.put(ERROR_EMAIL, "email");
            this.aedtEmailAddress.setSelected(true);
        }

        if(!this.validPassword(password)){
            errors.put(ERROR_PASSWORD, "password");
            this.aedtPassword.setSelected(true);
        }

        if(!confirmPassword.equals(password)) {
            if(!errors.containsKey(ERROR_PASSWORD))
                errors.put(ERROR_CONFIRM_PASSWORD, "password");
            this.aedtConfirmPassword.setSelected(true);
        }
        return errors;
    }

    private boolean validPassword(String password) {
        return password.length() > 0;
    }

    private boolean validEmailAddress(String emailAddress) {
        return (emailAddress.length() > 0);
    }

    private void showError(String error){
        if(this.txtError  != null){
            if(error != null){
                this.txtError.setMessage(error);
                this.txtError.setVisibility(View.VISIBLE);
            }else{
                this.txtError.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_create:{
                this.createAccount();
            }
        }
    }

    private void createAccount() {
        this.showIndeterminateProgress();
        this.clearErrors();

        String name = this.aedtName.getText().trim()
                , emailAddress = this.aedtEmailAddress.getText().trim()
                , password  = this.aedtPassword.getText().trim()
                , confirmPassword = this.aedtConfirmPassword.getText().trim();

        this.createUser(name, emailAddress, password, confirmPassword);
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE){

            return true;
        }
        return false;
    }

    @Override
    public String getTitle() {
        return "Sign Up";
    }


    public interface EventListener{
        void onCreateUser(CreateUserFragment creator, UserAfroObject user);
    }
}
