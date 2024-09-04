package com.vapps.room.rental.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.vapps.room.rental.Adapters.RoomAdapter;
import com.vapps.room.rental.Database.AdminDatabase;
import com.vapps.room.rental.Database.PaymentDatabase;
import com.vapps.room.rental.Model.Admob;
import com.vapps.room.rental.Model.Room;
import com.vapps.room.rental.Model.TenantRentRecord;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.ActivityBuildingViewBinding;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class BuildingView extends AppCompatActivity implements RoomAdapter.ItemClickListener {


    ActivityBuildingViewBinding binding;
    String rname;

    ArrayList<Room> rlist = new ArrayList<>();
    RoomAdapter adapter;
    boolean ch = false;

    ArrayList<Float> rem = new ArrayList<>();
    int height,width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuildingViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Admob.adMob1(getApplicationContext(),binding.adView);

        binding.list.setLayoutManager(new GridLayoutManager(this,3));

        rname = getIntent().getStringExtra("name");


        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;


        binding.back.getLayoutParams().width = (int) (height*0.045f);
        binding.back.getLayoutParams().height = (int) (height*0.045f);


        binding.viewred.getLayoutParams().width = (int) (height*0.035f);
        binding.viewred.getLayoutParams().height = (int) (height*0.035f);


        binding.viewgreen.getLayoutParams().width = (int) (height*0.035f);
        binding.viewgreen.getLayoutParams().height = (int) (height*0.035f);

        if (rname != null){
            AdminDatabase db = new AdminDatabase(getApplicationContext());
           int total = db.getRoom(rname);
           // int total = 12;
            binding.name.setText(rname);
            binding.rcount.setText(total+"");



            rlist = db.getRoomDetail(rname);




            Log.e("KKK","Size :"+rlist.size());
            if (rlist.size()> 0){
                adapter = new RoomAdapter(BuildingView.this,rlist);
                binding.list.setAdapter(adapter);
                adapter.setClickListener(BuildingView.this);





            }


        }




        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuildingView.this,Home.class);
                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    public void onItemClick(View view, int position) {


        int book = rlist.get(position).getBook();

        if (book == 0){
            PopupMenu popupMenu = new PopupMenu(BuildingView.this,view);

            // Inflating popup menu from popup_menu.xml file
            popupMenu.getMenuInflater().inflate(R.menu.option, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    // Toast message on menu item clicked
                    //  Toast.makeText(MainActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();

                    if (menuItem.getItemId()==R.id.add){
                        //show alert if room info is not provided...
                        float rent = rlist.get(position).getRent();
                        String room = rlist.get(position).getRoomNo();
                        if (rent==0){
                            alertDialog(room);

                        }
                        else{
                            //pass intent
                            Intent intent = new Intent(getApplicationContext(),AddTenant.class);
                            intent.putExtra("name",rname);
                            intent.putExtra("room",room);
                            startActivity(intent);
                            finish();
                        }
                    } else if (menuItem.getItemId()==R.id.edit) {
                        String room = rlist.get(position).getRoomNo();
                        Log.e("EERR","Room :"+room);
                       // Toast.makeText(getApplicationContext(),"Room "+room,Toast.LENGTH_SHORT).show();
                        showDialog1(BuildingView.this,room);
                    }
                    return true;
                }
            });
            // Showing the popup menu
            popupMenu.show();
        }
        else{
            //show other things like popup
            PaymentDatabase py = new PaymentDatabase(getApplicationContext());
            String room = rlist.get(position).getRoomNo();
            String dd = getDate();
            String [] ar = dd.split("-");
            String ss = ar[0];
            String mm = ar[1];
            String yy = ar[2];

            //add entry of all tenants record here
//            TenantRentRecord tr = py.getSingleTenantRent(mm,yy,room,rname);

            reload(room,rname,view);

          //  Log.e("EEFF","Rent :"+tr.getName());

        }




    }

    public String getDate(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        return formattedDate;
    }



    public void showPopup(View view,TenantRentRecord tr){
        String id = tr.getId();
        Drawable d = null;
        Bitmap photo=null;
        PaymentDatabase db = new PaymentDatabase(getApplicationContext());
        String path = db.getTenantImagePath(id);
        if (path != null){
             photo = getBitmap(path);
            // d = new BitmapDrawable(getResources(), photo);


        }

   /*     Balloon balloon = new Balloon.Builder(getApplicationContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setIconDrawable(d)
                .setArrowPosition(0.5f)
                .setWidth(BalloonSizeSpec.WRAP)

                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(15f)
                .setPadding(12)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("&nbsp Name :"+tr.getName()+"&nbsp <br>Rent :"+tr.getAmount()+"<br>Due :"+tr.getRemaining())
                .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white))
                .setTextIsHtml(true)
                .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.purple_700))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();*/


        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;







        Balloon balloon = new Balloon.Builder(getApplicationContext())
                .setLayout(R.layout.custom_pop)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPosition(0.5f)
               .setWidthRatio(0.55f)


                .setHeight(BalloonSizeSpec.WRAP)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.black12))
                .setBalloonAnimation(BalloonAnimation.FADE)

                .build();

        ImageView icon = balloon.getContentView().findViewById(R.id.icon);
        TextView name = balloon.getContentView().findViewById(R.id.name);

        TextView rent = balloon.getContentView().findViewById(R.id.rent);

        TextView due = balloon.getContentView().findViewById(R.id.due);

        Button btn = balloon.getContentView().findViewById(R.id.details);




      icon.getLayoutParams().width = (int) (height*0.055f);
        icon.getLayoutParams().height = (int) (height*0.055f);

        icon.setImageBitmap(photo);

        name.setText("Name : "+tr.getName());
        rent.setText("Rent : "+tr.getAmount()+"/-");
        due.setText("Due : "+tr.getRemaining()+"/-");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon.dismiss();

                Intent intent = new Intent(getApplicationContext(),Profile.class);
                intent.putExtra("data",id);
                intent.putExtra("name",rname);
                intent.putExtra("all","building");
                startActivity(intent);
                finish();
            }
        });

     /*   balloon.setOnBalloonClickListener(new OnBalloonClickListener() {
            @Override
            public void onBalloonClick(@NonNull View view) {
               //dismiss dailog here
                //open detail view

               // Toast.makeText(getApplicationContext(),"Data :"+id,Toast.LENGTH_SHORT).show();

                balloon.dismiss();

                Intent intent = new Intent(getApplicationContext(),Profile.class);
                intent.putExtra("data",id);
                intent.putExtra("name",rname);
                startActivity(intent);
                finish();
            }
        });*/

        balloon.showAlignBottom(view);
    }

    public Bitmap getBitmap(String path){
        Bitmap bitmap = null;

        try {


            Uri sr = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()+".provider", new File(path));

            bitmap = MediaStore.Images.Media.getBitmap( getApplicationContext().getContentResolver(), sr);



        } catch (IOException e) {
            //  throw new RuntimeException(e);
        }




        return bitmap;
    }
    @Override
    public void onBackPressed() {
      super.onBackPressed();
        Intent intent = new Intent(BuildingView.this,Home.class);
        startActivity(intent);
        finish();

    }

    private void alertDialog(String rm) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Before proceeding to add tenant do you want to update information for this flat like size,rent,name etc.");
        dialog.setTitle("Alert");
        dialog.setCancelable(false);
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        //show dialog here
                        showDialog1(BuildingView.this,rm);
                    }
                });
        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //pass intent to add tenant here for adding info
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }


    public void showDialog1(Activity context, String room){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.room_data);


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


        Button cancel = dialog.findViewById(R.id.cancel);
        Button update = dialog.findViewById(R.id.update);
        TextInputLayout te1 = (TextInputLayout)dialog.findViewById(R.id.erno);
        TextInputLayout te2 = (TextInputLayout)dialog.findViewById(R.id.errent);

        TextInputEditText t1 = dialog.findViewById(R.id.number);
        TextInputEditText t2 = dialog.findViewById(R.id.rent);

        Spinner sp = dialog.findViewById(R.id.spinner);


        t1.setText(room);

        RadioGroup rg = dialog.findViewById(R.id.radioGroup);

        RadioButton rb1 = dialog.findViewById(R.id.no);
        RadioButton rb2 = dialog.findViewById(R.id.yes);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });









        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if( t1.getText().length()< 0)
                {
                    te1.setError("Room/flat no Required");

                }
                else {
                    te1.setError(null);
                }

                //check for database if number is already acuired


                AdminDatabase db = new AdminDatabase(getApplicationContext());
                ArrayList<Room> list = db.getRoomDetail(rname);

                Log.e("EERR","Room :"+list.size());
                if (list.size() > 0){
                    boolean tmp = false;
                    for (int i=0;i<list.size();i++) {
                        tmp = false;
                        ch = false;
                        String tname = list.get(i).getRoomNo();

                        if (!(t1.getText().toString().trim().equalsIgnoreCase(room))){
                            if (t1.getText().toString().trim().equalsIgnoreCase(tname)){
                                te1.setError("Room no already exists");
                                tmp = true;
                            }

                            if (tmp){
                                ch = true;
                                break;
                            }
                        }




                    }//for
                }





            }



        });


        t2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if( t2.getText().length()< 0)
                {
                    te2.setError("Rent Required");

                }
                else {
                    te2.setError(null);
                }

                //check for database if number is already acuired







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
                if (t1.getText().length() > 0 && t2.getText().length() > 0){

                    dialog.dismiss();
                    //add room no entry to database



                    AdminDatabase db = new AdminDatabase(getApplicationContext());
                    Room rm = new Room();
                    rm.setRoomNo(t1.getText().toString().trim());
                    if (rb1.isChecked()){
                        rm.setInterior(0);
                        rm.setFurniture(0);
                    }
                    else{
                        rm.setInterior(1);
                        rm.setFurniture(1);
                    }
                    rm.setRent(Float.parseFloat(t2.getText().toString()));
                    rm.setSize(sp.getSelectedItem().toString());

                    db.updateRoom(rm,room,rname);
                    //reload list here

                    rlist.clear();

                    rlist = db.getRoomDetail(rname);
                    Log.e("KKK","Size :"+rlist.size());
                    if (rlist.size()> 0){
                        adapter = new RoomAdapter(BuildingView.this,rlist);
                        binding.list.setAdapter(adapter);
                        adapter.setClickListener(BuildingView.this);


                    }





                }
                else {

                    if (t1.getText().length() == 0){
                        te1.setError("Room/flat no Required");
                    }
                    else if (t2.getText().length() == 0) {
                        te2.setError("Rent Required");
                    }




                }



            }
        });





        dialog.show();


    }



    //new code


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

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date lastDayOfMonth = calendar.getTime();

        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        //   System.out.println("Today            : " + sdf.format(today));
        // System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
        return  sdf.format(lastDayOfMonth);
    }
    public void reload(String room,String building,View view){

        String dd = getDate();
        String [] ar = dd.split("-");
        String ss = ar[0];
        String mm = ar[1];
        String yy = ar[2];
        SharedPreferences prefs  = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();



     //   binding.monthyear.setText(mm+" "+yy);

        PaymentDatabase db = new PaymentDatabase(getApplicationContext());

        //check the date is 1st of month and update the last remaining paid amt to  month rent amt

        String ldate = getlastday().split("-")[0];
        Log.e("LLL","Date :"+getlastday());




        if (ss.equals(ldate)){
            editor.remove("value");
            editor.apply();
        }

        int value = prefs.getInt("value",0);
        Log.e("mmm","monthYear :"+ss+"/"+mm+"/"+yy+":"+value);
        if ((ss.equals("01") || ss.equals("1")) && value==0){



            editor.putInt("value",1);
            editor.apply();

            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                Log.e("DDD","PDATE :"+getPreviousMonthDate(format.parse(getDate())));
                String prev = getPreviousMonthDate(format.parse(getDate()));
                String [] ar1 = prev.split("-");
                String prevmm = ar1[1];
                String prevyy = ar1[2];
            //    ArrayList<TenantRentRecord> prevrecord = db.getAllTenantRent(prevmm,prevyy);


                //get rent remaining


                //get

                TenantRentRecord tr1 = db.getSingleTenantRent(prevmm,prevyy,room,building);



                if (tr1 != null){

                        String id = tr1.getId();
                        float  ramt = tr1.getAmount();
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
                            trec.setRemaining(0);
                        }
                        else{
                            trec.setRemaining(due+ramt);
                        }
                        // trec.setRemaining(due);
                        trec.setState(0);
                        db.addTenantRent(trec);


                        // tr.clear();


                    showPopup(view,trec);


                }
                else{
                    //if user has not pay for previous month then add a entry in History table for not paid.

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
           TenantRentRecord tr = db.getSingleTenantRent(mm,yy,room,building);
            if (tr != null){

                showPopup(view,tr);

            }

        }


    }



}