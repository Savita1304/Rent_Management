package com.vapps.room.rental.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.vapps.room.rental.Activities.PdfView;
import com.vapps.room.rental.Model.PdfData;
import com.vapps.room.rental.databinding.DataBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class CustomAdapter3 extends RecyclerView.Adapter<CustomAdapter3.ViewHolder> {

   DataBinding customBinding;
    public ArrayList<PdfData> mData;
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;

    PdfData it;



    // data is passed into the constructor
    public CustomAdapter3(Context context, ArrayList<PdfData> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        customBinding  = DataBinding .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(customBinding);

    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        it = mData.get(position);
        String name = String.valueOf(it.getName());
      holder.name.setText(name);




        File file1 = it.getUri();



        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.icon.setColorFilter(color);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = file1;
                String Name = name;

                Log.e("FGFG","file :"+file);


        Intent intent = new Intent(mInflater.getContext(), PdfView.class);
        intent.putExtra("file",file.getAbsolutePath());
        intent.putExtra("name",Name);
        mInflater.getContext().startActivity(intent);

            }
        });







    }







    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
     public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView name;
        ImageView icon;

        ViewHolder(DataBinding itemView) {
            super(itemView.getRoot());
            name = itemView.name;
            icon = itemView.icon;





        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
   /* Bitmap getItem(int id) {
        return mData.get(id);
    }*/

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}