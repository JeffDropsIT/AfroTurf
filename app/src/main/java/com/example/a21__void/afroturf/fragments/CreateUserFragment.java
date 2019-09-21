package com.example.a21__void.afroturf.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.libIX.fragment.AfroFragment;
import com.example.a21__void.afroturf.libIX.ui.RichTextView;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.UserManager;
import com.example.a21__void.afroturf.object.UserAfroObject;
import com.example.a21__void.afroturf.pkgCommon.APIConstants;
import com.example.a21__void.afroturf.pkgCommon.AfroDropDown;
import com.example.a21__void.afroturf.pkgCommon.AfroEditText;
import com.example.a21__void.afroturf.user.UserGeneral;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateUserFragment extends AfroFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final String ERROR_NAME = "error_name"
            , ERROR_EMAIL = "error_email"
            , ERROR_PASSWORD = "error_password"
            , ERROR_CONFIRM_PASSWORD = "error_confirm_password";
    private static final String TAG_ERROR_FRAGMENT = "error_fragment_create_user";
    private static final int PROCESS_CREATE_ACCOUNT = 0;

    private RichTextView txtError;
    private AfroEditText aedtName,  aedtEmailAddress,  aedtPassword,  aedtConfirmPassword;
    private AfroDropDown adrpType;
    private EventListener eventListener;

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

        this.aedtName.setNextFocusDownId(this.aedtEmailAddress.getId());
        this.aedtEmailAddress.setNextFocusDownId(this.aedtPassword.getId());
        this.aedtPassword.setNextFocusDownId(this.aedtConfirmPassword.getId());

        this.adrpType = relParent.findViewById(R.id.adrp_type);
        this.adrpType.setDataset(new String[]{"Customer", "Stylist", "Manager"});

        this.showError(null);
        relParent.findViewById(R.id.txt_create).setOnClickListener(this);

        return relParent;
    }


    private void createUser(String name, String emailAddress, String password, String confirmPassword, int userType) {
        Map<String, String> errors = this.handleErrors(name, emailAddress, password, confirmPassword);

        if(errors.isEmpty()){
            UserManager userManager = UserManager.getInstance(this.getContext());
            UserGeneral currentUser = userManager.getCurrentUser();
            if(false){

            }else{
                userManager.signUpUser(name, emailAddress, password,userType, new CacheManager.ManagerRequestListener<UserGeneral>() {
                    @Override
                    public void onRespond(UserGeneral result) {
                        CreateUserFragment.this.hideProcess();
                        if(CreateUserFragment.this.eventListener != null)
                            CreateUserFragment.this.eventListener.onCreateUser(CreateUserFragment.this, result);
                    }

                    @Override
                    public void onApiError(CacheManager.ApiError apiError) {
                        CreateUserFragment.this.hideProcess();
                        if(apiError.errorCode == 409){
                            showError("Email already exists");
                            aedtEmailAddress.setSelected(true);
                        }else{
                            CreateUserFragment.this.showProcessError(PROCESS_CREATE_ACCOUNT, R.drawable.ic_no_connection, APIConstants.TITLE_NO_CONNECTION, APIConstants.MSG_NO_CONNECTION);
                        }

                        Log.i("test0", "onApiError: " + apiError.message + "|" + apiError.errorCode);
                    }
                });
            }
        }else{
            this.hideProcess();
            showError(compileErrors(errors));
        }
    }


    private String compileErrors(@NonNull Map<String, String> errors) {
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
        this.showProcess("Creating Account");
        this.clearErrors();

        String name = this.aedtName.getText().trim()
                , emailAddress = this.aedtEmailAddress.getText().trim()
                , password  = this.aedtPassword.getText().trim()
                , confirmPassword = this.aedtConfirmPassword.getText().trim();
        int userType = this.adrpType.getSelection();

        this.createUser(name, emailAddress, password, confirmPassword, userType);
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
    public void retryProcess(int processId) {
        if(processId == PROCESS_CREATE_ACCOUNT){
            this.createAccount();
        }
    }

    @Override
    public void cancelProcess(int processId) {

    }

    @Override
    public void refresh() {

    }

    public interface EventListener{
        void onCreateUser(CreateUserFragment creator, UserGeneral user);
    }
}
