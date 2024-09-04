package com.vapps.room.rental.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vapps.room.rental.Model.TenantRentRecord;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.CustomlistBinding;
import com.vapps.room.rental.databinding.PaymentBinding;

import java.util.ArrayList;


public class CustomRentAdapter extends RecyclerView.Adapter<CustomRentAdapter.ViewHolder> {

   PaymentBinding customBinding;
    public ArrayList<TenantRentRecord> mData;
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;

    TenantRentRecord it;
    int height,width;

    SharedPreferences prefs ;


    // data is passed into the constructor
    public CustomRentAdapter(Context context, ArrayList<TenantRentRecord> data) {
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


        customBinding  = PaymentBinding .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(customBinding);

    }

    // binds the data to the TextView in each row

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        it = mData.get(position);



            float rent = it.getAmount();
            float rentpaid = it.getPaid();

            float ramt = it.getRemaining();
            //check here after full payment done
            if (ramt > 0) {
                //float famt = ramt+rent;
                holder.rent.setText(ramt + "/-");
            } else {
                if (rentpaid == 0) {
                    holder.rent.setText(rent + "/-");
                } else {
                    holder.rent.setText(rentpaid + "/-");
                }

            }


            String name = it.getName();
            holder.name.setText(name);

            int state = it.getState();

            Log.e("STST", "state :" + state);
            if (state == 0) {
                holder.state.setClickable(true);
                holder.state.setChecked(false);
                // holder.edit.setTextColor(mInflater.getContext().getColor(R.color.blue));
                if (prefs.getInt("color", 0) != 0) {
                    int value = prefs.getInt("color", 0);
                    holder.edit.setColorFilter(value);
                    holder.state.setButtonTintList(ColorStateList.valueOf(value));

                }
//            holder.edit.setColorFilter(mInflater.getContext().getColor(R.color.blue));
                holder.rent.setTextColor(mInflater.getContext().getColor(R.color.red));
                holder.edit.setClickable(true);
            } else {

                holder.state.setClickable(false);
                holder.state.setChecked(true);
                //  holder.edit.setTextColor(mInflater.getContext().getColor(R.color.gray));
                holder.edit.setColorFilter(mInflater.getContext().getColor(R.color.gray));
                holder.rent.setTextColor(mInflater.getContext().getColor(R.color.green));
                holder.edit.setClickable(false);
                holder.state.setButtonTintList(ColorStateList.valueOf(mInflater.getContext().getColor(R.color.gray)));
            }









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
        TextView rent;
//        TextView edit;

        ImageView edit;
        CheckBox state;
        LinearLayout main;


        ViewHolder(PaymentBinding itemView) {
            super(itemView.getRoot());
            rent = itemView.amt;
            name = itemView.name;
            edit = itemView.edit;
            state = itemView.state;
            main = itemView.main;
            state.setOnClickListener(this);
            main.setOnClickListener(this);
            edit.setOnClickListener(this);




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