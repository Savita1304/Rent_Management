package com.vapps.room.rental.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.vapps.room.rental.Model.Section;
import com.vapps.room.rental.databinding.SectionDataBinding;

import java.util.ArrayList;


public class CustomAdapter1 extends RecyclerView.Adapter<CustomAdapter1.ViewHolder>  {



   SectionDataBinding customBinding;
    public static ArrayList<Section> mData;
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;


    Section it;







    // data is passed into the constructor
    public CustomAdapter1(Context context, ArrayList<Section> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     /*   View view = mInflater.inflate(R.layout.layout, parent, false);
        return new ViewHolder(view);*/


        customBinding  = SectionDataBinding .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(customBinding);

    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        it = mData.get(position);



        String pdate = it.getTitle().replace("-"," ");

        holder.section_title.setText(pdate);

        //load child recycler view here
        ChildAdapter childAdapter = new ChildAdapter(mInflater.getContext(), it.getItems(),position);
        holder.child.setAdapter(childAdapter);
        holder.child.setLayoutManager(new LinearLayoutManager(mInflater.getContext(), LinearLayoutManager.VERTICAL, false));
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.child.getContext(),1);
//        holder.child.addItemDecoration(dividerItemDecoration);


      //  holder.child.addItemDecoration(new DividerItemDecoration(mInflater.getContext(),DividerItemDecoration.VERTICAL));


       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPosition = new ClickPosition() {
                    @Override
                    public int getPosition() {
                        return position;
                    }
                };
            }
        });

*/


    }




    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }




    // stores and recycles views as they are scrolled off screen
     public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView section_title;
        RecyclerView child;


        ViewHolder(SectionDataBinding itemView) {
            super(itemView.getRoot());
            section_title = itemView.headerMonth;
            child = itemView.childrecycler;

            //section_title.setOnClickListener(this);
            child.setOnClickListener(this);






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