package com.myapp.theagrim.torguo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Date;
import java.util.Random;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

//set a progress dialog while retrieving address
//review location permission

public class HomePageActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private BottomNavigationView navigation;

    private static final int LOCATION_PERMISSION=13;
    Bundle bundle;
    String lattitude;
    String longitude;
    Page1 page1=null;

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


        Intent intent=getIntent();
        lattitude=intent.getStringExtra("Lattitude");
        longitude=intent.getStringExtra("Longitude");


        commitUser();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.Pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i==1){
                    navigation.setSelectedItemId(R.id.navigation_dashboard);
                    return;
                }

                navigation.setSelectedItemId(R.id.navigation_home);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

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

            if (position == 0){
                page1=new Page1();
                bundle= new Bundle();
//                Log.v("Torguo",lattitude+" "+longitude);
                bundle.putString("Lattitude",lattitude);
                bundle.putString("Longitude",longitude);

                page1.setArguments(bundle);
                return page1;
            }

            else
                return new Page2();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }

    private void commitUser(){
        SharedPreferences sharedPreferences= getSharedPreferences("ForUser",MODE_APPEND);
        String key=sharedPreferences.getString("alpha",NULL);
        if(key.equals(NULL)){
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putString("alpha",key=getSaltString());
            editor.apply();
        }
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(key);
        MyUsers myUsers=new MyUsers(lattitude,longitude);
        databaseReference.push().setValue(myUsers);
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem()==0){
            if(page1!=null && page1.snackbar.isShown()){
                page1.snackbar.dismiss();
            }
            return;
        }
        super.onBackPressed();
    }
}
