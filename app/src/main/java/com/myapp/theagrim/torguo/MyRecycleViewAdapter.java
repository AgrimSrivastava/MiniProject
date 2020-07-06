package com.myapp.theagrim.torguo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {

    private Context context;
    //Declare an ArrayList
    private ArrayList<Dataset> arrayList;
    private ArrayList<String>  key;
    LatLng source;

    private int arr[] ={R.drawable.foodimage1,R.drawable.foodimage2,R.drawable.foodimage3,R.drawable.foodimage4};

    public MyRecycleViewAdapter(ArrayList<Dataset> arrayList,LatLng source){
        this.arrayList=arrayList;
        this.source=source;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_layout,viewGroup,false);
        context=viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Dataset dataset=arrayList.get(i);
        viewHolder.name.setText(dataset.getName());
        String lat=String.valueOf(dataset.getLattitude());
        String lo=String.valueOf(dataset.getLongitude());
        String s="Latitude- "+lat.substring(0,Math.min(4,lat.length()-1))+" Longitude- "+lo.substring(0,Math.min(4,lo.length()-1));
        viewHolder.coordinates.setText(s);
        viewHolder.date.setText(dataset.getDate());
        viewHolder.time.setText(dataset.getTime());
        String str=String.valueOf(source.distanceTo(new LatLng(dataset.getLattitude(),dataset.getLongitude())));
        str=str.substring(0,Math.min(4,str.length()-1))+"m";
        viewHolder.distance.setText(str);
        viewHolder.randomimage.setImageResource(arr[i%4]);
        final int val=i%4;
        viewHolder.outerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Detail.class);
                intent.putExtra("Key",dataset.getKey());
                intent.putExtra("Image",String.valueOf(val));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
//        Log.d("Torguo",String.valueOf(arrayList.size()));
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
