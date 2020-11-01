package com.nadim.gbe_gbe_final.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nadim.gbe_gbe_final.OrderActivity.ViewOrderActivity;
import com.nadim.gbe_gbe_final.R;
import com.nadim.gbe_gbe_final.SharePreference.SharePreferenceConfig;
import com.onesignal.OneSignal;

import java.util.HashMap;
import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;

public class ProfileActivity extends AppCompatActivity {

    int userId;
    private String phoneNumber,password;
    SharedPreferences sp;
    SharedPreferences.Editor sped;
    private SharePreferenceConfig preferenceConfig;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    Button imageChoiceButton,popularListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_profile);
        init();

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        HashMap<String, Object> defaultsRate = new HashMap<>();
        defaultsRate.put("Easy_GbeGbe_version_code", String.valueOf(getVersionCode()));

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10) // change to 3600 on published app
                .build();

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(defaultsRate);

        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    final String new_GbeGbe_version_code = mFirebaseRemoteConfig.getString("Easy_GbeGbe_version_code");

                    if(Integer.parseInt(new_GbeGbe_version_code) > getVersionCode())
                        showTheDialog("com.gbegbe.myapplication", new_GbeGbe_version_code );
                }
                else Log.e("MYLOG", "mFirebaseRemoteConfig.fetchAndActivate() NOT Successful");

            }
        });
    }
    private void showTheDialog(final String appPackageName, String versionFromRemoteConfig){
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setTitle("আপডেট!")
                .setMessage("নতুন আপডেট ভার্সনটি ইনস্টল করুন ।")
                .setPositiveButton("আপডেট করুন", null)
                .setNegativeButton("না", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .show();

        dialog.setCancelable(false);

        Button positiveButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
                }
                catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }
    private PackageInfo pInfo;
    public int getVersionCode() {
        pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("MYLOG", "NameNotFoundException: "+e.getMessage());
        }
        return pInfo.versionCode;
    }

    private void init(){
        userId = getIntent().getIntExtra("userId",0);
        phoneNumber = getIntent().getStringExtra("PhoneNumber");
        password = getIntent().getStringExtra("Password");

        Toast.makeText(this, "PhoneNumber: "+phoneNumber+"\nPassword: "+password, Toast.LENGTH_SHORT).show();

        sp = getApplicationContext().getSharedPreferences("GTR", MODE_PRIVATE);
        sped = sp.edit();
        preferenceConfig= new SharePreferenceConfig(getApplicationContext());

        imageChoiceButton = findViewById(R.id.imageChoiceButton);
        popularListButton = findViewById(R.id.popularListButton);
        imageChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ImageChoiceActivity.class)
                        .putExtra("userId", userId)
                        .putExtra("PhoneNumber", phoneNumber));
                customType(ProfileActivity.this,"left-to-right");
            }
        });
        popularListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, PopularListActivity.class)
                        .putExtra("userId", userId)
                        .putExtra("PhoneNumber", phoneNumber)
                        .putExtra("Password", password));
                customType(ProfileActivity.this,"left-to-right");

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
        startActivity(new Intent(ProfileActivity.this, UserProfileActivity.class)
                .putExtra("userId", userId)
                .putExtra("PhoneNumber", phoneNumber));
    }

    private void signOut() {
        /*preferenceConfig.writeLoginStatus(false);*/
        sped.putString("PhoneNumber", "");
        sped.putString("Password", "");
        sped.putString("Remember", "");
        sped.commit();

        Toast.makeText(ProfileActivity.this, R.string.profileActivityLogout, Toast.LENGTH_LONG).show();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }
    private void orderList(){

        startActivity(new Intent(ProfileActivity.this, ViewOrderActivity.class)
                .putExtra("userId", userId));

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.alert_dark_frame)
                .setTitle(getString(R.string.profileActivityDialog))
                .setMessage(getString(R.string.profileActivityExit))
                .setPositiveButton(getString(R.string.profileActivityYes), new DialogInterface.OnClickListener()
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
                .setNegativeButton(getString(R.string.profileActivityNo),new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                } )

                .show();
    }

}