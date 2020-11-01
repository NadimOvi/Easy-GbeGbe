package com.nadim.gbe_gbe_final.MainActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nadim.gbe_gbe_final.API.Api;
import com.nadim.gbe_gbe_final.MainActivity.ProfileAdapter.ReviewAdapter;
import com.nadim.gbe_gbe_final.OrderActivity.ViewOrderActivity;
import com.nadim.gbe_gbe_final.PostOrderObjectClass.ArrayImageClass;
import com.nadim.gbe_gbe_final.PostOrderObjectClass.OrderProductClass;
import com.nadim.gbe_gbe_final.R;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class FinishActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageShowReview;
    TextView textAddressReview, textNoteReview;

    int userId,selected;
    String image, addressShow,latitude,longitude,noteAndTime,phoneNumber;
    byte[] viewImage;

    ArrayList<String> arrayList;

    RecyclerView recyclerViewFinish;

   OrderProductClass orderProductClass;

   ArrayImageClass arrayImageClass;

    SharedPreferences sp;
    SharedPreferences.Editor sped;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        loadLocale();
        /*imageShowReview = findViewById(R.id.imageShowReview);*/
        textAddressReview = findViewById(R.id.textAddressReview);
        textNoteReview = findViewById(R.id.textNoteReview);

        findViewById(R.id.reviewFinishButton).setOnClickListener(this);
        findViewById(R.id.reviewCancelButton).setOnClickListener(this);


        recyclerViewFinish= findViewById(R.id.recyclerViewIdFinish);
        recyclerViewFinish.setLayoutManager(new LinearLayoutManager(this));

        selected= getIntent().getIntExtra("selected",1);
        phoneNumber = getIntent().getStringExtra("PhoneNumber");
        userId = getIntent().getIntExtra("userId", 0);
        image = getIntent().getStringExtra("image");
        addressShow = getIntent().getStringExtra("locationName");
        noteAndTime = getIntent().getStringExtra("note");

        sp = getApplicationContext().getSharedPreferences("GTR", MODE_PRIVATE);
        sped = sp.edit();

        progressDialog = new ProgressDialog(FinishActivity.this);
        progressDialog.setMessage(getString(R.string.addressActivityProgress));

        Bundle bundle = getIntent().getExtras();
        arrayList= bundle.getStringArrayList("ProfileItemList");

        recyclerViewFinish= findViewById(R.id.recyclerViewIdFinish);

        /*recyclerViewFinish.setLayoutManager(new LinearLayoutManager(this));*/
        recyclerViewFinish.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ReviewAdapter reviewAdapter= new ReviewAdapter(arrayList, this);
        recyclerViewFinish.setAdapter(reviewAdapter);

        /*
        ReviewAdapter secondAdapter= new ReviewAdapter(arrayList, this);

        recyclerViewFinish.setAdapter(secondAdapter);*/


        /*viewImage = Base64.decode(image, Base64.DEFAULT);
        Bitmap decoded = BitmapFactory.decodeByteArray(viewImage, 0, viewImage.length);

        imageShowReview.setImageBitmap(decoded);*/
        textAddressReview.setText(addressShow);
        textNoteReview.setText(noteAndTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reviewFinishButton:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.alert_dark_frame)
                        .setTitle(R.string.AlertFinishActivity)
                        .setMessage(R.string.alertMessageFinishActivity)
                        .setPositiveButton(getString(R.string.messageActivitySecond), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userConfirm();
                            }

                        })
                        .setNegativeButton(getString(R.string.messageNegative),new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }

                        } ).show();
                break;
            case R.id.reviewCancelButton:
                    orderCancel();
                break;

        }
    }

    private void orderCancel() {
        startActivity(new Intent(FinishActivity.this, ImageChoiceActivity.class)
                .putExtra("userId",userId));
        customType(FinishActivity.this,"left-to-right");
        finish();
    }

    private void userConfirm() {
        progressDialog.show();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.2.165.189:96/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        Api api = retrofit.create(Api.class);

        ArrayList<ArrayImageClass> listOfImage=new ArrayList<ArrayImageClass>();


      for(int i=0;i<arrayList.size();i++){
          ArrayImageClass arrayImageClass=new ArrayImageClass(arrayList.get(i));
         /* arrayImageClass.setImage(arrayList.get(i));*/
          listOfImage.add(arrayImageClass);
      }

        /*arrayImageClass = new ArrayImageClass(listOfImage);*/

        orderProductClass = new OrderProductClass(userId,latitude,longitude,addressShow,noteAndTime,listOfImage);

        Call<String> call = api.productPostOrder(orderProductClass);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String isSuccessful = response.body();
                    if (isSuccessful != null) {
                        if (isSuccessful.equals("false")) {
                            Toast.makeText(FinishActivity.this,"Error Message for order", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(FinishActivity.this, R.string.addressActivitySucessError, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(FinishActivity.this, MessageActivity.class)
                                    .putExtra("trxId", isSuccessful)
                                    .putExtra("userId", userId)
                                    .putExtra("PhoneNumber",phoneNumber));
                            customType(FinishActivity.this, "left-to-right");
                            finish();
                        }
                    }

                } else {
                    Log.d("", "onResponse: ");
                    Toast.makeText(FinishActivity.this,"response error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(FinishActivity.this, R.string.addressActivityFailed, Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.orderList:
                orderList();
                break ;

            case R.id.signOut:
                signOut();
                break;
            case R.id.myProfile:
                myProfile();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void myProfile() {
        startActivity(new Intent(FinishActivity.this, UserProfileActivity.class)
                .putExtra("userId", userId)
                .putExtra("PhoneNumber", phoneNumber));
    }

    private void signOut() {
        /*preferenceConfig.writeLoginStatus(false);*/
        sped.putString("PhoneNumber", "");
        sped.putString("Password", "");
        sped.putString("Remember", "");
        sped.commit();

        Toast.makeText(FinishActivity.this, R.string.profileActivityLogout, Toast.LENGTH_LONG).show();
        startActivity(new Intent(FinishActivity.this, LoginActivity.class));
        finish();
    }
    private void orderList(){

        startActivity(new Intent(FinishActivity.this, ViewOrderActivity.class)
                .putExtra("userId", userId));

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }

}
