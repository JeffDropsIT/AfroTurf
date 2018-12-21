package com.example.a21__void.afroturf.fragments;


import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
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
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.UserManager;
import com.example.a21__void.afroturf.object.UserAfroObject;

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

    private TextView txtError;
    private LinearLayout linName, linEmailAddress, linPassword, linConfirmPassword;
    private EditText edtName, edtEmailAddress, edtPassword, edtConfirmPassword;
    //private MaterialProgressBar progBackgroundWork;

    public CreateUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout relParent = (RelativeLayout)inflater.inflate(R.layout.fragment_create_user, container, false);

        this.txtError = relParent.findViewById(R.id.txt_error);

        this.linName = relParent.findViewById(R.id.lin_name);
        this.linEmailAddress = relParent.findViewById(R.id.lin_email_address);
        this.linPassword = relParent.findViewById(R.id.lin_password);
        this.linConfirmPassword = relParent.findViewById(R.id.lin_confirm_password);

        this.edtName = relParent.findViewById(R.id.edt_name);
        this.edtEmailAddress = relParent.findViewById(R.id.edt_email_address);
        this.edtPassword = relParent.findViewById(R.id.edt_password);
        this.edtConfirmPassword = relParent.findViewById(R.id.edt_confirm_password);
      //iviv  this.progBackgroundWork = relParent.findViewById(R.id.prog_background_work);

        relParent.findViewById(R.id.img_toggle).setOnClickListener(this);
        this.edtName.setOnClickListener(this);
        this.edtEmailAddress.setOnClickListener(this);
        this.edtPassword.setOnClickListener(this);
        this.edtConfirmPassword.setOnClickListener(this);
        this.edtConfirmPassword.setOnEditorActionListener(this);


        return relParent;
    }

    @Override
    public void shouldProceed(Callback<Boolean> callback) {
        if(this.getView() == null){
            callback.onRespond(true);
            return;
        }

        this.showIndeterminateProgress();
        this.clearErrors();

        String name = this.edtName.getText().toString().trim()
                , emailAddress = this.edtEmailAddress.getText().toString().trim()
                , password  = this.edtPassword.getText().toString().trim()
                , confirmPassword = this.edtConfirmPassword.getText().toString().trim();

        this.createUser(name, emailAddress, password, confirmPassword, callback);
    }

    private void createUser(String name, String emailAddress, String password, String confirmPassword, final Callback<Boolean> callback) {
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
                        callback.onRespond(true);
                    }

                    @Override
                    public void onApiError(CacheManager.ApiError apiError) {
                        if(apiError.errorCode == 409){
                            hideIndeterminateProgress();
                            showError("Email already exists");
                            linEmailAddress.setSelected(true);
                            callback.onRespond(false);
                        }else{
                            hideIndeterminateProgress();
                            CreateUserFragment.this.showNetworkError("No Connection", "Please make sure your wifi or mobile data is turned on.", new ErrorFragment.OnFragmentInteractionListener(){
                                @Override
                                public void onRequestRetry() {
                                    shouldProceed(callback);
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
            callback.onRespond(false);
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
        this.linName.setSelected(false);
        this.linEmailAddress.setSelected(false);
        this.linPassword.setSelected(false);
        this.linConfirmPassword.setSelected(false);
        this.txtError.setVisibility(View.INVISIBLE);
    }

    private Map<String, String> handleErrors(String name, String emailAddress, String password, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();
        if(name.length() == 0){
            errors.put(ERROR_NAME, "name");
            this.linName.setSelected(true);
        }

        if(!this.validEmailAddress(emailAddress)){
            errors.put(ERROR_EMAIL, "email");
            this.linEmailAddress.setSelected(true);
        }

        if(!this.validPassword(password)){
            errors.put(ERROR_PASSWORD, "password");
            this.linPassword.setSelected(true);
        }

        if(!confirmPassword.equals(password)) {
            if(!errors.containsKey(ERROR_PASSWORD))
                errors.put(ERROR_CONFIRM_PASSWORD, "password");
            this.linConfirmPassword.setSelected(true);
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
                this.txtError.setText(error);
                this.txtError.setVisibility(View.VISIBLE);
            }else{
                this.txtError.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edt_name:
                this.linName.setSelected(false);
                break;
            case R.id.edt_email_address:
                this.linEmailAddress.setSelected(false);
                break;
            case R.id.edt_password:
                this.linPassword.setSelected(false);
                break;
            case R.id.edt_confirm_password:
                this.linConfirmPassword.setSelected(false);
                break;
            case R.id.img_toggle:
                if(v.isSelected()){
                    v.setSelected(false);
                    ((ImageView)v).setColorFilter(ContextCompat.getColor(this.getContext(), R.color.background_light_shade3), PorterDuff.Mode.MULTIPLY);
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                }else{
                    v.setSelected(true);
                    ((ImageView)v).setColorFilter(ContextCompat.getColor(this.getContext(), R.color.colorAccent_three), PorterDuff.Mode.MULTIPLY);
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

        }
    }

  /*  public void showProgress(){
        if(this.progBackgroundWork != null)
            this.progBackgroundWork.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        if(this.progBackgroundWork != null)
            this.progBackgroundWork.setVisibility(View.GONE);
    }*/

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE){
            Interpol.hideKeyBoard(this.getContext(), edtConfirmPassword);
            return true;
        }
        return false;
    }

    @Override
    public String getTitle() {
        return "Sign Up";
    }
}
