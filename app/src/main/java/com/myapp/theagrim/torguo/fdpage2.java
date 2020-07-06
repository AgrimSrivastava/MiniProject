package com.myapp.theagrim.torguo;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class fdpage2 extends Fragment {
    ViewPager viewPager;
    Dataset dataset;
    int cnt=0;
    EditText timings,contact,landmark,capacity;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.view2,container,false);
        imageView=rootview.findViewById(R.id.imageView5);
        Glide.with(getApplicationContext()).load(R.drawable.view2)
                .apply(bitmapTransform(new BlurTransformation(40)))
                .into(imageView);
        timings=rootview.findViewById(R.id.timings);
        contact=rootview.findViewById(R.id.contact);
        landmark=rootview.findViewById(R.id.landmark);
        capacity=rootview.findViewById(R.id.capacity);
        Button next=rootview.findViewById(R.id.next);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timingsval=timings.getText().toString();
                String landmarkval=landmark.getText().toString();
                String contactval=contact.getText().toString();
                String capacityval=capacity.getText().toString();
                if(TextUtils.isEmpty(timingsval)){
                    timings.setError("Invalid timings");
                    return;
                }
                if(TextUtils.isEmpty(landmarkval)){
                    landmark.setError("Invalid landmark");
                    return;
                }
                if(TextUtils.isEmpty(contactval)||contactval.length()!=10){
                    contact.setError("Invalid contact");
                    return;
                }
                if(TextUtils.isEmpty(capacityval)){
                    capacity.setError("Invalid locality");
                    return;
                }

                dataset.setTime(timingsval);
                dataset.setLandmark(landmarkval);
                dataset.setContact(contactval);
                dataset.setCapacity(Integer.parseInt(capacityval));
                viewPager.setCurrentItem(2,true);

            }
        });
        return rootview;
    }

    boolean check(){
        String timingsval=timings.getText().toString();
        String landmarkval=landmark.getText().toString();
        String contactval=contact.getText().toString();
        String capacityval=capacity.getText().toString();
        if(TextUtils.isEmpty(timingsval)){
            timings.setError("Invalid timings");
            return true;
        }
        if(TextUtils.isEmpty(landmarkval)){
            landmark.setError("Invalid landmark");
            return true;
        }
        if(TextUtils.isEmpty(contactval)||contactval.length()!=10){
            contact.setError("Invalid contact");
            return true;
        }
        if(TextUtils.isEmpty(capacityval)){
            capacity.setError("Invalid locality");
            return true;
        }
        dataset.setTime(timingsval);
        dataset.setLandmark(landmarkval);
        dataset.setContact(contactval);
        dataset.setCapacity(Integer.parseInt(capacityval));

        return false;
    }


}
