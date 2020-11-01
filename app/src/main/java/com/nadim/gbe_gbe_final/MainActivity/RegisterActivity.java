package com.nadim.gbe_gbe_final.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nadim.gbe_gbe_final.API.Api;
import com.nadim.gbe_gbe_final.MainActivity.ForgetPassword.ForgetPasswordActivity;
import com.nadim.gbe_gbe_final.MainActivity.ForgetPassword.ForgetVerifyOtpActivity;
import com.nadim.gbe_gbe_final.OtpActivity.VerifyPhoneActivity;
import com.nadim.gbe_gbe_final.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextPhone, editTextName,
            editTextPassword, editTextConfirmPassword;

    String userName, userPassword, userConfirmPassword;
    String url;
    ProgressDialog progressDialog;
    private String beforeOtpPhone,afterOtpPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage(getString(R.string.registerActivityProgress));
        progressDialog.setCancelable(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignUp:
                userSignUp();
                break;
            case R.id.textViewLogin:
                startActivity(new Intent(this, LoginActivity.class));
                customType(RegisterActivity.this,"left-to-right");
                break;
        }
    }
    private void userSignUp() {

        userName = editTextName.getText().toString().trim();
        beforeOtpPhone = editTextPhone.getText().toString().trim();
        userPassword = editTextPassword.getText().toString().trim();
        userConfirmPassword = editTextConfirmPassword.getText().toString().trim();


        if (userName.isEmpty()) {
            editTextName.setError(getString(R.string.registerActivityUserNameError));
            editTextName.requestFocus();
            return;
        }

        if (beforeOtpPhone.isEmpty()) {
            editTextPhone.setError(getString(R.string.registerActivityPhoneError));
            editTextPhone.requestFocus();
            return;
        }
        if (beforeOtpPhone.length() < 11) {
            editTextPhone.setError(getString(R.string.registerActivitySecondPhoneError));
            editTextPhone.requestFocus();
            return;
        }

        if (userPassword.isEmpty()) {
            editTextPassword.setError(getString(R.string.registerActivityPasswordError));
            editTextPassword.requestFocus();
            return;
        }

        if (userPassword.length() < 5) {
            editTextPassword.setError(getString(R.string.registerActivitySencodEPassword));
            editTextPassword.requestFocus();
            return;
        }

        if (userConfirmPassword.isEmpty()) {
            editTextConfirmPassword.setError(getString(R.string.registerActivityConfirmPasswordError));
            editTextConfirmPassword.requestFocus();
            return;
        }
        if (!userConfirmPassword.equals(userPassword)) {
            editTextConfirmPassword.setError(getString(R.string.registerActivityCorfirmSencode));
            editTextConfirmPassword.requestFocus();
            return;
        }

        progressDialog.show();

        afterOtpPhone="+88"+beforeOtpPhone;

        checkUser();


        /*Retrofit retrofit = new Retrofit.Builder()
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
                            Toast.makeText(RegisterActivity.this, "এই ফোন নম্বরটি ইতিমধ্যে নিবন্ধভুক্ত হয়েছে", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "সফলভাবে নিবন্ধন সম্পন্ন", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, VerifyPhoneActivity.class));
                            finish();
                        }
                    }

                } else {
                    Log.d("", "onResponse: ");
                    Toast.makeText(RegisterActivity.this, "UserName Already Exist, Please try again later", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });*/


       /* url = String.format("GbegbeUserResistration/?fullName=%s&phoneNumber=%s&password=%s", userName, userPhone, userPassword);


        Api api = retrofit.create(Api.class);

        Call<String> regCall = api.userReg(url);

        regCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {

                    final String isSuccessful = response.body();
                    Log.d("Nothing", "onResponse: " + isSuccessful);
                    progressDialog.dismiss();


                    if (isSuccessful != null) {
                        if (isSuccessful.equals("false")) {
                            Toast.makeText(RegisterActivity.this, "এই ফোন নম্বরটি ইতিমধ্যে নিবন্ধভুক্ত হয়েছে", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "সফলভাবে নিবন্ধন সম্পন্ন", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                    }


                } else {
                    Toast.makeText(RegisterActivity.this, "নিবন্ধন ব্যর্থ", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "ব্যর্থ হয়েছে", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });*/


    }

    private void checkUser(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.2.165.189:96/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        Api api = retrofit.create(Api.class);
        JsonObject jsonObjectFinal = new JsonObject();

        JSONObject jsonObjectName = new JSONObject();
        try {
            jsonObjectName.put("PhoneNumber", afterOtpPhone);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonParser jsonParser = new JsonParser();
        jsonObjectFinal = (JsonObject) jsonParser.parse(jsonObjectName.toString());

        Call<String> call = api.postCheckUser(jsonObjectFinal);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String isSuccessful=response.body();
                    if (isSuccessful != null) {
                        if (isSuccessful.equals("true")) {
                            Toast.makeText(RegisterActivity.this, R.string.registerActivityTrueError, Toast.LENGTH_LONG).show();

                        } else {
                            startActivity(new Intent(RegisterActivity.this, VerifyPhoneActivity.class)
                                    .putExtra("FullName", userName)
                                    .putExtra("PhoneNumber", afterOtpPhone)
                                    .putExtra("Password", userPassword));
                            customType(RegisterActivity.this,"left-to-right");
                            finish();
                        }
                    }

                } else {
                    Log.d("", "onResponse: ");
                    Toast.makeText(RegisterActivity.this, R.string.registerActivityElseError, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, R.string.registerActivityFailureError, Toast.LENGTH_LONG).show();
            }
        });

    }
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
