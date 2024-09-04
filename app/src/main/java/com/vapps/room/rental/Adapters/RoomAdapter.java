package com.vapps.room.rental.Adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.vapps.room.rental.Database.PaymentDatabase;
import com.vapps.room.rental.Model.Room;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.LayoutBuildingBinding;
import com.vapps.room.rental.databinding.RoomsBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {


     RoomsBinding binding;
    public ArrayList<Room> mData;
    public LayoutInflater mInflater;
    public RoomAdapter.ItemClickListener mClickListener;

    Room it;
    PaymentDatabase db ;
    String mm,yy;

    int height,width;
    public RoomAdapter(Context context, ArrayList<Room> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

        db = new PaymentDatabase(mInflater.getContext());
        String dd = getDate();
        String [] ar = dd.split("-");

         mm = ar[1];
         yy = ar[2];

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;





    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding  = RoomsBinding .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        it = mData.get(position);

        String building = mData.get(position).getBuildingName();

        float rent = mData.get(position).getRent();

        String roomno = it.getRoomNo();
        int book = it.getBook();
        holder.main.getLayoutParams().height = (int) (height*0.15);

        holder.icon.getLayoutParams().width = (int) (width*0.12f);
        holder.icon.getLayoutParams().height = (int) (width*0.12f);


        holder.icon1.getLayoutParams().width = (int) (width*0.10f);
        holder.icon1.getLayoutParams().height = (int) (width*0.10f);
        if (book == 0){

            holder.icon1.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);


//            holder.icon.setVisibility(View.VISIBLE);
//            holder.icon1.setVisibility(View.GONE);
            //green
            holder.icon.setColorFilter(mInflater.getContext().getResources().getColor(R.color.green));
        }
        else{

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.name.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.icon1);

            String path = db.getTenantImagePath1(roomno,building);
            if (path != null){

                Bitmap bitmap = getBitmap(path);
                holder.icon1.setVisibility(View.VISIBLE);
                holder.icon.setVisibility(View.GONE);
                holder.icon1.setImageBitmap(bitmap);

            }

            //red
            holder.icon.setColorFilter(mInflater.getContext().getResources().getColor(R.color.red));
        }
        holder.name.setText(roomno);

        Log.e("DFDF","TR :"+mm+"/"+yy+"/"+roomno+"/"+ it.getBuildingName());

        float rem = db.getRentRemaining(mm,yy,roomno, it.getBuildingName());

        if (rem != 0){





            GradientDrawable backgroundGradient = (GradientDrawable)holder.rem.getBackground();
            backgroundGradient.setColor(mInflater.getContext().getResources().getColor(R.color.red));




            holder.rem.setVisibility(View.VISIBLE);
            holder.rem.setText("Due "+rem+"/-");



            Animation animBounce = AnimationUtils.loadAnimation(mInflater.getContext(),
                    R.anim.bounce);

            holder.rem.startAnimation(animBounce);
        }
        else if (rent != 0){






            GradientDrawable backgroundGradient = (GradientDrawable)holder.rem.getBackground();



            holder.rem.setVisibility(View.VISIBLE);
           // holder.rem.setBackgroundColor(mInflater.getContext().getResources().getColor(R.color.green));

            if (rem == 0 && book != 0){
                holder.rem.setText("Paid");
                backgroundGradient.setColor(mInflater.getContext().getResources().getColor(R.color.gray));
            }
            else{
                holder.icon.setColorFilter(mInflater.getContext().getResources().getColor(R.color.gray));
                backgroundGradient.setColor(mInflater.getContext().getResources().getColor(R.color.green));
                holder.rem.setText(rent+"/-");
            }




        }


    }
    public Bitmap getBitmap(String path){
        Bitmap bitmap = null;

        try {


            Uri sr = FileProvider.getUriForFile(mInflater.getContext(), mInflater.getContext().getPackageName()+".provider", new File(path));

            bitmap = MediaStore.Images.Media.getBitmap( mInflater.getContext().getContentResolver(), sr);



        } catch (IOException e) {
            //  throw new RuntimeException(e);
        }




        return bitmap;
    }
    public String getDate(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        return formattedDate;
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name,rem;
        ImageView icon,icon1;
        LinearLayout main;

        RelativeLayout rel;


        ViewHolder(RoomsBinding itemView) {
            super(itemView.getRoot());

            name = itemView.rname;
            icon = itemView.icon;
            rem = itemView.rem;
            main = itemView.main;
            icon1 = itemView.icon1;
            rel = itemView.rel;
           icon1.setOnClickListener(this);
            icon.setOnClickListener(this);




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
    public void setClickListener(RoomAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
