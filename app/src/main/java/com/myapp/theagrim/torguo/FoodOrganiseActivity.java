package com.myapp.theagrim.torguo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class FoodOrganiseActivity extends AppCompatActivity {


    MapView mapView;
    LatLng source;
    private MapboxMap mapbox;
    private ImageView hoveringMarker;
    private Layer droppedMarkerLayer;
    private Button selectLocationButton;
    private ImageButton next;
    boolean f=false;
    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
    Point pointMarker;
    LatLng mapTargetLatLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox));
        setContentView(R.layout.activity_food_organise);

        android.support.v7.widget.Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        next=findViewById(R.id.nextPage);
        mapView=findViewById(R.id.mapView);

        SharedPreferences sharedPreferences=getSharedPreferences("MyPreferences",MODE_PRIVATE);
        //use last location here
        final double srclattitude=Double.parseDouble(sharedPreferences.getString("Lattitude","0"));
        final double srclongitude=Double.parseDouble(sharedPreferences.getString("Longitude","0"));
        source=new LatLng(srclattitude,srclongitude);
        mapTargetLatLng=source;

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!f){
                    new AlertDialog.Builder(FoodOrganiseActivity.this,R.style.CustomDialogTheme).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //                add point to the intent
//                startActivity(new Intent(FoodOrganiseActivity.this,FurtherDeatil.class));
                            dialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(),FurtherDetailActivity.class);
                            intent.putExtra("Lattitude",String.valueOf(mapTargetLatLng.getLatitude()));
                            intent.putExtra("Longitude", String.valueOf(mapTargetLatLng.getLongitude()));
                            finish();
                            startActivity(intent);
                        }
                    }).setTitle("No results from MapBox").setMessage("Due to the small database of Mapbox we are unable to get results .We will use your mobile background service to detect location in the next step ").create().show();
                }



            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                mapbox=mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull final Style style) {

                        CameraPosition position = new CameraPosition.Builder()
                                .target(source) // Sets the new camera position
                                .zoom(15) // Sets the zoom
                                .bearing(180) // Rotate the camera
                                .tilt(30) // Set the camera tilt
                                .build(); // Creates a CameraPosition from the builder

                        mapboxMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(position), 7000);
                        enableLocationComponent(style);

                        Toast.makeText(FoodOrganiseActivity.this,"Move map to select location", Toast.LENGTH_SHORT).show();

                        hoveringMarker = new ImageView(FoodOrganiseActivity.this);
                        hoveringMarker.setImageResource(R.drawable.ic_red_marker);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                        hoveringMarker.setLayoutParams(params);
                        mapView.addView(hoveringMarker);
                        initDroppedMarker(style);
                        selectLocationButton=findViewById(R.id.select_location_button);
                        selectLocationButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (hoveringMarker.getVisibility() == View.VISIBLE) {

                                     mapTargetLatLng= mapboxMap.getCameraPosition().target;

                                    hoveringMarker.setVisibility(View.INVISIBLE);
                                    next.setVisibility(View.VISIBLE);
                                    next.setClickable(true);

                                    selectLocationButton.setBackgroundColor(
                                            ContextCompat.getColor(FoodOrganiseActivity.this, R.color.colorAccent));
                                    selectLocationButton.setText("SELECT ANOTHER LOCATION");

                                    if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                                        GeoJsonSource source = style.getSourceAs("dropped-marker-source-id");
                                        if (source != null) {
                                            source.setGeoJson(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));
                                        }
                                        droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                                        if (droppedMarkerLayer != null) {
                                            droppedMarkerLayer.setProperties(visibility(VISIBLE));
                                        }
                                    }

                                   reverseGeocode(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));

                                } else {

                                    next.setVisibility(View.INVISIBLE);
                                    next.setClickable(false);

                                    selectLocationButton.setBackgroundColor(
                                            ContextCompat.getColor(FoodOrganiseActivity.this, R.color.colorPrimary));
                                    selectLocationButton.setText("Select Location");

                                    hoveringMarker.setVisibility(View.VISIBLE);

                                    droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                                    if (droppedMarkerLayer != null) {
                                        droppedMarkerLayer.setProperties(visibility(NONE));
                                    }
                                }
                            }
                        });
                    }
                });

            }
        });
    }


    private void reverseGeocode(final Point point) {
        try {
            pointMarker=point;
            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.mapbox))
                    .query(Point.fromLngLat(point.longitude(), point.latitude()))
                    .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                    .build();

            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                    if (response.body() != null) {
                        List<CarmenFeature> results = response.body().features();
                        if (results.size() > 0) {
                            f=true;
                            final CarmenFeature feature = results.get(0);

// If the geocoder returns a result, we take the first in the list and show a Toast with the place name.
                            mapbox.getStyle(new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {
                                    if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                                        Toast.makeText(FoodOrganiseActivity.this, ""+feature.placeName(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            f=false;
                        }
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                }
            });
        } catch (ServicesException servicesException) {
            servicesException.printStackTrace();
        }
    }

    private void initDroppedMarker(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("dropped-icon-image", BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_blue_marker));
        loadedMapStyle.addSource(new GeoJsonSource("dropped-marker-source-id"));
        loadedMapStyle.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id").withProperties(
                iconImage("dropped-icon-image"),
                visibility(NONE),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        ));
    }


    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
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

            new AlertDialog.Builder(this).setNegativeButton("Close Map", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setTitle("Location Permission Not Found").setMessage("Allow app to access your location manually in settings").setCancelable(false).create().show();
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
