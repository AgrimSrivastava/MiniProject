package com.myapp.theagrim.torguo;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Calendar;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class fdpage3 extends Fragment {
    ViewPager viewPager;
    Dataset dataset;
    boolean f1=false;
    boolean f2=false;
    int cnt=0;
    EditText typeFood;
    ImageView imageView;
    int year,month,day;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.view3,container,false);
        imageView=rootview.findViewById(R.id.imageView5);
        Glide.with(getApplicationContext()).load(R.drawable.view6)
                .apply(bitmapTransform(new BlurTransformation(40)))
                .into(imageView);
        final Button tentative=rootview.findViewById(R.id.tentative);
        final Button confirm=rootview.findViewById(R.id.confirm);
        final Button yes=rootview.findViewById(R.id.yes);
        final Button no=rootview.findViewById(R.id.no);
        final EditText typeFood=rootview.findViewById(R.id.foodType);
        final Button next=rootview.findViewById(R.id.next);
        final TextView editText=rootview.findViewById(R.id.date);



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1=false;
                tentative.setBackgroundResource(R.drawable.outline_for_textview);
                tentative.setTextColor(Color.WHITE);
                confirm.setTextColor(Color.BLACK);
                confirm.setBackgroundColor(Color.WHITE);
            }
        });

        tentative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f1=true;
                confirm.setBackgroundResource(R.drawable.outline_for_textview);
                confirm.setTextColor(Color.WHITE);
                tentative.setTextColor(Color.BLACK);
                tentative.setBackgroundColor(Color.WHITE);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2=true;
                yes.setBackgroundResource(R.drawable.outline_for_textview);
                yes.setTextColor(Color.WHITE);
                no.setTextColor(Color.BLACK);
                no.setBackgroundColor(Color.WHITE);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f2=false;
                no.setBackgroundResource(R.drawable.outline_for_textview);
                no.setTextColor(Color.WHITE);
                yes.setTextColor(Color.BLACK);
                yes.setBackgroundColor(Color.WHITE);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodT=typeFood.getText().toString();
                String date =editText.getText().toString();
                if(TextUtils.isEmpty(foodT)){
                    foodT="none";
                }
                if(TextUtils.isEmpty(date)){
                    editText.setError("Invalid date");
                    return;
                }
                dataset.setDate(date);
                dataset.setType(foodT);
                dataset.setCutlery(f2);
                dataset.setStatus(f1);
                viewPager.setCurrentItem(3,true);
            }

        });
        final Calendar myCalendar=Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                StringBuilder stringBuilder=new StringBuilder();
                if(dayOfMonth<10)
                    stringBuilder.append(0);
                stringBuilder.append(dayOfMonth).append('/');
                if(monthOfYear<10)
                    stringBuilder.append(0);
                stringBuilder.append(monthOfYear).append('/').append(year);
                String date=stringBuilder.toString();
                editText.setText(date);
            }

        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);

                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        return rootview;
    }

    void updateLabel(){

    }






}
