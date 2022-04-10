package com.example.videocallapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.videocallapp.Models.User;
import com.example.videocallapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText signUpEmailEditText, signUpPasswordEditText, signUpNameEditText;
    Button signUpButton, alreadyHaveAccountButton;
    Intent intent;
    FirebaseAuth auth;
    ProgressBar progressBar;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setTitle("Sign Up");

        initialization();
        setListener();


    }

    private void initialization() {

        signUpEmailEditText = findViewById(R.id.signUpEmailEditTextId);
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditTextId);
        signUpNameEditText = findViewById(R.id.signUpNameEditTextId);

        signUpButton = findViewById(R.id.signUpButtonId);
        alreadyHaveAccountButton = findViewById(R.id.alreadyHaveAccountButtonId);

        progressBar = findViewById(R.id.progressBarId);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

    }


    private void setListener() {

        alreadyHaveAccountButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.signUpButtonId){

            userRegister();

        }

        if (view.getId()==R.id.alreadyHaveAccountButtonId){
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void userRegister() {

        String email, pass;

        email = signUpEmailEditText.getText().toString();
        pass = signUpPasswordEditText.getText().toString();
        String name = signUpNameEditText.getText().toString();

        if (name.isEmpty()){
            signUpNameEditText.setError("Enter Name");
            signUpNameEditText.requestFocus();
            return;
        }

        if (email.isEmpty()){
            signUpEmailEditText.setError("Enter email address");
            signUpEmailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signUpEmailEditText.setError("Enter a valid email address");
            signUpEmailEditText.requestFocus();
            return;
        }
        if (pass.isEmpty()){
            signUpPasswordEditText.setError("Enter password");
            signUpPasswordEditText.requestFocus();
            return;
        }
        if (pass.length()<6){
            signUpPasswordEditText.setError("Minimum password length is 6");
            signUpPasswordEditText.requestFocus();
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPass(pass);
        user.setName(name);

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()){

                    database.collection("Users")
                            .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                        }
                    });

                    Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                    signUpEmailEditText.setText("");
                    signUpPasswordEditText.setText("");
                    signUpNameEditText.setText("");

                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}