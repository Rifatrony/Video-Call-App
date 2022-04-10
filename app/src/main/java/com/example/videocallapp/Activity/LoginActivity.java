package com.example.videocallapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.videocallapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText loginEmailEditText, loginPasswordEditText;
    Button loginButton, signUpButton;
    Intent intent;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initialization();

        setListener();

    }


    private void initialization() {

        loginEmailEditText = findViewById(R.id.loginEmailEditTextId);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditTextId);
        loginButton = findViewById(R.id.loginButtonId);
        signUpButton = findViewById(R.id.createNewAccountButtonId);

        progressBar = findViewById(R.id.progressBarId);

        auth = FirebaseAuth.getInstance();

    }


    private void setListener() {

        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.loginButtonId) {

            userLogin();
        }

        if (view.getId()==R.id.createNewAccountButtonId) {
            intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        }
    }

    private void userLogin() {
        String email, password;

        email = loginEmailEditText.getText().toString();
        password = loginPasswordEditText.getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
                    intent = new Intent(getApplicationContext(),DashBoardActivity.class);
                    startActivity(intent);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}