package com.vapps.room.rental.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.vapps.room.rental.Model.HistoryData;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.SectionItemBinding;

import java.util.ArrayList;


public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder>  {

    SectionItemBinding customBinding;
    public ArrayList<HistoryData> mData;
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;


    int clickPosition;




    HistoryData it;

    SharedPreferences prefs;
    int value;
    String strlan = "";

    // data is passed into the constructor
    public ChildAdapter(Context context, ArrayList<HistoryData> data, int pos) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

        this.clickPosition = pos;

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Log.e("SDSD","Size :"+mData.size());



        if (prefs.getInt("color",0) != 0) {
             value = prefs.getInt("color",0);




        }
        else{
           value = mInflater.getContext().getResources().getColor(R.color.purple_200);

        }

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     /*   View view = mInflater.inflate(R.layout.layout, parent, false);
        return new ViewHolder(view);*/


        customBinding  = SectionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(customBinding);

    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        it = mData.get(position);


        //set Tenant Rent data here

        holder.txtamt.setText(it.getAmt()+"/-");
        holder.txttype.setText(it.getType());
        holder.txtdate.setText(it.getDate());




    }










    // total number of rows
    @Override
    public int getItemCount() {


        return mData.size();
    }



    // stores and recycles views as they are scrolled off screen
     public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtamt,txttype,txtdate;



        ViewHolder(SectionItemBinding itemView) {
            super(itemView.getRoot());
            txtamt = itemView.txtamt;
            txttype = itemView.txttype;
            txtdate = itemView.txtdate;





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