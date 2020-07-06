package com.myapp.theagrim.torguo;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.geojson.Point;

public class FurtherDetailActivity extends AppCompatActivity {

    private int curvalue=0;
    private FurtherDetailActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    Dataset dataset;
    fdpage1 f1;
    fdpage2 f2;
    fdpage3 f3;
    fdpage4 f4;
    private ProgressBar progressBar=null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_further_detail);
        android.support.v7.widget.Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dataset=new Dataset();
        dataset.setLattitude(Double.parseDouble(getIntent().getStringExtra("Lattitude")));
        dataset.setLongitude(Double.parseDouble(getIntent().getStringExtra("Longitude")));

        mSectionsPagerAdapter = new FurtherDetailActivity.SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
       mViewPager.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               int i=mViewPager.getCurrentItem();
               if(i==0){
                   if(f1.check()){
                       mViewPager.setCurrentItem(i);
                       Toast.makeText(getApplicationContext(),"Fill all the details",Toast.LENGTH_LONG).show();
                       return true;
                   }
               }
               else if(i==1){
                    if(f2.check()){
                        mViewPager.setCurrentItem(i);
                        Toast.makeText(getApplicationContext(),"Fill all the details",Toast.LENGTH_LONG).show();
                        return true;

                    }
               }
               return FurtherDetailActivity.super.onTouchEvent(event);
           }
       });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        @TargetApi(24)
        public Fragment getItem(int position) {


            Fragment fragment;
            if (position == 0){

                 fdpage1 f=new fdpage1();
                 f.viewPager=mViewPager;
                 f.dataset=dataset;
                 fragment=f;
                 f1=f;

            }

            else if(position==1){
                fdpage2 f = new fdpage2();
                f.viewPager=mViewPager;
                f.dataset=dataset;
                fragment=f;
                f2=f;
            }


            else if(position==2){
                fdpage3 f = new fdpage3();
                f.viewPager=mViewPager;
                f.dataset=dataset;
                fragment=f;
                f3=f;
            }


            else{
                fdpage4 f= new fdpage4();
                f.viewPager=mViewPager;
                f.dataset=dataset;
                f.activity=FurtherDetailActivity.this;
                fragment=f;
                f4=f;
            }


            return fragment;

        }

        @Override
        public int getCount() {
            //show 4 pages
            return 4;
        }
    }





    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {

            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            view.setTranslationX(-1 * view.getWidth() * position);

            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 2) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }

    private void startAnimation(int end,int start){
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", start,end);
        progressAnimator.setDuration(1000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }


}

