package com.vapps.room.rental.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;

import com.vapps.room.rental.Adapters.CustomAdapter3;
import com.vapps.room.rental.Model.Admob;
import com.vapps.room.rental.Model.PdfData;
import com.vapps.room.rental.databinding.ActivityReportsBinding;

import java.io.File;
import java.util.ArrayList;

public class Reports extends AppCompatActivity {


    ActivityReportsBinding binding;
    String id,name;
    int year;

    CustomAdapter3 adapter;
    int height,width;

    ArrayList<PdfData> pdf = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;


        Admob.adMob1(getApplicationContext(),binding.adView);


        String val = getIntent().getStringExtra("show");
        if (val != null){
            Admob.LoadInterestitialAd(this);
        }


        binding.back.getLayoutParams().width = (int) (height*0.045f);
        binding.back.getLayoutParams().height = (int) (height*0.045f);

        id = getIntent().getStringExtra("id");
        year = getIntent().getIntExtra("year",0);
        name = getIntent().getStringExtra("name");



        binding.report.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.report.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Room Rental");


        Search_Dir(file);




        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                finish();
            }
        });

    }



    public void Search_Dir(File dir) {
        String pdfPattern = ".pdf";
        Boolean temp = false;

        File FileList[] = dir.listFiles();

        //Toast.makeText(getApplicationContext(),"size :"+fileList().length,Toast.LENGTH_SHORT).show();
      //  Toast.makeText(getApplicationContext(),"size :"+dir.getName(),Toast.LENGTH_SHORT).show();

        if (FileList != null) {

//            binding.l1.setVisibility(View.VISIBLE);
//            binding.l2.setVisibility(View.GONE);
            for (int i = 0; i < FileList.length; i++) {

                /*if (FileList[i].isDirectory()) {
                    Search_Dir(FileList[i]);
                } else {
                    if (FileList[i].getName().endsWith(pdfPattern)){
                        //here you have that file.

                    }
                }*/


                if (FileList[i].getName().endsWith(pdfPattern)){

                    // temp = false;
                    //here you have that file.
                    String name = FileList[i].getName();
                    File path = FileList[i];
                    PdfData pdfData = new PdfData();
                    pdfData.setName(name);
                    pdfData.setUri(path);

                    pdf.add(pdfData);

                }
                else{
                    temp = true;
                }


            }
/*
            if (temp){
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(Reports.this, parentLayout,"Oop's no record found.",3000).setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
            }*/



        }
        else{
            /*binding.l2.setVisibility(View.VISIBLE);
            binding.l1.setVisibility(View.GONE);*/



        }

        //load list here

        adapter = new CustomAdapter3(this,pdf);


        binding.report.setAdapter(adapter);
     //   adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
        finish();
    }
}