package com.example.rahul.app6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText inputName, inputEmail, inputPass, inputConfirm;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth auth;
    private Button buttonSignUp;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        inputName = (EditText) findViewById(R.id.editText10);
        inputEmail = (EditText) findViewById(R.id.editText11);
        inputPass = (EditText) findViewById(R.id.editText12);
        inputConfirm = (EditText) findViewById(R.id.editText13);
        buttonSignUp = (Button)findViewById(R.id.button7);

        buttonSignUp.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://app6-a608f.firebaseio.com/");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
    }

    public void onClick(View view) {
        registerUser();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
    }

    private void registerUser() {
        final String name = inputName.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        String pass = inputPass.getText().toString().trim();
        String confirmPass = inputConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(confirmPass)) {
           Toast.makeText(getApplicationContext(), "Fill Up Empty Field", Toast.LENGTH_SHORT).show();
           return;
        } else
            if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Fill Up Empty Field", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Fill Up Empty Field", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(name)) {
           Toast.makeText(getApplicationContext(), "Fill Up Empty Field", Toast.LENGTH_SHORT).show();
           return;
        } else if (!pass.equals(confirmPass)) {
            Toast.makeText(getApplicationContext(), "Confirm your Password Correctly", Toast.LENGTH_SHORT).show();
            return;
        } else if (pass.length() < 6) {
            Toast.makeText(this, "Password must not be less than 6 letters", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.setMessage("Registering User...");
        mProgressDialog.show();

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressDialog.hide();
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(auth.getCurrentUser().getUid())
                                    .setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        emailVerify();
                                        auth.signOut();
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                        Toast.makeText(SignUpActivity.this, "Sign Up Failed." + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void emailVerify() {
        FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Verification Email Sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("SignUpActivity : ", "sendEmailVerification", task.getException());
                        Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}

