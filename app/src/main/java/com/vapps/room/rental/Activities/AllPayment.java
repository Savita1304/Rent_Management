package com.vapps.room.rental.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vapps.room.rental.Adapters.CustomRentAdapter;
import com.vapps.room.rental.Database.PaymentDatabase;
import com.vapps.room.rental.Model.Admob;
import com.vapps.room.rental.Model.HistoryData;
import com.vapps.room.rental.Model.TenantRentRecord;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.ActivityAllPaymentBinding;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AllPayment extends AppCompatActivity implements CustomRentAdapter.ItemClickListener{


    ActivityAllPaymentBinding binding;

    CustomRentAdapter adapter;
    ArrayList<TenantRentRecord> tr;
    boolean changing = false;
    int height,width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        binding.list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        Admob.adMob1(getApplicationContext(),binding.adView);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllPayment.this,Home.class);
                startActivity(intent);
                finish();

            }
        });


        //reload();

        updateRent();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AllPayment.this,Home.class);
        startActivity(intent);
        finish();
    }

    public String getDate(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        return formattedDate;
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


    public void reload(){

        String dd = getDate();
        String [] ar = dd.split("-");
        String ss = ar[0];
        String mm = ar[1];
        String yy = ar[2];
        SharedPreferences prefs  = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();



        binding.monthyear.setText(mm+" "+yy);

        PaymentDatabase db = new PaymentDatabase(AllPayment.this);

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
            Toast.makeText(getApplicationContext(),"match",Toast.LENGTH_SHORT).show();
            editor.remove("value");
            editor.apply();
        }

        int value = prefs.getInt("value",0);
        Log.e("mmm","monthYear :"+ss+"/"+mm+"/"+yy+":"+value);
//        if ((ss.equals("01") || ss.equals("1")) && value==0){

       // Toast.makeText(getApplicationContext(),"Size :"+ldate,Toast.LENGTH_SHORT).show();

        if (!(mm.equals(ldate)) && value==0){

            Toast.makeText(getApplicationContext(),"value",Toast.LENGTH_SHORT).show();

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





                        //update
                        db.addTenantRent(trec);


                        // tr.clear();

                    }//for loop

                    tr = db.getAllTenantRent(mm,yy);
                    adapter = new CustomRentAdapter(AllPayment.this,tr);
                    binding.list.setAdapter(adapter);
                    adapter.setClickListener(this);
                }

                else{
                    //data not found //check and then delete if needed
                    tr = db.getAllTenantRent(mm,yy);
                    adapter = new CustomRentAdapter(AllPayment.this,tr);
                    binding.list.setAdapter(adapter);
                    adapter.setClickListener(this);
                }

            }
            catch (Exception e){
                Log.e("DDD","EXE :"+e.getLocalizedMessage());
            }



        }
        else{

            //reset the shared pref



            Toast.makeText(getApplicationContext(),"First",Toast.LENGTH_SHORT).show();





            //date is not first of month
            //add a entry to database for current month
            tr = db.getAllTenantRent(mm,yy);



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
            }
        }


    }



    public void updateRent(){
        String dd = getDate();
        String [] ar = dd.split("-");
        String mm = ar[1];
        String yy = ar[2];

        binding.monthyear.setText(mm+" "+yy);

        PaymentDatabase db = new PaymentDatabase(AllPayment.this);

        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy",Locale.US);
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
                    float due = tr1.getRemaining();
                    float famt ;
                    if (due == 0){
                        famt = ramt;
                    }
                    else{
                        famt = due+ramt;
                    }



                    String my = db.getmonthyear(id);
                    String[] ch = my.split("_");
                    String new_month = ch[0];
                    String new_year = ch[1];

                    if (new_month != mm && new_year == yy){
                        db.resetTenantRent(id,famt,mm,yy,0);

                    } else if (new_month != mm && new_year != yy) {
                        //year changed
                        db.resetTenantRent(id,famt,mm,yy,0);
                    }


                }//for loop


            }



            tr = db.getAllTenantRent(mm,yy);

            if (tr.size() == 0){
                binding.l1.setVisibility(View.VISIBLE);
                binding.l2.setVisibility(View.GONE);

                binding.add.getLayoutParams().width = (int) (width*0.20f);
                binding.add.getLayoutParams().height = (int) (width*0.20f);
            }
            else{
                binding.l1.setVisibility(View.GONE);
                binding.l2.setVisibility(View.VISIBLE);

                adapter = new CustomRentAdapter(AllPayment.this,tr);
                binding.list.setAdapter(adapter);
                adapter.setClickListener(this);
            }

          /*
            adapter = new CustomRentAdapter(AllPayment.this,tr);
            binding.list.setAdapter(adapter);
            adapter.setClickListener(this);*/



        }
        catch (Exception e){
            Log.e("DDD","EXE :"+e.getLocalizedMessage());
        }







}

    @Override
    public void onItemClick(View view, int position) {


        if (view.getId() == R.id.state){
            //Toast.makeText(getContext(),"click :"+position,Toast.LENGTH_SHORT).show();
            TenantRentRecord tenantRentRecord = tr.get(position);

            int st = tenantRentRecord.getState();
            if (st == 0){
                //show confirm dialog
                String date = getDate();
                float amt = tenantRentRecord.getAmount();
                String name = tenantRentRecord.getName();
                String id = tenantRentRecord.getId();
                float ramt = tenantRentRecord.getRemaining();




                showDialog1(AllPayment.this,id,name,amt,date,position,ramt);
            }

        } else if (view.getId()==R.id.main) {
            showDialog2(AllPayment.this,position);
        } else if (view.getId() == R.id.edit) {
            showDialog(AllPayment.this,position);
        }

    }



    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }



    @SuppressLint("SetTextI18n")
    public void showDialog1(Activity activity, String id, String name, float amt, String date, int position, float ramt){
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //open other dialog
                showDialog(AllPayment.this,position);
            }
        });


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

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                adapter.notifyItemChanged(position);


            }
        });


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                PaymentDatabase db = new PaymentDatabase(AllPayment.this);
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



                tr = db.getAllTenantRent(mm,yy);
                adapter = new CustomRentAdapter(AllPayment.this,tr);
                binding.list.setAdapter(adapter);
                adapter.setClickListener(AllPayment.this);
                adapter.notifyItemChanged(position);



            }
        });



        String[] arr = AllPayment.this.getResources().getStringArray(R.array.mode);


        sp.setText(arr[0]);

        mode[0] = arr[0];




        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        AllPayment.this,
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


    public void showDialog2(Activity activity, int position){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.detail);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());


        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels*0.40f);


        lp.width = widthLcl;
        lp.height = heightLcl;
        lp.gravity = Gravity.CENTER;




        dialog.getWindow().setAttributes(lp);

        TextView name = (TextView) dialog.findViewById(R.id.name);//cancel
        TextView rent = (TextView) dialog.findViewById(R.id.rent);//confirm
        TextView date = (TextView) dialog.findViewById(R.id.date);//msg
        TextView due = (TextView) dialog.findViewById(R.id.due);//msg
        LinearLayout l1 = dialog.findViewById(R.id.title);
        ImageView icon = dialog.findViewById(R.id.status);
        ImageView call = dialog.findViewById(R.id.call);

        TextView paid = (TextView) dialog.findViewById(R.id.apaid);//msg





        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);



        if (prefs.getInt("color",0) != 0) {
            int value = prefs.getInt("color", 0);

            l1.setBackgroundColor(value);


        }

        TenantRentRecord tenantRentRecord = tr.get(position);

        name.setText(tenantRentRecord.getName());
        rent.setText(tenantRentRecord.getAmount()+"/-");

        float pdamt = tenantRentRecord.getPaid();
        if (pdamt > tenantRentRecord.getAmount()){
            float samt = pdamt - tenantRentRecord.getAmount();
//            paid.setText("Total="+pdamt+"(Rent="+tenantRentRecord.getAmount()+"/- last due :"+samt+"/-");

            SpannableStringBuilder builder = new SpannableStringBuilder();

            String test = "Total="+pdamt+"\n";
            builder.append(test);

            String red = "(Rent="+tenantRentRecord.getAmount()+"+"+samt+"(due))";
            SpannableString redSpannable= new SpannableString(red);
            redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
            builder.append(redSpannable);


            paid.setText(builder, TextView.BufferType.SPANNABLE);
            //  paid.setText("Total="+pdamt+"\n(Rent="+tenantRentRecord.getAmount()+"+"+samt+"(due))");

        }
        else{
            paid.setText(tenantRentRecord.getPaid()+"/-");
        }
        //   paid.setText(tenantRentRecord.getPaid()+"/-");
        int state = tenantRentRecord.getState();
        float ramt = tenantRentRecord.getRemaining();

        if (state == 0){


            if (state == 0 && ramt > 0){
                due.setText(tenantRentRecord.getRemaining()+"/-");
                date.setText(tenantRentRecord.getPaiddate());
            }
            else{
                date.setText("-");
                due.setText(tenantRentRecord.getAmount()+"/-");
            }

            icon.setImageResource(R.drawable.pending);
            call.setVisibility(View.VISIBLE);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //call the tenant mobile
                    dialog.dismiss();
                    PaymentDatabase db = new PaymentDatabase(AllPayment.this);
                    String mobile = db.getTenantMobileNumber(tenantRentRecord.getId());

                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + mobile));
                    startActivity(intent);
                }
            });
        }

        else{
            String pdate = tenantRentRecord.getPaiddate();
            date.setText(pdate);
            due.setText("0.0/-");
            icon.setImageResource(R.drawable.checkmark);
             call.setVisibility(View.GONE);


           // call.setImageResource(R.drawable.share);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Window window = dialog.getWindow();
                    View decorView = window.getDecorView();
                    Bitmap b = getBitmapFromView(decorView);

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/*");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(AllPayment.this.getContentResolver(),
                            b, "Title", null);
                    //File path = writeimage(b);

                    //   if (path != null){
                    Uri imageUri =  Uri.parse(path);
                    //Uri uri = FileProvider.getUriForFile(getContext(),AllPayment.this.getPackageName()+".provider",path);
                    // Log.e("PPPP","Path :"+uri);
                    share.putExtra(Intent.EXTRA_STREAM, imageUri);
                    startActivity(Intent.createChooser(share, "Select"));


//                    }
//                    else {
//                        Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
//                    }

                }
            });
        }






        dialog.show();



    }

    public void showDialog(Activity context,int position){

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


        TenantRentRecord data = tr.get(position);
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

                adapter.notifyItemChanged(position);

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
                        PaymentDatabase db = new PaymentDatabase(AllPayment.this);
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



                        tr.clear();

                        tr = db.getAllTenantRent(mm,yy);

                        adapter = new CustomRentAdapter(AllPayment.this,tr);
                        binding.list.setAdapter(adapter);
                        adapter.setClickListener(AllPayment.this);
                        adapter.notifyItemChanged(position);
                    }
                    else{

                        PaymentDatabase db = new PaymentDatabase(AllPayment.this);
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

                        tr.clear();
                        tr = db.getAllTenantRent(mm,yy);
                        adapter = new CustomRentAdapter(AllPayment.this,tr);
                        binding.list.setAdapter(adapter);
                        adapter.setClickListener(AllPayment.this);
                        adapter.notifyItemChanged(position);
                    }

                }
                else{
                    te1.setError("Rent Required");
                }







            }
        });


   /*     if (prefs.getInt("color",0) != 0) {
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
                        AllPayment.this,
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
}