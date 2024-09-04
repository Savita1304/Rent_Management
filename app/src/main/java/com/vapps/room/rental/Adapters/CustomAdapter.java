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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.vapps.room.rental.Model.BlurDrawable;
import com.vapps.room.rental.Model.TenantRecord;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.CustomlistBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

   CustomlistBinding customBinding;
    public ArrayList<TenantRecord> mData;
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;

    TenantRecord it;
    int height,width;

    SharedPreferences prefs ;

    // data is passed into the constructor
    public CustomAdapter(Context context, ArrayList<TenantRecord> data) {
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


        customBinding  = CustomlistBinding .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(customBinding);

    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       /* if (position == mData.size()-1){
            customBinding.p1.setBackgroundResource(R.drawable.borderi4);
            customBinding.p3.setBackgroundResource(R.drawable.borderi5);
        }*/
        it = mData.get(position);







        String rent = String.valueOf(it.getTrent());
        holder.rent.setText(rent+"/-");

        String name = it.getTname();
        holder.name.setText(name);

        String imgPath = it.getTimg();


        holder.icon.getLayoutParams().width = (int) (width*0.12f);
        holder.icon.getLayoutParams().height = (int) (width*0.12f);

        holder.l5.getLayoutParams().height = (int) (width*0.20f);

        if (imgPath != null){
            Bitmap photo = getBitmap(imgPath);
            holder.icon.setImageBitmap(photo);

        }



        String ldate = it.getTedate();
        if (ldate == null || ldate.isEmpty()){
            String date = it.getTsdate();
            holder.date.setText(date);
        }
        else{
            holder.l5.setVisibility(View.VISIBLE);
            String date = it.getTedate();
            holder.date.setText(date+" (left)");
            holder.date.setTextColor(mInflater.getContext().getResources().getColor(R.color.red));
//           holder.sec.setBackgroundColor(mInflater.getContext().getResources().getColor(R.color.gray2));

         //   holder.l5.setBackgroundColor(mInflater.getContext().getResources().getColor(R.color.gray2));

            holder.name.setTextColor(mInflater.getContext().getResources().getColor(R.color.gray2));
            holder.date.setTextColor(mInflater.getContext().getResources().getColor(R.color.gray2));
            holder.rent.setTextColor(mInflater.getContext().getResources().getColor(R.color.gray2));
            holder.call.setVisibility(View.GONE);
            holder.history.setVisibility(View.GONE);


            BlurDrawable blurDrawable = new BlurDrawable(holder.sec, 30);


            holder.l5.setBackground(blurDrawable);

        }


        if (prefs.getInt("color",0) != 0) {
            int value = prefs.getInt("color",0);

            holder.call.setColorFilter(value);
            holder.history.setColorFilter(value);





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


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
     public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView rent;
        TextView date;
        ImageView icon,call,history;
        LinearLayout main,sec,l5;

        ViewHolder(CustomlistBinding itemView) {
            super(itemView.getRoot());
            rent = itemView.rent;
            name = itemView.name;
            date = itemView.date;
            icon = itemView.icon;
            call = itemView.call;
            history = itemView.history;
            main = itemView.main;
            sec = itemView.l2;
            l5 = itemView.l5;
            history.setOnClickListener(this);
            call.setOnClickListener(this);
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
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}