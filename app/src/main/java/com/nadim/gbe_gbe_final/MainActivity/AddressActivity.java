package com.nadim.gbe_gbe_final.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.nadim.gbe_gbe_final.API.Api;
import com.nadim.gbe_gbe_final.OrderActivity.ViewOrderActivity;
import com.nadim.gbe_gbe_final.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {

    String address1,mapAddress,otherHome,houseAddress,noteAndTime;
    /*Double latitude ;
    Double longitude ;*/
    String latitude ;
    String longitude ;
    int userId,selected;
    String image,phoneNumber,url;
    String imageTime;
    Button placeShow;

    ProgressDialog progressDialog;
    RadioGroup radioGroup;
    RadioButton radioButton,homeAddressChecked,otherAddressChecked;

    LocationManager locationManager;
    private final int FINE_LOCATION_PERMISSION = 9999;
    Double postlatitude = 0.00;
    Double postlongitude = 0.00;
    String locationname = "";

    SharedPreferences sp;
    SharedPreferences.Editor sped;

    private EditText houseEditTextAddress,otherEditTextAddress,noteAndTimeSelect;

    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_address);

       /* editTextAddress = findViewById(R.id.editTextAddress);*/

        houseEditTextAddress= findViewById(R.id.houseEditTextAddress);
        otherEditTextAddress= findViewById(R.id.otherEditTextAddress);
        noteAndTimeSelect = findViewById(R.id.noteAndTimeSelect);
        radioGroup =findViewById(R.id.radioGroup);
        placeShow= findViewById(R.id.currentMap);
        homeAddressChecked=findViewById(R.id.radioHomeAddressChecked);
        otherAddressChecked=findViewById(R.id.radioOtherAddressChecked);

        /*editTextAddress1= findViewById(R.id.editTextAddress1);*/

       /* findViewById(R.id.placeShow).setOnClickListener(this);*/
        findViewById(R.id.userConfirm).setOnClickListener(this);
        findViewById(R.id.currentOthersMap).setOnClickListener(this);

        findViewById(R.id.radioHomeAddressChecked).setOnClickListener(this);
        findViewById(R.id.radioOtherAddressChecked).setOnClickListener(this);

        userId = getIntent().getIntExtra("userId", 0);
        image = getIntent().getStringExtra("image");

        sp = getApplicationContext().getSharedPreferences("GTR", MODE_PRIVATE);
        sped = sp.edit();

        address1 = getIntent().getStringExtra("locationName");
        mapAddress = getIntent().getStringExtra("locationNameMap");
        selected= getIntent().getIntExtra("selected",1);
        phoneNumber = getIntent().getStringExtra("PhoneNumber");

        Bundle bundle = getIntent().getExtras();
        list= bundle.getStringArrayList("ProfileItemList");


        otherEditTextAddress.setText(mapAddress);

        houseAddress = houseEditTextAddress.getText().toString().trim();
        otherHome = otherEditTextAddress.getText().toString().trim();

        if (otherHome.isEmpty()){
            homeAddressChecked.setChecked(true);
            otherAddressChecked.setChecked(false);
            otherEditTextAddress.setFocusableInTouchMode(false);

        }else{
            otherAddressChecked.setChecked(true);
            homeAddressChecked.setChecked(false);
            homeAddressChecked.setFocusableInTouchMode(false);
        }



        progressDialog = new ProgressDialog(AddressActivity.this);
        progressDialog.setMessage(getString(R.string.addressActivityProgress));


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.radioHomeAddressChecked:
                        otherEditTextAddress.setEnabled(false);
                        otherEditTextAddress.setInputType(InputType.TYPE_NULL);
                        otherEditTextAddress.setFocusableInTouchMode(false);

                        houseEditTextAddress.setEnabled(true);
                        houseEditTextAddress.setInputType(InputType.TYPE_CLASS_TEXT);
                        houseEditTextAddress.setFocusableInTouchMode(true);
                        otherEditTextAddress.setText("");
                        break;
                    case R.id.radioOtherAddressChecked:
                        houseEditTextAddress.setEnabled(false);
                        houseEditTextAddress.setInputType(InputType.TYPE_NULL);
                        houseEditTextAddress.setFocusableInTouchMode(false);

                        otherEditTextAddress.setEnabled(true);
                        otherEditTextAddress.setInputType(InputType.TYPE_CLASS_TEXT);
                        otherEditTextAddress.setFocusableInTouchMode(true);
                        houseEditTextAddress.setText("");
                        break;
                }
            }
        });

        placeShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage(getString(R.string.locationTrack));
                progressDialog.show();
                /*if (address1.isEmpty()){*/
                try{
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(AddressActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
                    } else if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    } else if (Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                LatLng latLng = new LatLng(latitude, longitude);
                                Geocoder geocoder = new Geocoder(getApplicationContext());
                                try {
                                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                                    String str = addressList.get(0).getFeatureName();
                                    str += "," + addressList.get(0).getSubLocality() + "," + addressList.get(0).getLocality() + "," + addressList.get(0).getCountryName();


                                    locationname = str;
                                    postlatitude = latitude;
                                    postlongitude = longitude;

                                    address1 =locationname;
                                    houseEditTextAddress.setText(address1);
                                    progressDialog.dismiss();
                                    /*editTextAddress.setText(locationname);*/


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        });
                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                LatLng latLng = new LatLng(latitude, longitude);
                                Geocoder geocoder = new Geocoder(getApplicationContext());
                                try {
                                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                                    String str = addressList.get(0).getFeatureName();
                                    str += "," + addressList.get(0).getSubLocality() + "," + addressList.get(0).getLocality() + "," + addressList.get(0).getCountryName();

                                    locationname = str;
                                    postlatitude = latitude;
                                    postlongitude = longitude;

                                    address1 =locationname;
                                    houseEditTextAddress.setText(address1);
                                    progressDialog.dismiss();



                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Location Access Failed!!", Toast.LENGTH_SHORT).show();

                    }

                }catch (Exception e) {
                    Log.e("Fail 2", e.toString());
                    //At the level Exception Class handle the error in Exception Table
                    // Exception Create That Error  Object and throw it
                    //E.g: FileNotFoundException ,etc
                    e.printStackTrace();
                }

               /* }else {

                }*/
                /*Toast.makeText(AddressActivity.this,"success",Toast.LENGTH_SHORT).show();*/
              /*  houseEditTextAddress.setText(address1);
                progressDialog.dismiss();*/
            }
        });


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userConfirm:
                userConfirm();
                break;
           /* case R.id.placeShow:
                addressShow();
                break;*/
           /* case R.id.homeAddressChecked:
                homeAddressShow();
                break;*/
            case R.id.currentOthersMap:
                otherAddressShow();
                break;
        }
    }
   /* private void homeAddressShow(){
        int radioId= radioGroup.getCheckedRadioButtonId();
        homeAddressChecked = findViewById(radioId);

    }*/
    private void otherAddressShow(){
        startActivity(new Intent(AddressActivity.this, MapActivity.class)
                .putExtra("locationName", address1)
                .putExtra("userId",userId)
                .putExtra("image", image)
                .putExtra("ProfileItemList",list));
        customType(AddressActivity.this,"left-to-right");
    }

 public void radioButton(View v){
     int radioId= radioGroup.getCheckedRadioButtonId();
     radioButton= findViewById(radioId);

 }


    private void userConfirm() {
        noteAndTime= noteAndTimeSelect.getText().toString().trim();
        houseAddress = houseEditTextAddress.getText().toString().trim();
        otherHome = otherEditTextAddress.getText().toString().trim();
        if (homeAddressChecked.isChecked()){
            if (houseAddress.isEmpty()){
                houseEditTextAddress.setError(getString(R.string.addressReqired));
                houseEditTextAddress.requestFocus();
                return;
            }else {
                startActivity(new Intent(AddressActivity.this, FinishActivity.class)
                        .putExtra("userId", userId)
                        .putExtra("latitude", latitude)
                        .putExtra("longitude", longitude)
                        .putExtra("locationName", houseAddress)
                        .putExtra("image", image)
                        .putExtra("note", noteAndTime)
                        .putExtra("ProfileItemList",list)
                        .putExtra("PhoneNumber",phoneNumber));

                customType(AddressActivity.this, "left-to-right");
            }
        }else if(otherAddressChecked.isChecked()) {
            if (otherHome.isEmpty()) {
                otherEditTextAddress.setError(getString(R.string.loginActivityOtpPhoneError));
                otherEditTextAddress.requestFocus();
                return;
            } else {
                startActivity(new Intent(AddressActivity.this, FinishActivity.class)
                        .putExtra("userId", userId)
                        .putExtra("latitude", latitude)
                        .putExtra("longitude", longitude)
                        .putExtra("locationName", otherHome)
                        .putExtra("image", image)
                        .putExtra("note", noteAndTime)
                        .putExtra("ProfileItemList",list)
                        .putExtra("PhoneNumber",phoneNumber));

                customType(AddressActivity.this, "left-to-right");
            }
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
        startActivity(new Intent(AddressActivity.this, UserProfileActivity.class)
                .putExtra("userId", userId)
                .putExtra("PhoneNumber", phoneNumber));
    }

    private void signOut() {
        /*preferenceConfig.writeLoginStatus(false);*/
        sped.putString("PhoneNumber", "");
        sped.putString("Password", "");
        sped.putString("Remember", "");
        sped.commit();

        Toast.makeText(AddressActivity.this, R.string.profileActivityLogout, Toast.LENGTH_LONG).show();
        startActivity(new Intent(AddressActivity.this, LoginActivity.class));
        finish();
    }
    private void orderList(){

        startActivity(new Intent(AddressActivity.this, ViewOrderActivity.class)
                .putExtra("userId", userId));

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }


}
