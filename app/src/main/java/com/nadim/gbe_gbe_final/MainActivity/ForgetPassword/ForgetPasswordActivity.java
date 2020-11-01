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
import android.widget.EditText;
import android.widget.Toast;

import com.nadim.gbe_gbe_final.API.Api;
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

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText editText;
    String phoneNumber;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_forget_password);

        editText= findViewById(R.id.editTextPhone);
        progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
        progressDialog.setMessage(getString(R.string.waitingmsgforget));
        progressDialog.setCancelable(false);

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number= editText.getText().toString();
                editText.setText(number);

                if(number.isEmpty()|| number.length()<10){
                    editText.setError(getString(R.string.forgetActivityemplyerror));
                    editText.requestFocus();
                    return;
                }

                phoneNumber="+88"+number;

                checkUser();
            }
        });

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
            jsonObjectName.put("phoneNumber", phoneNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog.show();

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
                        if (isSuccessful.equals("false")) {
                            Toast.makeText(ForgetPasswordActivity.this, R.string.forgetActivitytoast, Toast.LENGTH_LONG).show();

                        } else {
                            startActivity(new Intent(ForgetPasswordActivity.this, ForgetVerifyOtpActivity.class)
                                    .putExtra("phoneNumber",phoneNumber));
                            customType(ForgetPasswordActivity.this,"left-to-right");
                            finish();
                        }
                    }

                } else {
                    Log.d("", "onResponse: ");
                    Toast.makeText(ForgetPasswordActivity.this, R.string.forgetActivityelseerror, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ForgetPasswordActivity.this, R.string.forgetActivityFailurerror, Toast.LENGTH_LONG).show();
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

    /*Intent intent= new Intent(ForgetPasswordActivity.this,ForgetVerifyOtpActivity.class);
                intent.putExtra("phoneNumber",phoneNumber);
    startActivity(intent);
    finish();*/

}
