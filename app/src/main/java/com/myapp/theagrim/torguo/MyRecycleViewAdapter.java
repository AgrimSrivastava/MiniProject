package com.myapp.theagrim.torguo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {

    Context context;
    //Declare an ArrayList
    private ArrayList<Dataset> arrayList;
    //make a constructor and initialise our array list with the list passed
    //create a model class for receiving data

    public MyRecycleViewAdapter(ArrayList<Dataset> arrayList){
        this.arrayList=arrayList;
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
        viewHolder.textView1.setText(String.valueOf(dataset.getLattitude()));
        viewHolder.textView2.setText(String.valueOf(dataset.getLongitude()));
        final int pos=i;
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,pos+"",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context,DirectiononMap.class);
                intent.putExtra("Lattitude",String.valueOf(dataset.getLattitude()));
                intent.putExtra("Longitude",String.valueOf(dataset.getLongitude()));
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

        TextView textView1;
        TextView textView2;
        LinearLayout linearLayout;
        public ViewHolder(View view){
            super(view);
            textView1=view.findViewById(R.id.lattitude);
            textView2=view.findViewById(R.id.longitude);
            linearLayout=view.findViewById(R.id.outerlayout);
        }
    }

}
