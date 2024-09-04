package com.vapps.room.rental.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.vapps.room.rental.Adapters.CustomAdapter;
import com.vapps.room.rental.Database.PaymentDatabase;
import com.vapps.room.rental.Model.Admob;
import com.vapps.room.rental.Model.TenantRecord;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.ActivityAllTenantsBinding;

import java.util.ArrayList;

public class AllTenants extends AppCompatActivity implements CustomAdapter.ItemClickListener{

    ActivityAllTenantsBinding binding;
    int height,width;


    CustomAdapter adapter;
    ArrayList<TenantRecord> tr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllTenantsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Admob.adMob1(getApplicationContext(),binding.adView);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        binding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        binding.back.getLayoutParams().width = (int) (height*0.045f);
        binding.back.getLayoutParams().height = (int) (height*0.045f);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.list.getContext(),1);
        binding.list.addItemDecoration(dividerItemDecoration);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllTenants.this, Home.class);
                startActivity(intent);
                finish();
            }
        });


        load();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AllTenants.this, Home.class);
        startActivity(intent);
        finish();
    }

    public void load(){
        PaymentDatabase db = new PaymentDatabase(getApplicationContext());
        tr = db.getAllTenant();
        if (tr.size() == 0){


        //    Toast.makeText(getApplicationContext(),"No record Exists",Toast.LENGTH_SHORT).show();

            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout,"No record found please add tenant",Snackbar.LENGTH_LONG).show();

        }
        else{




            adapter = new CustomAdapter(AllTenants.this,tr);
            binding.list.setAdapter(adapter);
            adapter.setClickListener(this);

        }
    }

    @Override
    public void onItemClick(View view, int position) {

        String ldate = tr.get(position).getTedate();
        if (ldate == null || ldate.isEmpty()) {

            if (view.getId() == R.id.history) {
                String id = tr.get(position).getTid();
                String nm = tr.get(position).getTname();
                float amt = tr.get(position).getTrent();
                String rname = tr.get(position).getBuildingname();

                String room = tr.get(position).getRoomno();

                String path = tr.get(position).getTimg();

                //


                Intent intent = new Intent(getApplicationContext(), History.class);
                intent.putExtra("name", rname);
                intent.putExtra("data", id);
                intent.putExtra("uname", nm);
                intent.putExtra("rent", amt + "");
                intent.putExtra("all", "load");
                intent.putExtra("temp", room);
                intent.putExtra("path", path);
                startActivity(intent);
                finish();
            } else if (view.getId() == R.id.call) {
                String mobile = tr.get(position).getTmob();
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + mobile));
                startActivity(intent);
            } else if (view.getId() == R.id.main) {
                String id = tr.get(position).getTid();
                String rname = tr.get(position).getBuildingname();
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("data", id);
                intent.putExtra("name", rname);
                intent.putExtra("all", "alltenants");
                startActivity(intent);
                finish();
            }

        }
        else {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout,"Tenant has left",Snackbar.LENGTH_LONG).show();
        }
    }


}