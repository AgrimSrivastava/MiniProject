package com.myapp.theagrim.torguo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest.permission;
import android.util.Log;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class ReDirectActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private static final int LOCATION_PERMISSION=13;
    private int cnt=0;
    FirebaseAuth firebaseAuth;
    LocationManager locationManager;
    LocationListener locationListener;
    private ShimmerFrameLayout mShimmerViewContainer;
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_direct);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        checkPermission();
    }
    private synchronized void setUp(){
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,0,this).addOnConnectionFailedListener(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    public void checkPermission(){

        Activity thisActivity=ReDirectActivity.this;
        if (ContextCompat.checkSelfPermission(thisActivity,permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ++cnt;
            if(cnt>2){
                noPrompt();
            }
            else{
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION);
            }


        } else {
//            startActivity(new Intent(this,ProviderOptionsDrawerActvity.class));
            getLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    startActivity(new Intent(this,ProviderOptionsDrawerActvity.class));
                    getLocation();

                } else {
                    new AlertDialog.Builder(ReDirectActivity.this).setCancelable(false).setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            checkPermission();
                            dialog.dismiss();
                        }
                    }).setTitle("Permission Required").setMessage("We need to access of your location for functioning properly.").create().show();
                }

            }


        }
    }

    public void noPrompt(){
        new AlertDialog.Builder(getApplicationContext()).setTitle("Unable to access location").setMessage("We are unable to access your location .Please give location permission in settings.").setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).setCancelable(false).create().show();
    }

    public void getLocation() {

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        locationListener= new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,  locationListener,Looper.getMainLooper());
        }
        else{
            Toast.makeText(getApplicationContext(),"Unable to open GPS",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Torguo","Connected");
        checkPermission();
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d("Torguo","Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Torguo","Failed");
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            //stop progress dialog here
            Log.d("Torguo","Inside LocationR");
            Double lattitude=loc.getLatitude();
            Double longitude=loc.getLongitude();

            SharedPreferences sharedPreferences=getSharedPreferences("MyPreferences",MODE_APPEND);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Lattitude",String.valueOf(lattitude));
            editor.putString("Longitude",String.valueOf(longitude));
            editor.apply();
            Intent intent=null;
            firebaseAuth=FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                //start Activity for Provider
                intent=new Intent(ReDirectActivity.this,ProviderOptionsDrawerActvity.class);

            }
            else {

                intent=new Intent(ReDirectActivity.this,HomePageActivity.class);
            }
            intent.putExtra("Lattitude",String.valueOf(lattitude));
            intent.putExtra("Longitude",String.valueOf(longitude));
            startActivity(intent);
            finish();

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
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShimmerViewContainer.stopShimmerAnimation();
        locationManager.removeUpdates(locationListener);
//        Log.d("Torguo","Removed");
    }


}
