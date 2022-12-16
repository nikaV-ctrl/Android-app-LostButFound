package com.example.lostbutfound.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lostbutfound.MainActivity;
import com.example.lostbutfound.MainActivity2;
import com.example.lostbutfound.R;
import com.example.lostbutfound.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText emailEditText, passwordEditText;
    Button loginButton;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        createAccountBtnTextView = findViewById(R.id.createAccountBtnTextView);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(v-> loginUser());
        createAccountBtnTextView.setOnClickListener((v)-> startActivity(new Intent(LoginActivity.this, CreateAccount.class)));
    }



    void loginUser(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validateData(email, password);
        if(!isValidated){
            return ;
        }

        loginAccountInFirebase(email, password);
    }

    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                                Utility.showToast(LoginActivity.this, "Вы вошли в аккаунт");
                                finish();
                            }else{
                                Utility.showToast(LoginActivity.this, "Email не потвержден!");
                            }
                        }else{
                            Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
        });
    }


    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        } else{
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Введите правильный адрес электронной почты");
            return false;
        }

        if(password.length() == 0){
            passwordEditText.setError("Введите пароль");
            return false;
        }
        return true;
    }


}






