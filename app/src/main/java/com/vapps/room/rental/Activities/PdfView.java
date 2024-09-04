package com.vapps.room.rental.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.vapps.room.rental.Model.Admob;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.ActivityPdfViewBinding;

import java.io.File;

public class PdfView extends AppCompatActivity {


    ActivityPdfViewBinding binding;

    File file;

    int height,width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        Admob.adMob1(getApplicationContext(),binding.adView);



        binding.share.getLayoutParams().width = (int) (height*0.045f);
        binding.share.getLayoutParams().height = (int) (height*0.045f);

        binding.back.getLayoutParams().width = (int) (height*0.045f);
        binding.back.getLayoutParams().height = (int) (height*0.045f);

        file =  new File(getIntent().getStringExtra("file"));
        //name = getIntent().getStringExtra("name");

        if (file != null){

            //  binding.name.setText(name);
            showFile(file);
        }


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Reports.class);
                startActivity(intent);
                finish();
            }
        });


        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent();
                Uri uri = FileProvider.getUriForFile(PdfView.this,getPackageName()+".provider",file);

                share.setAction(Intent.ACTION_SEND);
                share.setDataAndType(uri,"application/pdf");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(share);
            }
        });
    }


    public void showFile(File file){


        try {
            binding.pdfView.fromFile(file)
                    .pages(0, 2, 1, 3, 3, 3)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true)
                    .spacing(0)
                    .load();
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Reports.class);
        startActivity(intent);
        finish();
    }
}