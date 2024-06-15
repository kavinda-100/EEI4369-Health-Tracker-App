package com.s22010170.heathtrakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.s22010170.heathtrakerapp.utils.DbBitmapUtility;
import com.s22010170.heathtrakerapp.utils.SendEmails;
import com.s22010170.heathtrakerapp.utils.SharedPrefsManager;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

public class SignUpActivity extends AppCompatActivity {
    DataBaseHelper authDataBaseHelper;
    ShowMessage showMessage;
    SharedPrefsManager prefsManager;
    DbBitmapUtility dbBitmapUtility;
    SendEmails sendEmails;

    String MyEmail, MyEmailAppPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // get the email and password from the strings.xml
        MyEmail = getResources().getString(R.string.my_email);
        MyEmailAppPassword = getResources().getString(R.string.my_email_app_password);

        // create send email object
        sendEmails = new SendEmails(MyEmail, MyEmailAppPassword);

        // create database
        authDataBaseHelper = new DataBaseHelper(this);
        // create shared preferences manager
        prefsManager = new SharedPrefsManager(this);
        // create dbBitmapUtility object
        dbBitmapUtility = new DbBitmapUtility();
        // create show message object
        showMessage = new ShowMessage();
        // call the methods
        signUp();
        backToWelcomeFromSingUp();
        backToSignInFromSingUp();
    }

    public void signUp(){
        Button signUpButton = findViewById(R.id.sign_up_button);
        EditText username = findViewById(R.id.sign_up_user_name);
        EditText email = findViewById(R.id.sign_up_email);
        EditText password = findViewById(R.id.sign_up_password);
        EditText confirmPassword = findViewById(R.id.sign_up_Confirm_password);

        // TODO:set on click listener for the sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if the username, email, password and confirm password are empty
                if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                    // show toast message
                    //Toast.makeText(SignUpActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    showMessage.show("Error", "Please fill all the fields", SignUpActivity.this);
                    return;
                }
                // check if the password length is less than 6
                if (password.getText().toString().length() < 6) {
                    // show toast message
                    //Toast.makeText(SignUpActivity.this, "Password length should be greater than 6", Toast.LENGTH_SHORT).show();
                    showMessage.show("Error", "Password length should be greater than 6", SignUpActivity.this);
                    return;
                }

                // check if the password and confirm password are not the same
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    // show toast message
                    //Toast.makeText(SignUpActivity.this, "Password and Confirm Password are not the same", Toast.LENGTH_SHORT).show();
                    showMessage.show("Error", "Password and Confirm Password are not the same", SignUpActivity.this);
                    return;
                }
                // sign up
                boolean isSignedUp = authDataBaseHelper.signUp(username.getText().toString(), email.getText().toString(), password.getText().toString());
                if (isSignedUp) {
                    // save email and user name to shared preferences
                    prefsManager.saveString("email", email.getText().toString());
                    prefsManager.saveString("name", username.getText().toString());
                    // subject and message for the email
                    String OPT = sendEmails.getOTP();
                    String subject = "Email Verification";
                    String message = "Please verify your email by entering the OTP: " + OPT + "\n\n" + "Thank you";
                    // send email to the user
                    boolean isEmailSent = sendEmails.sendEmailToUser(email.getText().toString(), subject, message);
                    // check if the email is sent
                    if (isEmailSent) {
                        // save the OTP to shared preferences
                        prefsManager.saveString("email_otp", OPT);
                        // show message
                        showMessage.show("Success", "OPT has send to you email. please check your email for verify you email in next screen.", SignUpActivity.this);
                        // wait for 5 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // go to the email verify activity
                                Intent intent = new Intent(SignUpActivity.this, EmailVerifyActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 4000); // 4000 milliseconds = 4 seconds

                    } else {
                        showMessage.show("Error", "Email Not Sent. please try again.", SignUpActivity.this);
                    }
                } else {
                    showMessage.show("Error", "Not Signed Up", SignUpActivity.this);
                }
            }
        });
    }



    private void backToSignInFromSingUp() {
        TextView backToSignInButton = findViewById(R.id.textSignUp);
        backToSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
    public void backToWelcomeFromSingUp(){
        LinearLayout backButtonArea = findViewById(R.id.Back_button_area_sign_up);
        backButtonArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

