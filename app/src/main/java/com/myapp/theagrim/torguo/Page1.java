package com.myapp.theagrim.torguo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_NULL;

public class Page1 extends Fragment {

    private int searchRange=5;
    Button btn;
    Button time;
    Button distance;

    RecyclerView recyclerView;

    //declare your global list here
    ArrayList<Dataset> global;

    //declare local list
    ArrayList<Dataset> local;
    FirebaseDatabase database;
    Double srclattitude;
    Double srclongitude;
    LatLng source;
    MyRecycleViewAdapter adapter;
    LinearLayout linearLayout;
    View viewLinear;
    TextView textView;
    View view;
    View snackview;
    Snackbar snackbar;
    ProgressBar progressBar;

    public Page1(){

    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_page1, container, false);
        viewLinear=inflater.inflate(R.layout.no_content_in_recycle_view,container,false);
        final CoordinatorLayout coordinatorLayout=rootView.findViewById(R.id.coordinator);
        btn=rootView.findViewById(R.id.filter);
        time=rootView.findViewById(R.id.time);
        distance=rootView.findViewById(R.id.distance);
        linearLayout=rootView.findViewById(R.id.nocontent);
        linearLayout.addView(viewLinear);
        linearLayout.setVisibility(View.INVISIBLE);
        textView=rootView.findViewById(R.id.locationTextView);
        progressBar=rootView.findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.VISIBLE);
        setLocationInTextView();

        srclattitude = Double.parseDouble(getArguments().getString("Lattitude"));
        srclongitude = Double.parseDouble(getArguments().getString("Longitude"));
        source=new LatLng(srclattitude,srclongitude);

        recyclerView=rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        database=FirebaseDatabase.getInstance();
        fetchGlobalList();

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(local, new Comparator<Dataset>() {
                    @Override
                    public int compare(Dataset d1, Dataset d2) {
                        String s1=d1.getDate();
                        String s2=d2.getDate();
                        Log.d("Torguo",s1+" "+s2);
                        String arr[]=s1.split("/");
                        String arr2[]=s2.split("/");
                        for(int i=arr.length-1;i>=0;--i){
                            Log.d("Torguo",arr[i]+" "+arr2[i]);
                            if(!arr[i].equals(arr2[i]))
                                return arr[i].compareTo(arr2[i]);
                        }
                        return d1.getTime().compareTo(d2.getTime());
                    }
                });
                distance.setBackgroundResource(R.drawable.btn_bg4);
                distance.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn.setBackgroundResource(R.drawable.btn_bg4);
                time.setBackgroundResource(R.drawable.btn_bg5);
                time.setTextColor(Color.WHITE);
                if(snackbar!=null && snackbar.isShown()){
                    snackbar.dismiss();
                }
                adapter.notifyDataSetChanged();
            }
        });

        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(local, new Comparator<Dataset>() {
                    @Override
                    public int compare(Dataset d1, Dataset d2) {
                        return String.valueOf(source.distanceTo(new LatLng(d1.getLattitude(),d1.getLongitude()))).compareTo(String.valueOf(source.distanceTo(new LatLng(d2.getLattitude(),d2.getLongitude()))));
                    }
                });
                time.setBackgroundResource(R.drawable.btn_bg4);
                time.setTextColor(getResources().getColor(R.color.colorPrimary));
                btn.setBackgroundResource(R.drawable.btn_bg4);
                time.setTextColor(getResources().getColor(R.color.colorPrimary));

                distance.setBackgroundResource(R.drawable.btn_bg5);
                distance.setTextColor(Color.WHITE);
                if(snackbar!=null && snackbar.isShown()){
                    snackbar.dismiss();
                }
                adapter.notifyDataSetChanged();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.setBackgroundResource(R.drawable.btn_bg4);
                time.setTextColor(getResources().getColor(R.color.colorPrimary));

                distance.setTextColor(getResources().getColor(R.color.colorPrimary));
                distance.setBackgroundResource(R.drawable.btn_bg4);
                btn.setBackgroundResource(R.drawable.btn_bg5);
                btn.setTextColor(Color.WHITE);
                snackbar=Snackbar.make(coordinatorLayout,"Select Area of Search",Snackbar.LENGTH_INDEFINITE);
                Snackbar.SnackbarLayout layout=(Snackbar.SnackbarLayout)  snackbar.getView();
                layout.setBackgroundColor(Color.WHITE);
                TextView textView= layout.findViewById(android.support.design.R.id.snackbar_text);
                textView.setVisibility(View.INVISIBLE);
                snackview= inflater.inflate(R.layout.filter_view,null);
                Button filterbutton=snackview.findViewById(R.id.filterbutton);
                final EditText editText=snackview.findViewById(R.id.filtervalue);
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        return actionId == IME_NULL || actionId == IME_ACTION_DONE;
                    }
                });
                filterbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String value=editText.getText().toString().trim();
                        try{
                            if(!TextUtils.isEmpty(value)){
                                searchRange= Integer.parseInt(value);
                            }
                            editText.setText("");
                            change();
                            snackbar.dismiss();

                        }
                        catch (Exception e){
                            editText.setError("Invalid value");
                        }

                    }
                });


                layout.setPadding(0,0,0,0);
                layout.addView(snackview);
                snackbar.show();
            }
        });



        return rootView;
    }

    public void change(){
        local=new ArrayList<>();
        for(Dataset dataset:global){

            if(Math.ceil((source.distanceTo(new LatLng(dataset.getLattitude(),dataset.getLongitude())))/1000.0)<=searchRange){
                local.add(dataset);
            }
        }
        if(local.size()==0){
            linearLayout.setVisibility(View.VISIBLE);
        }
        else{
            linearLayout.setVisibility(View.INVISIBLE);
        }
        adapter=new MyRecycleViewAdapter(local,source);
        recyclerView.setAdapter(adapter);

    }

    public void setDataset(){
        local=new ArrayList<>();
        for(Dataset dataset:global){

            if(Math.ceil((source.distanceTo(new LatLng(dataset.getLattitude(),dataset.getLongitude())))/1000.0)<=searchRange){

                local.add(dataset);
            }
        }
        if(local.size()==0){
            linearLayout.setVisibility(View.VISIBLE);
        }
        else{
            linearLayout.setVisibility(View.INVISIBLE);
        }

        adapter=new MyRecycleViewAdapter(local,source);
        recyclerView.setAdapter(adapter);


    }


    public void fetchGlobalList(){
        //fetch data from the Firebase to an arraylist
        DatabaseReference databaseReference=database.getReference().child("EventDetails");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                global=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Dataset dataset=ds.getValue(Dataset.class);
                    global.add(dataset);
                }
                setDataset();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void setLocationInTextView() {
        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,  locationListener,Looper.getMainLooper());
        }
        else{
            //how that location service has to be enabled in settings

        }

    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            String cityName = null;

            try {
                Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addresses;
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);

                if (addresses.size() > 0) {

                    textView.setText("Home - " +addresses.get(0).getFeatureName()+" "+addresses.get(0).getThoroughfare()+","+addresses.get(0).getLocality());
//                    Log.v("Torguo",addresses.get(0).toString());
                }
                else{
                    textView.setText("Unknown Location");
                }
            }
            catch (Exception e) {
                Toast.makeText(getContext(),"We are facing trouble fetching your address",Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.INVISIBLE);



        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }






}
