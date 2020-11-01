package com.nadim.gbe_gbe_final.MainActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nadim.gbe_gbe_final.OrderActivity.ViewOrderActivity;
import com.nadim.gbe_gbe_final.R;

import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;

public class MessageActivity extends AppCompatActivity {

    private TextView details;
    String trxId,phoneNumber;
    int userId;

    SharedPreferences sp;
    SharedPreferences.Editor sped;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_message);
        if (getIntent().getExtras() != null) {
            trxId = getIntent().getStringExtra("trxId");
            userId = getIntent().getIntExtra("userId",0);
        }
        details= findViewById(R.id.details);
        phoneNumber = getIntent().getStringExtra("PhoneNumber");

        sp = getApplicationContext().getSharedPreferences("GTR", MODE_PRIVATE);
        sped = sp.edit();
       /* SimpleDateFormat dateformat = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm");
        String currentTime = dateformat.format(Calendar.getInstance().getTime());*/




        String text=trxId;



        details.setText(text);




    }

    public void btnHome(View view) {
        startActivity(new Intent(MessageActivity.this, ImageChoiceActivity.class)
                        .putExtra("userId",userId));
        customType(MessageActivity.this,"left-to-right");
        finish();
    }

    public void btnExit(View view) {

        startActivity(new Intent(MessageActivity.this, ViewOrderActivity.class)
                .putExtra("userId",userId));
        customType(MessageActivity.this,"left-to-right");


      /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("আপনি প্রস্থান করতে চান?")
                .setCancelable(false)
                .setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("না", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();*/
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.alert_dark_frame)
                .setTitle(getString(R.string.messageActivityset))
                .setMessage(getString(R.string.messageActivityExit))
                .setPositiveButton(getString(R.string.messageActivitySecond), new DialogInterface.OnClickListener()
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
                .setNegativeButton(getString(R.string.messageNegative),new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                } )

                .show();
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
        startActivity(new Intent(MessageActivity.this, UserProfileActivity.class)
                .putExtra("userId", userId)
                .putExtra("PhoneNumber", phoneNumber));
    }

    private void signOut() {
        /*preferenceConfig.writeLoginStatus(false);*/
        sped.putString("PhoneNumber", "");
        sped.putString("Password", "");
        sped.putString("Remember", "");
        sped.commit();

        Toast.makeText(MessageActivity.this, R.string.profileActivityLogout, Toast.LENGTH_LONG).show();
        startActivity(new Intent(MessageActivity.this, LoginActivity.class));
        finish();
    }
    private void orderList(){

        startActivity(new Intent(MessageActivity.this, ViewOrderActivity.class)
                .putExtra("userId", userId));

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }

}
