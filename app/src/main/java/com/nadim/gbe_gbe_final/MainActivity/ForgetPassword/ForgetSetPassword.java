package com.nadim.gbe_gbe_final.MainActivity.ForgetPassword;

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
import android.widget.Toast;

import com.nadim.gbe_gbe_final.API.Api;
import com.nadim.gbe_gbe_final.MainActivity.LoginActivity;
import com.nadim.gbe_gbe_final.MainActivity.RegisterActivity;
import com.nadim.gbe_gbe_final.OtpActivity.VerifyPhoneActivity;
import com.nadim.gbe_gbe_final.R;
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

import static com.nadim.gbe_gbe_final.R.*;
import static com.nadim.gbe_gbe_final.R.string.*;
import static maes.tech.intentanim.CustomIntent.customType;

public class ForgetSetPassword extends AppCompatActivity implements View.OnClickListener {

    private String phoneNumber,otpNumber,forgetPassword,forgetConfirmPassword;

    private EditText editTextForgetPassword,editTextForgetConfirmPassword;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(layout.activity_forget_set_password);

        phoneNumber= getIntent().getStringExtra("phoneNumber");
        otpNumber= getIntent().getStringExtra("otpNumber");

        editTextForgetPassword= findViewById(id.editTextForgetPassword);
        editTextForgetConfirmPassword= findViewById(id.editTextForgetConfirmPassword);

        findViewById(id.buttonForgetNext).setOnClickListener(this);
        progressDialog = new ProgressDialog(ForgetSetPassword.this);
        progressDialog.setMessage(getString(forgetSetProgress));
        progressDialog.setCancelable(false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case id.buttonForgetNext:
                forgetPassword();
                break;

        }
    }

    private void forgetPassword() {
        forgetPassword = editTextForgetPassword.getText().toString().trim();
        forgetConfirmPassword = editTextForgetConfirmPassword.getText().toString().trim();

        if (forgetPassword.isEmpty()) {
            editTextForgetPassword.setError(getString(forgetsetError));
            editTextForgetPassword.requestFocus();
            return;
        }

        if (forgetPassword.length() < 5) {
            editTextForgetPassword.setError(getString(forgetSeterrortwo));
            editTextForgetPassword.requestFocus();
            return;
        }

        if (forgetConfirmPassword.isEmpty()) {
            editTextForgetConfirmPassword.setError(getString(forgetsetErrorthird));
            editTextForgetConfirmPassword.requestFocus();
            return;
        }
        if (!forgetConfirmPassword.equals(forgetPassword)) {
            editTextForgetConfirmPassword.setError(getString(forgetSetconfirmerror));
            editTextForgetConfirmPassword.requestFocus();
            return;
        }

        progressDialog.show();

        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.2.165.189:96/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        JsonObject jsonObjectFinal = new JsonObject();

        JSONObject jsonObjectName = new JSONObject();
        try {

            jsonObjectName.put("phoneNumber", phoneNumber);
            jsonObjectName.put("password", forgetPassword);
            jsonObjectName.put("otpNumber", otpNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        jsonObjectFinal = (JsonObject) jsonParser.parse(jsonObjectName.toString());
        Call<String> call = api.postForget(jsonObjectFinal);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String isSuccessful=response.body();
                    if (isSuccessful != null) {
                        if (isSuccessful.equals("false")) {
                            Toast.makeText(ForgetSetPassword.this, forgetSetFalseerror, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgetSetPassword.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ForgetSetPassword.this, forgetSetSussessful, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgetSetPassword.this, LoginActivity.class));
                            finish();
                        }
                    }

                } else {
                    Log.d("", "onResponse: ");
                    Toast.makeText(ForgetSetPassword.this, forgetSetResponseError, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ForgetSetPassword.this, forgetSetfailureError, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
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
