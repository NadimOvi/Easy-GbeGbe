package com.nadim.gbe_gbe_final.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nadim.gbe_gbe_final.MainActivity.ProfileAdapter.ProfileAdapter;
import com.nadim.gbe_gbe_final.OrderActivity.ViewOrderActivity;
import com.nadim.gbe_gbe_final.R;
import com.nadim.gbe_gbe_final.SharePreference.SharePreferenceConfig;
import com.onesignal.OneSignal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;

public class ImageChoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private SharePreferenceConfig preferenceConfig;
    private static final int CAPTURE_REQUEST_CODE = 0;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private ImageView imageView;
    private Button uploadImage;
    private EditText editTextPhone,editTextName;

    byte[] byteArray;
    String image;
    private String currentPhotoPath;
    private String imagePath;
    TextView addTexts;

    SharedPreferences sp;
    SharedPreferences.Editor sped;

    String imageTime;
    int userId;

    //location

    LocationManager locationManager;
    private final int FINE_LOCATION_PERMISSION = 9999;
    Double postlatitude = 0.00;
    Double postlongitude = 0.00;
    String locationname = "";
    SharedPreferences editor;

    Bitmap bitmap;

    private String phoneNumber;
    private ArrayList<String> list;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_image_choice);
        addTexts=findViewById(R.id.addText);
        uploadImage = findViewById(R.id.uploadPicture);
        imageView = findViewById(R.id.Image_view);

        userId = getIntent().getIntExtra("userId",0);
        phoneNumber = getIntent().getStringExtra("PhoneNumber");



        sp = getApplicationContext().getSharedPreferences("GTR", MODE_PRIVATE);
        sped = sp.edit();

        findViewById(R.id.usersConfirm).setOnClickListener(this);
        preferenceConfig= new SharePreferenceConfig(getApplicationContext());

        progressDialog = new ProgressDialog(ImageChoiceActivity.this);
        progressDialog.setMessage(getString(R.string.profileActivityProgress));
        progressDialog.setCancelable(false);

        recyclerView= findViewById(R.id.recyclerViewId);
        list=new ArrayList<String>();


        ProfileAdapter myImageAdapter= new ProfileAdapter(list/*,CityNames*/, this);
        /*recyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(myImageAdapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CheckPermission()) {
                        String fileName="photo";
                        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File imageFile= File.createTempFile(fileName,".jpg",storageDirectory);
                        currentPhotoPath = imageFile.getAbsolutePath();

                       Uri imageUri= FileProvider.getUriForFile(ImageChoiceActivity.this,
                                "com.nadim.gbe_gbe_final.fileprovider",imageFile);

                        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,0);

                        if (imageView.isPressed()) {
                            uploadImage.setBackgroundResource(R.drawable.photocamera);
                            addTexts.setText(R.string.addText);
                        }
                        else {
                            /* btnTest.setImageResource(R.drawable.yourImage2);*/
                        }


                        //if using upload image from gallary folder

                        /* Intent capture = new Intent();
                        capture.setType("image/*");
                        capture.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(capture, 0);*/


                    //direct ope open camera
                        /*Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        capture.putExtra("android.intent.extras.CAMERA_FACING", 0);
                        startActivityForResult(capture, 0);*/
                       /* dispatchImageCapture();*/
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (CheckPermission()) {

                        String fileName="photo";
                        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File imageFile= File.createTempFile(fileName,".jpg",storageDirectory);
                        currentPhotoPath = imageFile.getAbsolutePath();

                        Uri imageUri= FileProvider.getUriForFile(ImageChoiceActivity.this,
                                "com.nadim.gbe_gbe_final.fileprovider",imageFile);

                        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,0);
                        uploadImage.setSelected(!uploadImage.isPressed());

                        if (uploadImage.isPressed()) {
                            uploadImage.setBackgroundResource(R.drawable.photocamera);
                            addTexts.setText(R.string.addText);
                        }
                        else {
                            /* btnTest.setImageResource(R.drawable.yourImage2);*/
                        }


                        /*Intent capture = new Intent();
                        capture.setType("image/*");
                        capture.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(capture, 0);*/


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.usersConfirm:
                userConfirm();
                break;
        }
    }
    private void userConfirm() {
        if (bitmap != null /*&& !bitmap.isEmpty()*/){

            {
            startActivity(new Intent(ImageChoiceActivity.this, AddressActivity.class)
                    .putExtra("userId", userId)
                    .putExtra("latitude", postlatitude)
                    .putExtra("longitude", postlongitude)
                    .putExtra("locationName", locationname)
                    .putExtra("image", image)
                    .putExtra("ProfileItemList",list)
                    .putExtra("PhoneNumber",phoneNumber)
            );

                customType(ImageChoiceActivity.this,"left-to-right");

            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.profileActivityToast,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*bitmap = (Bitmap) data.getExtras().get("data");*/
        if (resultCode==RESULT_OK){
            bitmap= BitmapFactory.decodeFile(currentPhotoPath);

            Bitmap bOutput;
            float degrees = 90;//rotation degree
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            final float densityMultiplier = getResources().getDisplayMetrics().density;
            int h = (int) (250 * densityMultiplier);
            int w = (int) (h * bitmap.getWidth() / ((double) bitmap.getHeight()));

            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            bOutput = Bitmap.createBitmap(bitmap,0,0,w,h, matrix, true);

            imageView.setImageBitmap(bOutput);
            ImageUpload(bOutput);


/*

            Uri ImageUri = data.getData();

               */
            /* BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize=1;
                bOutput = Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);*//*


            */
            /* bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageUri);*//*


            Bitmap bOutput;
            float degrees = 90;//rotation degree
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            final float densityMultiplier = getResources().getDisplayMetrics().density;

            int h = (int) (999 * densityMultiplier);
            int w = (int) (h * bitmap.getWidth() / ((double) bitmap.getHeight()));

            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            */
            /*bOutput = Bitmap.createBitmap(bitmap,0,0,w,h, matrix, true);*//*


*/



        }

    }
    private void dispatchImageCapture(){
        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (capture.resolveActivity(getPackageManager()) != null) {
            File imageFile= null;
            try {
                imageFile = createImageFile();
            }catch (IOException exception){
                Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT).show();

            }
            if (imageFile!= null){
                Uri imagUri = FileProvider.getUriForFile(
                        this,
                        "com.nadim.gbe_gbe_final.fileprovider",
                        imageFile
                );
                capture.putExtra(MediaStore.EXTRA_OUTPUT,imagUri);
                startActivityForResult(capture, 0);
            }

        }

    }

    private File createImageFile() throws IOException{
        String fileName="IMAGE"+
                new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",Locale.getDefault()).format(new Date());
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile= File.createTempFile(
                fileName,".Jpg",directory
        );
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }



    public boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(ImageChoiceActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ImageChoiceActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ImageChoiceActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ImageChoiceActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(ImageChoiceActivity.this,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(ImageChoiceActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(ImageChoiceActivity.this)
                        .setTitle(getString(R.string.profileActivityPermission))
                        .setMessage(getString(R.string.profileActivityPremissionsecond))
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ImageChoiceActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                                startActivity(new Intent(ImageChoiceActivity
                                        .this, ImageChoiceActivity.class));
                                ImageChoiceActivity.this.overridePendingTransition(0, 0);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(ImageChoiceActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {

            return true;

        }
    }


    private void ImageUpload(Bitmap bOutput) {

        byte[] data = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bOutput.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        data = baos.toByteArray();
        image = Base64.encodeToString(data, Base64.DEFAULT);
        list.add(image);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

        imageTime = dateformat.format(Calendar.getInstance().getTime());
        /*imageTime = Date.valueOf(String.valueOf(Calendar.getInstance().getTimeInMillis()));*/
        progressDialog.dismiss();
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
        startActivity(new Intent(ImageChoiceActivity.this, UserProfileActivity.class)
                .putExtra("userId", userId)
                .putExtra("PhoneNumber", phoneNumber));
    }

    private void signOut() {
        /*preferenceConfig.writeLoginStatus(false);*/
        sped.putString("PhoneNumber", "");
        sped.putString("Password", "");
        sped.putString("Remember", "");
        sped.commit();

        Toast.makeText(ImageChoiceActivity.this, R.string.profileActivityLogout, Toast.LENGTH_LONG).show();
        startActivity(new Intent(ImageChoiceActivity.this, LoginActivity.class));
        finish();
    }
    private void orderList(){

        startActivity(new Intent(ImageChoiceActivity.this, ViewOrderActivity.class)
                .putExtra("userId", userId));

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }
}
