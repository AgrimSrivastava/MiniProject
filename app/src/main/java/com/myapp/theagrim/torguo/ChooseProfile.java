package com.myapp.theagrim.torguo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ChooseProfile extends AppCompatActivity {

    ImageView i1[];
    ImageView i2[];
    Button cancel,save;
    int ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile);
        i1=new ImageView[9];
        i2=new ImageView[9];
        ans=getIntent().getIntExtra("Image",0);

        i1[0]= findViewById(R.id.apple);
        i1[1]=findViewById(R.id.mango);
        i1[2]=findViewById(R.id.grape);
        i1[3]=findViewById(R.id.pear);
        i1[4]=findViewById(R.id.pineapple);
        i1[5]=findViewById(R.id.pomegranate);
        i1[6]=findViewById(R.id.lemon);
        i1[7]= findViewById(R.id.strawberry);
        i1[8]=findViewById(R.id.watermelon);

        i2[0]= findViewById(R.id.appleview);
        i2[1]=findViewById(R.id.mangoview);
        i2[2]=findViewById(R.id.grapeview);
        i2[3]=findViewById(R.id.pearview);
        i2[4]=findViewById(R.id.pineappleview);
        i2[5]=findViewById(R.id.pomegranateview);
        i2[6]=findViewById(R.id.lemonview);
        i2[7]= findViewById(R.id.strawberryview);
        i2[8]=findViewById(R.id.watermelonview);

        for(int i=0;i<9;++i){
            addon(i);
        }
        cancel=findViewById(R.id.cancel);
        save=findViewById(R.id.selectphoto);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("Result",String.valueOf(ans));
                setResult(Activity.RESULT_CANCELED,intent);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("Result",String.valueOf(ans));
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

    }
    private void addon(final int pos){
        i1[pos].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perform(pos);
                ans=pos;
            }
        });
    }

    private void perform(int pos){
        for(int i=0;i<9;++i){
            if(i!=pos){
                i2[i].setVisibility(View.INVISIBLE);
            }
            else{
                i2[i].setVisibility(View.VISIBLE);
            }
        }
    }
}
