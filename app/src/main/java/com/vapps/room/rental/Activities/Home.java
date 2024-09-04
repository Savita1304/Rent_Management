package com.vapps.room.rental.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.vapps.room.rental.Adapters.Building;
import com.vapps.room.rental.Adapters.CustomAdapter2;
import com.vapps.room.rental.Database.AdminDatabase;
import com.vapps.room.rental.Database.PaymentDatabase;
import com.vapps.room.rental.Model.Admin;
import com.vapps.room.rental.Model.Admob;
import com.vapps.room.rental.Model.Room;
import com.vapps.room.rental.Model.TenantRentRecord;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.ActivityHomeNewBinding;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;


public class Home extends AppCompatActivity implements Building.ItemClickListener,CustomAdapter2.ItemClickListener {

    private ConsentInformation consentInformation;
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);

    ActivityHomeNewBinding binding;

    Building adapter;
    int height,width;
    Boolean ch=false;

    String ldate;

    ArrayList<Admin> bnames;
    String permisions[] = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE};
    String permisions1[] = {Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE};


    public void createCMP(){








        // Set tag for under age of consent. false means users are not under age
        // of consent.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            this,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.w(TAG, String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }

                                // Consent has been gathered.
                                if (consentInformation.canRequestAds()) {
                                    initializeMobileAdsSdk();
                                }
                            }
                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }
    }

    private void initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this);

        // TODO: Request an ad.
        // InterstitialAd.load(...);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        createCMP();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requestPermissions(permisions1,101);
        }
        else{

            requestPermissions(permisions,101);
        }

        Admob.adMob1(getApplicationContext(),binding.adView);
       // load();

        showBottom();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;
        binding.list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.list.getContext(),1);
        binding.list.addItemDecoration(dividerItemDecoration);



        binding.logo.getLayoutParams().width = (int) (width*0.1f);
        binding.logo.getLayoutParams().height = (int) (width*0.1f);

        binding.building.getLayoutParams().width = (int) (width*0.1f);
        binding.building.getLayoutParams().height = (int) (width*0.1f);


        binding.notification.getLayoutParams().width = (int) (width*0.1f);
        binding.notification.getLayoutParams().height = (int) (width*0.1f);



        binding.tenants.getLayoutParams().width = (int) (width*0.1f);
        binding.tenants.getLayoutParams().height = (int) (width*0.1f);

        binding.reports.getLayoutParams().width = (int) (width*0.1f);
        binding.reports.getLayoutParams().height = (int) (width*0.1f);

        binding.pending.getLayoutParams().width = (int) (width*0.12f);
        binding.pending.getLayoutParams().height = (int) (width*0.12f);






        //set textsize

    /*    binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(Home.this);
            }
        });
*/





        binding.l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(Home.this);
            }
        });



        binding.l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AllTenants.class);
                startActivity(intent);
                finish();
            }
        });



        binding.l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AllPayment.class);
                startActivity(intent);
                finish();
            }
        });



        binding.l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Reports.class);
                startActivity(intent);
                finish();
            }
        });



        loadnames();


      //  setsize();





    }





    public void load(){

        String dd = getDate();
        String [] ar = dd.split("-");
        String ss = ar[0];
        String mm = ar[1];
        String yy = ar[2];
        PaymentDatabase db = new PaymentDatabase(getApplicationContext());
        ArrayList<TenantRentRecord> list = db.getAllTenantRent11(mm,yy);


        Log.e("SSSD","ER :"+list.size());

        if (list.size() > 0){
          // binding.title.setVisibility(View.VISIBLE);

           String st = getIntent().getStringExtra("splash");
           if ( st != null){
              /* Animation anim = new AlphaAnimation(0.0f, 1.0f);
               anim.setDuration(1500); //You can manage the blinking time with this parameter
               anim.setStartOffset(20);
               anim.setRepeatMode(Animation.REVERSE);
               anim.setRepeatCount(3);
               binding.title.startAnimation(anim);*/
               binding.cartBadge.setVisibility(View.VISIBLE);

               binding.cartBadge.setText(list.size()+"");
           }





        }
        else{
            binding.cartBadge.setVisibility(View.GONE);
            binding.notification.setVisibility(View.GONE);
        }
     /*   binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog1(Home.this,list,mm,yy);
            }
        });*/

        binding.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    binding.cartBadge.setVisibility(View.GONE);
                    showDialog1(Home.this,list,mm,yy);


            }
        });

    }
    public void showDialog1(Activity context, ArrayList<TenantRentRecord> rec,String mm,String yy){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_pop2);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());


        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels*0.65f);


        lp.width = widthLcl;
        lp.height = heightLcl;
        lp.gravity = Gravity.CENTER;




        dialog.getWindow().setAttributes(lp);



        Button dismiss = dialog.findViewById(R.id.dismiss);
        TextView title = dialog.findViewById(R.id.title);
        RecyclerView recyclerView = dialog.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.list.getContext(),1);
        recyclerView.addItemDecoration(dividerItemDecoration);

        CustomAdapter2 customAdapter2 = new CustomAdapter2(context,rec);
        recyclerView.setAdapter(customAdapter2);



        title.setText("Dues "+mm+" "+yy);




        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });







        dialog.show();


    }

    public String getDate(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        return formattedDate;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //create Destination Folder
                    createDestination();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        requestPermissions(permisions1,101);
                    }
                    else{
                        requestPermissions(permisions,101);
                    }
                }
        }
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

    public void loadnames(){
        AdminDatabase db = new AdminDatabase(getApplicationContext());
         bnames = db.getbuilding();
        Log.e("NNN","BName :"+bnames);
        if (bnames.size() > 0){

            adapter = new Building(Home.this,bnames);
            binding.list.setAdapter(adapter);
            adapter.setClickListener(this);
           // adapter.notifyDataSetChanged();

        }
    }


    public void showDialog(Activity context){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_building);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());


        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.8f);
        int heightLcl = (int) (displayMetrics.heightPixels*0.40f);


        lp.width = widthLcl;
        lp.height = heightLcl;
        lp.gravity = Gravity.CENTER;




        dialog.getWindow().setAttributes(lp);


        Button cancel = dialog.findViewById(R.id.cancel);
        Button update = dialog.findViewById(R.id.update);
        TextInputLayout te1 = (TextInputLayout)dialog.findViewById(R.id.ername);
        TextInputEditText name = dialog.findViewById(R.id.name);
        LinearLayout header = dialog.findViewById(R.id.header);





        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if( name.getText().length()< 0)
                {
                    te1.setError("Name Required");

                }
                else {
                    te1.setError(null);
                }


                AdminDatabase db = new AdminDatabase(getApplicationContext());
                ArrayList<Admin> list = db.getbuilding();
                if (list.size() > 0){
                    boolean tmp = false;
                    for (int i=0;i<list.size();i++) {
                        tmp = false;
                        ch = false;
                        String tname = list.get(i).getName();
                        if (name.getText().toString().trim().equalsIgnoreCase(tname)){
                            te1.setError("Name already exists");
                           tmp = true;
                        }

                        if (tmp){
                            ch = true;
                            break;
                        }


                    }//for
                }



            }



        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() > 0 && !ch){

                    dialog.dismiss();
                    //add building name entry to database
                    AdminDatabase db = new AdminDatabase(getApplicationContext());
                    String id = UUID.randomUUID().toString();
                    String bname = name.getText().toString().trim();

                    Admin admin = new Admin();
                    admin.setId(id);
                    admin.setName(bname);
                    admin.setStatus("No");
                    db.addBuilding(admin);
                    loadnames();




                }
                else {
                    name.setText("");
                    te1.setError("Name required");
                }



            }
        });





        dialog.show();


    }



    public void showDialog1(Activity context,String buildingNam,String id){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_room);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());


        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.8f);
        int heightLcl = (int) (displayMetrics.heightPixels*0.40f);


        lp.width = widthLcl;
        lp.height = heightLcl;
        lp.gravity = Gravity.CENTER;




        dialog.getWindow().setAttributes(lp);


        Button cancel = dialog.findViewById(R.id.cancel);
        Button update = dialog.findViewById(R.id.update);
        TextInputLayout te1 = (TextInputLayout)dialog.findViewById(R.id.erroom);
        TextInputEditText room = dialog.findViewById(R.id.room);
        LinearLayout header = dialog.findViewById(R.id.header);

        TextView title = dialog.findViewById(R.id.title);

        title.setText(buildingNam);




        room.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if( room.getText().length()< 0)
                {
                    te1.setError("Room/flat no Required");

                }
                else {
                    te1.setError(null);
                }






            }



        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (room.getText().length() > 0){

                    dialog.dismiss();
                    //add room no entry to database
                    AdminDatabase db = new AdminDatabase(getApplicationContext());

                    int no = Integer.parseInt(room.getText().toString());

                    for (int i = 0; i < no; i++) {
                        String roomno = i+1+"";
                        int book = 0;
                        int interior = 0;
                        String size = "";
                        int furniture = 0;
                        float rent = 0;
                        String name = buildingNam;
                        Room rm = new Room();
                        rm.setRoomNo(roomno);
                        rm.setBook(book);
                        rm.setInterior(interior);
                        rm.setSize(size);
                        rm.setFurniture(furniture);
                        rm.setRent(rent);
                        rm.setBuildingName(name);

                        db.addRoom(rm);


                        //update building table

                        db.updateBuilding(id);


                        Intent intent = new Intent(Home.this,BuildingView.class);
                        intent.putExtra("name",buildingNam);
                        startActivity(intent);
                        finish();

                    }




                }
                else {

                    te1.setError("Room/flat no Required");
                }



            }
        });





        dialog.show();


    }
    @Override
    public void onItemClick(View view, int position) {


        String id = bnames.get(position).getId();

        AdminDatabase adminDatabase = new AdminDatabase(getApplicationContext());
        String status = adminDatabase.getstatus(id);
        if (status.equals("Yes")){

            String buildingName = bnames.get(position).getName();
            //pass intent to grid layout
            Intent intent = new Intent(Home.this,BuildingView.class);
            intent.putExtra("name",buildingName);
            startActivity(intent);
            finish();
        }
        else{
            //show dilaog here

            String buildingName = bnames.get(position).getName();
            showDialog1(Home.this,buildingName,id);
        }



    }
    public String getPreviousMonthDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.DATE, -1);

        Date preMonthDate = cal.getTime();
        return format.format(preMonthDate);
    }



    public String getlastday(){
        // String date;
        Date today = new Date();



        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date lastDayOfMonth = calendar.getTime();

        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        //   System.out.println("Today            : " + sdf.format(today));
        // System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
        return  sdf.format(lastDayOfMonth);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }



    ArrayList<TenantRentRecord> mlist;
    public void showBottom(){

        BottomSheetDialog dialog = new BottomSheetDialog(Home.this,R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(R.layout.bottom_sheet_view);

      //  dialog.setTitle("Dues this month");

        // BottomSheetListView listView = (BottomSheetListView) dialog.findViewById(R.id.list);
// apply some adapter - add some data to listview
        //   CustomAdapter6 adapter6 = new CustomAdapter6(getApplicationContext(),)

        RecyclerView  recyclerView = dialog.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        String dd = getDate();
        String [] ar = dd.split("-");
        String ss = ar[0];
        String mm = ar[1];
        String yy = ar[2];
        PaymentDatabase db = new PaymentDatabase(getApplicationContext());
         mlist = db.getAllTenantRent11(mm,yy);
        CustomAdapter2 customAdapter2 = new CustomAdapter2(Home.this,mlist);
        customAdapter2.setClickListener(Home.this);
        recyclerView.setAdapter(customAdapter2);

        String st = getIntent().getStringExtra("splash");
        if ( st != null && mlist.size() > 0){
            dialog.show();
        }



    }


    public void reload(){

        String dd = getDate();
        String [] ar = dd.split("-");
        String ss = ar[0];
        String mm = ar[1];
        String yy = ar[2];
        SharedPreferences prefs  = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();




        PaymentDatabase db = new PaymentDatabase(Home.this);

        //check the date is 1st of month and update the last remaining paid amt to  month rent amt

//        String ldate = getlastday().split("-")[0];
//
        String ldate = getlastday().split("-")[1];
        Log.e("LLL","Date :"+getlastday());




      /*  if (ss.equals(ldate)){
            editor.remove("value");
            editor.apply();
        }*/

        if (!mm.equals(ldate)){
            editor.remove("value");
            editor.apply();
        }

        int value = prefs.getInt("value",0);
        Log.e("mmm","monthYear :"+ss+"/"+mm+"/"+yy+":"+value);
//        if ((ss.equals("01") || ss.equals("1")) && value==0){

        Toast.makeText(getApplicationContext(),"Size :"+ldate,Toast.LENGTH_SHORT).show();

        if (!(mm.equals(ldate)) && value==0){


            editor.putInt("value",1);
            editor.apply();

            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                Log.e("DDD","PDATE :"+getPreviousMonthDate(format.parse(getDate())));
                String prev = getPreviousMonthDate(format.parse(getDate()));
                String [] ar1 = prev.split("-");
                String prevmm = ar1[1];
                String prevyy = ar1[2];
                ArrayList<TenantRentRecord> prevrecord = db.getAllTenantRent(prevmm,prevyy);


                if (prevrecord.size() > 0){
                    for (TenantRentRecord tr1 : prevrecord) {
                        String id = tr1.getId();
                        float  ramt = tr1.getAmount();
                        //float paid = tr.getPaid();
                        float due = tr1.getRemaining();
                        String name = tr1.getName();




                        TenantRentRecord trec = new TenantRentRecord();
                        trec.setId(id);
                        trec.setName(name);
                        trec.setDate(getDate());
                        trec.setAmount(ramt);


                        trec.setMonth(mm);
                        trec.setYear(yy);
                        trec.setPaid(0);

                        if (due == 0){
                            //trec.setRemaining(0);
                            trec.setRemaining(ramt);
                        }
                        else{
                            trec.setRemaining(due+ramt);
                        }
                        // trec.setRemaining(due);
                        trec.setState(0);
                        trec.setRoom(tr1.getRoom());
                        trec.setBuilding(tr1.getBuilding());
                        db.addTenantRent(trec);


                        // tr.clear();

                    }//for loop

              /*      tr = db.getAllTenantRent(mm,yy);
                    adapter = new CustomRentAdapter(AllPayment.this,tr);
                    binding.list.setAdapter(adapter);
                    adapter.setClickListener(this);*/
                }

            }
            catch (Exception e){
                Log.e("DDD","EXE :"+e.getLocalizedMessage());
            }



        }
        else{

            //reset the shared pref








            //date is not first of month
            //add a entry to database for current month
        /*    tr = db.getAllTenantRent(mm,yy);



            if (tr.size() == 0){
                binding.l1.setVisibility(View.VISIBLE);
                binding.l2.setVisibility(View.GONE);

                binding.add.getLayoutParams().width = (int) (width*0.20f);
                binding.add.getLayoutParams().height = (int) (width*0.20f);



            }
            else{

                Log.e("AHAH","Size :"+tr.size());
                binding.l1.setVisibility(View.GONE);
                binding.l2.setVisibility(View.VISIBLE);

                adapter = new CustomRentAdapter(AllPayment.this,tr);
                binding.list.setAdapter(adapter);
                adapter.setClickListener(this);
                //adapter.notifyDataSetChanged();
            }*/
        }


    }

    @Override
    public void onItemClick1(View view, int position) {

        if (view.getId() == R.id.call){
            String id = mlist.get(position).getId();

         String mob =    new PaymentDatabase(getApplicationContext()).getTenantMobileNumber(id);
            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + mob));
            startActivity(intent);

        }







    }
}