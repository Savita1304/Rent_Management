package com.vapps.room.rental.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.vapps.room.rental.Database.PaymentDatabase;
import com.vapps.room.rental.Model.TenantRentRecord;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.Customlist1Binding;
import com.vapps.room.rental.databinding.PaymentBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.ViewHolder> {

   Customlist1Binding customBinding;
    public ArrayList<TenantRentRecord> mData;
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;

    TenantRentRecord it;
    int height,width;

    SharedPreferences prefs ;


    // data is passed into the constructor
    public CustomAdapter2(Context context, ArrayList<TenantRentRecord> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        prefs = PreferenceManager.getDefaultSharedPreferences(mInflater.getContext());
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     /*   View view = mInflater.inflate(R.layout.layout, parent, false);
        return new ViewHolder(view);*/


        customBinding  = Customlist1Binding .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(customBinding);

    }

    // binds the data to the TextView in each row

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        it = mData.get(position);


        float ramt = it.getRemaining();

        holder.rent.setText(ramt+"/-");


        String name = it.getName();
        holder.name.setText(name);

        String house = it.getBuilding();
        String room = it.getRoom();

        holder.house.setText(house+"("+room+")");

        String id = it.getId();
        String imgPath = new PaymentDatabase(mInflater.getContext()).getTenantImagePath(id);


        holder.icon.getLayoutParams().width = (int) (width*0.10f);
        holder.icon.getLayoutParams().height = (int) (width*0.10f);

        if (imgPath != null){
            Bitmap photo = getBitmap(imgPath);
            holder.icon.setImageBitmap(photo);

        }





    }



    public Bitmap getBitmap(String path){
        Bitmap bitmap = null;

        try {


            Uri sr = FileProvider.getUriForFile(((AppCompatActivity) mInflater.getContext()), ((AppCompatActivity) mInflater.getContext()).getPackageName()+".provider", new File(path));

            bitmap = MediaStore.Images.Media.getBitmap( ((AppCompatActivity) mInflater.getContext()).getContentResolver(), sr);



        } catch (IOException e) {
            //  throw new RuntimeException(e);
        }




        return bitmap;
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
     public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView rent,house;
//        TextView edit;

        ImageView icon,call;



        ViewHolder(Customlist1Binding itemView) {
            super(itemView.getRoot());
            rent = itemView.rent;
            name = itemView.name;
            icon = itemView.icon;
            house = itemView.house;
            call = itemView.call;

            call.setOnClickListener(this);






        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick1(view, getAdapterPosition());
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
        void onItemClick1(View view, int position);
    }
}