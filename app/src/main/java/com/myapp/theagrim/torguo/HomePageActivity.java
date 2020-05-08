package com.myapp.theagrim.torguo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//set a progress dialog while retrieving address
//review location permission

public class HomePageActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private BottomNavigationView navigation;
    private TextView textView;
    private static final int LOCATION_PERMISSION=13;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0, true);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1, true);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        textView = findViewById(R.id.locationTextView);
        setLocationInTextView();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.Pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        getSupportActionBar().hide();



    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (position == 0)
                return new Page1();
            else
                return new Page2();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    public void setLocationInTextView() {
        Activity thisActivity=HomePageActivity.this;
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Log.v("Torguo","insideSetLocation");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
        else{
            //how that location service has to be enabled in settings
        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case LOCATION_PERMISSION: {
//
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                }
//
//
//            }
//        }
//    }

    private class MyLocationListener implements LocationListener {



        @Override
        public void onLocationChanged(Location loc) {


//            Toast.makeText(
//                    getBaseContext(),
//                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();


            String longitude = "Longitude: " + loc.getLongitude();
//            Log.v("Torguo", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
//            Log.v("Torguo", latitude);

            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {

                    cityName = addresses.get(0).getLocality();
//                    Log.v("Torguo",addresses.get(0).toString());
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude;

            textView.setText(cityName);
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

}
