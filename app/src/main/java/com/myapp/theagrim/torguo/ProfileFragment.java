package com.myapp.theagrim.torguo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    TextView name,age,occupation,address,contact,events,ratings;
    DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_profile,container,false);
        name=rootview.findViewById(R.id.name);
        age=rootview.findViewById(R.id.age);
        occupation=rootview.findViewById(R.id.occupation);
        address=rootview.findViewById(R.id.address);
        contact=rootview.findViewById(R.id.contact);
        events=rootview.findViewById(R.id.events);
        ratings=rootview.findViewById(R.id.ratings);


        databaseReference=FirebaseDatabase.getInstance().getReference().child("Provider").child(FirebaseAuth.getInstance()
                                                                                                .getCurrentUser().getUid()).child("Profile");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot d:dataSnapshot.getChildren()){
                        Profile p=d.getValue(Profile.class);
                        name.setText(p.getName());
                        age.setText(String.valueOf(p.getAge()));
                        occupation.setText(p.getOccupation());
                        address.setText(p.getAddress());
                        contact.setText(p.getContact());
                        events.setText(String.valueOf(p.getEvents()));
                        ratings.setText(String.valueOf(p.getRatings()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootview;
    }
}
