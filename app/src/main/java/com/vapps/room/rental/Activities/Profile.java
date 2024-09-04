package com.vapps.room.rental.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vapps.room.rental.Database.AdminDatabase;
import com.vapps.room.rental.Database.PaymentDatabase;
import com.vapps.room.rental.Model.Admob;
import com.vapps.room.rental.Model.HistoryData;
import com.vapps.room.rental.Model.TenantRecord;
import com.vapps.room.rental.Model.TenantRentRecord;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.ActivityProfileBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Profile extends AppCompatActivity {

    ActivityProfileBinding binding;

    TenantRentRecord record;

    String path;

    String id,rname,mobile,uname;
    String rent;
    boolean changing = false;
    int height,width;
    String all;
    String room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        Admob.adMob1(getApplicationContext(),binding.adView);

        binding.history.getLayoutParams().width = (int) (height*0.045f);
        binding.history.getLayoutParams().height = (int) (height*0.045f);

        binding.icon.getLayoutParams().width = (int) (height*0.045f);
        binding.icon.getLayoutParams().height = (int) (height*0.045f);


        binding.back.getLayoutParams().width = (int) (height*0.045f);
        binding.back.getLayoutParams().height = (int) (height*0.045f);

        setData();


        String dd = getDate();
        String [] ar = dd.split("-");
        String mm = ar[1];
        String yy = ar[2];

        binding.cdate.setText(mm+" "+yy);


         all = getIntent().getStringExtra("all");



        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  if (all == null || all.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), BuildingView.class);
                    intent.putExtra("name",rname);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), AllTenants.class);
                    startActivity(intent);
                    finish();
                }*/

                if (all.equals("building")){
                    Intent intent = new Intent(getApplicationContext(), BuildingView.class);
                    intent.putExtra("name",rname);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), AllTenants.class);
                    startActivity(intent);
                    finish();
                }

            }
        });



        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + mobile));
                startActivity(intent);
            }
        });


        binding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TenantRentRecord tenantRentRecord = record;


                int st = tenantRentRecord.getState();
                if (st == 0){
                    //show confirm dialog
                    String date = getDate();
                    float amt = tenantRentRecord.getAmount();
                    String name = tenantRentRecord.getName();
                    String id = tenantRentRecord.getId();
                    float ramt = tenantRentRecord.getRemaining();




                    showDialog1(Profile.this,id,name,amt,date,ramt);
                }

            }
        });



        binding.history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), History.class);
                intent.putExtra("name",rname);
                intent.putExtra("data",id);
                intent.putExtra("uname",uname);
                intent.putExtra("rent",rent);

                intent.putExtra("temp",room);
                intent.putExtra("path",path);



                    intent.putExtra("all",all);

                startActivity(intent);
                finish();
            }
        });


        binding.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update all database table here.

                Log.e("RERE ","Detail :"+rname+"/"+room);


                new PaymentDatabase(getApplicationContext()).updateTenantLeft(id,getDate());
                new AdminDatabase(getApplicationContext()).updateRoom(room,rname);
                new PaymentDatabase(getApplicationContext()).updateTenantLeft1(id,getDate());

                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();

            }
        });


    }
    public void showDialog1(Activity activity, String id, String name, float amt, String date, float ramt){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.confirmation1);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());


        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.8f);
        int heightLcl = (int) (displayMetrics.heightPixels*0.60f);


        lp.width = widthLcl;
        lp.height = heightLcl;
        lp.gravity = Gravity.CENTER;




        dialog.getWindow().setAttributes(lp);

        TextView no = (TextView) dialog.findViewById(R.id.no);//cancel
        TextView yes = (TextView) dialog.findViewById(R.id.yes);//confirm
        TextView msg = (TextView) dialog.findViewById(R.id.msg);//msg
        LinearLayout l1 = dialog.findViewById(R.id.confirm_dialog_header);
        ImageView edit = dialog.findViewById(R.id.edit);




        AutoCompleteTextView sp = dialog.findViewById(R.id.type);


        final String[] mode = {null};






        String dd = getDate();
        String [] ar = dd.split("-");
        String mm = ar[1];
        String yy = ar[2];
        if (ramt > 0){
            msg.setText("You are taking remaining due "+ramt+" /-" +" from "+name+" for month and year "+mm +" "+yy+" it will not be editable.");

        }
        else{
            msg.setText("You are taking "+amt+" /-" +" rent from "+name+" for month and year "+mm +" "+yy+" it will not be editable.");

        }



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);



        if (prefs.getInt("color",0) != 0) {
            int value = prefs.getInt("color", 0);

            l1.setBackgroundColor(value);
            no.setBackgroundColor(value);
            yes.setBackgroundColor(value);



        }

        float samt;
        if (ramt == 0){
            samt = amt;
        }
        else{
            samt = ramt;
        }


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //open other dialog
                showDialog2(Profile.this);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                PaymentDatabase db = new PaymentDatabase(getApplicationContext());
                TenantRentRecord trec = new TenantRentRecord();
//                tr.setId(id);
                //  trec.setPaid(amt);
                trec.setPaid(samt);
                trec.setPaiddate(date);
                trec.setState(1);
                trec.setRemaining(0);
                trec.setMode(mode[0]);

                db.updateTenantRent(id,trec,mm,yy);

// user history record
                HistoryData historyData = new HistoryData();
                historyData.setId(id);
                historyData.setAmt(samt+"");
                historyData.setType(mode[0]);
                historyData.setDate(date);
                historyData.setMonth(mm);
                historyData.setYear(yy);

                db.addTenantRentHistory(historyData);


                //close



            //reset data here again
                setData();



            }
        });



        String[] arr = getResources().getStringArray(R.array.mode);


        sp.setText(arr[0]);

       // Toast.makeText(getApplicationContext(),"Mode :"+mode[0],Toast.LENGTH_SHORT).show();

        mode[0] = arr[0];




        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        arr);


        sp.setAdapter(adapter);


        sp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                mode[0] = (String)parent.getItemAtPosition(position);

                //TODO Do something with the selected text
            }
        });


        dialog.show();



    }
    public void showDialog2(Activity context){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.entry1);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());


        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.8f);
        int heightLcl = (int) (displayMetrics.heightPixels*0.60f);


        lp.width = widthLcl;
        lp.height = heightLcl;
        lp.gravity = Gravity.CENTER;




        dialog.getWindow().setAttributes(lp);


        Button cancel = dialog.findViewById(R.id.cancel);
        Button update = dialog.findViewById(R.id.update);
        TextInputLayout te1 = (TextInputLayout)dialog.findViewById(R.id.eramt);
        TextInputEditText amt = dialog.findViewById(R.id.amt);
        LinearLayout header = dialog.findViewById(R.id.header);
        AutoCompleteTextView sp = dialog.findViewById(R.id.type);

        final String[] mode = {null};


        TenantRentRecord data = record;
        float rent = data.getAmount();
        String id = data.getId();
        float rem = data.getRemaining();


        amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!changing && (amt.getText().toString().startsWith("0") || amt.getText().toString().startsWith(".") ) ){
                    changing = true;
                    amt.setText(amt.getText().toString().replace(".", ""));
                    amt.setText(amt.getText().toString().replace("0", ""));
                }
                changing = false;
                if( amt.getText().length()>0)
                {
                    float chamt = Float.parseFloat(amt.getText().toString());


                    if (rem == 0){
                        if (chamt > rent){
                            te1.setError("Required rent "+rent +"/-");
                        }
                        else{
                            te1.setError(null);
                        }
                    }
                    else{
                        if (chamt > rem){

                            te1.setError("Required rent "+rem +"/-");
                        }
                        else{
                            te1.setError(null);
                        }
                    }



                }
                else {
                    te1.setError("Rent Required");
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
                if (amt.getText().length() > 0){
                    dialog.dismiss();

                    //add entry to database
                    float chamt = Float.parseFloat(amt.getText().toString());
                    float samt;
                    if (rem == 0){
                        samt = rent;
                    }
                    else{
                        samt = rem;
                    }


                    if (chamt == samt){
                        //update database rent
                        PaymentDatabase db = new PaymentDatabase(getApplicationContext());
                        TenantRentRecord trec = new TenantRentRecord();
//                tr.setId(id);
                        trec.setPaid(chamt);
                        trec.setPaiddate(getDate());
                        trec.setState(1);
                        trec.setRemaining(0);
                        trec.setMode(sp.getText().toString());





                        String dd = getDate();
                        String [] ar = dd.split("-");
                        String mm = ar[1];
                        String yy = ar[2];

                        db.updateTenantRent(id,trec,mm,yy);



//history
                        HistoryData historyData = new HistoryData();
                        historyData.setId(id);
                        historyData.setAmt(chamt+"");
                        historyData.setType(mode[0]);
                        historyData.setDate(getDate());
                        historyData.setMonth(mm);
                        historyData.setYear(yy);

                        db.addTenantRentHistory(historyData);

//close



                       // tr.clear();

                       setData();
                    }
                    else{

                        PaymentDatabase db = new PaymentDatabase(getApplicationContext());
                        TenantRentRecord trec = new TenantRentRecord();
                        trec.setPaid(chamt);
                        trec.setPaiddate(getDate());
                        trec.setState(0);
                        float famt = samt-chamt;
                        trec.setRemaining(famt);
                        trec.setMode(sp.getText().toString());


                        String dd = getDate();
                        String [] ar = dd.split("-");
                        String mm = ar[1];
                        String yy = ar[2];
                        db.updateTenantRent(id,trec,mm,yy);


                        //history
                        HistoryData historyData = new HistoryData();
                        historyData.setId(id);
                        historyData.setAmt(chamt+"");
                        historyData.setType(mode[0]);
                        historyData.setDate(getDate());
                        historyData.setMonth(mm);
                        historyData.setYear(yy);

                        db.addTenantRentHistory(historyData);

//close

                       // tr.clear();
                       setData();
                    }

                }
                else{
                    te1.setError("Rent Required");
                }







            }
        });

/*
        if (prefs.getInt("color",0) != 0) {
            int value = prefs.getInt("color", 0);

            cancel.setBackgroundColor(value);
            update.setBackgroundColor(value);
            header.setBackgroundColor(value);

        }*/

        String[] arr = getResources().getStringArray(R.array.mode);


        sp.setText(arr[0]);

        mode[0] = arr[0];


        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        arr);


        sp.setAdapter(adapter);

        sp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                mode[0] = (String)parent.getItemAtPosition(position);

                //TODO Do something with the selected text
            }
        });
        dialog.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();




        /*if (all == null || all.isEmpty()){
            Intent intent = new Intent(getApplicationContext(), BuildingView.class);
            intent.putExtra("name",rname);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), AllTenants.class);
            startActivity(intent);
            finish();
        }*/

        if (all.equals("building")){
            Intent intent = new Intent(getApplicationContext(), BuildingView.class);
            intent.putExtra("name",rname);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), AllTenants.class);
            startActivity(intent);
            finish();
        }
    }

    public void setData(){
        Intent intent = getIntent();
        id =  intent.getStringExtra("data");
        rname = intent.getStringExtra("name");


     //   id = record.getId();



        PaymentDatabase db = new PaymentDatabase(getApplicationContext());
        TenantRecord tr = db.getTenant(id);


        mobile = tr.getTmob();
                record = db.getSingleTenantRent11(id);
        uname = record.getName();
        rent = record.getAmount()+"";

        room = record.getRoom();

        path = tr.getTimg();




                int state = record.getState();
                if (state == 0){
                    binding.pay.setBackgroundColor(getResources().getColor(R.color.purple_700));
                    binding.pay.setClickable(true);
                }
                else {
                    binding.pay.setBackgroundColor(getResources().getColor(R.color.gray));
                    binding.pay.setClickable(false);
                }

        Bitmap bitmap = getBitmap(tr.getTimg());
        binding.icon.setImageBitmap(bitmap);
        binding.name.setText(record.getName());
        binding.number.setText(tr.getTmob());
        binding.rent.setText(record.getAmount()+"/-");
        binding.sdate.setText(record.getDate());
        binding.due.setText(record.getRemaining()+"/-");
        binding.address.setText(tr.getTadd());
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

    public String getDate(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        return formattedDate;
    }

}