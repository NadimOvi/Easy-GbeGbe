package com.nadim.gbe_gbe_final.OtpActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nadim.gbe_gbe_final.API.Api;
import com.nadim.gbe_gbe_final.MainActivity.LoginActivity;
import com.nadim.gbe_gbe_final.MainActivity.RegisterActivity;
import com.nadim.gbe_gbe_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyPhoneActivity extends AppCompatActivity {

    String userName, userPhone, userPassword;
    private  String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private Button buttonReTry;
    String code,code1;


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_verify_phone);
        if (getIntent().getExtras() != null) {
            userName = getIntent().getStringExtra("FullName");
            userPhone = getIntent().getStringExtra("PhoneNumber");
            userPassword = getIntent().getStringExtra("Password");
        }

        progressDialog = new ProgressDialog(VerifyPhoneActivity.this);
        progressDialog.setMessage(getString(R.string.VerifyPhoneProgress));
        progressDialog.setCancelable(false);


        mAuth = FirebaseAuth.getInstance();
        buttonReTry= findViewById(R.id.buttonReTry);

        editText=findViewById(R.id.editTextCode);

        sendVerificationCode(userPhone);
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText.getText().toString().trim();

                if (code.isEmpty()||code.length()<6){
                    editText.setError(getString(R.string.verifyPhoneCodeError));
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });

        findViewById(R.id.buttonReTry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCodeAgain(userPhone);
            }
        });
    }


    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        code1=code;
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            /*code1= code;*/
                            openRetrofit();
                            /*Intent intent= new Intent(VerifyPhoneActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/
                        }else{
                            Toast.makeText(VerifyPhoneActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void openRetrofit() {

        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.2.165.189:96/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        JsonObject jsonObjectFinal = new JsonObject();

        JSONObject jsonObjectName = new JSONObject();
        try {
            jsonObjectName.put("FullName", userName);
            jsonObjectName.put("PhoneNumber", userPhone);
            jsonObjectName.put("Password", userPassword);
            jsonObjectName.put("otpNumber", code1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        jsonObjectFinal = (JsonObject) jsonParser.parse(jsonObjectName.toString());
        Call<String> call = api.postReg(jsonObjectFinal);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String isSuccessful=response.body();
                    if (isSuccessful != null) {
                        if (isSuccessful.equals("false")) {
                            Toast.makeText(VerifyPhoneActivity.this, R.string.verifyPhoneFalseError, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(VerifyPhoneActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(VerifyPhoneActivity.this, R.string.verifyPhoneSuccessError, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(VerifyPhoneActivity.this, LoginActivity.class));
                            finish();
                        }
                    }

                } else {
                    Log.d("", "onResponse: ");
                    Toast.makeText(VerifyPhoneActivity.this, R.string.verifyPhoneElseError, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(VerifyPhoneActivity.this, R.string.verifyPhoneFailureError, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }
    private void sendVerificationCodeAgain(String phoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,30, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
    }

    private void sendVerificationCode(String userPhone){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(userPhone,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
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
             code = phoneAuthCredential.getSmsCode();
            if(code!= null){
                editText.setText(code);

                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this,  e.getMessage(),Toast.LENGTH_LONG).show();
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
