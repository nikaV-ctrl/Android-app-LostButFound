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

import com.example.lostbutfound.Model.Users;
import com.example.lostbutfound.R;
import com.example.lostbutfound.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {


    TextInputEditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountButton;
    ProgressBar progressBar;
    TextView loginBtnTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        init();

        createAccountButton.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener((v)-> startActivity(new Intent(CreateAccount.this, LoginActivity.class)));
    }

    private void init(){
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        createAccountButton = findViewById(R.id.createAccountButton);
        progressBar = findViewById(R.id.progressBar);
        loginBtnTextView = findViewById(R.id.loginBtnTextView);
    }

    void createAccount(){
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(name, email, password, confirmPassword);
        if(!isValidated){
            return ;
        }

        createAccountInFirebase(name, email, password);
    }

    void createAccountInFirebase(String name, String email, String password){
        changeInProgress(true);

        DatabaseReference usersDataBase = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccount.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            Users newUser = new Users(name, email);
                            usersDataBase.push().setValue(newUser);
                            // creating acc is done
                            Utility.showToast(CreateAccount.this, "Аккаунт успешно создан, проверьте почту для потверждения");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else{
                            // failure
                            Utility.showToast(CreateAccount.this, task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountButton.setVisibility(View.GONE);
        } else{
            progressBar.setVisibility(View.GONE);
            createAccountButton.setVisibility(View.VISIBLE);
        }
    }



    boolean validateData(String name, String email, String password, String confirmPassword){
        if(name.length() == 0){
            nameEditText.setError("Введите имя");
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Введите правильный адрес электронной почты");
            return false;
        }

        if(password.length()<6){
            passwordEditText.setError("Минимальное количество символов в пароле: 6");
            return false;
        }

        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Пароль не совпадает");
            return false;
        }

        return true;
    }
}



















