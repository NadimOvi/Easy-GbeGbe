package com.nadim.gbe_gbe_final.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nadim.gbe_gbe_final.API.Api;
import com.nadim.gbe_gbe_final.Model.MyProfileModelClass;
import com.nadim.gbe_gbe_final.R;

import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class UserEditActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String currentPhotoPath;
    Bitmap bitmap,bOutput,decoded;

    int userId,selected;
    String phoneNumber,fullName,password,address,image,email;
    String newPhoneNumber,newFullName,newPassword,newAddress,newImage;
    ImageView circleImageView;
    EditText myEditProfileName;
    EditText myEditPhoneEmail;
    EditText myEditPassword;
    EditText myEditAge;
    EditText myEditAddress;

    TextView oldName;
    TextView oldEmail;
    TextView myAge;
    TextView old_birthDate;
    TextView oldAddress;
    Button submitButton;
    Button addBirthDate;
    TextView birthDateTextView;
    DatePickerDialog.OnDateSetListener dateSetListener;
    String date,btBirth,todayDate;
    int age,editAge;
    byte[] orderImage;
    ProgressDialog progressDialog;

    private static final int CAPTURE_REQUEST_CODE = 0;
    private static final int SELECT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        loadLocale();
        progressDialog = new ProgressDialog(UserEditActivity.this);
        progressDialog.setMessage(getString(R.string.loginActivityProgress));
        progressDialog.setCancelable(false);

        userId = getIntent().getIntExtra("userId",0);
        selected= getIntent().getIntExtra("selected",1);
        phoneNumber = getIntent().getStringExtra("PhoneNumber");
        fullName = getIntent().getStringExtra("fullName");
        email = getIntent().getStringExtra("email");

        password = getIntent().getStringExtra("password");
        address = getIntent().getStringExtra("address");
        date = getIntent().getStringExtra("birthDate");
       /* image = getIntent().getStringExtra("image");*/
        age = getIntent().getIntExtra("age",0);

        oldName= findViewById(R.id.oldName);
        oldEmail =  findViewById(R.id.oldEmail);
        old_birthDate= findViewById(R.id.old_birthDate);
        addBirthDate = findViewById(R.id.addBirthDate);
        birthDateTextView = findViewById(R.id.birthDateTextView);
        oldAddress = findViewById(R.id.oldAddress);


        oldName.setText(fullName);
        oldEmail.setText(email);
        oldAddress.setText(address);

        circleImageView = findViewById(R.id.myEditImage);
        myEditProfileName = findViewById(R.id.myEditProfileName);
        myEditProfileName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        myEditPhoneEmail = findViewById(R.id.myEditProfileEmail);

        submitButton= findViewById(R.id.submitButton);
        myEditAddress = findViewById(R.id.myEditAddress);
        myEditAge= findViewById(R.id.myEditAge);
        myAge  = findViewById(R.id.addAge);

        myEditProfileName.setText(fullName);
        myEditPhoneEmail.setText(email);
        myEditAddress.setText(address);
        old_birthDate.setText(date);

        birthDateTextView.setText(date);

        /*myAge.setText(age);*/
        myAge.setText(Integer.toString(age));
        myEditAge.setText(Integer.toString(age));

        getImage();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProfile();
            }
        });
        addBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal= Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog= new DatePickerDialog(
                        UserEditActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                date= day +"-"+ month +"-"+ year;
                /*date= day +"-"+ month +"-"+ year;*/
                birthDateTextView.setText(date);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                todayDate = dateformat.format(calendar.getInstance().getTime());

                String sDate= birthDateTextView.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date date1= simpleDateFormat.parse(date);
                    Date date2= simpleDateFormat.parse(todayDate);

                    long startDate =  date1.getTime();
                    long endDate = date2.getTime();

                    if (startDate<=endDate){
                        Period period = new Period(startDate,endDate, PeriodType.yearMonthDay());
                        editAge = period.getYears();
                        int months = period.getMonths();
                        int days = period.getDays();

                        myEditAge.setText(Integer.toString(editAge));

                    }else{
                        Toast.makeText(getApplicationContext(),"Birthdate Invalid",Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        };

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CheckPermission()) {

                        final CharSequence[] options = { getString(R.string.takePhoto), getString(R.string.chooseGallery),getString(R.string.cancelPhotoSelect) };
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserEditActivity.this);
                        builder.setTitle(R.string.addphotos);
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (options[item].equals(getString(R.string.takephotos)))
                                {
                                    String fileName="photo";
                                    File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                    File imageFile= null;
                                    try {
                                        imageFile = File.createTempFile(fileName,".jpg",storageDirectory);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    currentPhotoPath = imageFile.getAbsolutePath();

                                    Uri imageUri= FileProvider.getUriForFile(UserEditActivity.this,
                                            "com.nadim.gbe_gbe_final.fileprovider",imageFile);

                                    Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                    startActivityForResult(intent,CAPTURE_REQUEST_CODE);

                                    /*Intent capture  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(capture , CAPTURE_REQUEST_CODE);*/

                                }
                                else if (options[item].equals(getString(R.string.chooseGallery)))
                                {
                                    Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(select, SELECT_REQUEST_CODE);
                                }
                                else if (options[item].equals(getString(R.string.cancelPhotoSelect))) {
                                    dialog.dismiss();
                                }
                            }
                        });
                        builder.show();



                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void getImage() {
        progressDialog.show();
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
                    Toast.makeText(UserEditActivity.this,"Response Failed",Toast.LENGTH_SHORT).show();

                }else{

                    image = myProfileModelClass.getImage();
                    decodeImage();

                }

            }

            @Override
            public void onFailure(Call<MyProfileModelClass> call, Throwable t) {
                Toast.makeText(UserEditActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void decodeImage() {

        try {
            orderImage = Base64.decode(image,Base64.DEFAULT);
            Bitmap decoded= BitmapFactory.decodeByteArray(orderImage,0,orderImage.length);
            circleImageView.setImageBitmap(decoded);

        } catch(Exception e) {
            e.getMessage();
        }

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        bitmap = (Bitmap) data.getExtras().get("data");
        if (resultCode==RESULT_OK){
            bitmap= BitmapFactory.decodeFile(currentPhotoPath);

            Bitmap bOutput;
            float degrees = -0;//rotation degree
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            final float densityMultiplier = getResources().getDisplayMetrics().density;
            int h = (int) (200 * densityMultiplier);
            int w = (int) (h * bitmap.getWidth() / ((double) bitmap.getHeight()));

            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            bOutput = Bitmap.createBitmap(bitmap,0,0,w,h, matrix, true);


            circleImageView.setImageBitmap(bOutput);
            ImageUpload(bOutput);
        }

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case CAPTURE_REQUEST_CODE:
            {
                if (resultCode==RESULT_OK){
                    bOutput= BitmapFactory.decodeFile(currentPhotoPath);

                    float degrees = 0;//rotation degree
                    Matrix matrix = new Matrix();
                    matrix.setRotate(degrees);
                    final float densityMultiplier = getResources().getDisplayMetrics().density;
                    int h = (int) (200 * densityMultiplier);
                    int w = (int) (h * bOutput.getWidth() / ((double) bOutput.getHeight()));

                    bitmap = Bitmap.createScaledBitmap(bOutput, w, h, true);
                    /*bitmap = Bitmap.createBitmap(bOutput,0,0,w,h,matrix, true);*/


                    circleImageView.setImageBitmap(bitmap);
                    ImageUpload(bitmap);
                }



               /* if(resultCode == RESULT_OK){

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    circleImageView.setImageBitmap(bitmap);
                    circleImageView.setRotation(0);
                    ImageUpload(bitmap);

                }*/

            }
            break;

            case SELECT_REQUEST_CODE:
            {
                if(resultCode == RESULT_OK){

                    try {
                        Uri ImageUri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageUri);
                        circleImageView.setImageBitmap(bitmap);
                        circleImageView.setRotation(0);

                        ImageUpload(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }

}

    private void ImageUpload(Bitmap bitmap) {
        byte[] data = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        data = baos.toByteArray();
        image = Base64.encodeToString(data, Base64.DEFAULT);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

        /*imageTime = Date.valueOf(String.valueOf(Calendar.getInstance().getTimeInMillis()));*/

    }


    public boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(UserEditActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(UserEditActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(UserEditActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserEditActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(UserEditActivity.this,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(UserEditActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(UserEditActivity.this)
                        .setTitle(getString(R.string.profileActivityPermission))
                        .setMessage(getString(R.string.profileActivityPremissionsecond))
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(UserEditActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                                startActivity(new Intent(UserEditActivity
                                        .this, UserEditActivity.class));
                                UserEditActivity.this.overridePendingTransition(0, 0);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(UserEditActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {

            return true;

        }
    }

    public void submitProfile(){
        progressDialog.show();
        newFullName = myEditProfileName.getText().toString().trim();
        email= myEditPhoneEmail.getText().toString().trim();
        newAddress = myEditAddress.getText().toString().trim();
        editAge = Integer.parseInt(myEditAge.getText().toString().trim());

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
            jsonObjectName.put("userId", userId);
            jsonObjectName.put("phoneNumber", phoneNumber);
            jsonObjectName.put("password", password);
            jsonObjectName.put("fullName", newFullName);
            jsonObjectName.put("birthDate", date);
            jsonObjectName.put("age", editAge);
            jsonObjectName.put("address", newAddress);
            jsonObjectName.put("image", image);
            jsonObjectName.put("email", email);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        jsonObjectFinal = (JsonObject) jsonParser.parse(jsonObjectName.toString());

        Call<String> call = api.postProfileEdit(jsonObjectFinal);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String isSuccessful=response.body();
                    if (isSuccessful.equals("true")){
                        Toast.makeText(UserEditActivity.this, R.string.successfuMsg, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserEditActivity.this, UserProfileActivity.class)
                                .putExtra("userId", userId));
                        customType(UserEditActivity.this,"left-to-right");
                        finish();
                    } else {
                        Toast.makeText(UserEditActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d("", "onResponse: ");
                    Toast.makeText(UserEditActivity.this, "wrong", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(UserEditActivity.this, "Response Error", Toast.LENGTH_SHORT).show();
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
