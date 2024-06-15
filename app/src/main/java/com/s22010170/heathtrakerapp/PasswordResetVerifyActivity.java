package com.s22010170.heathtrakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.s22010170.heathtrakerapp.utils.SendEmails;
import com.s22010170.heathtrakerapp.utils.SharedPrefsManager;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

public class PasswordResetVerifyActivity extends AppCompatActivity {

    ShowMessage showMessage;
    SharedPrefsManager prefsManager;
    SendEmails sendEmails;

    String MyEmail, MyEmailAppPassword, userEmail, passwordOPT;
    EditText passwordVerifyOPT;
    TextView resendEmail;
    Button verifyPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_reset_verfy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        passwordVerifyOPT = findViewById(R.id.password_verify_opt);
        resendEmail = findViewById(R.id.resend_email_text_view_password);
        verifyPasswordButton = findViewById(R.id.password_button);
        // get the email and password from the strings.xml
        MyEmail = getResources().getString(R.string.my_email);
        MyEmailAppPassword = getResources().getString(R.string.my_email_app_password);

        // create send email object
        sendEmails = new SendEmails(MyEmail, MyEmailAppPassword);
        // create shared preferences manager
        prefsManager = new SharedPrefsManager(this);
        // create show message object
        showMessage = new ShowMessage();

        // get the email from shared preferences
        userEmail = getIntent().getStringExtra("email");

        //TODO: Implement the password reset verification logic
        verifyPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordOPT = prefsManager.getString("password_otp", null);
                String OPT = passwordVerifyOPT.getText().toString();
                // check OPT input is empty
                if(OPT.isEmpty()){
                    showMessage.show("Warning", "OPT is empty!", PasswordResetVerifyActivity.this);
                    return;
                }
                // check OPT input is 6 digit
                if(OPT.length() != 6){
                    showMessage.show("Warning", "OPT should have 6 digit!", PasswordResetVerifyActivity.this);
                    return;
                }
                // check EmailOPT is null
                if(passwordOPT == null){
                    showMessage.show("Error", "Some thing went wrong. please sign up again!", PasswordResetVerifyActivity.this);
                    return;
                }
                // check OPT are same
                if(OPT.equals(passwordOPT)){
                    // clear OPT
                    String[] keysToRemove = {"password_otp"};
                    prefsManager.clearFields(keysToRemove);
                    // show toast message
                    Toast.makeText(PasswordResetVerifyActivity.this, "Password reset successfully.", Toast.LENGTH_SHORT).show();
                    // go to the email verify activity
                    Intent intent = new Intent(PasswordResetVerifyActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    showMessage.show("Error", "Invalid OPT", PasswordResetVerifyActivity.this);
                    System.out.println("EmailOPT: " + passwordOPT + " OPT: " + OPT);
                    return;
                }
            }
        });

        //TODO: Implement the resend email logic
        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear previous OPT
                String[] keysToRemove = {"password_otp"};
                prefsManager.clearFields(keysToRemove);
                // generate new OTP
                String newOPT = sendEmails.getOTP();
                // subject and message for the email
                String subject = "For Password Reset";
                String message = "Please verify your email by entering the OTP: " + newOPT + "\n\n" + "Thank you";
                // check userEmail is null
                if(userEmail == null){
                    showMessage.show("Error", "Some thing went wrong. please sign up again!", PasswordResetVerifyActivity.this);
                    return;
                }
                // send email to the user
                boolean isEmailSent = sendEmails.sendEmailToUser(userEmail, subject, message);
                // check if the email is sent
                if (isEmailSent) {
                    // save the OTP to shared preferences
                    prefsManager.saveString("password_otp", newOPT);
                    // show message
                    showMessage.show("Success",
                            "OPT has send to you email. please check your email for verify you email",
                            PasswordResetVerifyActivity.this);

                } else {
                    showMessage.show("Error", "Email Not Sent. please try again.", PasswordResetVerifyActivity.this);
                    return;
                }

            }
        });
    }
}