package com.s22010170.heathtrakerapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.s22010170.heathtrakerapp.utils.DataBaseHelper;
import com.s22010170.heathtrakerapp.utils.SharedPrefsManager;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

public class SignInActivity extends AppCompatActivity {
    DataBaseHelper authDataBaseHelper;
    ShowMessage showMessage;
    SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // create database
        authDataBaseHelper = new DataBaseHelper(this);
        // create shared preferences manager
        prefsManager = new SharedPrefsManager(this);
        // create show message object
        showMessage = new ShowMessage();

        signIn();
        reset();
        backToWelcomeFromSignIn();
        backToSignUpFromSignIn();

    }

    public void signIn(){
        Button signInButton = findViewById(R.id.sign_in_button);
        EditText email = findViewById(R.id.sign_in_email);
        EditText password = findViewById(R.id.sign_in_password);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if the email and password are empty
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    // show toast message
                    showMessage.show("Error", "Please fill all the fields", SignInActivity.this);
                    return;
                }
                // check if the password length is less than 6
                if (password.getText().toString().length() < 6) {
                    // show toast message
                    //Toast.makeText(SignUpActivity.this, "Password length should be greater than 6", Toast.LENGTH_SHORT).show();
                    showMessage.show("Error", "Password length should be greater than 6", SignInActivity.this);
                    return;
                }
                // sign in
                Cursor cursor = authDataBaseHelper.signIn(email.getText().toString(), password.getText().toString());
                // check if the cursor is empty
                if (cursor.getCount() == 0) {
                    showMessage.show("Error", "Invalid Credentials", SignInActivity.this);
                    return;
                }else {
                    while (cursor.moveToNext()){
                        // save email and user name to shared preferences
                        prefsManager.saveString("email", cursor.getString(2));
                        prefsManager.saveString("name", cursor.getString(1));
                    }
                    Toast.makeText(SignInActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void backToSignUpFromSignIn() {
        TextView backToSignUpButton = findViewById(R.id.textSignIn);
        backToSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void reset(){
        TextView resetButton = findViewById(R.id.textReset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void backToWelcomeFromSignIn(){
        LinearLayout backButtonArea = findViewById(R.id.Back_button_area_sign_in);
        backButtonArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}