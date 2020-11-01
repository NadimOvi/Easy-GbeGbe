package com.nadim.gbe_gbe_final.OrderActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

import com.nadim.gbe_gbe_final.API.Api;
import com.nadim.gbe_gbe_final.MainActivity.AddressActivity;
import com.nadim.gbe_gbe_final.MainActivity.LoginActivity;
import com.nadim.gbe_gbe_final.MainActivity.MessageActivity;
import com.nadim.gbe_gbe_final.OrderActivity.Adeptar.DataAdapter;
import com.nadim.gbe_gbe_final.OrderActivity.Adeptar.OurDataSet;
import com.nadim.gbe_gbe_final.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class ViewOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    int userId;
    byte[] image1;
    String image;

    TextView showView;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_view_order);

        showView= findViewById(R.id.showView);
        progressDialog = new ProgressDialog(ViewOrderActivity.this);
        progressDialog.setMessage(getString(R.string.verifyOrderProgress));
        progressDialog.setCancelable(false);

        userId = getIntent().getIntExtra("userId",0);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.2.165.189:96/api/OrderProduct/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api ourRetrofit= retrofit.create(Api.class);

        Call<List<OurDataSet>> listCall= ourRetrofit.getDataSet(userId);
        listCall.enqueue(new Callback<List<OurDataSet>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<OurDataSet>> call, Response<List<OurDataSet>> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()){
                    showView.setText("code" +response.code());
                    return;
                }

                List<OurDataSet> ourDataSets = response.body();

                for (OurDataSet ourDataSet:ourDataSets){

                    showIt(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<OurDataSet>> call, Throwable t) {
                Toast.makeText(ViewOrderActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void showIt(List<OurDataSet> response) {


        DataAdapter dataAdapter = new DataAdapter(response,getApplicationContext());

        recyclerView.setAdapter(dataAdapter);
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
