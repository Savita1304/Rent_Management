package com.vapps.room.rental.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vapps.room.rental.Model.Admin;
import com.vapps.room.rental.databinding.CustomlistBinding;
import com.vapps.room.rental.databinding.LayoutBuildingBinding;

import java.util.ArrayList;
import java.util.Random;

public class Building extends RecyclerView.Adapter<Building.ViewHolder> {


    LayoutBuildingBinding binding;
    public ArrayList<Admin> mData;
    public LayoutInflater mInflater;
    public Building.ItemClickListener mClickListener;

    Admin it;

    int height,width;

    public Building(Context context, ArrayList<Admin> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;



        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding  = LayoutBuildingBinding .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        it = mData.get(position);

        holder.name.setText(it.getName());



        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.icon.setColorFilter(color);

        binding.icon.getLayoutParams().width = (int) (width*0.1f);
        binding.icon.getLayoutParams().height = (int) (width*0.08f);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ImageView icon;

        LinearLayout main;


        ViewHolder(LayoutBuildingBinding itemView) {
            super(itemView.getRoot());

            name = itemView.bname;
            icon = itemView.icon;
            main = itemView.main;

            main.setOnClickListener(this);




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
    public void setClickListener(Building.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
