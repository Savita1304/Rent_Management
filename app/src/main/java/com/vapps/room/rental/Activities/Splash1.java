package com.vapps.room.rental.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;


import com.vapps.room.rental.R;

import java.util.Random;

public class Splash1 extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    public static int screenSize_category;
    public static int result;
    SharedPreferences prefs;



    LinearLayout l1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash1);

        l1 = findViewById(R.id.layout);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        screenSize_category=getResources().getConfiguration().screenLayout& Configuration.SCREENLAYOUT_SIZE_MASK;


        prefs = PreferenceManager.getDefaultSharedPreferences(Splash1.this);





        if (prefs.getInt("color",0) != 0) {
            int value = prefs.getInt("color",0);
            l1.setBackgroundColor(value);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(value);
            }

        }
        else{

        }



        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }







        Random r = new Random();
        int low = 0;
        int high = 8;
        result = r.nextInt(high-low) + low;

        //   Log.e("KUNDALI","RANDOM :"+result);


        int screen_category11=getResources().getConfiguration().screenHeightDp;
        //Log.e("splash", "onCreate:111 "+screen_category11 );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(Splash1.this, Home.class);
                mainIntent.putExtra("splash","splash");
                Splash1.this.startActivity(mainIntent);
                Splash1.this.finish();








            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
