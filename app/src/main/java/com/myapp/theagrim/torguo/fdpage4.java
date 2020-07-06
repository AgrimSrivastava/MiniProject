package com.myapp.theagrim.torguo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class fdpage4 extends Fragment {
    ViewPager viewPager;
    Dataset dataset;
    Activity activity;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.view4,container,false);
        Button confirm=rootview.findViewById(R.id.confirm);
        Button cancel=rootview.findViewById(R.id.cancel);


        startAnimation(100,66);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add to the database
                //add any transition if you want
                final DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Provider")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Profile p=null;
                        String k=NULL;
                        for(DataSnapshot d:dataSnapshot.getChildren()){
                            k=d.getKey();
                            p=d.getValue(Profile.class);
                        }

                        String name=p.getName();
                        p.setEvents(p.getEvents()+1);
                        db.child(k).setValue(p);

                        dataset.setName(name);
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("EventDetails");
                        String key=databaseReference.push().getKey();
                        dataset.setKey(key);
                        databaseReference.child(key).setValue(dataset);
                        FirebaseDatabase.getInstance().getReference().child("Events").push().setValue(key);
                        databaseReference=FirebaseDatabase.getInstance().getReference().child("Provider").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("EventsOrganised");
                        databaseReference.push().setValue(dataset);
                        Toast.makeText(getContext(),"Event Created Successfully",Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity,R.style.CustomDialogTheme).setTitle("Cancel Event Creation").setMessage("Are you sure you want to dismiss this event?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                        startActivity(new Intent(activity,ProviderOptionsDrawerActvity.class));
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).create().show();
            }
        });
        return rootview;
    }

    private void startAnimation(int end,int start){
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", start,end);
        progressAnimator.setDuration(1000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }

    boolean check(){

        return false;
    }


}
