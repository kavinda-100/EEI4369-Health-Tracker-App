package com.s22010170.heathtrakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.s22010170.heathtrakerapp.utils.DataBaseHelper;
import com.s22010170.heathtrakerapp.utils.SendEmails;
import com.s22010170.heathtrakerapp.utils.SharedPrefsManager;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

public class ResetPasswordActivity extends AppCompatActivity {
    DataBaseHelper authDataBaseHelper;
    ShowMessage showMessage;
    SendEmails sendEmails;
    SharedPrefsManager prefsManager;
    String MyEmail, MyEmailAppPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
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
        // create show message object
        showMessage = new ShowMessage();
        // create shared preferences manager
        prefsManager = new SharedPrefsManager(this);
        // methods
        backToResetFromToSingUp();
        reset();
    }
    public void reset(){
        Button resetButton = findViewById(R.id.reset_button);
        EditText email = findViewById(R.id.reset_email);
        EditText password = findViewById(R.id.reset_password);
        EditText confirmPassword = findViewById(R.id.reset_Confirm_password);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if the email, password and confirm password are empty
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                    // show toast message
                    showMessage.show("Error", "Please fill all the fields", ResetPasswordActivity.this);
                    return;
                }
                // check if the password and confirm password are not the same
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    // show toast message
                    showMessage.show("Error", "Password and confirm password are not the same", ResetPasswordActivity.this);
                    return;
                }
                // reset password
                boolean isResetPassword = authDataBaseHelper.resetPassword(email.getText().toString(), password.getText().toString());
                // check if the password is reset
                if (isResetPassword) {
                    // get the OTP
                    String OPT = sendEmails.getOTP();
                    // subject and message for the email
                    String subject = "For Password Reset";
                    String message = "Please verify your email by entering the OTP: " + OPT + "\n\n" + "Thank you";
                    // send email to the user
                    boolean isEmailSent = sendEmails.sendEmailToUser(email.getText().toString(), subject, message);
                    // check if the email is sent
                    if (isEmailSent) {
                        // save the OTP to shared preferences
                        prefsManager.saveString("password_otp", OPT);
                        // show message
                        showMessage.show("Success",
                                "OPT has send to you email. please check your email for verify you email in next screen.",
                                ResetPasswordActivity.this);
                        // wait for 5 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // go to the email verify activity
                                Intent intent = new Intent(ResetPasswordActivity.this, PasswordResetVerifyActivity.class);
                                intent.putExtra("email", email.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                        }, 4000); // 4000 milliseconds = 4 seconds

                    } else {
                        showMessage.show("Error", "Email Not Sent. please try again.", ResetPasswordActivity.this);
                    }

                }else{
                    // show toast message
                    showMessage.show("Error", "Invalid email", ResetPasswordActivity.this);
                }

            }
        });
    }

    private void backToResetFromToSingUp() {
        LinearLayout backToSignInButton = findViewById(R.id.Back_button_area_reset);
        backToSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

}