package com.s22010170.heathtrakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.s22010170.heathtrakerapp.utils.DataBaseHelper;
import com.s22010170.heathtrakerapp.utils.DbBitmapUtility;
import com.s22010170.heathtrakerapp.utils.SendEmails;
import com.s22010170.heathtrakerapp.utils.SharedPrefsManager;
import com.s22010170.heathtrakerapp.utils.ShowMessage;

public class EmailVerifyActivity extends AppCompatActivity {
    ShowMessage showMessage;
    SharedPrefsManager prefsManager;
    SendEmails sendEmails;

    String MyEmail, MyEmailAppPassword, userEmail, EmailOPT;
    EditText emailVerifyOPT;
    TextView resendEmail;
    Button verifyEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_verfy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailVerifyOPT = findViewById(R.id.email_verify_opt);
        resendEmail = findViewById(R.id.resend_email_text_view);
        verifyEmailButton = findViewById(R.id.verify_email_button);
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
        userEmail = prefsManager.getString("email", null);

        //TODO: handle verify OPT
        verifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailOPT = prefsManager.getString("email_otp", null);
                String OPT = emailVerifyOPT.getText().toString();
                // check OPT input is empty
                if(OPT.isEmpty()){
                    showMessage.show("Warning", "OPT is empty!", EmailVerifyActivity.this);
                    return;
                }
                // check OPT input is 6 digit
                if(OPT.length() != 6){
                    showMessage.show("Warning", "OPT should have 6 digit!", EmailVerifyActivity.this);
                    return;
                }
                // check EmailOPT is null
                if(EmailOPT == null){
                    showMessage.show("Error", "Some thing went wrong. please sign up again!", EmailVerifyActivity.this);
                    return;
                }
                // check OPT are same
                if(OPT.equals(EmailOPT)){
                    // clear OPT
                    String[] keysToRemove = {"email_otp"};
                    prefsManager.clearFields(keysToRemove);
                    // save the email verified status to shared preferences
                    prefsManager.saveBoolean("isEmailVerified", true);
                    // show toast message
                    Toast.makeText(EmailVerifyActivity.this, "Signed Up", Toast.LENGTH_SHORT).show();
                    // go to the email verify activity
                    Intent intent = new Intent(EmailVerifyActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    showMessage.show("Error", "Invalid OPT", EmailVerifyActivity.this);
                    System.out.println("EmailOPT: " + EmailOPT + " OPT: " + OPT);
                    return;
                }
            }
        });

        //TODO: resend Email
        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear previous OPT
                String[] keysToRemove = {"email_otp"};
                prefsManager.clearFields(keysToRemove);
                // generate new OTP
                String newOPT = sendEmails.getOTP();
                // subject and message for the email
                String subject = "Email Verification";
                String message = "Please verify your email by entering the OTP: " + newOPT + "\n\n" + "Thank you";
                // check userEmail is null
                if(userEmail == null){
                    showMessage.show("Error", "Some thing went wrong. please sign up again!", EmailVerifyActivity.this);
                    return;
                }
                // send email to the user
                boolean isEmailSent = sendEmails.sendEmailToUser(userEmail, subject, message);
                // check if the email is sent
                if (isEmailSent) {
                    // save the OTP to shared preferences
                    prefsManager.saveString("email_otp", newOPT);
                    // show message
                    showMessage.show("Success",
                            "OPT has send to you email. please check your email for verify you email",
                            EmailVerifyActivity.this);

                } else {
                    showMessage.show("Error", "Email Not Sent. please try again.", EmailVerifyActivity.this);
                    return;
                }
            }
        });
    }


}