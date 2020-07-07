package com.myapp.theagrim.torguo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Page5 extends Fragment {
    public Page5(){

    }

    String key;
    RecyclerView recyclerViewUp,recyclerAtt;
    ArrayList<Dataset> a1,a2;
    DatabaseReference databaseReference;

     boolean cmp(String s1,String s2){

        String arr[]=s1.split("/");
        String arr2[]=s2.split("/");
        for(int i=arr.length-1;i>=0;--i){
            if(!arr[i].equals(arr2[i])){
                return arr[i].compareTo(arr2[i]) <= 0;
            }
        }
        return true;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.events_details_show,container,false);
        recyclerViewUp=view.findViewById(R.id.upcoming);
        recyclerAtt=view.findViewById(R.id.attended);

        recyclerViewUp.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewUp.setItemAnimator(new DefaultItemAnimator());

        recyclerAtt.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAtt.setItemAnimator(new DefaultItemAnimator());


        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("Registered");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Calendar calendar=Calendar.getInstance();
                a1=new ArrayList<>();
                a2=new ArrayList<>();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);
                StringBuilder stringBuilder=new StringBuilder();
                if(day<10)
                    stringBuilder.append(0);
                stringBuilder.append(day).append('/');
                if(month<10)
                    stringBuilder.append(0);
                stringBuilder.append(month).append('/').append(year);
                String date=stringBuilder.toString();

                Log.i("Torgu",dataSnapshot.toString());
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Dataset ds=d.getValue(Dataset.class);
                    if(cmp(date,ds.getDate())){
                        a1.add(ds);
                    }
                    else{
                        a2.add(ds);
                    }
                }
                recyclerViewUp.setAdapter(new UpcomingEventsAdapter(a1));
                recyclerAtt.setAdapter(new AttendedEventsAdapter(a2));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return  view;
    }
}
