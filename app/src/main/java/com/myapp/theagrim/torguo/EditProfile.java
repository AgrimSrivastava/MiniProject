package com.myapp.theagrim.torguo;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class EditProfile extends AppCompatActivity {

    ImageView done;
    ImageView profilephoto;
    EditText name,age,occupation,contact,address,description;
    TextView changeprofile;
    Profile p=null;


    DatabaseReference databaseReference;
    int temp=0;
    int PROFILE_RESULT_CODE=13;
    int images[]={R.drawable.apple,R.drawable.mango,R.drawable.grapes,R.drawable.pear,R.drawable.pineapple,R.drawable.pomegranate,R.drawable.lemon,R.drawable.strawberry,R.drawable.watermelon};
    String key=NULL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Provider").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");

        done=findViewById(R.id.done);
        name=findViewById(R.id.name);
        contact=findViewById(R.id.contact);
        age=findViewById(R.id.age);
        occupation=findViewById(R.id.occupation);
        address=findViewById(R.id.address);
        changeprofile=findViewById(R.id.change);
        profilephoto=findViewById(R.id.circleImageView2);
        description=findViewById(R.id.description);

        init();

        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ChooseProfile.class);
                intent.putExtra("Image",temp);
                startActivityForResult(intent,PROFILE_RESULT_CODE);
            }
        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.focus);
                }
                else{
                    v.setBackgroundResource(R.drawable.no_focus);
                }
            }
        });

        age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.focus);
                }
                else{
                    v.setBackgroundResource(R.drawable.no_focus);
                }
            }
        });

        occupation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.focus);
                }
                else{
                    v.setBackgroundResource(R.drawable.no_focus);
                }
            }
        });

        contact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.focus);
                }
                else{
                    v.setBackgroundResource(R.drawable.no_focus);
                }
            }
        });

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.focus);
                }
                else{
                    v.setBackgroundResource(R.drawable.no_focus);
                }
            }
        });

        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackgroundResource(R.drawable.focus);
                }
                else{
                    v.setBackgroundResource(R.drawable.no_focus);
                }
            }
        });



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameval=name.getText().toString();
                int ageval=Integer.parseInt(age.getText().toString());
                String occupationval=occupation.getText().toString();
                String contactval=contact.getText().toString();
                String addressval=address.getText().toString();
                String descripval=description.getText().toString();
                Profile profile;
                if(p==null){
                    profile=new Profile(temp,nameval,addressval,occupationval,contactval,ageval,0,0,0,descripval);
                }
                else{
                    profile=new Profile(temp,nameval,addressval,occupationval,contactval,ageval,p.getEvents(),p.getReviews(),p.getRatings(),descripval);
                }

                if(key.equals(NULL)){
                    databaseReference.push().setValue(profile);
                }
                else{
                    databaseReference.child(key).setValue(profile);
                }
                Toast.makeText(getApplicationContext(),"Profile updated successfully",Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }

    private void init(){



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot d:dataSnapshot.getChildren()){
                        key=d.getKey();
                        p=d.getValue(Profile.class);

                        name.setText(p.getName());
                        age.setText(String.valueOf(p.getAge()));
                        address.setText(p.getAddress());
                        occupation.setText(p.getOccupation());
                        contact.setText(p.getContact());
                        description.setText(p.getDescription());
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PROFILE_RESULT_CODE){
            if(resultCode==Activity.RESULT_OK){
                temp=Integer.parseInt(data.getStringExtra("Result"));
                profilephoto.setImageResource(images[temp]);
            }
        }

    }
}
