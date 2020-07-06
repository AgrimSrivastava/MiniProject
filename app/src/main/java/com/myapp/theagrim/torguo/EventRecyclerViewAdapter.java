package com.myapp.theagrim.torguo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>{


    private Context context;
    //Declare an ArrayList
    private ArrayList<Dataset> arrayList;
    private ArrayList<String>  key;


    private int arr[] ={R.drawable.foodimage1,R.drawable.foodimage2,R.drawable.foodimage3,R.drawable.foodimage4};

    public EventRecyclerViewAdapter(ArrayList<Dataset> arrayList){
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_layout,viewGroup,false);
        context=viewGroup.getContext();
        return new EventRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        final Dataset dataset=arrayList.get(i);
        viewHolder.name.setText(dataset.getName());
        String s=String.valueOf(dataset.getLattitude());
        String s1=String.valueOf(dataset.getLongitude());
        String s2="Latitude- "+s.substring(0,Math.min(4,s.length()-1))+" Longitude- "+s1.substring(0,Math.min(4,s1.length()-1));
        viewHolder.coordinates.setText(s2);
        viewHolder.date.setText(dataset.getDate());
        viewHolder.time.setText(dataset.getTime());
        String ss="0 KM";
        viewHolder.distance.setText(ss);
        viewHolder.randomimage.setImageResource(arr[i%4]);
        final int val=i%4;
        viewHolder.outerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EventDetails.class);
                intent.putExtra("Key",dataset.getKey());
                intent.putExtra("Image",String.valueOf(val));
                context.startActivity(intent);
            }
        });
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
        RelativeLayout outerlayout;



        public ViewHolder(View view){
            super(view);
            name=view.findViewById(R.id.name);
            coordinates=view.findViewById(R.id.coordinates);
            date=view.findViewById(R.id.date);
            time=view.findViewById(R.id.time);
            distance=view.findViewById(R.id.distance);
            randomimage=view.findViewById(R.id.thumbnail);
            outerlayout=view.findViewById(R.id.outerlayout);

        }
    }
}
