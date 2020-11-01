package com.nadim.gbe_gbe_final.MainActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nadim.gbe_gbe_final.API.Api;
import com.nadim.gbe_gbe_final.BuildConfig;
import com.nadim.gbe_gbe_final.MainActivity.ForgetPassword.ForgetPasswordActivity;
import com.nadim.gbe_gbe_final.Model.Login;
import com.nadim.gbe_gbe_final.R;
import com.nadim.gbe_gbe_final.SharePreference.SharePreferenceConfig;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SharePreferenceConfig preferenceConfig;
    private EditText editTextPhone, editTextPassword;
    private TextView forgetPassword;
    Button changeLang;
    CheckBox checkBox;
    CheckBox chkRemember;
    SharedPreferences sp;
    SharedPreferences.Editor sped;
    /*SwitchCompat langSwitch;*/


    private Button submitButton;

    private String beforeOtpPhone,afterOtpPhone;
    private String password;
    String  url;
    ProgressDialog progressDialog;
    private int userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstRun();
        setContentView(R.layout.activity_login);


        if (isConnected()) {
            preInitialize();
            prcGetRemember();
        } else {
            Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
        }
        /*changeLang=findViewById(R.id.changeMyLanguage);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* showChangeLanguageDialog();*//*
            }
        });*/

    }


    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            loadLocale();
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO This is a new install (or the user cleared the shared preferences)
            setLocale("bn");


        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }
    private void preInitialize(){
        preferenceConfig= new SharePreferenceConfig(getApplicationContext());

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        forgetPassword= findViewById(R.id.forgetPassword);

        userId = getIntent().getIntExtra("userId",0);


        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.textViewRegister).setOnClickListener(this);
        findViewById(R.id.forgetPassword).setOnClickListener(this);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getString(R.string.loginActivityProgress));
        progressDialog.setCancelable(false);

        /*if (preferenceConfig.readLoginStatus()){
            startActivity(new Intent(LoginActivity.this, ImageChoiceActivity.class)
                    .putExtra("PhoneNumber", afterOtpPhone));
            finish();
        }*/
        /*langSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (langSwitch.isChecked()){
                    setLocale("bn");
                    recreate();

                }else{
                    setLocale("en");
                    recreate();
                }
            }
        });*/
        chkRemember = (CheckBox) findViewById(R.id.chkRemember);
        sp = getApplicationContext().getSharedPreferences("GTR", MODE_PRIVATE);
        sped = sp.edit();


        checkBox= findViewById(R.id.changeMyLanguageCheck);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageCheck();

                progressDialog.dismiss();
            }
        });
    }
    private void showChangeLanguageCheck(){
        progressDialog.dismiss();


        if(checkBox.isChecked()){

            setLocale("bn");
            recreate();

        }else{
            setLocale("en");
            recreate();
            checkBox.setChecked(false);
        }


    }


    private void showChangeLanguageDialog(){

        final String[] listItems ={"English","বাংলা"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
        mBuilder.setTitle(getString(R.string.changeLang));
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i==0){
                    setLocale("en");
                    recreate();
                }else if(i==1){

                    setLocale("bn");
                    recreate();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();


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

    public void loadLocale(){
            SharedPreferences prefs= getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            String language = prefs.getString("My_Lang","");
            setLocale(language);
    }
    private boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                validationUser();
                break;
            case R.id.textViewRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                customType(LoginActivity.this,"left-to-right");
                break;
            case R.id.forgetPassword:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                customType(LoginActivity.this,"left-to-right");
                break;
        }
    }
    private void prcSetRemember() {
        String strRemember = "";
        if (chkRemember.isChecked()) {
            strRemember = "Remember";
            sped.putString("PhoneNumber", editTextPhone.getText().toString().trim());
            sped.putString("Password", editTextPassword.getText().toString().trim());
            sped.putString("Remember", strRemember);
            sped.commit();
        } else {
            sped.putString("PhoneNumber", "");
            sped.putString("Password", "");
            sped.putString("Remember", "");
            sped.commit();
        }
    }
    private void prcGetRemember() {
        progressDialog.dismiss();
        if (sp.contains("PhoneNumber")) {
            String strPassword = "";
            editTextPhone.setText(sp.getString("PhoneNumber", ""));
            editTextPassword.setText(sp.getString("Password", ""));
            chkRemember.setChecked(false);
            strPassword = sp.getString("Remember", "");
            if (strPassword.length() != 0) {
                chkRemember.setChecked(true);
            }
            prcValidateUser("Auto");
        }
    }
    private void prcValidateUser(String Flag) {
       /* progressDialog.show();*/
        //Validating User :: Using Async Task
        try {
            validationUser();

        } catch (Exception ex) {
            Log.d("ValUser", ex.getMessage());
        }
    }

    private void validationUser(){

        beforeOtpPhone = editTextPhone.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
      /*  beforeOtpPhone="";
        password="";*/

        if (beforeOtpPhone.isEmpty()) {
            editTextPhone.setError(getString(R.string.loginActivityOtpPhoneError));
            editTextPhone.requestFocus();
            return;
        }
        if (beforeOtpPhone.length() < 11) {
            editTextPhone.setError(getString(R.string.loginActivityphoneErrortwo));
            editTextPhone.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.loginActivityPasswordError));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 5) {
            editTextPassword.setError(getString(R.string.loginActivityPasswordLenthError));
            editTextPassword.requestFocus();
            return;
        }

        userLogin();
    }

    private void userLogin() {
       /* prcValidateUser("");*/

        progressDialog.show();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.2.165.189:96/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Api api = retrofit.create(Api.class);

        JsonObject jsonObjectFinal = new JsonObject();

        JSONObject jsonObjectName = new JSONObject();

        afterOtpPhone="+88"+beforeOtpPhone;

        try {

            jsonObjectName.put("PhoneNumber", afterOtpPhone);
            jsonObjectName.put("Password", password);
            /*jsonObjectName.put("userId", userId);*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        jsonObjectFinal = (JsonObject) jsonParser.parse(jsonObjectName.toString());
        Call<Login> obj = api.postLogin(jsonObjectFinal);

        obj.enqueue(new Callback<Login>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<Login> obj, Response<Login> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    int counter = 0;

                    Login login= response.body();

                    Login isSuccessful=response.body();

                    if (isSuccessful != null) {
                        login.getUserId();
                        Integer a= login.getUserId();
                        if (isSuccessful.equals("")) {
                            Toast.makeText(LoginActivity.this, R.string.loginActivitySucessError, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.loginActivitySuccess, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, ProfileActivity.class)
                                    .putExtra("userId", a)
                                    .putExtra("PhoneNumber", afterOtpPhone)
                                    .putExtra("Password", password));
                            prcSetRemember();
                            customType(LoginActivity.this,"left-to-right");
                            /*preferenceConfig.writeLoginStatus(true);*/
                            finish();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, R.string.loginActivityElseerror, Toast.LENGTH_LONG).show();

                        if (counter<3) {
                            forgetPassword.setVisibility(View.VISIBLE);
                            forgetPassword.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                                    customType(LoginActivity.this,"left-to-right");
                                }
                            });
                        } else {
                            forgetPassword.setVisibility(View.INVISIBLE);
                            counter++;
                        }





                        /*forgetPassword.setVisibility(View.VISIBLE);*/

                        /*forgetPassword.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                                customType(LoginActivity.this,"left-to-right");
                            }
                        });*/
                    }

                } else {
                    progressDialog.dismiss();
                    Log.d("", "onResponse: ");
                    Toast.makeText(LoginActivity.this, R.string.loginActivityFailedError, Toast.LENGTH_SHORT).show();

                }

                //StringtxtViewShow.setText(response.body().getJson().getFullNameEng());


                /*Toast.makeText(MainActivity.this, "SuccessFull", Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onFailure(Call<Login> obj, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,  R.string.loginActivityFailureError, Toast.LENGTH_LONG).show();

            }
        });


       /* url = String.format("GbegbeUserLogin/?PhoneNumber=%s&Password=%s",phone,password);


        Call<String> loginCall = api.userLogin(url);

        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {

                    final String isSuccessful = response.body();
                    Log.d("Nothing", "onResponse: " + isSuccessful);
                    progressDialog.dismiss();


                    if (isSuccessful != null) {
                        if (isSuccessful.equals("false")) {
                            Toast.makeText(LoginActivity.this, "আপনার ফোন নম্বর বা পাসওয়ার্ড সঠিক নয়", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(LoginActivity.this, "সফলভাবে লগইন হয়েছে", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, ImageChoiceActivity.class)
                                    .putExtra("MobileNumber",phone));
                            finish();
                        }
                    }

                }else {
                    Toast.makeText(LoginActivity.this,"লগইন ব্যর্থ হয়েছে",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(LoginActivity.this,"ব্যর্থ হয়েছে",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });*/

    }
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.alert_dark_frame)
                .setTitle(getString(R.string.loginExit))
                .setMessage(getString(R.string.loginExittwo))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }

                })
                .setNegativeButton(getString(R.string.loginActivityNegative),new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                } )

                .show();
    }
}
