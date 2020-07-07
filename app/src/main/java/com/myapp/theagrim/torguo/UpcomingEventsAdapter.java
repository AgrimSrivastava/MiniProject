package com.myapp.theagrim.torguo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

public class UpcomingEventsAdapter extends RecyclerView.Adapter<UpcomingEventsAdapter.ViewHolder> {

    public UpcomingEventsAdapter(){

    }
    private int arr[] ={R.drawable.foodimage1,R.drawable.foodimage2,R.drawable.foodimage3,R.drawable.foodimage4};
    ArrayList<Dataset> arrayList;
    Context context;

    public UpcomingEventsAdapter (ArrayList<Dataset> a){
        this.arrayList=a;
        Log.i("Torgu",String.valueOf(a.size()));

    }

    @NonNull
    @Override
    public UpcomingEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_layout,viewGroup,false);
        context=viewGroup.getContext();
        return new UpcomingEventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingEventsAdapter.ViewHolder viewHolder, int i) {
        final Dataset dataset=arrayList.get(i);
        viewHolder.name.setText(dataset.getName());
        String lat=String.valueOf(dataset.getLattitude());
        String lo=String.valueOf(dataset.getLongitude());
        String s="Latitude- "+lat.substring(0,Math.min(4,lat.length()-1))+" Longitude- "+lo.substring(0,Math.min(4,lo.length()-1));
        viewHolder.coordinates.setText(s);
        viewHolder.date.setText(dataset.getDate());
        viewHolder.time.setText(dataset.getTime());
        String str="N/A";
        viewHolder.distance.setText(str);
        viewHolder.randomimage.setImageResource(arr[i%4]);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

    TextView name;
    TextView coordinates;
    TextView date;
    TextView time;
    TextView distance;
    ImageView randomimage;

    public ViewHolder(View view){
        super(view);
        name=view.findViewById(R.id.name);
        coordinates=view.findViewById(R.id.coordinates);
        date=view.findViewById(R.id.date);
        time=view.findViewById(R.id.time);
        distance=view.findViewById(R.id.distance);
        randomimage=view.findViewById(R.id.thumbnail);

    }
}
}
