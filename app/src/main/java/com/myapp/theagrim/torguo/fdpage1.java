package com.myapp.theagrim.torguo;

import android.animation.ObjectAnimator;
import android.graphics.Point;
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

public class fdpage1 extends Fragment {

     ViewPager viewPager;
     Dataset dataset;
     EditText locality,state,city;
     ImageView imageView;

    public fdpage1(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.view1,container,false);
         locality=rootview.findViewById(R.id.locality);
         city=rootview.findViewById(R.id.city);
         state=rootview.findViewById(R.id.state);
         imageView=rootview.findViewById(R.id.image);
        Glide.with(getApplicationContext()).load(R.drawable.gettingstarted)
                .apply(bitmapTransform(new BlurTransformation(40)))
                .into(imageView);
        TextView latitude=rootview.findViewById(R.id.lattitude);
        TextView longitude=rootview.findViewById(R.id.longitude);

        latitude.setText(String.valueOf(dataset.getLattitude()));
        longitude.setText(String.valueOf(dataset.getLongitude()));




        Button next=rootview.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String localityVal=locality.getText().toString();
                String cityVal=city.getText().toString();
                String stateVal=state.getText().toString();
                if(TextUtils.isEmpty(localityVal)){
                    locality.setError("Invalid locality");
                    return;
                }
                if(TextUtils.isEmpty(cityVal)){
                    city.setError("Invalid city");
                    return;
                }
                if(TextUtils.isEmpty(stateVal)){
                    state.setError("Invalid state");
                    return;
                }
                dataset.setCity(cityVal);
                dataset.setLocality(localityVal);
                dataset.setState(stateVal);
                viewPager.setCurrentItem(1,true);
            }
        });
        return rootview;
    }

    public boolean check(){
        String localityVal=locality.getText().toString();
        String cityVal=city.getText().toString();
        String stateVal=state.getText().toString();
        if(TextUtils.isEmpty(localityVal)){
            locality.setError("Invalid locality");
            return true;
        }
        if(TextUtils.isEmpty(cityVal)){
            city.setError("Invalid city");
            return true;
        }
        if(TextUtils.isEmpty(stateVal)){
            state.setError("Invalid state");
            return true;
        }
        dataset.setCity(cityVal);
        dataset.setLocality(localityVal);
        dataset.setState(stateVal);
        return false;
    }


}
