package com.nadim.gbe_gbe_final.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nadim.gbe_gbe_final.API.Api;
import com.nadim.gbe_gbe_final.Model.MyProfileModelClass;
import com.nadim.gbe_gbe_final.R;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class UserProfileActivity extends AppCompatActivity {
   private int userId,selected;
    private String password,image,fullname,address,myBirthDate, email;
    private int age;
    private String phoneNumber;
    String image1;
    private CircleImageView circleImageView;
    private TextView myProfileName;
    private TextView myPhoneNumber;
    private  TextView myPassword;
    private  TextView myAge;
    private TextView myAddress;
    private TextView birthDate;
    private TextView myEmail;
    private Button myEditProfileButton;
    private  ProgressDialog progressDialog;
    private  byte[] orderImage3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userId = getIntent().getIntExtra("userId",0);
        phoneNumber = getIntent().getStringExtra("PhoneNumber");

        selected= getIntent().getIntExtra("selected",1);

        progressDialog = new ProgressDialog(UserProfileActivity.this);
        progressDialog.setMessage(getString(R.string.loginActivityProgress));
        progressDialog.setCancelable(false);

        circleImageView = findViewById(R.id.myImage);

        myProfileName = findViewById(R.id.myProfileName);
        myPhoneNumber = findViewById(R.id.myPhoneNumber);
        birthDate = findViewById(R.id.myBirthDate);
        myAge = findViewById(R.id.myAges);
        myEmail = findViewById(R.id.myEmail);
        myAddress = findViewById(R.id.myAddress);

        myEditProfileButton = findViewById(R.id.editButton);

        myEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1 =image;
                startActivity(new Intent(UserProfileActivity.this, UserEditActivity.class)
                        .putExtra("userId", userId)
                        .putExtra("PhoneNumber", phoneNumber)
                        /*.putExtra("PhoneNumber", circleImageView)*/
                        .putExtra("fullName", fullname)
                        .putExtra("password", password)
                        /*.putExtra("image", image1)*/
                        .putExtra("age", age)
                        .putExtra("birthDate", myBirthDate)
                        .putExtra("address", address)
                        .putExtra("email", email));
                customType(UserProfileActivity.this,"left-to-right");
                finish();
            }
        });

        progressDialog.show();

         fullname = myProfileName.getText().toString().trim();
         /*password = myPassword.getText().toString().trim();*/
      /*   age = myAge.getText().toString().trim();*/
         address = myAddress.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.2.165.189:96/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api ourRetrofit= retrofit.create(Api.class);

        Call<MyProfileModelClass> call= ourRetrofit.getUserDataSet(userId);
        call.enqueue(new Callback<MyProfileModelClass>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<MyProfileModelClass> call, Response<MyProfileModelClass> response) {
                progressDialog.dismiss();

                MyProfileModelClass myProfileModelClass= response.body();
                if (!response.isSuccessful()){
                    Toast.makeText(UserProfileActivity.this,"Response Failed",Toast.LENGTH_SHORT).show();

                }else{
                    fullname= myProfileModelClass.getFullName();
                    myProfileName.setText(fullname);

                    phoneNumber =myProfileModelClass.getPhoneNumber();
                    myPhoneNumber.setText(phoneNumber);

                    password = myProfileModelClass.getPassword();

                    age = myProfileModelClass.getAge();
                    myAge.setText(Integer.toString(age));

                    myBirthDate = myProfileModelClass.getBirthDate();
                    birthDate.setText(myBirthDate);

                    address = myProfileModelClass.getAddress();
                    myAddress.setText(address);

                    email = myProfileModelClass.getEmail();
                    myEmail.setText(email);

                    image = myProfileModelClass.getImage();
                    decodeImage();

                }

            }

            @Override
            public void onFailure(Call<MyProfileModelClass> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void decodeImage() {
        try {
            orderImage3 = Base64.decode(image,Base64.DEFAULT);
            Bitmap decoded= BitmapFactory.decodeByteArray(orderImage3,0,orderImage3.length);
            circleImageView.setImageBitmap(decoded);

        } catch(Exception e) {
            e.getMessage();
        }

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