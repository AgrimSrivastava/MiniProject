package com.myapp.theagrim.torguo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDetails extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10;
    CheckBox cb1,cb2,cb3,cb4;
    Dataset ds=null;
    android.support.v7.widget.Toolbar toolbar;
    private int arr[] ={R.drawable.foodimage1,R.drawable.foodimage2,R.drawable.foodimage3,R.drawable.foodimage4};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        String key=getIntent().getStringExtra("Key");
        String index=getIntent().getStringExtra("Image");
        ImageView imageView=findViewById(R.id.imageview);
        imageView.setImageResource(arr[Integer.parseInt(index)]);
        t1=findViewById(R.id.locality);
        t2=findViewById(R.id.landmark);
        t3=findViewById(R.id.city);
        t4=findViewById(R.id.state);
        t5=findViewById(R.id.val);
        t6=findViewById(R.id.date);
        t7=findViewById(R.id.time);
        t8=findViewById(R.id.longitude);
        t9=findViewById(R.id.latitude);
        t10=findViewById(R.id.typeof);
        cb1=findViewById(R.id.confirm);
        cb2=findViewById(R.id.tentative);
        cb3=findViewById(R.id.yes);
        cb4=findViewById(R.id.no);

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Provider")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("EventsOrganised");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    ds=d.getValue(Dataset.class);
                    fun();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void fun(){
        t1.setText(ds.getLocality());
        t2.setText(ds.getLandmark());
        t3.setText(ds.getCity());
        t4.setText(ds.getState());
        t5.setText(String.valueOf(ds.getCapacity()));
        t6.setText(ds.getDate());
        t7.setText(ds.getTime());
        String str=String.valueOf(ds.getLongitude());
        str="Longitude- "+str.substring(0,Math.min(4,str.length()-1));
        t8.setText(str);
        str=String.valueOf(ds.getLattitude());
        str="Latitude- "+str.substring(0,Math.min(str.length()-1,4));
        t9.setText(str);
        t10.setText(ds.getType());
        if(ds.isStatus()){
            cb1.setChecked(true);
            cb2.setChecked(false);
            cb2.setVisibility(View.INVISIBLE);
        }
        else{
            cb2.setChecked(true);
            cb1.setChecked(false);
            cb1.setVisibility(View.INVISIBLE);
        }
        if(ds.isCutlery()){
            cb3.setChecked(true);
            cb4.setChecked(false);
            cb4.setVisibility(View.INVISIBLE);
        }
        else{
            cb4.setChecked(true);
            cb3.setChecked(false);
            cb3.setVisibility(View.INVISIBLE);
        }
        String s=ds.getName()+"'s Event";
        getSupportActionBar().setTitle(s);


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
