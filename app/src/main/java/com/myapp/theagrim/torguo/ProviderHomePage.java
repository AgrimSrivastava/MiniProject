package com.myapp.theagrim.torguo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class ProviderHomePage extends AppCompatActivity {


    //NOT IN USE WILL DELETE LATER
//    private ProviderHomePage.SectionsPagerAdapter mSectionsPagerAdapter;
//    private ViewPager mViewPager;
//    private BottomNavigationView navigation;


//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mViewPager.setCurrentItem(0,true);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mViewPager.setCurrentItem(1,true);
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home_page);

//        mSectionsPagerAdapter = new ProviderHomePage.SectionsPagerAdapter(getSupportFragmentManager());
//
//
//        mViewPager = (ViewPager) findViewById(R.id.PagerProvider);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//
//        navigation = (BottomNavigationView) findViewById(R.id.navigationprovider);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }

//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//
//            if(position==0)
//                return new Page1();
//            else if(position==1)
//                return new Page2();
//            else
//                return new Page3();
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return 3;
//        }
//    }

}
