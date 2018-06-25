package com.example.a21__void.afroturf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class signUpActivity extends AppCompatActivity implements View.OnClickListener{


    Button loginBtn;
    EditText editTextUsername, editTextPassword;
    ProgressBar progBar;


    //firebase stuff
    private FirebaseAuth mAuth;
    private String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        //firebase init
        mAuth = FirebaseAuth.getInstance();


        //init views
        progBar = findViewById(R.id.progBar);
        loginBtn = findViewById(R.id.login_btn);
        editTextUsername = findViewById(R.id.username_tv);
        editTextPassword = findViewById(R.id.password_tv);

        //setOnClick
        loginBtn.setOnClickListener(this);


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    private void validateAndSignIn(){

        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        if(username.isEmpty()){

            editTextUsername.setHint("email required");
            editTextUsername.requestFocus();
            return;

        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            editTextPassword.setHint("enter valid email");
            editTextPassword.requestFocus();
            return;
        }
        if(password.isEmpty()){

            editTextPassword.setHint("password required");
            editTextPassword.requestFocus();
            return;

        }
        if(password.length() < 6 ){

            editTextPassword.setHint("minimum password length is 6");
            editTextPassword.requestFocus();
            return;

        }

        userLogin();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                //do something
                validateAndSignIn();
                break;

        }
    }

    private void userLogin() {
       // validate();
        //if(!(username.isEmpty() || password.isEmpty())){

            progBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    progBar.setVisibility(View.GONE);

                    if(task.isSuccessful()){

                        Toast.makeText(signUpActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }else {
                        Toast.makeText(signUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }





                }

            });



        editTextUsername.setHint("username");
        editTextPassword.setHint("password");
    }
}
