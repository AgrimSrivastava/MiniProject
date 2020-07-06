package com.myapp.theagrim.torguo;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.myapp.theagrim.torguo.ProviderOptionsDrawerActvity.PROFILE_UPDATE_CODE;

public class Page3 extends Fragment {


    private Page3.SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    Button button;
    TabLayout tabLayout;
    DatabaseReference databaseReference;
    TextView events,ratings,reviews,name,description;
    ImageView profilPic;
    int images[]={R.drawable.apple,R.drawable.mango,R.drawable.grapes,R.drawable.pear,R.drawable.pineapple,R.drawable.pomegranate,R.drawable.lemon,R.drawable.strawberry,R.drawable.watermelon};
    String Tab[]=new String[]{"DETAILS","EVENTS"};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page3, container, false);
        button=rootView.findViewById(R.id.editProfile);
        mViewPager=rootView.findViewById(R.id.viewpager);
        tabLayout=rootView.findViewById(R.id.tabs);
        events=rootView.findViewById(R.id.events);
        ratings=rootView.findViewById(R.id.ratings);
        reviews=rootView.findViewById(R.id.reviews);
        name=rootView.findViewById(R.id.textView11);
        description=rootView.findViewById(R.id.textView9);
        profilPic=rootView.findViewById(R.id.circleImageView);
        tabLayout.setupWithViewPager(mViewPager);

        final ProgressBar progressBar=rootView.findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.VISIBLE);

        databaseReference=FirebaseDatabase.getInstance().getReference().child("Provider").
                                                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
            Log.d("Torguo",FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if(dataSnapshot.exists()){
                        for(DataSnapshot d:dataSnapshot.getChildren()){
                            Profile p=d.getValue(Profile.class);
                            int num=p.getImageNnum();
                            profilPic.setImageResource(images[num]);
                            events.setText(String.valueOf(p.getEvents()));
                            ratings.setText(String.valueOf(p.getRatings()));
                            reviews.setText(String.valueOf(p.getReviews()));
                            name.setText(p.getName());
                            description.setText(p.getDescription());
                        }

                    }
                    else{
                        new AlertDialog.Builder(getContext()).setCancelable(false).setTitle("New User").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                getActivity().startActivityForResult(new Intent(getContext(),EditProfile.class),PROFILE_UPDATE_CODE);
                            }
                        }).setMessage("Thank you for joining us.Start creating your profile?").create().show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        mSectionsPagerAdapter=new Page3.SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText(Tab[0]);
        tabLayout.getTabAt(1).setText(Tab[1]);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(),EditProfile.class));
            }
        });
        return rootView;
    }
    public Page3(){

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        @TargetApi(24)
        public Fragment getItem(int position) {

            Fragment f;

            if(position==0)
                f=new ProfileFragment();
            else
                f=new EventsDisplayFragment();

            return f;

        }

        @Override
        public int getCount() {
            //show 2 pages
            return 2;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PROFILE_UPDATE_CODE){
            if(resultCode==RESULT_OK){
                Toast.makeText(getContext(), "Profile created successfully", Toast.LENGTH_LONG).show();
            }
            if(resultCode==RESULT_CANCELED){
                new AlertDialog.Builder(getContext()).setCancelable(false).setTitle("Profile Not Created").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        getActivity().startActivityForResult(new Intent(getContext(),EditProfile.class),PROFILE_UPDATE_CODE);
                    }
                }).setMessage("Unable to create profile!!Start Again").create().show();
            }
        }

    }


}
