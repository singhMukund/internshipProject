package com.example.rahul.app6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputName, inputEmail, inputPass, inputConfirm;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        inputName = (EditText) findViewById(R.id.editText10);
        inputEmail = (EditText) findViewById(R.id.editText11);
        inputPass = (EditText) findViewById(R.id.editText12);
        inputConfirm = (EditText) findViewById(R.id.editText13);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser CurrentUser = auth.getCurrentUser();
    }

    public void signUp(View view) {
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String pass = inputPass.getText().toString().trim();
        String confirmPass = inputConfirm.getText().toString().trim();

        if(!validateForm(name, email, pass, confirmPass)){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Sign Up Completed, Login Now", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    Toast.makeText(SignUpActivity.this, "Sign Up Failed.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private boolean validateForm(String name, String email, String pass, String confirmPass) {
        boolean valid = true;
        if (TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(getApplicationContext(), "Fill Up Empty Field", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputConfirm.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Fill Up Empty Field", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Fill Up Empty Field", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputPass.setError(null);
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Fill Up Empty Field", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputName.setError(null);
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(getApplicationContext(), "Confirm your Password Correctly", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            inputConfirm.setError(null);
        }
        
        return valid;
    }

}

