package com.myapp.theagrim.torguo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsDisplayFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayout linearLayout;
    EventRecyclerViewAdapter adapter;
    ArrayList<Dataset> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_event_display,container,false);

        recyclerView=rootview.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        linearLayout=rootview.findViewById(R.id.nocontent);
        linearLayout.setVisibility(View.INVISIBLE);

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Provider")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("EventsOrganised");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    arrayList= new ArrayList<>();
                    for(DataSnapshot d:dataSnapshot.getChildren()){
                        arrayList.add(d.getValue(Dataset.class));
                    }
                    adapter=new EventRecyclerViewAdapter(arrayList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return rootview;
    }
}
