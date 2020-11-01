package com.nadim.gbe_gbe_final.MainActivity.ForgetPassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nadim.gbe_gbe_final.MainActivity.LoginActivity;
import com.nadim.gbe_gbe_final.OtpActivity.VerifyPhoneActivity;
import com.nadim.gbe_gbe_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ForgetVerifyOtpActivity extends AppCompatActivity {

    private  String verificationId;
    private Button buttonReTry;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    String otpCode;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_forget_verify_otp);

        mAuth = FirebaseAuth.getInstance();

        editText=findViewById(R.id.editTextCode);
        buttonReTry= findViewById(R.id.buttonReTry);

        phoneNumber= getIntent().getStringExtra("phoneNumber");
        sendVerificationCode(phoneNumber);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();
                if (code.isEmpty()||code.length()<6){
                    editText.setError(getString(R.string.forgetVerifyCodeerror));
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });

        findViewById(R.id.buttonReTry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCodeAgain(phoneNumber);
            }
        });
    }
    private void verifyCode(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        otpCode=code;
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(ForgetVerifyOtpActivity.this, ForgetSetPassword.class)
                            .putExtra("phoneNumber",phoneNumber)
                            .putExtra("otpNumber",otpCode));
                            finish();

                        }else{
                            Toast.makeText(ForgetVerifyOtpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void sendVerificationCodeAgain(String phoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,30, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,30, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!= null){

                editText.setText(code);


                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ForgetVerifyOtpActivity.this, R.string.forgetVerifyVerificationError,Toast.LENGTH_LONG).show();
        }
    };

    public void loadLocale(){
        SharedPreferences prefs= getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }


    private void setLocale(String lang){

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor= getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
}
