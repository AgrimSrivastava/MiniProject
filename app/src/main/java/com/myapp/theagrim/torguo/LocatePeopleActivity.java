package com.myapp.theagrim.torguo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.ArrayList;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class LocatePeopleActivity extends AppCompatActivity {
    private MapView mapView;
    private MapboxMap mapbox;
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    private DatabaseReference databaseReference;
    ArrayList<LatLng> latLngs;
    ArrayList<LatLng> local;
    Double srclattitude;
    Double srclongitude;
    private int defaultsearch=5;
    private ImageButton loginbtn;
    private TextView textView;
    String str;
    Source source;
    Layer  layer;
    Style globalstyle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox));
        setContentView(R.layout.activity_locate_people);
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");

        local=new ArrayList<>();

        textView=findViewById(R.id.searcharea);
        loginbtn=findViewById(R.id.changebtn);

        str="Search Range - "+String.valueOf(defaultsearch)+" (in KM)";
        textView.setText(str);

        localList();
        mapView=findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);



        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog= new AlertDialog.Builder(LocatePeopleActivity.this);
                LayoutInflater layoutInflater= getLayoutInflater();
                final View view=layoutInflater.inflate(R.layout.custom_layout_alert_dialog,null);
                dialog.setView(view).setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText=view.findViewById(R.id.searchdistance);
                        String s=editText.getText().toString().trim();
                        if(!TextUtils.isEmpty(s)){
                            defaultsearch=Integer.parseInt(s);
                            str="Search Range - "+String.valueOf(defaultsearch)+" (in KM)";
                            textView.setText(str);
                            generateLists();
                            removeMarkers();
                            addMarkers(globalstyle);
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("Change Search Area").setCancelable(false).create().show();

            }
        });

    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//            Log.d("Torguo","Inside location");
            LocationComponent locationComponent = mapbox.getLocationComponent();

            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);

            locationComponent.setRenderMode(RenderMode.COMPASS);
        }
        else{
//            Log.d("Torguo","Nhi h permission saale");
            new AlertDialog.Builder(this).setNegativeButton("Close Map", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setTitle("Location Permission Not Found").setMessage("Allow app to acces your location manually in settings").setCancelable(false).create().show();
        }
    }

    public void addMarkers(Style loadedmapStyle){
        Feature f[]=new Feature[latLngs.size()];
        for(int i=0;i<latLngs.size();++i)
            f[i]=Feature.fromGeometry(Point.fromLngLat(latLngs.get(i).getLongitude(),latLngs.get(i).getLatitude()));


        source=new GeoJsonSource(ICON_SOURCE_ID,FeatureCollection.fromFeatures(f));
        loadedmapStyle.addSource(source);

        layer=new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[] {0f, -9f}));

        loadedmapStyle.addImage(RED_PIN_ICON_ID,BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_red_marker)));
        loadedmapStyle.addLayer(layer);
    }

    public void generateLists(){

        latLngs=new ArrayList<>();
        SharedPreferences sharedPreferences=getSharedPreferences("MyPreferences",MODE_PRIVATE);
        //use last location here
        srclattitude=Double.parseDouble(sharedPreferences.getString("Lattitude","0"));
        srclongitude=Double.parseDouble(sharedPreferences.getString("Longitude","0"));
        Log.d("Torguo",srclattitude.toString());
        final LatLng source=new LatLng(srclattitude,srclongitude);
//        Log.d("Torguo",source.toString());
//
//        Log.d("Torguo",String.valueOf(local.size()));

        for(int i=0;i<local.size();++i){
            if(source.distanceTo(local.get(i))<=defaultsearch*1000){
                latLngs.add(local.get(i));
            }
        }
//        Log.d("Torguo",String.valueOf(local.size()));
//        Log.d("Torguo",String.valueOf(latLngs.size()));


    }

    public void localList(){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                       for(DataSnapshot d:ds.getChildren()){
                           MyUsers myUsers=d.getValue(MyUsers.class);
                           Log.d("Torguo",myUsers.toString());
                           local.add(new LatLng(Double.parseDouble(myUsers.getLat()),Double.parseDouble(myUsers.getLo())));
                       }
                    }
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull MapboxMap mapboxMap) {
                            mapbox=mapboxMap;
                            mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {
                                    globalstyle=style;
                                    generateLists();
                                    addMarkers(style);
                                    enableLocationComponent(style);
                                }
                            });

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeMarkers(){

        globalstyle.removeImage(RED_PIN_ICON_ID);
        globalstyle.removeLayer(layer);
        globalstyle.removeSource(source);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
