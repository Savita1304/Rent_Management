package com.vapps.room.rental.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vapps.room.rental.Adapters.CustomAdapter1;
import com.vapps.room.rental.Database.PaymentDatabase;
import com.vapps.room.rental.Model.Admob;
import com.vapps.room.rental.Model.HistoryData;
import com.vapps.room.rental.Model.Section;
import com.vapps.room.rental.Model.TenantRentRecord;
import com.vapps.room.rental.R;
import com.vapps.room.rental.databinding.ActivityHistoryBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;



public class History extends AppCompatActivity {

    ActivityHistoryBinding binding;
    SharedPreferences prefs;

    ArrayList<String> Year = new ArrayList<>();
    String year;
    CustomAdapter1 adapter1;

    ArrayList<String> month = new ArrayList<>();
    ArrayList<Section> sec = new ArrayList<>();

   // ArrayList<String> month = new ArrayList<>();

    String rname,id,name;
    int height,width;
    String all,val;

    File direct;
    File file;

    String rent;

    ArrayList<TenantRentRecord> rec;

    String room;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = PreferenceManager.getDefaultSharedPreferences(History.this);
        Admob.adMob1(getApplicationContext(),binding.adView);

         all = getIntent().getStringExtra("all");
        val = getIntent().getStringExtra("val");

         Log.e("ALLLL","mg :"+all);
        if (prefs.getInt("color",0) != 0) {
            int value = prefs.getInt("color", 0);
            binding.header.setBackgroundColor(value);
            binding.footer.setBackgroundColor(value);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                String col = String.format("#%06X", (0xFFFFFF & value));
                StringBuffer stringBuffer = new StringBuffer(col);
                String vc = stringBuffer.insert(1,"CC").toString();
                window.setStatusBarColor(Color.parseColor(vc));
            }
        }

        else{

        }


        month.add("Jan");
        month.add("Feb");
        month.add("Mar");
        month.add("Apr");
        month.add("May");
        month.add("Jun");
        month.add("Jul");
        month.add("Aug");
        month.add("Sept");
        month.add("Oct");
        month.add("Nov");
        month.add("Dec");


        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;




        binding.download.getLayoutParams().width = (int) (height*0.045f);
        binding.download.getLayoutParams().height = (int) (height*0.045f);

        binding.back.getLayoutParams().width = (int) (height*0.045f);
        binding.back.getLayoutParams().height = (int) (height*0.045f);

        binding.historylist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        id = getIntent().getStringExtra("data");
         name =getIntent().getStringExtra("uname");
         rent = getIntent().getStringExtra("rent");

        rname = getIntent().getStringExtra("name");

        room = getIntent().getStringExtra("temp");


        binding.name.setText(name);
        binding.rent.setText(rent+"/-");



        //fetch year of user

        PaymentDatabase db = new PaymentDatabase(getApplicationContext());
        ArrayList<String> spinner = db.exploreYear(id);
        Log.e("RTRT","ID :"+spinner);
        if (spinner.size() > 0){


            binding.l1.setVisibility(View.VISIBLE);
            binding.main.setVisibility(View.VISIBLE);
            binding.download.setVisibility(View.VISIBLE);

            for (int i = 0; i < spinner.size(); i++) {
                String year = spinner.get(i);
                if (!Year.contains(year)){
                    Year.add(year);
                    Collections.sort(Year);
                }





            }//end of for loop


            binding.year.setText(String.valueOf(Year.get(0)));
            year = Year.get(0);


            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            Year);


            binding.year.setAdapter(adapter);


            load(id,year);




        }
        else{
            //no record exist

            binding.l1.setVisibility(View.GONE);
            binding.main.setVisibility(View.GONE);
            binding.download.setVisibility(View.GONE);

            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(History.this, parentLayout,"Oop's no record found.",3000).setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
        }



        binding.year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                year =(String) adapterView.getItemAtPosition(i);
                //Toast.makeText(getApplicationContext(),"Year "+month,Toast.LENGTH_SHORT).show();
                load(id,year);
            }
        });


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // finish();

               /* if (all == null || all.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    intent.putExtra("name",rname);
                    intent.putExtra("data",id);
                    intent.putExtra("all","tenants");
                    startActivity(intent);
                    finish();
                }
                else if ( val != null) {
                    Intent intent = new Intent(getApplicationContext(), AllTenants.class);
                    startActivity(intent);
                    finish();
                }

                else{
                    //directly comes from All Tenants and back to all tenants
                    Intent intent = new Intent(getApplicationContext(), AllTenants.class);
                    startActivity(intent);
                    finish();
                }*/


                if (all.equals("building")){
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    intent.putExtra("name",rname);
                    intent.putExtra("data",id);
                    intent.putExtra("all",all);
                    startActivity(intent);
                    finish();
                }
                else if (all.equals("load")){
                    Intent intent = new Intent(getApplicationContext(), AllTenants.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    intent.putExtra("name",rname);
                    intent.putExtra("data",id);
                    intent.putExtra("all",all);
                    startActivity(intent);
                    finish();
                }


            }
        });

        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    progressDialog = new ProgressDialog(History.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Downloading....");
                    progressDialog.show();

                    if (createPdf()){
                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(),Reports.class);
                        intent.putExtra("id",id);
                        intent.putExtra("year",year);
                        intent.putExtra("name",name);
                        intent.putExtra("show","show");
                        startActivity(intent);
                        finish();
                    }
                } catch (FileNotFoundException e) {
                    Log.e("EEE","ER :"+e.getLocalizedMessage());
                    progressDialog.dismiss();
                    e.printStackTrace();
                } catch (DocumentException e) {

                    e.printStackTrace();
                }

            }
        });


    }

    public File createDestination(String fileName) {
        File wallpaperDirectory=null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Room Rental");

        }
        else{
            direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Room Rental");

        }

        if (!direct.exists()) {
            try {
                wallpaperDirectory = new File(direct.getAbsolutePath());
                wallpaperDirectory.mkdirs();

            }catch (Exception e){
                Log.e("FGFB","ERF :"+e.getLocalizedMessage());
            }


        }

        File file = new File(direct, fileName);




        return file ;
    }



    public boolean createPdf() throws FileNotFoundException, DocumentException
    {


        boolean temp;
        int color = R.color.purple_700;
        Log.e("NNNN","name :"+name);

        File f = createDestination(id+"_"+year+".pdf");
        file = new File(f.getAbsolutePath());


        Document document = new Document();  // create the document
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();


        Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.BOLD, BaseColor.RED);
        Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.NORMAL, BaseColor.BLACK);


        //add image to pdf


        String path = getIntent().getStringExtra("path");

        Chunk c1 ;
        try {
            Image i = Image.getInstance(path);
            i.scaleToFit(150,150);
            c1 = new Chunk(i,0,-100);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        Chunk c0 = new Chunk("\n\n\n\n", f1);

// you created a font, but you never used it:
        Chunk c = new Chunk(name+" "+year+" Rent Report\n", f1);
//        Chunk c2 = new Chunk(currentDate()+"\n\n\n", f2);
        Chunk c2 = new Chunk(currentDate()+"\n", f2);
        Chunk c3 = new Chunk("Rent"+" "+rent+"/-"+"\n", f2);

        Chunk c4 = new Chunk(rname+"("+room+")"+"\n\n\n", f2);






        Paragraph p0 = new Paragraph(c1);
        p0.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p0);



        Paragraph p00 = new Paragraph(c0);
        p00.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p00);


        Paragraph p1 = new Paragraph(c);
        p1.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p1);

        Paragraph p2 = new Paragraph(c2);
        p2.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p2);


        Paragraph p3 = new Paragraph(c3);
        p3.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p3);


        Paragraph p4 = new Paragraph(c4);
        p4.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p4);


        PdfPTable table = new PdfPTable(3);
        int width = getWindowManager(). getDefaultDisplay(). getWidth();
        Log.e("WWWF","wid :"+width);
        //  table.setTotalWidth(width*80/100);
        //table.setWidthPercentage(100f);

        // table.setLockedWidth(true);



        Font font = new Font(Font.FontFamily.TIMES_ROMAN,25);
        font.setColor(255,255,255);

        PdfPCell cell1 = new PdfPCell(new Phrase("Amount",font));
        cell1.setBackgroundColor(new BaseColor(color));
        // cell1.setBackgroundColor(new BaseColor(32,42,68,1));
        cell1.setFixedHeight(40);

        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase("Mode",font));
        cell2.setBackgroundColor(new BaseColor(color));
        // cell2.setBackgroundColor(new BaseColor(32,42,68,1));
        cell2.setFixedHeight(40);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell2);


        PdfPCell cell3 = new PdfPCell(new Phrase("Date",font));
        cell3.setBackgroundColor(new BaseColor(color));
        //cell3.setBackgroundColor(new BaseColor(32,42,68,1));
        cell3.setFixedHeight(40);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell3);






//       Font fcolor = new Font();



        ArrayList<TenantRentRecord> list = new PaymentDatabase(getApplicationContext()).getTenantRentRecord11(id,year);



        Log.e("SDFDS","wid :"+list.size());

        if (list.size() > 0){

            temp = true;

//           fcolor.setColor(0,0,0);
           // fcolor.setColor(new BaseColor(R.color.purple_700));

            for (TenantRentRecord model : list) {

                PdfPCell entry = new PdfPCell(new Phrase(model.getPaid()+"/-",new Font(Font.FontFamily.TIMES_ROMAN,20)));
                entry.setFixedHeight(40);
                entry.setVerticalAlignment(Element.ALIGN_MIDDLE);
                entry.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(entry);











                PdfPCell entry1 = new PdfPCell(new Phrase(String.valueOf(model.getMode()+" /-"),new Font(Font.FontFamily.TIMES_ROMAN,20)));
                entry1.setFixedHeight(40);
                entry1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                entry1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(entry1);



                PdfPCell entry2 = new PdfPCell(new Phrase(model.getPaiddate(),new Font(Font.FontFamily.TIMES_ROMAN,20)));
                entry2.setFixedHeight(40);
                entry2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                entry2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(entry2);












            }





        }
        else{
            temp = false;
        }






       





        document.add(table);
        document.addCreationDate();



       /* Chunk c3 = new Chunk("Total Expense : "+expense+" /-"+"\n", red);
        Chunk c4 = new Chunk("Income : "+income+" /-"+"\n\n", green);


        Paragraph p3 = new Paragraph(c3);
        p3.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p3);

        Paragraph p4 = new Paragraph(c4);
        p4.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p4);*/



        //  document.add(myImg);




        document.close();


        return temp;



    }


    public static String currentDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        String Datetime = sdf.format(c.getTime());

        return  Datetime;
    }


    public String[] sortDates(String[] dates){

        for (int i = 0; i < dates.length; i++)
        {
            for (int j = i + 1; j < dates.length; j++)
            {
                String tmp = "";
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = dateFormat.parse(dates[i]);
                    d2 = dateFormat.parse(dates[j]);

                    Log.e("DDD","dates :"+d1+"/"+d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (d1.after(d2))
                {
                    tmp = dates[i];
                    dates[i] = dates[j];
                    dates[j] = tmp;

                }
            }
//prints the sorted element of the array
            //   System.out.println(arr[i]);
        }
        return dates;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

     /*   if (all == null || all.isEmpty()){
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            intent.putExtra("name",rname);
            intent.putExtra("data",id);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), AllTenants.class);
            startActivity(intent);
            finish();
        }*/

        if (all.equals("building")){
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            intent.putExtra("name",rname);
            intent.putExtra("data",id);
            intent.putExtra("all",all);
            startActivity(intent);
            finish();
        }
        else if (all.equals("load")){
            Intent intent = new Intent(getApplicationContext(), AllTenants.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            intent.putExtra("name",rname);
            intent.putExtra("data",id);
            intent.putExtra("all",all);
            startActivity(intent);
            finish();
        }

    }

    public void load(String id, String year){
       PaymentDatabase db = new PaymentDatabase(getApplicationContext());
//        rec = db.getTenantRentRecord(id,year);
//
//        if (rec.size() > 0){

        sec.clear();




//            for (TenantRentRecord tr1 : rec) {
            for (String tr1 : month) {
//                String mm = tr1.getMonth();
                String mm = tr1;


              ArrayList<HistoryData> hdata =  db.getTenantRentRecordMonth(id,year,mm);

               if (hdata.size() > 0){


                   binding.main.setVisibility(View.VISIBLE);
                   binding.download.setVisibility(View.VISIBLE);
                   binding.year.setVisibility(View.VISIBLE);
                   Section section = new Section();
                   section.setTitle(mm);
                   section.setItems(hdata);

                   sec.add(section);

               }
               else{
                /*   binding.main.setVisibility(View.GONE);
                   binding.download.setVisibility(View.GONE);
                   binding.year.setVisibility(View.GONE);

                   View parentLayout = findViewById(android.R.id.content);
                   Snackbar.make(parentLayout,"No record found",Snackbar.LENGTH_LONG).show();*/
               }




            }//end of for

            //load adapter here
            adapter1 = new CustomAdapter1(this,sec);

            //   adapter1.setClickListener(this);
            binding.historylist.setAdapter(adapter1);

            adapter1.notifyDataSetChanged();


//        }
//        else {
//
//        }
    }
}