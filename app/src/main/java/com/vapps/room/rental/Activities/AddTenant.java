package com.vapps.room.rental.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vapps.room.rental.Database.AdminDatabase;
import com.vapps.room.rental.Database.PaymentDatabase;
import com.vapps.room.rental.Model.Admob;
import com.vapps.room.rental.Model.TenantRecord;
import com.vapps.room.rental.Model.TenantRentRecord;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.ActivityAddTenantBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class AddTenant extends AppCompatActivity {

    ActivityAddTenantBinding binding;

    private static final int GALLERY_REQUEST = 101;
    private static final int CAMERA_REQUEST = 102;
    Uri selectedImage=null;
    int width,height;
    Bitmap photo;
    ProgressDialog progressDialog;
    boolean changing;
    Calendar calendar;


    SharedPreferences prefs ;
    int theme;
    String builingName;
    String room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTenantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Admob.adMob1(getApplicationContext(),binding.adView);

        builingName = getIntent().getStringExtra("name");
        room = getIntent().getStringExtra("room");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        binding.userpic.getLayoutParams().width = (int) (width*0.12f);
        binding.userpic.getLayoutParams().height = (int) (width*0.12f);


        binding.back.getLayoutParams().width = (int) (height*0.045f);
        binding.back.getLayoutParams().height = (int) (height*0.045f);


        int imgwidth2=height*17/100;
        int imgheight2=height*19/100;
        binding.mainbg.getLayoutParams().width=imgwidth2;
        binding.mainbg.getLayoutParams().height=imgheight2;



        binding.middleinner.setVisibility(View.GONE);
        binding.layelc.setVisibility(View.GONE);
        binding.laywater.setVisibility(View.GONE);




        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        if (prefs.getInt("color",0) != 0) {
            int value = prefs.getInt("color",0);
            binding.snap.setBackgroundColor(value);
            binding.add.setBackgroundColor(value);
            binding.Tdate.setColorFilter(value);


        }



        //get rent from room table for this flat/house
        AdminDatabase db = new AdminDatabase(getApplicationContext());
        float rent = db.getrent(room,builingName);

        binding.amount.setText(rent+"");



      /*  LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                0.45f
        );
        binding.middle.setLayoutParams(param);*/

        // binding.middle.getLayoutParams().height = (int) (height*0.25f);


        binding.elc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.middleinner.setVisibility(View.VISIBLE);
                    binding.layelc.setVisibility(View.VISIBLE);


                }
                else{
                    if (!binding.water.isChecked()){
                        binding.middleinner.setVisibility(View.GONE);
                        binding.layelc.setVisibility(View.GONE);
                        binding.eamount.setText("");
                        binding.eamtError.setError(null);


                    }
                    else{
                        binding.layelc.setVisibility(View.GONE);
                        binding.eamount.setText("");
                        binding.eamtError.setError(null);
                    }



                }
            }
        });

        binding.water.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.middleinner.setVisibility(View.VISIBLE);

                    binding.laywater.setVisibility(View.VISIBLE);

                }
                else{

                    if (!binding.elc.isChecked()){
                        binding.middleinner.setVisibility(View.GONE);
                        binding.laywater.setVisibility(View.GONE);
                        binding.wamount.setText("");
                        binding.wamtError.setError(null);

                    }
                    else{
                        binding.laywater.setVisibility(View.GONE);
                        binding.wamount.setText("");
                        binding.wamtError.setError(null);
                    }



                }
            }
        });


        binding.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 121);
                    }
                    else{
                        showBottomSheetDialog();
                    }

                }
                else{
                    //createDestination();
                    showBottomSheetDialog();
                }


            }
        });


        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if( binding.name.getText().length()>0)
                {
                    binding.nameError.setError(null);
                }
                else {
                    binding.nameError.setError("Name Required");
                }
            }
        });


        binding.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!changing && (binding.amount.getText().toString().startsWith("0") || binding.amount.getText().toString().startsWith(".") ) ){
                    changing = true;
                    binding.amount.setText(binding.amount.getText().toString().replace(".", ""));
                    binding.amount.setText(binding.amount.getText().toString().replace("0", ""));
                }
                changing = false;
                if( binding.amount.getText().length()>0)
                {
                    binding.amtError.setError(null);
                }
                else {
                    binding.amtError.setError("Rent Required");
                }
            }
        });

        binding.number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if( binding.number.getText().length()>0)
                {
                    binding.numError.setError(null);
                }
                else {
                    binding.numError.setError("Mobile Required");
                }
            }
        });


        binding.jdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if( binding.jdate.getText().length()>0)
                {
                    binding.jdateError.setError(null);
                }
                else {
                    binding.jdateError.setError("Date Required");
                }
            }
        });




        binding.eamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!changing && (binding.eamount.getText().toString().startsWith("0") || binding.eamount.getText().toString().startsWith(".") ) ){
                    changing = true;
                    binding.eamount.setText(binding.eamount.getText().toString().replace(".", ""));
                    binding.eamount.setText(binding.eamount.getText().toString().replace("0", ""));
                }
                changing = false;
                if( binding.eamount.getText().length()>0)
                {
                    binding.eamtError.setError(null);
                }
                else {
                    binding.eamtError.setError("Required");
                }
            }
        });
        binding.wamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!changing && (binding.wamount.getText().toString().startsWith("0") || binding.wamount.getText().toString().startsWith(".") ) ){
                    changing = true;
                    binding.wamount.setText(binding.wamount.getText().toString().replace(".", ""));
                    binding.wamount.setText(binding.wamount.getText().toString().replace("0", ""));
                }
                changing = false;
                if( binding.wamount.getText().length()>0)
                {
                    binding.wamtError.setError(null);
                }
                else {
                    binding.wamtError.setError("Required");
                }
            }
        });


        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddata();
            }
        });

        binding.jdate.setFocusable(false);



        binding.Tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupUi();
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTenant.this,BuildingView.class);
                intent.putExtra("name",builingName);
                startActivity(intent);
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddTenant.this,BuildingView.class);
        intent.putExtra("name",builingName);
        startActivity(intent);
        finish();
    }

    public void createDestination() {
        File direct,recording;
        File wallpaperDirectory=null;

        direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Room Rental");

        if (!direct.exists()) {
            try {
                wallpaperDirectory = new File(direct.getAbsolutePath());
                wallpaperDirectory.mkdirs();

            }catch (Exception e){
            }


        }



        File nonamed = new File(direct,".nomedia");
        try {
            nonamed.createNewFile();
        }catch (IOException e){

        }






    }


    private void setupUi() {


        if (prefs.getInt("color",0) != 0) {
            int value = prefs.getInt("color", 0);


            String hexColor = String.format("#%06X", (0xFFFFFF & value));
            if (hexColor.equals("#2F4FE3")){
                theme =  R.style.D5;
            }
            else if (hexColor.equals("#F44236")){
                theme =  R.style.D1;
            }
            else if (hexColor.equals("#EA1E63")){
                theme =  R.style.D2;
            }
            else if (hexColor.equals("#9A28B1")){
                theme =  R.style.D3;
            }
            else if (hexColor.equals("#683AB7")){
                theme =  R.style.D4;
            }

            else if (hexColor.equals("#2295F0")){
                theme =  R.style.D6;
            }
            else if (hexColor.equals("#04A8F5")){
                theme =  R.style.D7;
            }
            else if (hexColor.equals("#00BED2")){
                theme =  R.style.D8;
            }
            else if (hexColor.equals("#009788")){
                theme =  R.style.D9;
            }
            else if (hexColor.equals("#4CB050")){
                theme =  R.style.D10;
            }
            else if (hexColor.equals("#FF9700")){
                theme =  R.style.D11;
            }
            else if (hexColor.equals("#FFC000")){
                theme =  R.style.D12;
            }
            else if (hexColor.equals("#D2E41D")){
                theme =  R.style.D13;
            }
            else if (hexColor.equals("#FE5722")){
                theme =  R.style.D14;
            }
            else if (hexColor.equals("#795547")){
                theme =  R.style.D15;
            }



        }
        else{

            theme = R.style.D6;

        }
        // Get calendar instance
        calendar= Calendar.getInstance();

        // Get current time
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Create listener
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                int m = month+1;



                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                updateLabel();

              /*  SimpleDateFormat month_date = new SimpleDateFormat("MMM");
                 month_name = month_date.format(month);
                // binding.date.setText(String.valueOf(day+"-"+month_name+"-"+year));
                 year1 = year;*/
                // Show Toast after selection
                //Toast.makeText(Add.this, String.format("Selected: %s.%s.%s", day, month, year), Toast.LENGTH_SHORT).show();
            }
        };





        // Create dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddTenant.this,theme,
                listener,
                currentYear,
                currentMonth,
                currentDay
        );

        datePickerDialog.getDatePicker().setMinDate(getFirstDayOfMonth(calendar).getTimeInMillis());

        datePickerDialog.getDatePicker().setMaxDate(getLastDayOfMonth(calendar).getTimeInMillis());

        datePickerDialog.show();



        Button btn_ok = datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btn_ok .setBackgroundColor(0); //add your color
        btn_ok.setTextColor(Color.BLACK);

        Button btn_cancel = datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        btn_cancel .setBackgroundColor(0); //add your color
        btn_cancel.setTextColor(Color.BLACK);
    }

    private Calendar getFirstDayOfMonth(Calendar calendar) {
        Calendar firstDay = Calendar.getInstance();
        firstDay.setTimeInMillis(calendar.getTimeInMillis());
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        return firstDay;
    }

    private Calendar getLastDayOfMonth(Calendar calendar) {
        Calendar lastDay = Calendar.getInstance();
        lastDay.setTimeInMillis(calendar.getTimeInMillis());
        lastDay.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return lastDay;
    }
    private void updateLabel(){
        String myFormat="dd-MMM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        String[] dd = dateFormat.format(calendar.getTime()).split("-");

//        month_name = dd[1];
//        year1 = Integer.valueOf(dd[2]);
        binding.jdate.setText(dateFormat.format(calendar.getTime()));


    }


    public void adddata(){

        if (binding.name.getText().length() == 0){
            binding.nameError.setError("Name Required");
        }
        else if (binding.number.getText().length() == 0){
            binding.numError.setError("Number Required");
        }
        else if (binding.amount.getText().length() == 0){
            binding.amtError.setError("Rent Required");
        }

        else if (binding.jdate.getText().length() == 0){
            binding.jdateError.setError("Date Required");
        }
        else if (binding.elc.isChecked() && binding.eamount.getText().length() == 0){

            binding.eamtError.setError("Required");

        }

        else if (binding.water.isChecked() && binding.wamount.getText().length() == 0){

            binding.wamtError.setError("Required");

        }

        else {

            progressDialog = new ProgressDialog(AddTenant.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Adding Tenant....");
            progressDialog.show();

            String id = UUID.randomUUID().toString();
            String name = binding.name.getText().toString().trim();
            String num = binding.number.getText().toString().trim();
            float amt = Float.parseFloat(binding.amount.getText().toString().trim());
            String sdate = binding.jdate.getText().toString();
            String add = binding.address.getText().toString();

            String tag = binding.userpic.getTag().toString();

            float famt = 0;
            float eamt= 0;
            float wamt=0;
            if (binding.eamount.getText().length() > 0){
                eamt = Float.parseFloat(binding.eamount.getText().toString().trim());
            }
            if (binding.wamount.getText().length() > 0){
                wamt = Float.parseFloat(binding.wamount.getText().toString().trim());
            }



           /* if (binding.elc.isChecked()){
                float eamt = Float.parseFloat(binding.eamount.getText().toString());

                famt = famt+amt+eamt;
            }

            if (binding.water.isChecked()){
                float wamt = Float.parseFloat(binding.wamount.getText().toString());
                famt = famt+wamt;
            }*/


            famt = amt+eamt+wamt;

            if (tag.equals("none")){

                //  selectedImage = Uri.parse("android.resource://"+getActivity().getPackageName()+"/drawable/person");

                photo = BitmapFactory.decodeResource(getResources(),
                        R.drawable.person1);

                // photo = getResizedBitmap(photo,120,120);
            }

            File direct;
            direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Room Rental");


            File file = new File(direct, id+".jpg");
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("EEE","Error :"+e.getLocalizedMessage());
            }





            PaymentDatabase db = new PaymentDatabase(getApplicationContext());
            TenantRecord tr = new TenantRecord();
            tr.setTid(id);
            tr.setTname(name);
            tr.setTmob(num);
            //write image to file before save and get path
            tr.setTimg(file.getAbsolutePath());
            tr.setTrent(famt);
            tr.setTsdate(sdate);
            tr.setTadd(add);
            tr.setRoomno(room);
            tr.setBuildingname(builingName);
            db.addTenant(tr);




            //add data to rent table for current month year
            TenantRentRecord tr1 = new TenantRentRecord();
            tr1.setId(id);
            tr1.setName(name);
            tr1.setDate(sdate);
            tr1.setAmount(famt);

            String[] ar = sdate.split("-");
            String mm = ar[1];
            String yy = ar[2];
            tr1.setMonth(mm);
            tr1.setYear(yy);
            tr1.setPaid(0);
            tr1.setRemaining(famt);
            tr1.setState(0);

            tr1.setRoom(room);
            tr1.setBuilding(builingName);

            db.addTenantRent(tr1);



            //update rent in RoomTable of that room and bulding.

            AdminDatabase db1 = new AdminDatabase(getApplicationContext());
            db1.updateRoomRent(famt,room,builingName);




            binding.name.setText("");
            binding.nameError.setError(null);
            binding.number.setText("");
            binding.numError.setError(null);
            binding.amount.setText("");
            binding.amtError.setError(null);
            binding.jdate.setText("");
            binding.jdateError.setError(null);
            binding.address.setText("");
            binding.elc.setChecked(false);
            binding.water.setChecked(false);
            binding.eamount.setText("");
            binding.eamtError.setError(null);
            binding.wamount.setText("");
            binding.wamtError.setError(null);



            binding.userpic.setImageBitmap(null);
            binding.userpic.getLayoutParams().width = (int) (width*0.12f);
            binding.userpic.getLayoutParams().height = (int) (width*0.12f);
            binding.userpic.setBackgroundResource(R.drawable.camre);
            binding.snap.setVisibility(View.VISIBLE);
            binding.userpic.setTag("none");


            binding.middleinner.setVisibility(View.GONE);
            binding.layelc.setVisibility(View.GONE);
            binding.laywater.setVisibility(View.GONE);
            binding.elc.setChecked(false);
            binding.water.setChecked(false);




            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();


                    //pass intent here

                    Intent intent = new Intent(AddTenant.this,BuildingView.class);
                    intent.putExtra("name",builingName);
                    startActivity(intent);
                    finish();











                }
            },4000);


        }




    }




    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddTenant.this);
        bottomSheetDialog.setContentView(R.layout.bottom);

        LinearLayout main = bottomSheetDialog.findViewById(R.id.layout);
        LinearLayout gal1 = bottomSheetDialog.findViewById(R.id.l1);
        LinearLayout cam1 = bottomSheetDialog.findViewById(R.id.l2);

        ImageView gal = bottomSheetDialog.findViewById(R.id.gal);
        ImageView cam = bottomSheetDialog.findViewById(R.id.camera);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightLcl = (int) (displayMetrics.heightPixels*0.15f);



        main.getLayoutParams().height = heightLcl;
        gal1.getLayoutParams().width = heightLcl;
        gal1.getLayoutParams().height = heightLcl;

        cam1.getLayoutParams().width = heightLcl;
        cam1.getLayoutParams().height = heightLcl;




        if (prefs.getInt("color",0) != 0) {
            int value = prefs.getInt("color",0);
            gal.setColorFilter(value);
            cam.setColorFilter(value);


        }




        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ){
                        requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);

                    }
                    else{
                        //open intent for gallery
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , GALLERY_REQUEST);//one can be replaced with any action code
                    }
                }
                else{
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    }
                    else {
                        //  createNotification();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , GALLERY_REQUEST);//one can be replaced with any action code

                    }
                }



            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);

                    }
                    else{
                        //open intent for gallery

                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    }
                }
                else{
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);

                    }
                    else {
                        //  createNotification();
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });




        bottomSheetDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_REQUEST:
                if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
                    selectedImage = data.getData();
                    // binding.userpic.setColorFilter(0);
                    binding.snap.setVisibility(View.INVISIBLE);
                    int imgwidth2=height*17/100;
                    int imgheight2=height*19/100;
                    binding.userpic.getLayoutParams().width=imgwidth2;
                    binding.userpic.getLayoutParams().height=imgheight2;
                    binding.userpic.setTag("gal");




                    photo = (Bitmap) data.getExtras().get("data");
                    binding.userpic.setImageBitmap(photo);

                    Log.e("URIIMAGE","cam"+selectedImage);
                }
                break;
            case GALLERY_REQUEST:
                if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
                    selectedImage = data.getData();
                    binding.snap.setVisibility(View.INVISIBLE);
                    //binding.userpic.setColorFilter(0);
                    int imgwidth2=height*17/100;
                    int imgheight2=height*19/100;
                    binding.userpic.getLayoutParams().width=imgwidth2;
                    binding.userpic.getLayoutParams().height=imgheight2;
                    //binding.userpic.setImageURI(selectedImage);
                    binding.userpic.setTag("gal");

                    try {
                        photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);
                        binding.userpic.setImageBitmap(photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //create Destination Folder


                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES},1);
                    }
                    else{
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                }
            case 121:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //create Destination Folder
                    createDestination();

                }
        }
    }




    public void reload(){







        binding.name.setText("");
        binding.nameError.setError(null);
        binding.number.setText("");
        binding.numError.setError(null);
        binding.amount.setText("");
        binding.amtError.setError(null);
        binding.jdate.setText("");
        binding.jdateError.setError(null);
        binding.address.setText("");
        binding.elc.setChecked(false);
        binding.water.setChecked(false);
        binding.eamount.setText("");
        binding.eamtError.setError(null);
        binding.wamount.setText("");
        binding.wamtError.setError(null);



        binding.userpic.setImageBitmap(null);
        binding.userpic.getLayoutParams().width = (int) (width*0.12f);
        binding.userpic.getLayoutParams().height = (int) (width*0.12f);
        binding.userpic.setBackgroundResource(R.drawable.camre);
        binding.snap.setVisibility(View.VISIBLE);
        binding.userpic.setTag("none");


        binding.middleinner.setVisibility(View.GONE);
        binding.layelc.setVisibility(View.GONE);
        binding.laywater.setVisibility(View.GONE);
        binding.elc.setChecked(false);
        binding.water.setChecked(false);
    }
}