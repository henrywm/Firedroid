package com.app.henry.firedroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private EditText email;
    private EditText password;
    private Button   btnSignUp;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        email       = (EditText) findViewById(R.id.emailSignUp);
        password    = (EditText) findViewById(R.id.passwordSignUp);
        btnSignUp   = (Button) findViewById(R.id.signUpButton);

        btnSignUp.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.signUpButton:
                registerUser(view);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
    }

    private void registerUser(View view){
        String _email   = email.getText().toString().trim();
        String _pass    = password.getText().toString().trim();
        if(TextUtils.isEmpty(_email)){
            Snackbar.make(view,"Email is required!",Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(_pass)){
            Snackbar.make(view,"Password is required!",Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(!_email.contains("@")){
            Snackbar.make(view,"Invalid email address!",Snackbar.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Loading...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(_email,_pass)
        .addOnCompleteListener(task -> {
            if(task.isComplete()) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class ));
                }else{
                    Snackbar.make(view,"Couldn't register... Try again!",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, LoginActivity.class ));
    }
}
