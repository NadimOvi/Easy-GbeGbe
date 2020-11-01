package com.nadim.gbe_gbe_final.MainActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.nadim.gbe_gbe_final.BuildConfig;
import com.nadim.gbe_gbe_final.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static maes.tech.intentanim.CustomIntent.customType;


public class MapActivity extends  FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, LocationListener, Animation.AnimationListener {

    public static int map_to_addsignup = 0;
    SupportMapFragment mapFragment;
    CameraPosition cameraPosition;
    String longitude, latitude;
    double lat;
    double lan;
    String locality = null;
    String country = null;
    String state;
    String sub_admin;
    String city;
    String pincode;
    String locality_city;
    String sub_localoty;
    String country_code;
    String locationname;
    FusedLocationProviderClient mFusedLocationProviderClient;
    RippleBackground content;
    // Animation
    Animation animFadein;
    private GoogleMap mMap;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private TextView resutText;
    private LatLng latLng = null;
    private boolean mRequestingLocationUpdates;
    private Button ic_save_proceed;
    private Context context;
    private TextView timeView,distanceView;
    private ImageView img_back, img_pin;
    //private RadarView radar_view;
    private ProgressBar pro_bar;
    private SearchView searchView;
    String image;
    String imageTime;
    int userId,radioChecked;
    ArrayList<String> list;



    Boolean isInitialized=false,isStart=false,isFirstTimeLocationChange=true;
    Location currLocation,prevLocation;
    Location mLastLocation;
    private Long prevTime=null,currTime=null;
    float distance=0.0f;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        try {

            findViewByID();
           /* setupAutoCompleteFragment();*/
            startLocationButtonClick();
            pro_bar.setVisibility(View.VISIBLE);
            configureCameraIdle();
            ic_save_proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        startActivity(new Intent(MapActivity.this, AddressActivity.class)
                                .putExtra("userId",userId)
                                .putExtra("locationNameMap",locationname)
                                .putExtra("image", image)
                                .putExtra("UploadTime", imageTime)
                                .putExtra("selected", radioChecked)
                                .putExtra("ProfileItemList",list));

                        customType(MapActivity.this,"left-to-right");
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                    finish();
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String location = searchView.getQuery().toString();
                    List<Address> addressList=null;

                    /*if (location !=null || !location.equals("")){*/

                        Geocoder geocoder = new Geocoder(MapActivity.this);
                        try {
                            addressList=geocoder.getFromLocationName(location,1);
                            Address address= addressList.get(0);
                            String str= address.getFeatureName();
                            str += "," + address.getSubLocality() + "," +address.getLocality() + "," + address.getCountryName();
                            resutText.setText(str);
                            locationname = str;

                            LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                          /*  mMap.addMarker(new MarkerOptions().position(latLng).title(location));*/
                            moveCamera(latLng,16f);
                        }catch (IOException e){
                            e.printStackTrace();
                            Toast.makeText(MapActivity.this,"Cannot Find the location",Toast.LENGTH_SHORT).show();
                        }
                      /*  Address address= addressList.get(0);*/
                        /*String str = addressList.get(0).getFeatureName();
                        str += "," + addressList.get(0).getSubLocality() + "," + addressList.get(0).getLocality() + "," + addressList.get(0).getCountryName();

                        if (str != null) {
                            resutText.setText(str);
                            locationname = str;

                            pro_bar.setVisibility(View.GONE);
                        } else {
                            resutText.setText("Location could not be fetched...");
                            pro_bar.setVisibility(View.GONE);
                        }*/


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void configureCameraIdle() {
        pro_bar.setVisibility(View.VISIBLE);
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCameraIdle() {
                try {
                    pro_bar.setVisibility(View.VISIBLE);
                    LatLng latLng = mMap.getCameraPosition().target;
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                    resutText.setText("Loading...");
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (addressList != null && addressList.size() > 0) {
                            locality = addressList.get(0).getAddressLine(0);
                            country = addressList.get(0).getCountryName();


                            String str = addressList.get(0).getFeatureName();
                            str += "," + addressList.get(0).getSubLocality() + "," + addressList.get(0).getLocality() + "," + addressList.get(0).getCountryName();


                            state = addressList.get(0).getAdminArea();
                            sub_admin = addressList.get(0).getSubAdminArea();
                            city = addressList.get(0).getFeatureName();
                            pincode = addressList.get(0).getPostalCode();
                            locality_city = addressList.get(0).getLocality();
                            sub_localoty = addressList.get(0).getSubLocality();
                            country_code = addressList.get(0).getCountryCode();

                            if (str != null) {
                                if(!isInitialized && isStart){
                                    prevLocation = new Location(latitude);
                                    currLocation = (Location) geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                                    prevTime = System.currentTimeMillis();
                                    currTime = System.currentTimeMillis();
                                    isInitialized=true;
                                }else if(isStart) {
                                    prevLocation = currLocation;
                                    currLocation = (Location) geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

                                    long millis = prevTime - System.currentTimeMillis();
                                    long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
                                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
                                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
                                    String time = String.format("%02d:%02d:%02d", -hours, -minutes, -(seconds % 60));

                                    timeView.setText("Time = " + time);

                                    //1 paisa per second & 1 paisa per meter
                                    float cost = ((-seconds) + distance) / 100.0f;

                                    distance += prevLocation.distanceTo(currLocation);
                                    distanceView.setText("Distance = " + distance + " meters");

                                }













                                resutText.setText(str);
                                locationname = str;

                                pro_bar.setVisibility(View.GONE);
                            } else {
                                resutText.setText("Location could not be fetched...");
                                pro_bar.setVisibility(View.GONE);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        try {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            mRequestingLocationUpdates = true;
                            configureCameraIdle();
                        }
                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            if (response.isPermanentlyDenied()) {
                                // open device settings when the permission is
                                // denied permanently
                                openSettings();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken token) {
                            token.continuePermissionRequest();
                        }

                    }).check();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void findViewByID() {
        try {
            context = MapActivity.this;
            img_back = findViewById(R.id.img_back);
            resutText = findViewById(R.id.dragg_result);
            ic_save_proceed = findViewById(R.id.ic_save_proceed);
          /*  id_tv_change = findViewById(R.id.id_tv_change);*/
            img_pin = findViewById(R.id.img_pin);
            content = findViewById(R.id.content);
            pro_bar = findViewById(R.id.pro_bar);
            searchView= findViewById(R.id.search_View);


            timeView = findViewById(R.id.timeView);
            distanceView= findViewById(R.id.distanceView);



            userId = getIntent().getIntExtra("userId", 0);
            locationname = getIntent().getStringExtra("locationName");
            image = getIntent().getStringExtra("image");
            imageTime = getIntent().getStringExtra("UploadTime");

            Bundle bundle = getIntent().getExtras();
            list= bundle.getStringArrayList("ProfileItemList");

            resutText.setText(locationname);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            content.startRippleAnimation();
            // radar_view = findViewById(R.id.radar_view);
            // radar_view.startAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openSettings() {
        try {
            Intent intent = new Intent();
            intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //get device location
    private void getDeviceLocation() {
        Log.d("GoogleMapsActivity", "get location your device");
        try {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 16f);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void moveCamera(LatLng latLng, float zoom) {
        try {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

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
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            getDeviceLocation();
            mMap.setOnCameraIdleListener(onCameraIdleListener);
            mMap.setTrafficEnabled(true);
            mMap.setIndoorEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.resetMinMaxZoomPreference();
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            mMap.isIndoorEnabled();
            mMap.isBuildingsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public void onAnimationStart(Animation animation) {
    }
    @Override
    public void onAnimationEnd(Animation animation) {
    }
    @Override
    public void onAnimationRepeat(Animation animation) {
    }
    /*private void setupAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng sydney = place.getLatLng();
                mapFragment.getMapAsync(MapActivity.this);
            }

            @Override
            public void onError(Status status) {
                Log.e("Error", status.getStatusMessage());
            }
        });
    }*/
}
